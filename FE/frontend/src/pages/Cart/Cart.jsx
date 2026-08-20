import {
    useNavigate
} from "react-router-dom";

import {
    LuArrowLeft,
    LuShoppingCart
} from "react-icons/lu";



import CartItem
    from "../../components/cart/CartItem/CartItem";

import CartSummary
    from "../../components/cart/CartSummary/CartSummary";

import useCart
    from "../../hooks/useCart";

import "./Cart.scss";


function Cart() {

    const navigate = useNavigate();

    const {
        items,
        totalItems,
        subtotal,
        increaseQuantity,
        decreaseQuantity,
        removeFromCart
    } = useCart();


    const handleCheckout = () => {

        navigate("/checkout");

    };


    /*
     * =========================
     * EMPTY CART
     * =========================
     */

    if (items.length === 0) {

        return (

            <main className="cart-page">


                    <section className="cart-page__empty">

                        <div className="cart-page__empty-icon">

                            <LuShoppingCart />

                        </div>

                        <h1>
                            Your cart is empty
                        </h1>

                        <p>
                            Looks like you haven't
                            added anything to your cart yet.
                        </p>

                        <button
                            type="button"
                            onClick={() =>
                                navigate("/products")
                            }
                        >
                            Start shopping
                        </button>

                    </section>


            </main>

        );
    }


    /*
     * =========================
     * CART
     * =========================
     */

    return (

        <main className="cart-page">


                {/* ================= HEADER ================= */}

                <div className="cart-page__header">

                    <div>

                        <span className="cart-page__eyebrow">
                            SHOPPING CART
                        </span>

                        <h1>
                            Your Cart
                        </h1>

                        <p>
                            {totalItems}{" "}
                            {totalItems === 1
                                ? "item"
                                : "items"
                            }{" "}
                            in your cart
                        </p>

                    </div>

                </div>


                {/* ================= CONTENT ================= */}

                <div className="cart-page__content">


                    {/* ================= ITEMS ================= */}

                    <section className="cart-page__items">

                        <div className="cart-page__items-header">

                            <span>
                                Product
                            </span>

                            <span>
                                Quantity
                            </span>

                            <span>
                                Total
                            </span>

                        </div>


                        <div className="cart-page__items-list">

                            {items.map(item => (

                                <CartItem
                                    key={item.product.id}
                                    item={item}
                                    onIncrease={increaseQuantity}
                                    onDecrease={decreaseQuantity}
                                    onRemove={removeFromCart}
                                />

                            ))}

                        </div>


                        {/* Continue shopping */}

                        <button
                            type="button"
                            className="cart-page__continue"
                            onClick={() =>
                                navigate("/products")
                            }
                        >

                            <LuArrowLeft />

                            Continue shopping

                        </button>

                    </section>


                    {/* ================= SUMMARY ================= */}

                    <CartSummary
                        subtotal={subtotal}
                        totalItems={totalItems}
                        onCheckout={handleCheckout}
                    />

                </div>


        </main>

    );
}


export default Cart;