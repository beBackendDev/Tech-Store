import "./ProductSort.scss";


function ProductSort({
    value,
    onChange
}) {

    return (
        <div className="product-sort">

            <label htmlFor="product-sort">
                Sort by
            </label>

            <select
                id="product-sort"
                value={value}
                onChange={event =>
                    onChange(event.target.value)
                }
            >

                <option value="default">
                    Featured
                </option>

                <option value="price-low">
                    Price: Low to High
                </option>

                <option value="price-high">
                    Price: High to Low
                </option>

                <option value="rating">
                    Highest Rated
                </option>

                <option value="newest">
                    Newest
                </option>

            </select>

        </div>
    );
}

export default ProductSort;