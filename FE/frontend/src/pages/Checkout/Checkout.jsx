import { useState } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "../../hooks/useAuth";

import useCart from "../../hooks/useCart";

import "./Checkout.scss";


function Checkout() {

    const navigate = useNavigate();

    const { auth } = useAuth();
    const [isSubmitting, setIsSubmitting] = useState(false);

    const {
        items,
        subtotal,
        totalItems
    } = useCart();


    const [formData, setFormData] = useState({

        firstName: "",
        lastName: "",
        phone: "",
        email: auth.user?.email || "",
        address: "",
        city: "",
        district: "",
        note: ""

    });


    const [paymentMethod, setPaymentMethod] =
        useState("COD");


    const formatPrice = (price) => {

        return new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
        }).format(price);

    };


    const shippingFee =
        subtotal >= 500000
            ? 0
            : 30000;


    const total =
        subtotal + shippingFee;


    const handleChange = (event) => {

        const {
            name,
            value
        } = event.target;

        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

    };


    const handleSubmit = (event) => {

        event.preventDefault();
        if (isSubmitting) {
            return;
        }

        setIsSubmitting(true);
        try {
            const orderData = {

                shipping: {
                    firstName: formData.firstName.trim(),
                    lastName: formData.lastName.trim(),
                    phone: formData.phone.trim(),
                    email: formData.email.trim(),
                    address: formData.address.trim(),
                    city: formData.city,
                    district: formData.district,
                    note: formData.note.trim()
                },

                paymentMethod,

                items: items.map(item => ({
                    productId: item.product.id,
                    quantity: item.quantity
                })),

                subtotal,
                shippingFee,
                total
            };
            console.log(
                "Checkout:",
                orderData
            );

            /*
 * Sau này:
 *
 * await createOrder(...)
 *
 */

        } catch (error) {

            console.error(
                "Create order failed:",
                error
            );

        } finally {

            setIsSubmitting(false);

        }




    };


    /*
     * Không có sản phẩm
     */

    if (items.length === 0) {

        return (

            <main className="checkout-page">

                <div className="checkout-page__empty">

                    <h1>
                        Your cart is empty
                    </h1>

                    <p>
                        Add some products before
                        proceeding to checkout.
                    </p>

                    <button
                        type="button"
                        onClick={() => navigate("/products")}
                    >
                        Continue shopping
                    </button>

                </div>

            </main>

        );

    }


    return (

        <main className="checkout-page">

            <div className="checkout-page__container">

                {/* ================= HEADER ================= */}

                <header className="checkout-page__header">

                    <button
                        type="button"
                        onClick={() => navigate("/cart")}
                        className="checkout-page__back"
                    >
                        ← Back to cart
                    </button>

                    <div>

                        <span className="checkout-page__eyebrow">
                            CHECKOUT
                        </span>

                        <h1>
                            Complete your order
                        </h1>

                        <p>
                            Review your information
                            and place your order.
                        </p>

                    </div>

                </header>


                {/* ================= CONTENT ================= */}

                <form
                    className="checkout-page__layout"
                    onSubmit={handleSubmit}
                >

                    {/* ================= LEFT ================= */}

                    <div className="checkout-page__main">


                        {/* SHIPPING */}

                        <section className="checkout-card">

                            <div className="checkout-card__header">

                                <div>

                                    <span className="checkout-card__number">
                                        01
                                    </span>

                                    <div>

                                        <h2>
                                            Shipping information
                                        </h2>

                                        <p>
                                            Where should we deliver
                                            your order?
                                        </p>

                                    </div>

                                </div>

                            </div>


                            <div className="checkout-form">


                                <div className="checkout-form__row">

                                    <div className="checkout-field">

                                        <label>
                                            First name
                                        </label>

                                        <input
                                            type="text"
                                            name="firstName"
                                            value={formData.firstName}
                                            onChange={handleChange}
                                            placeholder="John"
                                            required
                                        />

                                    </div>


                                    <div className="checkout-field">

                                        <label>
                                            Last name
                                        </label>

                                        <input
                                            type="text"
                                            name="lastName"
                                            value={formData.lastName}
                                            onChange={handleChange}
                                            placeholder="Doe"
                                            required
                                        />

                                    </div>

                                </div>


                                <div className="checkout-form__row">

                                    <div className="checkout-field">

                                        <label>
                                            Phone number
                                        </label>

                                        <input
                                            type="tel"
                                            name="phone"
                                            value={formData.phone}
                                            onChange={handleChange}
                                            placeholder="0912 345 678"
                                            required
                                        />

                                    </div>


                                    <div className="checkout-field">

                                        <label>
                                            Email
                                        </label>

                                        <input
                                            type="email"
                                            name="email"
                                            value={formData.email}
                                            onChange={handleChange}
                                            placeholder="john@example.com"
                                            required
                                        />

                                    </div>

                                </div>


                                <div className="checkout-field">

                                    <label>
                                        Address
                                    </label>

                                    <input
                                        type="text"
                                        name="address"
                                        value={formData.address}
                                        onChange={handleChange}
                                        placeholder="Street address"
                                        required
                                    />

                                </div>


                                <div className="checkout-form__row">

                                    <div className="checkout-field">

                                        <label>
                                            City
                                        </label>

                                        <select
                                            name="city"
                                            value={formData.city}
                                            onChange={handleChange}
                                            required
                                        >

                                            <option value="">
                                                Select city
                                            </option>

                                            <option value="hcm">
                                                Ho Chi Minh City
                                            </option>

                                            <option value="hanoi">
                                                Hanoi
                                            </option>

                                            <option value="danang">
                                                Da Nang
                                            </option>

                                        </select>

                                    </div>


                                    <div className="checkout-field">

                                        <label>
                                            District
                                        </label>

                                        <input
                                            type="text"
                                            name="district"
                                            value={formData.district}
                                            onChange={handleChange}
                                            placeholder="District"
                                            required
                                        />

                                    </div>

                                </div>


                                <div className="checkout-field">

                                    <label>
                                        Order note
                                        <span>
                                            (optional)
                                        </span>
                                    </label>

                                    <textarea
                                        name="note"
                                        value={formData.note}
                                        onChange={handleChange}
                                        placeholder="Any special instructions?"
                                        rows="4"
                                    />

                                </div>

                            </div>

                        </section>


                        {/* PAYMENT */}

                        <section className="checkout-card">

                            <div className="checkout-card__header">

                                <div>

                                    <span className="checkout-card__number">
                                        02
                                    </span>

                                    <div>

                                        <h2>
                                            Payment method
                                        </h2>

                                        <p>
                                            Choose how you want to pay.
                                        </p>

                                    </div>

                                </div>

                            </div>


                            <div className="checkout-payment">


                                {/* COD */}

                                <label
                                    className={`checkout-payment__option ${paymentMethod === "cod"
                                        ? "checkout-payment__option--active"
                                        : ""
                                        }`}
                                >

                                    <input
                                        type="radio"
                                        name="payment"
                                        value="COD"
                                        checked={
                                            paymentMethod === "COD"
                                        }
                                        onChange={(event) =>
                                            setPaymentMethod(
                                                event.target.value
                                            )
                                        }
                                    />

                                    <div>

                                        <strong>
                                            Cash on delivery
                                        </strong>

                                        <span>
                                            Pay when your order arrives.
                                        </span>

                                    </div>

                                </label>


                                {/* BANK */}

                                <label
                                    className={`checkout-payment__option ${paymentMethod === "bank"
                                        ? "checkout-payment__option--active"
                                        : ""
                                        }`}
                                >

                                    <input
                                        type="radio"
                                        name="payment"
                                        value="BANK_TRANSFER"
                                        checked={
                                            paymentMethod === "BANK_TRANSFER"
                                        }
                                        onChange={(event) =>
                                            setPaymentMethod(
                                                event.target.value
                                            )
                                        }
                                    />

                                    <div>

                                        <strong>
                                            Bank transfer
                                        </strong>

                                        <span>
                                            Transfer directly to our bank account.
                                        </span>

                                    </div>

                                </label>


                                {/* MOCK */}

                                <label
                                    className={`checkout-payment__option ${paymentMethod === "online"
                                        ? "checkout-payment__option--active"
                                        : ""
                                        }`}
                                >

                                    <input
                                        type="radio"
                                        name="payment"
                                        value="ONLINE"
                                        checked={
                                            paymentMethod === "ONLINE"
                                        }
                                        onChange={(event) =>
                                            setPaymentMethod(
                                                event.target.value
                                            )
                                        }
                                    />

                                    <div>

                                        <strong>
                                            Online payment
                                        </strong>

                                        <span>
                                            Secure online payment.
                                        </span>

                                    </div>

                                </label>

                            </div>

                        </section>

                    </div>


                    {/* ================= RIGHT ================= */}

                    <aside className="checkout-page__sidebar">


                        {/* ORDER SUMMARY */}

                        <section className="checkout-card checkout-summary">

                            <div className="checkout-summary__header">

                                <div>

                                    <h2>
                                        Order summary
                                    </h2>

                                    <span>
                                        {totalItems} items
                                    </span>

                                </div>

                            </div>


                            {/* ITEMS */}

                            <div className="checkout-summary__items">

                                {items.map(item => (

                                    <div
                                        className="checkout-summary__item"
                                        key={item.product.id}
                                    >

                                        <div className="checkout-summary__image">

                                            <img
                                                src={item.product.image}
                                                alt={item.product.name}
                                            />

                                            <span>
                                                {item.quantity}
                                            </span>

                                        </div>


                                        <div className="checkout-summary__product">

                                            <strong>
                                                {item.product.name}
                                            </strong>

                                            <span>
                                                {item.product.category}
                                            </span>

                                        </div>


                                        <strong>
                                            {formatPrice(
                                                item.product.price *
                                                item.quantity
                                            )}
                                        </strong>

                                    </div>

                                ))}

                            </div>


                            {/* TOTAL */}

                            <div className="checkout-summary__totals">

                                <div>

                                    <span>
                                        Subtotal
                                    </span>

                                    <strong>
                                        {formatPrice(subtotal)}
                                    </strong>

                                </div>


                                <div>

                                    <span>
                                        Shipping
                                    </span>

                                    <strong>

                                        {shippingFee === 0
                                            ? "Free"
                                            : formatPrice(shippingFee)
                                        }

                                    </strong>

                                </div>

                            </div>


                            <div className="checkout-summary__total">

                                <span>
                                    Total
                                </span>

                                <strong>
                                    {formatPrice(total)}
                                </strong>

                            </div>


                            <button
                                type="submit"
                                className="checkout-summary__submit"
                                disabled={isSubmitting}
                            >
                                {isSubmitting
                                    ? "Processing..."
                                    : "Place order"
                                }
                            </button>


                            <p className="checkout-summary__secure">
                                🔒 Secure checkout
                            </p>

                        </section>


                        {/* BENEFITS */}

                        <div className="checkout-benefits">

                            <div>

                                <strong>
                                    Free shipping
                                </strong>

                                <span>
                                    On orders over 500,000₫
                                </span>

                            </div>

                            <div>

                                <strong>
                                    Secure payment
                                </strong>

                                <span>
                                    Your payment information is protected.
                                </span>

                            </div>

                            <div>

                                <strong>
                                    Easy returns
                                </strong>

                                <span>
                                    7-day return policy.
                                </span>

                            </div>

                        </div>

                    </aside>

                </form>

            </div>

        </main>

    );

}

export default Checkout;