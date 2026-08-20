import {
    LuMinus,
    LuPlus,
    LuTrash2
} from "react-icons/lu";

import "./CartItem.scss";


function CartItem({
    item,
    onIncrease,
    onDecrease,
    onRemove
}) {

    const {
        product,
        quantity
    } = item;


    const formatPrice = (price) => {

        return new Intl.NumberFormat(
            "vi-VN",
            {
                style: "currency",
                currency: "VND"
            }
        ).format(price);

    };


    const totalPrice =
        product.price * quantity;


    return (

        <article className="cart-item">

            {/* ================= IMAGE ================= */}

            <div className="cart-item__image">

                <img
                    src={product.image}
                    alt={product.name}
                />

            </div>


            {/* ================= INFORMATION ================= */}

            <div className="cart-item__info">

                <span className="cart-item__category">
                    {product.category}
                </span>

                <h3 className="cart-item__name">
                    {product.name}
                </h3>

                <span className="cart-item__unit-price">
                    {formatPrice(product.price)}
                </span>

            </div>


            {/* ================= QUANTITY ================= */}

            <div className="cart-item__quantity">

                <button
                    type="button"
                    onClick={() =>
                        onDecrease(product.id)
                    }
                    aria-label="Decrease quantity"
                >
                    <LuMinus />
                </button>

                <span>
                    {quantity}
                </span>

                <button
                    type="button"
                    onClick={() =>
                        onIncrease(product.id)
                    }
                    disabled={
                        quantity >= product.stock
                    }
                    aria-label="Increase quantity"
                >
                    <LuPlus />
                </button>

            </div>


            {/* ================= TOTAL ================= */}

            <div className="cart-item__total">

                <strong>
                    {formatPrice(totalPrice)}
                </strong>

            </div>


            {/* ================= REMOVE ================= */}

            <button
                type="button"
                className="cart-item__remove"
                onClick={() =>
                    onRemove(product.id)
                }
                aria-label={`Remove ${product.name}`}
            >
                <LuTrash2 />
            </button>

        </article>

    );
}


export default CartItem;