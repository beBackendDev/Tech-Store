import { useNavigate, useLocation } from "react-router-dom";

import "./OrderFailed.scss";


function OrderFailed() {

    const navigate = useNavigate();
    const location = useLocation();

    const message =
        location.state?.message ||
        "We were unable to process your order.";


    return (

        <main className="order-result">

            <section className="order-result__card">

                <div className="order-result__icon">
                    !
                </div>

                <span className="order-result__eyebrow">
                    ORDER FAILED
                </span>

                <h1>
                    Something went wrong
                </h1>

                <p>
                    {message}
                </p>

                <div className="order-result__actions">

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/checkout")
                        }
                    >
                        Try again
                    </button>

                    <button
                        type="button"
                        onClick={() =>
                            navigate("/products")
                        }
                    >
                        Continue shopping
                    </button>

                </div>

            </section>

        </main>

    );

}

export default OrderFailed;