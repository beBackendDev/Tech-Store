import { useNavigate } from "react-router-dom";
import "./ProductCard.scss";

function ProductCard({ product, onAddToCart }) {
    const navigate = useNavigate();
    const formatPrice = (price) => {
        return new Intl.NumberFormat("vi-VN", {
            style: "currency",
            currency: "VND",
        }).format(price);
    };
    const handleProductClick = () => {
        navigate(`/products/${product.id}`);
    };
    return (
        <article className="product-card">

            {/* Product Image */}
            <div className="product-card__image"
                onClick={handleProductClick}>

                {product.discount > 0 && (
                    <span className="product-card__discount">
                        -{product.discount}%
                    </span>
                )}

                {product.isNew && (
                    <span className="product-card__new">
                        NEW
                    </span>
                )}

                <button
                    className="product-card__wishlist"
                    aria-label="Add to wishlist"
                >
                    ♡
                </button>

                <img
                    src={product.image}
                    alt={product.name}
                />

            </div>

            {/* Product information */}
            <div className="product-card__content">

                <span className="product-card__category">
                    {product.category}
                </span>

                <h3 className="product-card__name">
                    {product.name}
                </h3>

                <div className="product-card__rating">
                    <span>★</span>
                    <span>{product.rating}</span>
                    <span>
                        ({product.reviewCount})
                    </span>
                </div>

                <div className="product-card__price">

                    <strong>
                        {formatPrice(product.price)}
                    </strong>

                    {product.oldPrice && (
                        <del>
                            {formatPrice(product.oldPrice)}
                        </del>
                    )}

                </div>

                <button
                    className="product-card__cart"
                    disabled={product.stock <= 0}
                    onClick={() => onAddToCart?.(product)}
                >
                    {product.stock > 0
                        ? "Add to cart"
                        : "Out of stock"
                    }
                </button>

            </div>

        </article>
    );
}

export default ProductCard;