import Pagination from "../Pagination/pagination";
import ProductGrid from "../ProductGrid/ProductGrid";
import { useState } from "react";

import "./ProductSection.scss";

function ProductSection({
    title,
    subtitle,
    products,
    onAddToCart,
}
) {

    return (
        <section className="product-section">

            <div className="product-section__header">

                <div className="product-section__heading">

                    <h2>
                        {title}
                    </h2>

                    {subtitle && (
                        <p>
                            {subtitle}
                        </p>
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