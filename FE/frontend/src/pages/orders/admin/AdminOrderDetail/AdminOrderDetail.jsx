import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useAxiosPrivate from "../../../../hooks/useAxiosPrivate";
import {
    getAdminOrderById,
    confirmOrder,
    processOrder,
    shipOrder,
    deliverOrder,
    completeOrder,
    cancelAdminOrder,
    markPaymentSuccess,
    markPaymentFailed,
    completeCodPayment,
} from "../../../../api/adminApi";
import OrderStatusBadge from "../../../../components/admin/order/OrderStatusBadge/OrderStatusBadge";
import PaymentStatusBadge from "../../../../components/admin/order/PaymentStatusBadge/PaymentStatusBadge";
import OrderTimeline from "../../../../components/admin/order/OrderTimeline/OrderTimeline";
import "./AdminOrderDetail.scss";

const unwrapResponse = (value) =>
    value?.response ?? value?.data?.response ?? value?.data ?? value;

const formatPrice = (value) =>
    new Intl.NumberFormat("vi-VN", {
        style: "currency",
        currency: "VND",
        maximumFractionDigits: 0,
    }).format(Number(value ?? 0));

const formatDate = (value) => {
    if (!value) return "—";

    const date = new Date(value);
    if (Number.isNaN(date.getTime())) return "—";

    return new Intl.DateTimeFormat("vi-VN", {
        day: "2-digit",
        month: "long",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    }).format(date);
};

const getErrorMessage = (error) =>
    error?.response?.data?.message ??
    error?.response?.data?.response?.message ??
    error?.message ??
    "The action could not be completed. Please try again.";

const ORDER_ACTIONS = {
    PENDING: [
        {
            label: "Confirm order",
            action: "confirm",
            successMessage: "Order confirmed.",
        },
        {
            label: "Cancel order",
            action: "cancel",
            danger: true,
            confirmation:
                "Cancel this order? The backend will validate whether its reservation can be released.",
            successMessage: "Order cancelled.",
        },
    ],
    CONFIRMED: [
        {
            label: "Start processing",
            action: "process",
            successMessage: "Order moved to processing.",
        },
        {
            label: "Cancel order",
            action: "cancel",
            danger: true,
            confirmation: "Cancel this order?",
            successMessage: "Order cancelled.",
        },
    ],
    PROCESSING: [
        {
            label: "Mark as shipped",
            action: "ship",
            confirmation:
                "Confirm shipment? Inventory will be committed by the backend.",
            successMessage: "Order marked as shipping.",
        },
    ],
    SHIPPING: [
        {
            label: "Mark as delivered",
            action: "deliver",
            successMessage: "Order marked as delivered.",
        },
    ],
    DELIVERED: [
        {
            label: "Complete order",
            action: "complete",
            successMessage: "Order completed.",
        },
    ],
};

export default function AdminOrderDetail() {
    const { id } = useParams();
    const navigate = useNavigate();
    const axiosPrivate = useAxiosPrivate();

    const [order, setOrder] = useState(null);
    const [loading, setLoading] = useState(true);
    const [busy, setBusy] = useState(false);
    const [error, setError] = useState("");
    const [notice, setNotice] = useState("");
    const [failureReason, setFailureReason] = useState("");
    const [showFailureForm, setShowFailureForm] = useState(false);

    const loadOrder = useCallback(async () => {
        setLoading(true);
        setError("");

        try {
            const result = await getAdminOrderById(axiosPrivate, id);
            const data = unwrapResponse(result);

            if (!data || data.id == null) {
                throw new Error("The server returned an unexpected order response.");
            }

            setOrder(data);
        } catch (loadError) {
            console.error("Failed to load admin order", loadError);
            setError(getErrorMessage(loadError));
        } finally {
            setLoading(false);
        }
    }, [axiosPrivate, id]);

    useEffect(() => {
        void loadOrder();
    }, [loadOrder]);

    const executeOrderAction = useCallback(
        async (actionName) => {
            switch (actionName) {
                case "confirm":
                    return confirmOrder(axiosPrivate, id);
                case "process":
                    return processOrder(axiosPrivate, id);
                case "ship":
                    return shipOrder(axiosPrivate, id);
                case "deliver":
                    return deliverOrder(axiosPrivate, id);
                case "complete":
                    return completeOrder(axiosPrivate, id);
                case "cancel":
                    return cancelAdminOrder(
                        axiosPrivate,
                        id,
                        "Cancelled by administrator"
                    );
                default:
                    throw new Error(`Unsupported order action: ${actionName}`);
            }
        },
        [axiosPrivate, id]
    );

    const runAction = useCallback(
        async (action, successMessage) => {
            setBusy(true);
            setError("");
            setNotice("");

            try {
                await action();
                setNotice(successMessage);
                setShowFailureForm(false);
                setFailureReason("");
                await loadOrder();
            } catch (actionError) {
                console.error("Order action failed", actionError);
                setError(getErrorMessage(actionError));
            } finally {
                setBusy(false);
            }
        },
        [loadOrder]
    );

    const handleOrderAction = (item) => {
        if (
            item.confirmation &&
            !window.confirm(item.confirmation)
        ) {
            return;
        }

        void runAction(
            () => executeOrderAction(item.action),
            item.successMessage
        );
    };

    const handlePaymentSuccess = () => {
        const confirmed = window.confirm(
            "Confirm that this payment has actually been received?"
        );
        if (!confirmed) return;

        void runAction(
            () => markPaymentSuccess(axiosPrivate, id),
            "Payment marked as PAID."
        );
    };

    const handleCodPaymentComplete = () => {
        const confirmed = window.confirm(
            "Confirm that cash was collected for this delivered COD order?"
        );
        if (!confirmed) return;

        void runAction(
            () => completeCodPayment(axiosPrivate, id),
            "COD payment completed and marked as PAID."
        );
    };

    const handlePaymentFailureSubmit = (event) => {
        event.preventDefault();

        const reason = failureReason.trim();
        if (!reason || busy) return;

        void runAction(
            () => markPaymentFailed(axiosPrivate, id, reason),
            "Payment marked as FAILED."
        );
    };

    const items = useMemo(
        () => order?.items ?? order?.orderItems ?? [],
        [order]
    );
    const actions = ORDER_ACTIONS[order?.status] ?? [];

    const canMarkPaymentSuccess =
        order?.paymentStatus === "PENDING" &&
        order?.paymentMethod !== "COD";
    const canMarkPaymentFailed =
        order?.paymentStatus === "PENDING" &&
        order?.paymentMethod !== "COD";
    const canCompleteCodPayment =
        order?.paymentMethod === "COD" &&
        order?.paymentStatus === "PENDING" &&
        order?.status === "DELIVERED";

    if (loading && !order) {
        return (
            <main className="admin-order-detail">
                <div className="admin-order-detail__state" role="status">
                    Loading order details…
                </div>
            </main>
        );
    }

    if (error && !order) {
        return (
            <main className="admin-order-detail">
                <div className="admin-order-detail__state" role="alert">
                    <strong>Unable to load order</strong>
                    <span>{error}</span>
                    <div className="admin-order-detail__state-actions">
                        <button
                            type="button"
                            onClick={() => navigate("/admin/orders")}
                        >
                            Back to orders
                        </button>
                        <button
                            type="button"
                            className="admin-order-detail__state-button--secondary"
                            onClick={() => void loadOrder()}
                        >
                            Try again
                        </button>
                    </div>
                </div>
            </main>
        );
    }

    if (!order) return null;

    return (
        <main className="admin-order-detail">
            <header className="admin-order-detail__header">
                <div className="admin-order-detail__heading">
                    <button className="admin-order-detail__back" type="button" onClick={() => navigate("/admin/orders")}>
                        <span aria-hidden="true">←</span> Back to orders
                    </button>
                    <h1>Order Details</h1>
                </div>
                <div className="admin-order-detail__header-meta">
                    <span>Order ID: <strong>#{order.id}</strong></span>
                    <button className="admin-order-detail__secondary" type="button" onClick={() => void loadOrder()} disabled={loading || busy}>
                        <span aria-hidden="true">↻</span> {loading ? "Refreshing…" : "Refresh"}
                    </button>
                </div>
            </header>

            {notice && <div className="admin-order-detail__notice admin-order-detail__notice--success" role="status" aria-live="polite">{notice}</div>}
            {error && <div className="admin-order-detail__notice admin-order-detail__notice--error" role="alert">{error}</div>}

            <div className="admin-order-detail__layout">
                <aside className="admin-order-detail__sidebar">
                    <section className="admin-order-detail__card admin-order-detail__order-card">
                        <div className="admin-order-detail__card-heading"><h2>Your Order</h2><span>{items.length} item{items.length === 1 ? "" : "s"}</span></div>
                        {items.length === 0 ? <div className="admin-order-detail__empty-items">No order items were returned by the API.</div> : <div className="admin-order-detail__items">
                            {items.map((item, index) => {
                                const image = item.productImage || item.imageUrl;
                                const quantity = Number(item.quantity ?? 0);
                                const price = Number(item.price ?? 0);
                                return <article className="admin-order-detail__item" key={item.id ?? item.productId ?? index}>
                                    <div className="admin-order-detail__image">{image ? <img src={image} alt={item.productName || "Product"} loading="lazy" /> : <span>No image</span>}</div>
                                    <div className="admin-order-detail__item-info"><strong>{item.productName || "Product"}</strong><span>{formatPrice(price)} · Qty {quantity}</span></div>
                                    <strong className="admin-order-detail__item-subtotal">{formatPrice(item.subtotal ?? price * quantity)}</strong>
                                </article>;
                            })}
                        </div>}
                        <dl className="admin-order-detail__summary">
                            <div><dt>Subtotal</dt><dd>{formatPrice(order.subtotal)}</dd></div>
                            <div><dt>Shipping</dt><dd>{Number(order.shippingFee ?? 0) === 0 ? "Free" : formatPrice(order.shippingFee)}</dd></div>
                            <div><dt>Discount</dt><dd>−{formatPrice(order.discount)}</dd></div>
                            <div className="is-total"><dt>Total</dt><dd>{formatPrice(order.totalAmount)}</dd></div>
                        </dl>
                        <div className="admin-order-detail__sidebar-payment"><span>Payment Information</span><strong>{String(order.paymentMethod || "—").replaceAll("_", " ")}</strong><div><PaymentStatusBadge status={order.paymentStatus} /></div></div>
                    </section>
                    <section className="admin-order-detail__card">
                        <div className="admin-order-detail__card-heading"><h2>Customer & Delivery</h2></div>
                        <dl className="admin-order-detail__details">
                            <div><dt>Customer</dt><dd>{order.customerName || "—"}</dd></div>
                            <div><dt>Email</dt><dd>{order.email || "—"}</dd></div>
                            <div><dt>Phone</dt><dd>{order.phone || "—"}</dd></div>
                            <div><dt>Delivery address</dt><dd>{order.address || order.shippingAddress || "—"}</dd></div>
                        </dl>
                    </section>
                </aside>

                <div className="admin-order-detail__main-column">
                    <section className="admin-order-detail__card admin-order-detail__progress-card">
                        <div className="admin-order-detail__card-heading"><div><h2>Order Progress</h2><p>Placed on {formatDate(order.createdAt)}</p></div><OrderStatusBadge status={order.status} /></div>
                        <div className="admin-order-detail__timeline"><OrderTimeline status={order.status} order={order} /></div>
                        <div className="admin-order-detail__progress-meta">
                            <div><span>Payment status</span><PaymentStatusBadge status={order.paymentStatus} /></div>
                            <div><span>Payment method</span><strong>{String(order.paymentMethod || "—").replaceAll("_", " ")}</strong></div>
                            <div><span>Order total</span><strong className="admin-order-detail__total">{formatPrice(order.totalAmount)}</strong></div>
                        </div>
                    </section>

                    <section className="admin-order-detail__card admin-order-detail__management-card">
                        <div className="admin-order-detail__card-heading"><div><h2>Order Management</h2><p>Choose the next valid action for the current order status.</p></div></div>
                        <div className="admin-order-detail__actions">
                            {actions.map((item) => <button key={item.action} type="button" className={`admin-order-detail__button${item.danger ? " admin-order-detail__button--danger" : " admin-order-detail__button--primary"}`} disabled={busy || loading} onClick={() => handleOrderAction(item)}>{busy ? "Processing…" : item.label}</button>)}
                            {actions.length === 0 && <span className="admin-order-detail__muted">No fulfilment action is available for this status.</span>}
                        </div>
                        <p className="admin-order-detail__hint">Order status, payment status and inventory are handled by separate backend workflows.</p>
                    </section>

                    <section className="admin-order-detail__card admin-order-detail__payment-card">
                        <div className="admin-order-detail__card-heading"><div><h2>Payment Management</h2><p>Verify the payment before recording a result.</p></div><PaymentStatusBadge status={order.paymentStatus} /></div>
                        {canMarkPaymentSuccess && <div className="admin-order-detail__payment-action"><div><strong>Payment received?</strong><span>Only mark as paid after verifying the bank transfer or online payment with the provider or bank.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--success" disabled={busy || loading} onClick={handlePaymentSuccess}>Mark payment successful</button></div>}
                        {canMarkPaymentFailed && <div className="admin-order-detail__payment-action"><div><strong>Payment failed</strong><span>Recording failure does not cancel the order or release reserved stock.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--danger" disabled={busy || loading} aria-expanded={showFailureForm} aria-controls="payment-failure-form" onClick={() => setShowFailureForm((visible) => !visible)}>{showFailureForm ? "Close form" : "Mark payment failed"}</button></div>}
                        {showFailureForm && canMarkPaymentFailed && <form id="payment-failure-form" className="admin-order-detail__failure-form" onSubmit={handlePaymentFailureSubmit}><label htmlFor="payment-failure-reason">Failure reason <span>(required, max 500 characters)</span></label><textarea id="payment-failure-reason" name="reason" value={failureReason} onChange={(event) => setFailureReason(event.target.value.slice(0, 500))} maxLength={500} required disabled={busy} placeholder="e.g. Bank transfer not received or payment provider declined the transaction."/><div className="admin-order-detail__form-footer"><span>{failureReason.length}/500</span><button className="admin-order-detail__button admin-order-detail__button--danger" type="submit" disabled={busy || !failureReason.trim()}>{busy ? "Saving…" : "Confirm failure"}</button></div></form>}
                        {canCompleteCodPayment && <div className="admin-order-detail__payment-action"><div><strong>Cash collected on delivery?</strong><span>Complete COD payment only after delivery and cash collection have been confirmed.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--success" disabled={busy || loading} onClick={handleCodPaymentComplete}>Complete COD payment</button></div>}
                        {order.paymentStatus !== "PENDING" && <p className="admin-order-detail__payment-locked">Payment is <strong>{order.paymentStatus}</strong>. No further payment action is available in this workflow.</p>}
                        {order.paymentMethod === "COD" && order.paymentStatus === "PENDING" && order.status !== "DELIVERED" && <p className="admin-order-detail__payment-locked">COD can be completed after the order reaches DELIVERED.</p>}
                    </section>
                </div>
            </div>
        </main>
    );
}
