import "./ProductFilter.scss";


function ProductFilter({
    categories,
    selectedCategory,
    onCategoryChange
}) {

    return (
        <aside className="product-filter">

            <div className="product-filter__header">

                <h3>
                    Filters
                </h3>

            </div>


            <div className="product-filter__section">

                <h4>
                    Category
                </h4>


                <div className="product-filter__categories">

                    <button
                        type="button"
                        className={
                            !selectedCategory
                                ? "active"
                                : ""
                        }
                        onClick={() =>
                            onCategoryChange("")
                        }
                    >
                        All products
                    </button>


                    {categories.map(category => (

                        <button
                            type="button"
                            key={category}
                            className={
                                selectedCategory === category
                                    ? "active"
                                    : ""
                            }
                            onClick={() =>
                                onCategoryChange(category)
                            }
                        >
                            {category}
                        </button>

                    ))}

                </div>

            </div>

        </aside>
    );
}

export default ProductFilter;