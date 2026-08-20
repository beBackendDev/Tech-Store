import { useMemo, useState } from "react";

import ProductGrid
    from "../../../components/product/ProductGrid/ProductGrid";

import ProductFilter
    from "../../../components/product/ProductFilter/ProductFilter";

import ProductSort
    from "../../../components/product/ProductSort/ProductSort";

import { mockProducts }
    from "../../../data/mockProducts";

import "./ProductListing.scss";


function ProductListing() {

    const [selectedCategory, setSelectedCategory] =
        useState("");

    const [sort, setSort] =
        useState("default");


    const handleAddToCart = (product) => {

        console.log(
            "Add to cart:",
            product
        );
    };


    /*
     * Lấy danh sách category
     */

    const categories = useMemo(() => {

        return [
            ...new Set(
                mockProducts.map(
                    product => product.category
                )
            )
        ];

    }, []);


    /*
     * Filter + Sort
     */

    const filteredProducts = useMemo(() => {

        let products = [...mockProducts];


        // Category

        if (selectedCategory) {

            products = products.filter(
                product =>
                    product.category === selectedCategory
            );
        }


        // Sort

        switch (sort) {

            case "price-low":

                products.sort(
                    (a, b) =>
                        a.price - b.price
                );

                break;


            case "price-high":

                products.sort(
                    (a, b) =>
                        b.price - a.price
                );

                break;


            case "rating":

                products.sort(
                    (a, b) =>
                        b.rating - a.rating
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
        sort
    ]);


    return (

        <main className="product-listing">

            {/* ================= HEADER ================= */}

            <header className="product-listing__header">

                <span className="product-listing__eyebrow">
                    SHOP
                </span>

                <h1>
                    All Products
                </h1>

                <p>
                    Explore our collection of
                    technology products.
                </p>

            </header>


            {/* ================= CONTENT ================= */}

            <div className="product-listing__layout">

                {/* FILTER */}

                <ProductFilter
                    categories={categories}
                    selectedCategory={selectedCategory}
                    onCategoryChange={setSelectedCategory}
                />


                {/* PRODUCTS */}

                <section className="product-listing__products">

                    <div className="product-listing__toolbar">

                        <span>
                            {filteredProducts.length}
                            {" "}
                            products
                        </span>

                        <ProductSort
                            value={sort}
                            onChange={setSort}
                        />

                    </div>


                    <ProductGrid
                        products={filteredProducts}
                        onAddToCart={handleAddToCart}
                    />

                </section>

            </div>

        </main>
    );
}

export default ProductListing;