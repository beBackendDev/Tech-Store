import ProductGrid from "../ProductGrid/ProductGrid";
import "./ProductSection.scss";

function ProductSection({
    title,
    subtitle,
    products,
    onAddToCart,
}) {

    return (
        <section className="product-section">

            <div className="product-section__header">

                <div>
                    <h2>{title}</h2>

                    {subtitle && (
                        <p>{subtitle}</p>
                    )}
                </div>

                <button>
                    View all
                </button>

            </div>

            <ProductGrid
                products={products}
                onAddToCart={onAddToCart}
            />

        </section>
    );
}

export default ProductSection;