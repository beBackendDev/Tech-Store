import { useNavigate, useLocation, useSearchParams } from "react-router-dom";

import "./OrderSuccess.scss";


function OrderSuccess() {

    const navigate = useNavigate();
    const location = useLocation();

    const order = location.state?.order;
    const [searchParams] = useSearchParams();
    const orderId = searchParams.get("orderId");
    const handleViewOrder = () => { if (!orderId) { return; } navigate(`/orders/${orderId}`); };
    const handleContinueShopping = () => { navigate("/products"); };

    return (

        <main className="order-success">

            <section className="order-success__container">

                <div className="order-result__icon">
                    ✓
                </div>

                <span className="order-result__eyebrow">
                    ORDER CONFIRMED
                </span>

                <h1>
                    Order placed successfully
                </h1>

                <p>
                    Thank you for your purchase.
                    Your order has been successfully created.
                </p>

                {order?.id && (

                    <div className="order-result__order-id">

                        <span>
                            Order number
                        </span>

                        <strong>
                            #{order.id}
                        </strong>

                    </div>

                )}

                <div className="order-result__actions">

                    <button
                        type="button"
                        onClick={handleViewOrder}
                        disabled={!orderId}
                    >
                        View my orders
                    </button>

                    <button
                        type="button"
                        onClick={handleContinueShopping}
                    >
                        Continue shopping
                    </button>

                </div>

            </section>

        </main>

    );

}

export default OrderSuccess;