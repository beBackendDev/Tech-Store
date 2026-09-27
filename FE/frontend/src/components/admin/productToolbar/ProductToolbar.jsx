import "./ProductToolbar.scss";


function ProductToolbar({
    keyword,
    category,
    brand,
    minPrice,
    maxPrice,
    minRating,
    active,
    sort,

    onKeywordChange,
    onCategoryChange,
    onBrandChange,
    onMinPriceChange,
    onMaxPriceChange,
    onMinRatingChange,
    onActiveChange,
    onSortChange,
    onReset
}) {

    return (

        <section className="product-toolbar">

            {/* SEARCH */}

            <div className="product-toolbar__search">

                <input
                    type="text"
                    value={keyword}
                    placeholder="Search products..."
                    onChange={event =>
                        onKeywordChange(
                            event.target.value
                        )
                    }
                />

            </div>


            {/* FILTERS */}

            <div className="product-toolbar__filters">

                <select
                    value={category}
                    onChange={event =>
                        onCategoryChange(
                            event.target.value
                        )
                    }
                >

                    <option value="">
                        All categories
                    </option>

                    <option value="Laptop">
                        Laptop
                    </option>

                    <option value="Mobile">
                        Mobile
                    </option>

                    <option value="PC Component">
                        PC Component
                    </option>

                    <option value="Accessory">
                        Accessory
                    </option>

                </select>


                <input
                    type="text"
                    value={brand}
                    placeholder="Brand"
                    onChange={event =>
                        onBrandChange(
                            event.target.value
                        )
                    }
                />


                <input
                    type="number"
                    min="0"
                    value={minPrice}
                    placeholder="Min price"
                    onChange={event =>
                        onMinPriceChange(
                            event.target.value
                        )
                    }
                />


                <input
                    type="number"
                    min="0"
                    value={maxPrice}
                    placeholder="Max price"
                    onChange={event =>
                        onMaxPriceChange(
                            event.target.value
                        )
                    }
                />


                <select
                    value={minRating}
                    onChange={event =>
                        onMinRatingChange(
                            event.target.value
                        )
                    }
                >

                    <option value="">
                        Rating
                    </option>

                    <option value="4">
                        4+ stars
                    </option>

                    <option value="3">
                        3+ stars
                    </option>

                    <option value="2">
                        2+ stars
                    </option>

                    <option value="1">
                        1+ stars
                    </option>

                </select>


                <select
                    value={active}
                    onChange={event =>
                        onActiveChange(
                            event.target.value
                        )
                    }
                >

                    <option value="">
                        All status
                    </option>

                    <option value="true">
                        Active
                    </option>

                    <option value="false">
                        Inactive
                    </option>

                </select>


                <select
                    value={sort}
                    onChange={event =>
                        onSortChange(
                            event.target.value
                        )
                    }
                >

                    <option value="createdAt,desc">
                        Newest
                    </option>

                    <option value="createdAt,asc">
                        Oldest
                    </option>

                    <option value="name,asc">
                        Name A-Z
                    </option>

                    <option value="name,desc">
                        Name Z-A
                    </option>

                    <option value="price,asc">
                        Price low-high
                    </option>

                    <option value="price,desc">
                        Price high-low
                    </option>

                    <option value="rating,desc">
                        Rating high-low
                    </option>

                    <option value="reviewCount,desc">
                        Most reviewed
                    </option>

                    <option value="stock,asc">
                        Stock low-high
                    </option>

                </select>


                <button
                    type="button"
                    onClick={onReset}
                >
                    Reset
                </button>

            </div>

        </section>
    );
}


export default ProductToolbar;