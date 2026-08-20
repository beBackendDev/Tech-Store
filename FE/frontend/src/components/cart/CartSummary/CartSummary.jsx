import {
    LuArrowRight,
    LuTruck
} from "react-icons/lu";

import "./CartSummary.scss";


function CartSummary({
    subtotal,
    totalItems,
    onCheckout
}) {

    const formatPrice = (price) => {

        return new Intl.NumberFormat(
            "vi-VN",
            {
                style: "currency",
                currency: "VND"
            }
        ).format(price);

    };


    /*
     * Tạm thời:
     *
     * >= 1.000.000đ
     * → miễn phí ship
     *
     * < 1.000.000đ
     * → 30.000đ
     *
     * Sau này BE sẽ xử lý
     * shipping thực tế.
     */

    const shipping =
        subtotal >= 1000000
            ? 0
            : 30000;


    const total =
        subtotal + shipping;


    return (

        <aside className="cart-summary">

            <div className="cart-summary__header">

                <h2>
                    Order Summary
                </h2>

            </div>


            {/* ================= ITEMS ================= */}

            <div className="cart-summary__row">

                <span>
                    Subtotal
                </span>

                <strong>
                    {formatPrice(subtotal)}
                </strong>

            </div>


            <div className="cart-summary__row">

                <span>
                    Items
                </span>

                <strong>
                    {totalItems}
                </strong>

            </div>


            <div className="cart-summary__row">

                <span className="cart-summary__shipping">

                    <LuTruck />

                    Shipping

                </span>

                <strong>

                    {shipping === 0
                        ? "Free"
                        : formatPrice(shipping)
                    }

                </strong>

            </div>


            {/* ================= DIVIDER ================= */}

            <div className="cart-summary__divider" />


            {/* ================= TOTAL ================= */}

            <div className="cart-summary__total">

                <span>
                    Total
                </span>

                <strong>
                    {formatPrice(total)}
                </strong>

            </div>


            {/* ================= CHECKOUT ================= */}

            <button
                type="button"
                className="cart-summary__checkout"
                onClick={onCheckout}
            >

                Proceed to checkout

                <LuArrowRight />

            </button>


            {/* ================= NOTE ================= */}

            <p className="cart-summary__note">

                Free shipping on orders
                over 1.000.000đ.

            </p>

        </aside>

    );
}


export default CartSummary;