import ProductCard from "../ProductCard/ProductCard";
import "./ProductGrid.scss";

function ProductGrid({ products, onAddToCart }) {
    if (!products || products.length === 0) {
        return (
            <div className="product-grid__empty">
                <h3>No products found</h3>

                <p>
                    Try changing your filters or search keywords.
                </p>
            </div>
        );
    }
    return (
        <div className="product-grid">

            {products.map(product => (
                <ProductCard
                    key={product.id}
                    product={product}
                    onAddToCart={onAddToCart}
                />
            ))}

        </div>
    );
}

export default ProductGrid;