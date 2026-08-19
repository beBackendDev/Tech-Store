import { useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";

import ProductCard
    from "../../components/product/ProductCard/ProductCard";

import { mockProducts }
    from "../../data/mockProducts";

import { mockCategories }
    from "../../data/mockCategories";

import "./Categories.scss";


function Categories() {

    const navigate = useNavigate();

    const [searchParams] = useSearchParams();

    const initialCategory =
        searchParams.get("category") || "All";


    const [selectedCategory, setSelectedCategory] =
        useState(initialCategory);

    const [sortBy, setSortBy] =
        useState("default");


    const [search, setSearch] =
        useState("");


    // ==============================
    // FILTER + SORT
    // ==============================

    const filteredProducts = useMemo(() => {
//thuc hien tren mockProductsz
        let products = [...mockProducts];


        // Category

        if (selectedCategory !== "All") {

            products = products.filter(
                product =>
                    product.category.toLowerCase() ===
                    selectedCategory.toLowerCase()
            );

        }


        // Search

        if (search.trim()) {

            const keyword =
                search.toLowerCase().trim();

            products = products.filter(product =>
                product.category
                    .toLowerCase()
                    .includes(keyword)
            );

        }


        // Sort

        switch (sortBy) {

            case "price-low":

                products.sort(
                    (a, b) => a.price - b.price
                );

                break;


            case "price-high":

                products.sort(
                    (a, b) => b.price - a.price
                );

                break;


            case "rating":

                products.sort(
                    (a, b) => b.rating - a.rating
                );

                break;


            case "newest":

                products.sort(
                    (a, b) =>
                        Number(b.isNew) -
                        Number(a.isNew)
                );

                break;


            default:
                break;
        }


        return products;

    }, [
        selectedCategory,
        search,
        sortBy
    ]);


    // ==============================
    // HANDLERS
    // ==============================

    const handleCategoryChange = (category) => {

        setSelectedCategory(category);

        if (category === "All") {

            navigate("/categories");

        } else {

            navigate(
                `/categories?category=${encodeURIComponent(category)}`
            );

        }

    };


    const handleAddToCart = (product) => {

        console.log(
            "Add to cart:",
            product
        );

    };


    return (

        <main className="categories-page">

            {/* =========================
                HEADER
            ========================= */}

            <section className="categories-page__header">

                <span className="categories-page__eyebrow">
                    COLLECTION
                </span>

                <h1>
                    Shop all products
                </h1>

                <p>
                    Explore our complete collection
                    of technology products.
                </p>

            </section>


            {/* =========================
                TOOLBAR
            ========================= */}

            <section className="categories-page__toolbar">

                {/* Search */}

                <div className="categories-page__search">

                    <span>
                        ⌕
                    </span>

                    <input
                        type="search"
                        placeholder="Search products..."
                        value={search}
                        onChange={(event) =>
                            setSearch(event.target.value)
                        }
                    />

                </div>


                {/* Sort */}

                <div className="categories-page__sort">

                    <label htmlFor="sort">
                        Sort by
                    </label>

                    <select
                        id="sort"
                        value={sortBy}
                        onChange={(event) =>
                            setSortBy(event.target.value)
                        }
                    >

                        <option value="default">
                            Featured
                        </option>

                        <option value="newest">
                            Newest
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

                    </select>

                </div>

            </section>


            {/* =========================
                CATEGORY FILTER
            ========================= */}

            <section className="categories-page__content">

                <aside className="categories-page__sidebar">

                    <div className="categories-page__filter-header">

                        <span>
                            CATEGORIES
                        </span>

                    </div>


                    <button
                        type="button"
                        className={
                            selectedCategory === "All"
                                ? "is-active"
                                : ""
                        }
                        onClick={() =>
                            handleCategoryChange("All")
                        }
                    >
                        All products
                    </button>


                    {mockCategories.map(category => (

                        <button
                            type="button"
                            key={category.id}
                            className={
                                selectedCategory === category.name
                                    ? "is-active"
                                    : ""
                            }
                            onClick={() =>
                                handleCategoryChange(
                                    category.name
                                )
                            }
                        >

                            <span>
                                {category.name}
                            </span>

                        </button>

                    ))}

                </aside>


                {/* =========================
                    PRODUCTS
                ========================= */}

                <div className="categories-page__products">

                    <div className="categories-page__result">

                        <span>
                            {filteredProducts.length}
                            {" "}
                            products
                        </span>

                    </div>


                    {filteredProducts.length > 0 ? (

                        <div className="categories-page__grid">

                            {filteredProducts.map(product => (

                                <ProductCard
                                    key={product.id}
                                    product={product}
                                    onAddToCart={
                                        handleAddToCart
                                    }
                                />

                            ))}

                        </div>

                    ) : (

                        <div className="categories-page__empty">

                            <div>
                                ∅
                            </div>

                            <h2>
                                No products found
                            </h2>

                            <p>
                                Try changing your search
                                or category filter.
                            </p>

                            <button
                                type="button"
                                onClick={() => {

                                    setSearch("");
                                    handleCategoryChange("All");

                                }}
                            >
                                Clear filters
                            </button>

                        </div>

                    )}

                </div>

            </section>

        </main>

    );

}

export default Categories;