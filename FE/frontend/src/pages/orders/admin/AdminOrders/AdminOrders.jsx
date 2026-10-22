import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import useAxiosPrivate from "../../../../hooks/useAxiosPrivate";
import { getAdminOrders } from "../../../../api/adminApi";
import OrderStatusBadge from "../../../../components/admin/order/OrderStatusBadge/OrderStatusBadge";
import PaymentStatusBadge from "../../../../components/admin/order/PaymentStatusBadge/PaymentStatusBadge";
import "./AdminOrders.scss";

const PAGE_SIZE = 10;
const unwrap = (value) => value?.response ?? value?.data?.response ?? value?.data ?? value;
const formatPrice = (value) => new Intl.NumberFormat("vi-VN", {
    style: "currency", currency: "VND", maximumFractionDigits: 0,
}).format(Number(value ?? 0));
const formatDate = (value) => value ? new Intl.DateTimeFormat("vi-VN", {
    day: "2-digit", month: "2-digit", year: "numeric", hour: "2-digit", minute: "2-digit",
}).format(new Date(value)) : "—";

export default function AdminOrders() {
    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();
    const [page, setPage] = useState(0);
    const [status, setStatus] = useState("");
    const [paymentStatus, setPaymentStatus] = useState("");
    const [keyword, setKeyword] = useState("");
    const [orders, setOrders] = useState([]);
    const [pagination, setPagination] = useState({ totalPages: 0, totalElements: 0, first: true, last: true });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const loadOrders = useCallback(async () => {
        setLoading(true);
        setError("");
        try {
            const raw = await getAdminOrders(axiosPrivate, {
                page,
                size: PAGE_SIZE,
                sort: "createdAt,desc",
                ...(status ? { status } : {}),
                ...(paymentStatus ? { paymentStatus } : {}),
            });
            const data = unwrap(raw);
            const rows = Array.isArray(data) ? data : (data?.content ?? data?.orders ?? []);
            const query = keyword.trim().toLowerCase();
            const filtered = query ? rows.filter((order) =>
                [order.id, order.customerName, order.email, order.phone]
                    .some((field) => String(field ?? "").toLowerCase().includes(query))
            ) : rows;
            setOrders(filtered);
            setPagination({
                page: data?.number ?? data?.page ?? page,
                totalPages: data?.totalPages ?? 1,
                totalElements: data?.totalElements ?? rows.length,
                first: data?.first ?? page === 0,
                last: data?.last ?? true,
            });
        } catch (e) {
            console.error("Failed to load admin orders", e);
            setError(e?.response?.data?.message || "Unable to load orders. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [axiosPrivate, page, status, paymentStatus, keyword]);

    useEffect(() => { loadOrders(); }, [loadOrders]);

    const resetFilters = () => {
        setStatus(""); setPaymentStatus(""); setKeyword(""); setPage(0);
    };

    return (
        <div className="admin-orders">
            <header className="admin-orders__header">
                <div>
                    <span className="admin-orders__eyebrow">ORDER MANAGEMENT</span>
                    <h1>Orders</h1>
                    <p>Track fulfilment and manage payment outcomes independently.</p>
                </div>
                <button type="button" className="admin-orders__refresh" onClick={loadOrders} disabled={loading}>
                    {loading ? "Refreshing…" : "↻ Refresh"}
                </button>
            </header>

            <section className="admin-orders__filters" aria-label="Filter orders">
                <label className="admin-orders__search">
                    <span>Search</span>
                    <input value={keyword} onChange={(e) => { setKeyword(e.target.value); setPage(0); }} placeholder="Order ID, customer, email or phone" />
                </label>
                <label>
                    <span>Order status</span>
                    <select value={status} onChange={(e) => { setStatus(e.target.value); setPage(0); }}>
                        <option value="">All order statuses</option>
                        { ["PENDING", "CONFIRMED", "PROCESSING", "SHIPPING", "DELIVERED", "COMPLETED", "CANCELLED", "RETURN_REQUESTED", "RETURNED"].map((s) => <option key={s} value={s}>{s.replaceAll("_", " ")}</option>) }
                    </select>
                </label>
                <label>
                    <span>Payment status</span>
                    <select value={paymentStatus} onChange={(e) => { setPaymentStatus(e.target.value); setPage(0); }}>
                        <option value="">All payment statuses</option>
                        {["PENDING", "PAID", "FAILED", "REFUNDED"].map((s) => <option key={s} value={s}>{s}</option>)}
                    </select>
                </label>
                <button type="button" className="admin-orders__reset" onClick={resetFilters}>Reset</button>
            </section>

            {error && <div className="admin-orders__notice admin-orders__notice--error" role="alert">{error}</div>}
            <section className="admin-orders__table-card">
                {loading ? <div className="admin-orders__state">Loading orders…</div> : orders.length === 0 ? (
                    <div className="admin-orders__state"><strong>No orders found</strong><span>Try changing the filters or refresh the list.</span></div>
                ) : (
                    <div className="admin-orders__table-wrap">
                        <table className="admin-orders__table">
                            <thead><tr><th>Order</th><th>Customer</th><th>Created</th><th>Order status</th><th>Payment</th><th>Method</th><th className="numeric">Total</th><th></th></tr></thead>
                            <tbody>{orders.map((order) => (
                                <tr key={order.id}>
                                    <td><strong>#{order.id}</strong></td>
                                    <td><div className="admin-orders__customer"><strong>{order.customerName || "Customer"}</strong><span>{order.email || order.phone || "—"}</span></div></td>
                                    <td>{formatDate(order.createdAt)}</td>
                                    <td><OrderStatusBadge status={order.status} /></td>
                                    <td><PaymentStatusBadge status={order.paymentStatus} /></td>
                                    <td>{String(order.paymentMethod || "—").replaceAll("_", " ")}</td>
                                    <td className="numeric admin-orders__amount">{formatPrice(order.totalAmount)}</td>
                                    <td><button type="button" className="admin-orders__view" onClick={() => navigate(`/admin/orders/${order.id}`)}>View <span aria-hidden="true">→</span></button></td>
                                </tr>
                            ))}</tbody>
                        </table>
                    </div>
                )}
            </section>
            <footer className="admin-orders__pagination">
                <span>{pagination.totalElements} order{pagination.totalElements === 1 ? "" : "s"} · Page {page + 1} of {Math.max(pagination.totalPages || 1, 1)}</span>
                <div>
                    <button type="button" onClick={() => setPage((p) => Math.max(0, p - 1))} disabled={loading || page === 0}>Previous</button>
                    <button type="button" onClick={() => setPage((p) => p + 1)} disabled={loading || pagination.last || (pagination.totalPages > 0 && page + 1 >= pagination.totalPages)}>Next</button>
                </div>
            </footer>
        </div>
    );
}