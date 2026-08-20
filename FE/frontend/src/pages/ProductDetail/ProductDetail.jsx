import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

import { mockProducts } from "../../data/mockProducts";
import useCart from "../../hooks/useCart";

import "./ProductDetail.scss";


function ProductDetail() {

    const { id } = useParams();

    const navigate = useNavigate();

    const { addToCart } = useCart();

    const product = mockProducts.find(
        item => item.id === Number(id)
    );

    const [quantity, setQuantity] = useState(1);


    const formatPrice = (price) => {

        return new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
        }).format(price);

    };


    const increaseQuantity = () => {

        setQuantity(prev =>
            Math.min(prev + 1, product.stock)
        );

    };


    const decreaseQuantity = () => {

        setQuantity(prev =>
            Math.max(prev - 1, 1)
        );

    };


    const handleAddToCart = () => {

        if (!product || product.stock <= 0) {
            return;
        }

        for (let i = 0; i < quantity; i++) {
            addToCart(product);
        }

        navigate("/cart");

    };


    const handleBuyNow = () => {

        if (!product || product.stock <= 0) {
            return;
        }

        for (let i = 0; i < quantity; i++) {
            addToCart(product);
        }

        navigate("/cart");

    };


    if (!product) {

        return (

            <main className="product-detail">

                <div className="product-detail__not-found">

                    <h1>
                        Product not found
                    </h1>

                    <button
                        type="button"
                        onClick={() => navigate("/products")}
                    >
                        Back to products
                    </button>

                </div>

            </main>

        );
    }


    return (

        <main className="product-detail">

            {/* ================= BREADCRUMB ================= */}

            <div className="product-detail__breadcrumb">

                <button
                    type="button"
                    onClick={() => navigate("/")}
                >
                    Home
                </button>

                <span>/</span>

                <button
                    type="button"
                    onClick={() =>
                        navigate(
                            `/categories/${product.category}`
                        )
                    }
                >
                    {product.category}
                </button>

                <span>/</span>

                <span>
                    {product.name}
                </span>

            </div>


            {/* ================= PRODUCT ================= */}

            <section className="product-detail__main">

                {/* ================= IMAGE ================= */}

                <div className="product-detail__gallery">

                    <div className="product-detail__image">

                        {product.discount > 0 && (

                            <span className="product-detail__discount">
                                -{product.discount}%
                            </span>

                        )}

                        {product.isNew && (

                            <span className="product-detail__new">
                                NEW
                            </span>

                        )}

                        <img
                            src={product.image}
                            alt={product.name}
                        />

                    </div>

                </div>


                {/* ================= INFORMATION ================= */}

                <div className="product-detail__info">

                    <span className="product-detail__category">
                        {product.category}
                    </span>


                    <h1 className="product-detail__name">
                        {product.name}
                    </h1>


                    {/* Rating */}

                    <div className="product-detail__rating">

                        <span className="product-detail__stars">
                            ★
                        </span>

                        <strong>
                            {product.rating}
                        </strong>

                        <span>
                            ({product.reviewCount} reviews)
                        </span>

                    </div>


                    {/* Price */}

                    <div className="product-detail__price">

                        <strong>
                            {formatPrice(product.price)}
                        </strong>

                        {product.oldPrice && (

                            <del>
                                {formatPrice(product.oldPrice)}
                            </del>

                        )}

                    </div>


                    {/* Stock */}

                    <div className="product-detail__stock">

                        {product.stock > 0 ? (

                            <>
                                <span className="product-detail__stock-dot" />

                                <span>
                                    In stock
                                </span>

                                <span>
                                    ({product.stock} available)
                                </span>
                            </>

                        ) : (

                            <span>
                                Out of stock
                            </span>

                        )}

                    </div>


                    <div className="product-detail__divider" />


                    {/* ================= QUANTITY ================= */}

                    <div className="product-detail__quantity">

                        <span>
                            Quantity
                        </span>

                        <div className="product-detail__quantity-control">

                            <button
                                type="button"
                                onClick={decreaseQuantity}
                                disabled={quantity <= 1}
                            >
                                −
                            </button>

                            <span>
                                {quantity}
                            </span>

                            <button
                                type="button"
                                onClick={increaseQuantity}
                                disabled={
                                    quantity >= product.stock
                                }
                            >
                                +
                            </button>

                        </div>

                    </div>


                    {/* ================= ACTIONS ================= */}

                    <div className="product-detail__actions">

                        <button
                            type="button"
                            className="product-detail__add-cart"
                            disabled={product.stock <= 0}
                            onClick={handleAddToCart}
                        >
                            Add to cart
                        </button>

                        <button
                            type="button"
                            className="product-detail__buy-now"
                            disabled={product.stock <= 0}
                            onClick={handleBuyNow}
                        >
                            Buy now
                        </button>

                    </div>


                    {/* ================= BENEFITS ================= */}

                    <div className="product-detail__benefits">

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
                                Your payment is protected
                            </span>

                        </div>


                        <div>

                            <strong>
                                Easy returns
                            </strong>

                            <span>
                                7-day return policy
                            </span>

                        </div>

                    </div>

                </div>

            </section>


            {/* ================= DESCRIPTION ================= */}

            <section className="product-detail__description">

                <div className="product-detail__section-header">

                    <span>
                        PRODUCT DETAILS
                    </span>

                    <h2>
                        About this product
                    </h2>

                </div>

                <p>
                    Discover premium technology designed
                    to deliver an exceptional experience.
                    This product combines performance,
                    reliability and modern design for
                    everyday use.
                </p>

            </section>

        </main>

    );

}

export default ProductDetail;