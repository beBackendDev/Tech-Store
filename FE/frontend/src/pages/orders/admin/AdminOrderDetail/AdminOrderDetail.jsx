import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import useAxiosPrivate from "../../../../hooks/useAxiosPrivate";
import {
    getAdminOrderById, confirmOrder, processOrder, shipOrder,
    deliverOrder, completeOrder, cancelAdminOrder,
    markPaymentSuccess, markPaymentFailed, completeCodPayment,
} from "../../../../api/adminApi";
import OrderStatusBadge from "../../../../components/admin/order/OrderStatusBadge/OrderStatusBadge";
import PaymentStatusBadge from "../../../../components/admin/order/PaymentStatusBadge/PaymentStatusBadge";
import OrderTimeline from "../../../../components/admin/order/OrderTimeline/OrderTimeline";
import "./AdminOrderDetail.scss";

const unwrap = (value) => value?.response ?? value?.data?.response ?? value?.data ?? value;
const formatPrice = (value) => new Intl.NumberFormat("vi-VN", { style: "currency", currency: "VND", maximumFractionDigits: 0 }).format(Number(value ?? 0));
const formatDate = (value) => value ? new Intl.DateTimeFormat("vi-VN", { day: "2-digit", month: "long", year: "numeric", hour: "2-digit", minute: "2-digit" }).format(new Date(value)) : "—";
const errorMessage = (e) => e?.response?.data?.message || e?.response?.data?.response?.message || e?.message || "The action could not be completed. Please try again.";

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
        setLoading(true); setError("");
        try {
            const result = await getAdminOrderById(axiosPrivate, id);
            const data = unwrap(result);
            if (!data || data.id == null) throw new Error("The server returned an unexpected order response.");
            setOrder(data);
        } catch (e) {
            console.error("Failed to load admin order", e);
            setError(errorMessage(e));
        } finally { setLoading(false); }
    }, [axiosPrivate, id]);

    useEffect(() => { loadOrder(); }, [loadOrder]);

    const runAction = async (action, successMessage) => {
        setBusy(true); setError(""); setNotice("");
        try {
            await action();
            setNotice(successMessage);
            setShowFailureForm(false);
            setFailureReason("");
            await loadOrder();
        } catch (e) {
            console.error("Order action failed", e);
            setError(errorMessage(e));
        } finally { setBusy(false); }
    };

    const orderActions = {
        PENDING: [
            { label: "Confirm order", action: () => confirmOrder(axiosPrivate, id), success: "Order confirmed." },
            { label: "Cancel order", danger: true, action: () => cancelAdminOrder(axiosPrivate, id, "Cancelled by administrator"), confirm: "Cancel this order? The backend will validate whether its reservation can be released.", success: "Order cancelled." },
        ],
        CONFIRMED: [
            { label: "Start processing", action: () => processOrder(axiosPrivate, id), success: "Order moved to processing." },
            { label: "Cancel order", danger: true, action: () => cancelAdminOrder(axiosPrivate, id, "Cancelled by administrator"), confirm: "Cancel this order?", success: "Order cancelled." },
        ],
        PROCESSING: [{ label: "Mark as shipped", action: () => shipOrder(axiosPrivate, id), confirm: "Confirm shipment? Inventory will be committed by the backend.", success: "Order marked as shipping." }],
        SHIPPING: [{ label: "Mark as delivered", action: () => deliverOrder(axiosPrivate, id), success: "Order marked as delivered." }],
        DELIVERED: [{ label: "Complete order", action: () => completeOrder(axiosPrivate, id), success: "Order completed." }],
    };

    const runDefinedAction = (item) => {
        if (item.confirm && !window.confirm(item.confirm)) return;
        runAction(item.action, item.success);
    };

    const canMarkSuccess = order?.paymentStatus === "PENDING" && order?.paymentMethod !== "COD";
    const canMarkFailed = order?.paymentStatus === "PENDING" && order?.paymentMethod !== "COD";
    const canCompleteCod = order?.paymentMethod === "COD" && order?.paymentStatus === "PENDING" && order?.status === "DELIVERED";

    if (loading && !order) return <div className="admin-order-detail"><div className="admin-order-detail__state">Loading order details…</div></div>;
    if (error && !order) return <div className="admin-order-detail"><div className="admin-order-detail__state"><strong>Unable to load order</strong><span>{error}</span><button type="button" onClick={() => navigate("/admin/orders")}>Back to orders</button></div></div>;
    if (!order) return null;

    const items = order.items ?? order.orderItems ?? [];
    const actions = orderActions[order.status] ?? [];

    return (
        <div className="admin-order-detail">
            <header className="admin-order-detail__header">
                <div>
                    <button className="admin-order-detail__back" type="button" onClick={() => navigate("/admin/orders")}>← Back to orders</button>
                    <span className="admin-order-detail__eyebrow">ORDER MANAGEMENT</span>
                    <h1>Order #{order.id}</h1>
                    <p>Placed on {formatDate(order.createdAt)}</p>
                </div>
                <button className="admin-order-detail__secondary" type="button" onClick={loadOrder} disabled={loading || busy}>↻ Refresh</button>
            </header>

            {notice && <div className="admin-order-detail__notice admin-order-detail__notice--success" role="status">{notice}</div>}
            {error && <div className="admin-order-detail__notice admin-order-detail__notice--error" role="alert">{error}</div>}

            <section className="admin-order-detail__status-grid">
                <div><span>Order status</span><OrderStatusBadge status={order.status} /></div>
                <div><span>Payment status</span><PaymentStatusBadge status={order.paymentStatus} /></div>
                <div><span>Payment method</span><strong>{String(order.paymentMethod || "—").replaceAll("_", " ")}</strong></div>
                <div><span>Order total</span><strong className="admin-order-detail__total">{formatPrice(order.totalAmount)}</strong></div>
            </section>

            <section className="admin-order-detail__card">
                <div className="admin-order-detail__card-heading"><div><h2>Order fulfilment</h2><p>Order status and payment status are managed separately.</p></div></div>
                <div className="admin-order-detail__timeline"><OrderTimeline status={order.status} /></div>
                <div className="admin-order-detail__actions">
                    {actions.map((item) => <button key={item.label} type="button" className={item.danger ? "admin-order-detail__button admin-order-detail__button--danger" : "admin-order-detail__button"} disabled={busy} onClick={() => runDefinedAction(item)}>{item.label}</button>)}
                    {actions.length === 0 && <span className="admin-order-detail__muted">No fulfilment action is available for this status.</span>}
                </div>
            </section>

            <section className="admin-order-detail__card">
                <div className="admin-order-detail__card-heading"><div><h2>Payment management</h2><p>Changing payment status does not change order status or inventory.</p></div><PaymentStatusBadge status={order.paymentStatus} /></div>
                <div className="admin-order-detail__payment-meta">
                    <div><span>Method</span><strong>{String(order.paymentMethod || "—").replaceAll("_", " ")}</strong></div>
                    <div><span>Current status</span><strong>{order.paymentStatus || "—"}</strong></div>
                    <div><span>Amount</span><strong>{formatPrice(order.totalAmount)}</strong></div>
                </div>
                {canMarkSuccess && <div className="admin-order-detail__payment-action"><div><strong>Payment received?</strong><span>Use this only after verifying the bank transfer or online payment in the relevant provider/bank.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--success" disabled={busy} onClick={() => { if (window.confirm("Confirm that this payment has actually been received?")) runAction(() => markPaymentSuccess(axiosPrivate, id), "Payment marked as PAID."); }}>Mark payment successful</button></div>}
                {canMarkFailed && <div className="admin-order-detail__payment-action"><div><strong>Payment failed</strong><span>Record a failed payment attempt. This does not cancel the order or release reserved stock.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--danger" disabled={busy} onClick={() => setShowFailureForm((v) => !v)}>{showFailureForm ? "Close form" : "Mark payment failed"}</button></div>}
                {showFailureForm && canMarkFailed && <form className="admin-order-detail__failure-form" onSubmit={(e) => { e.preventDefault(); const reason = failureReason.trim(); if (!reason) return; runAction(() => markPaymentFailed(axiosPrivate, id, reason), "Payment marked as FAILED."); }}><label htmlFor="payment-failure-reason">Failure reason <span>(required, max 500 characters)</span></label><textarea id="payment-failure-reason" value={failureReason} onChange={(e) => setFailureReason(e.target.value.slice(0, 500))} maxLength={500} required placeholder="e.g. Bank transfer not received or payment provider declined the transaction."/><div className="admin-order-detail__form-footer"><span>{failureReason.length}/500</span><button className="admin-order-detail__button admin-order-detail__button--danger" type="submit" disabled={busy || !failureReason.trim()}>{busy ? "Saving…" : "Confirm failure"}</button></div></form>}
                {canCompleteCod && <div className="admin-order-detail__payment-action"><div><strong>Cash collected on delivery?</strong><span>Complete COD payment only after delivery and cash collection have been confirmed.</span></div><button type="button" className="admin-order-detail__button admin-order-detail__button--success" disabled={busy} onClick={() => { if (window.confirm("Confirm that cash was collected for this delivered COD order?")) runAction(() => completeCodPayment(axiosPrivate, id), "COD payment completed and marked as PAID."); }}>Complete COD payment</button></div>}
                {order.paymentStatus !== "PENDING" && <p className="admin-order-detail__payment-locked">Payment is <strong>{order.paymentStatus}</strong>. No further payment action is available in this workflow.</p>}
                {order.paymentMethod === "COD" && order.paymentStatus === "PENDING" && order.status !== "DELIVERED" && <p className="admin-order-detail__payment-locked">COD can be completed after the order reaches DELIVERED.</p>}
            </section>

            <div className="admin-order-detail__columns">
                <section className="admin-order-detail__card">
                    <div className="admin-order-detail__card-heading"><div><h2>Customer & delivery</h2><p>Details captured when the order was placed.</p></div></div>
                    <dl className="admin-order-detail__details">
                        <div><dt>Customer</dt><dd>{order.customerName || "—"}</dd></div>
                        <div><dt>Email</dt><dd>{order.email || "—"}</dd></div>
                        <div><dt>Phone</dt><dd>{order.phone || "—"}</dd></div>
                        <div><dt>Address</dt><dd>{order.address || order.shippingAddress || "—"}</dd></div>
                    </dl>
                </section>
                <section className="admin-order-detail__card">
                    <div className="admin-order-detail__card-heading"><div><h2>Order summary</h2><p>Final amounts from the order snapshot.</p></div></div>
                    <dl className="admin-order-detail__summary">
                        <div><dt>Subtotal</dt><dd>{formatPrice(order.subtotal)}</dd></div>
                        <div><dt>Shipping fee</dt><dd>{formatPrice(order.shippingFee)}</dd></div>
                        <div><dt>Discount</dt><dd>−{formatPrice(order.discount)}</dd></div>
                        <div className="is-total"><dt>Total</dt><dd>{formatPrice(order.totalAmount)}</dd></div>
                    </dl>
                </section>
            </div>

            <section className="admin-order-detail__card">
                <div className="admin-order-detail__card-heading"><div><h2>Items ({items.length})</h2><p>Products and prices saved with this order.</p></div></div>
                {items.length === 0 ? <div className="admin-order-detail__empty-items">No order items were returned by the API.</div> : <div className="admin-order-detail__items">{items.map((item, index) => <article className="admin-order-detail__item" key={item.id ?? item.productId ?? index}>
                    <div className="admin-order-detail__image">{item.productImage || item.imageUrl ? <img src={item.productImage || item.imageUrl} alt={item.productName || "Product"} /> : <span>IMG</span>}</div>
                    <div className="admin-order-detail__item-info"><strong>{item.productName || "Product"}</strong><span>Qty: {item.quantity ?? 0} × {formatPrice(item.price)}</span></div>
                    <strong>{formatPrice(item.subtotal ?? Number(item.price ?? 0) * Number(item.quantity ?? 0))}</strong>
                </article>)}</div>}
            </section>
        </div>
    );
}