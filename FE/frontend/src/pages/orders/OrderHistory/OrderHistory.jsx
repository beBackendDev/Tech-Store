import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import useAxiosPrivate from "../../../hooks/useAxiosPrivate";

import { getOrders } from "../../../services/orderService";

import "./OrderHistory.scss";


function OrderHistory() {

    const navigate = useNavigate();

    const axiosPrivate = useAxiosPrivate();


    const [orders, setOrders] = useState([]);

    const [loading, setLoading] = useState(true);

    const [error, setError] = useState(null);


    useEffect(() => {

        let isMounted = true;


        const loadOrders = async () => {

            try {

                setLoading(true);

                setError(null);


                const data =
                    await getOrders(axiosPrivate);


                if (isMounted) {

                    setOrders(data);

                }

            } catch (error) {

                console.error(
                    "Failed to load orders:",
                    error
                );


                if (isMounted) {

                    setError(
                        "Unable to load your orders."
                    );

                }

            } finally {

                if (isMounted) {

                    setLoading(false);

                }

            }

        };


        loadOrders();


        return () => {

            isMounted = false;

        };

    }, [axiosPrivate]);


    const formatPrice = (price) => {

        return new Intl.NumberFormat("vi-VN", {

            style: "currency",

            currency: "VND"

        }).format(price);

    };


    const formatDate = (date) => {

        return new Intl.DateTimeFormat(
            "vi-VN",
            {
                day: "2-digit",
                month: "2-digit",
                year: "numeric"
            }
        ).format(new Date(date));

    };


    const getStatusLabel = (status) => {

        const labels = {

            PENDING: "Pending",

            CONFIRMED: "Confirmed",

            PROCESSING: "Processing",

            SHIPPED: "Shipped",

            DELIVERED: "Delivered",

            CANCELLED: "Cancelled"

        };


        return labels[status] || status;

    };


    const handleViewOrder = (orderId) => {

        navigate(`/orders/${orderId}`);

    };


    if (loading) {

        return (

            <main className="order-history">

                <div className="order-history__state">

                    <p>
                        Loading your orders...
                    </p>

                </div>

            </main>

        );

    }


    if (error) {

        return (

            <main className="order-history">

                <div className="order-history__state">

                    <p>
                        {error}
                    </p>

                    <button
                        type="button"
                        onClick={() => window.location.reload()}
                    >
                        Try again
                    </button>

                </div>

            </main>

        );

    }


    if (orders.length === 0) {

        return (

            <main className="order-history">

                <div className="order-history__empty">

                    <span>
                        ◎
                    </span>

                    <h1>
                        No orders yet
                    </h1>

                    <p>
                        You haven't placed any orders yet.
                    </p>

                    <button
                        type="button"
                        onClick={() => navigate("/products")}
                    >
                        Start shopping
                    </button>

                </div>

            </main>

        );

    }


    return (

        <main className="order-history">

            <div className="order-history__container">

                {/* =========================
                    HEADER
                ========================= */}

                <header className="order-history__header">

                    <div>

                        <span className="order-history__eyebrow">
                            ACCOUNT
                        </span>

                        <h1>
                            My orders
                        </h1>

                        <p>
                            View and track your recent orders.
                        </p>

                    </div>


                    <button
                        type="button"
                        className="order-history__shop-button"
                        onClick={() => navigate("/products")}
                    >
                        Continue shopping
                    </button>

                </header>


                {/* =========================
                    SUMMARY
                ========================= */}

                <div className="order-history__summary">

                    <span>
                        {orders.length} orders
                    </span>

                </div>


                {/* =========================
                    ORDER LIST
                ========================= */}

                <section className="order-history__list">

                    {orders.map(order => {

                        const totalItems =
                            order.items?.reduce(
                                (total, item) =>
                                    total + item.quantity,
                                0
                            ) || 0;


                        return (

                            <article
                                className="order-card"
                                key={order.id}
                            >

                                {/* HEADER */}

                                <div className="order-card__header">

                                    <div>

                                        <span>
                                            Order
                                        </span>

                                        <strong>
                                            #{order.id}
                                        </strong>

                                    </div>


                                    <span
                                        className={`order-card__status order-card__status--${order.status.toLowerCase()}`}
                                    >
                                        {getStatusLabel(
                                            order.status
                                        )}
                                    </span>

                                </div>


                                {/* META */}

                                <div className="order-card__meta">

                                    <div>

                                        <span>
                                            Placed on
                                        </span>

                                        <strong>
                                            {formatDate(
                                                order.createdAt
                                            )}
                                        </strong>

                                    </div>


                                    <div>

                                        <span>
                                            Items
                                        </span>

                                        <strong>
                                            {totalItems}
                                        </strong>

                                    </div>


                                    <div>

                                        <span>
                                            Payment
                                        </span>

                                        <strong>
                                            {order.paymentMethod}
                                        </strong>

                                    </div>


                                    <div>

                                        <span>
                                            Total
                                        </span>

                                        <strong>
                                            {formatPrice(
                                                order.totalAmount
                                            )}
                                        </strong>

                                    </div>

                                </div>


                                {/* PRODUCTS */}

                                <div className="order-card__products">

                                    {order.items
                                        ?.slice(0, 3)
                                        .map(item => (

                                            <div
                                                className="order-card__product"
                                                key={item.id}
                                            >

                                                <div className="order-card__image">

                                                    <img
                                                        src={
                                                            item.productImage
                                                        }
                                                        alt={
                                                            item.productName
                                                        }
                                                    />

                                                </div>


                                                <div className="order-card__product-info">

                                                    <strong>
                                                        {
                                                            item.productName
                                                        }
                                                    </strong>

                                                    <span>
                                                        ×
                                                        {" "}
                                                        {
                                                            item.quantity
                                                        }
                                                    </span>

                                                </div>

                                            </div>

                                        ))}

                                </div>


                                {/* FOOTER */}

                                <div className="order-card__footer">

                                    <span>
                                        {order.paymentStatus === "PAID"
                                            ? "Payment completed"
                                            : "Payment pending"
                                        }
                                    </span>


                                    <button
                                        type="button"
                                        onClick={() =>
                                            handleViewOrder(
                                                order.id
                                            )
                                        }
                                    >
                                        View order

                                        <span>
                                            →
                                        </span>

                                    </button>

                                </div>

                            </article>

                        );

                    })}

                </section>

            </div>

        </main>

    );

}


export default OrderHistory;