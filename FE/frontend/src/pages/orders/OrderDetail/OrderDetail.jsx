import {
    useEffect,
    useState
} from "react";

import {
    useNavigate,
    useParams
} from "react-router-dom";

import useAxiosPrivate from "../../../hooks/useAxiosPrivate";

import {
    getOrderById
} from "../../../services/orderService";

import "./OrderDetail.scss";


function OrderDetail() {

    const {
        id
    } = useParams();

    const navigate = useNavigate();

    const axiosPrivate =
        useAxiosPrivate();


    const [order, setOrder] =
        useState(null);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState(null);


    useEffect(() => {

        let isMounted = true;


        const loadOrder = async () => {

            try {

                setLoading(true);

                setError(null);


                const data =
                    await getOrderById(
                        id,
                        axiosPrivate
                    );


                if (isMounted) {

                    setOrder(data);

                }

            } catch (error) {

                console.error(
                    "Failed to load order:",
                    error
                );


                if (isMounted) {

                    setError(
                        "Unable to load this order."
                    );

                }

            } finally {

                if (isMounted) {

                    setLoading(false);

                }

            }

        };


        if (id) {

            loadOrder();

        }


        return () => {

            isMounted = false;

        };

    }, [
        id,
        axiosPrivate
    ]);


    const formatPrice = (price) => {

        return new Intl.NumberFormat(
            "vi-VN",
            {
                style: "currency",
                currency: "VND"
            }
        ).format(price);

    };


    const formatDate = (date) => {

        return new Intl.DateTimeFormat(
            "vi-VN",
            {
                day: "2-digit",
                month: "2-digit",
                year: "numeric",
                hour: "2-digit",
                minute: "2-digit"
            }
        ).format(
            new Date(date)
        );

    };


    if (loading) {

        return (

            <main className="order-detail">

                <div className="order-detail__state">

                    <p>
                        Loading order...
                    </p>

                </div>

            </main>

        );

    }


    if (error || !order) {

        return (

            <main className="order-detail">

                <div className="order-detail__state">

                    <p>
                        {error ||
                            "Order not found."}
                    </p>

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/orders")
                        }
                    >
                        Back to orders
                    </button>

                </div>

            </main>

        );

    }


    return (

        <main className="order-detail">

            <div className="order-detail__container">


                {/* =========================
                    HEADER
                ========================= */}

                <header className="order-detail__header">

                    <button
                        type="button"
                        className="order-detail__back"
                        onClick={() =>
                            navigate("/orders")
                        }
                    >
                        ← Back to orders
                    </button>


                    <div>

                        <span className="order-detail__eyebrow">
                            ORDER DETAILS
                        </span>

                        <h1>
                            Order #{order.id}
                        </h1>

                        <p>
                            Placed on{" "}
                            {formatDate(
                                order.createdAt
                            )}
                        </p>

                    </div>

                </header>


                {/* =========================
                    STATUS
                ========================= */}

                <section className="order-detail__status">

                    <div>

                        <span>
                            Order status
                        </span>

                        <strong>
                            {order.status}
                        </strong>

                    </div>


                    <div>

                        <span>
                            Payment
                        </span>

                        <strong>
                            {order.paymentStatus}
                        </strong>

                    </div>


                    <div>

                        <span>
                            Payment method
                        </span>

                        <strong>
                            {order.paymentMethod}
                        </strong>

                    </div>

                </section>


                {/* =========================
                    PRODUCTS
                ========================= */}

                <section className="order-detail__card">

                    <div className="order-detail__card-header">

                        <h2>
                            Items
                        </h2>

                        <span>
                            {order.items?.length || 0} products
                        </span>

                    </div>


                    <div className="order-detail__items">

                        {order.items?.map(
                            item => (

                                <article
                                    className="order-detail__item"
                                    key={item.id}
                                >

                                    <div className="order-detail__image">

                                        <img
                                            src={
                                                item.productImage
                                            }
                                            alt={
                                                item.productName
                                            }
                                        />

                                    </div>


                                    <div className="order-detail__item-info">

                                        <strong>
                                            {
                                                item.productName
                                            }
                                        </strong>

                                        <span>
                                            Quantity:{" "}
                                            {
                                                item.quantity
                                            }
                                        </span>

                                        <span>
                                            {
                                                formatPrice(
                                                    item.price
                                                )
                                            }
                                        </span>

                                    </div>


                                    <strong>
                                        {
                                            formatPrice(
                                                item.subtotal
                                            )
                                        }
                                    </strong>

                                </article>

                            )
                        )}

                    </div>

                </section>


                {/* =========================
                    SHIPPING
                ========================= */}

                <section className="order-detail__card">

                    <div className="order-detail__card-header">

                        <h2>
                            Shipping information
                        </h2>

                    </div>


                    <div className="order-detail__shipping">

                        <div>

                            <span>
                                Customer
                            </span>

                            <strong>
                                {order.customerName}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Phone
                            </span>

                            <strong>
                                {order.phone}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Email
                            </span>

                            <strong>
                                {order.email}
                            </strong>

                        </div>


                        <div>

                            <span>
                                Address
                            </span>

                            <strong>
                                {order.address}
                            </strong>

                        </div>


                        <div>

                            <span>
                                District
                            </span>

                            <strong>
                                {order.district}
                            </strong>

                        </div>


                        <div>

                            <span>
                                City
                            </span>

                            <strong>
                                {order.city}
                            </strong>

                        </div>

                    </div>

                </section>


                {/* =========================
                    SUMMARY
                ========================= */}

                <section className="order-detail__summary">

                    <div>

                        <span>
                            Subtotal
                        </span>

                        <strong>
                            {formatPrice(
                                order.subtotal
                            )}
                        </strong>

                    </div>


                    <div>

                        <span>
                            Shipping
                        </span>

                        <strong>
                            {order.shippingFee === 0
                                ? "Free"
                                : formatPrice(
                                    order.shippingFee
                                )
                            }
                        </strong>

                    </div>


                    <div>

                        <span>
                            Discount
                        </span>

                        <strong>
                            {formatPrice(
                                order.discount
                            )}
                        </strong>

                    </div>


                    <div className="order-detail__summary-total">

                        <span>
                            Total
                        </span>

                        <strong>
                            {formatPrice(
                                order.totalAmount
                            )}
                        </strong>

                    </div>

                </section>

            </div>

        </main>

    );

}


export default OrderDetail;