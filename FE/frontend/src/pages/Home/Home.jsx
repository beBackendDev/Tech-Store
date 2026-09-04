import { useEffect, useState } from "react";
import ProductSection
    from "../../components/product/ProductSection/ProductSection";

import CategoryCard
    from "../../components/product/CategoryCard/CategoryCard";

import { mockCategories }
    from "../../data/mockCategories";

import {
    getProducts
} from "../../services/productService";

import "./Home.scss";
import { useNavigate } from "react-router-dom";

function Home() {
    const navigate = useNavigate();
    const [products, setProducts] =
        useState([]);
    const [featuredProducts, setFeaturedProducts] =
        useState([]);
    const [newArrivals, setNewArrivals] =
        useState([]);
    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState(null);

    const handleAddToCart = (product) => {

        console.log(
            "Add to cart:",
            product
        );

    };

    const handleCategoryClick = (category) => {

        navigate(
            `/categories?category=${encodeURIComponent(category.name)}`
        );

    };

    const handleViewAllCategories = () => {
        navigate("/categories");
    };


    useEffect(() => {

        const loadProducts = async () => {

            try {

                setLoading(true);

                const data =
                    await getProducts();

                setProducts(data.content);

                //featured products
                const featuredData = await getProducts({
                    page: 0,
                    size: 8,
                    sort: "rating,desc"
                });
                console.log("Featured products:", featuredData.content);
                setFeaturedProducts(featuredData.content);
                //new arrivals
                const newArrivalsData = await getProducts({
                    page: 0,
                    size: 8,
                    sort: "createdAt,desc"
                });
                console.log("New arrivals:", newArrivalsData.content);
                setNewArrivals(newArrivalsData.content);
            } catch (error) {

                console.error(
                    "Failed to load products:",
                    error
                );

                setError(
                    "Unable to load products."
                );

            } finally {

                setLoading(false);
            }
        };


        loadProducts();

    }, []);
    if (loading) {

        return (
            <main className="products-page">
                <p>Loading products...</p>
            </main>
        );
    }


    if (error) {

        return (
            <main className="products-page">
                <p>{error}</p>
            </main>
        );
    }
    return (

        <main className="home-page">

            {/* ================= HERO ================= */}

            <section className="home-page__hero">

                <div className="home-page__hero-content">

                    <span className="home-page__hero-badge">
                        NEW COLLECTION
                    </span>

                    <h1>
                        Technology
                        <br />
                        made for you.
                    </h1>

                    <p>
                        Discover the latest laptops,
                        smartphones, gaming gear and
                        accessories.
                    </p>

                    <div className="home-page__hero-actions">

                        <button className="home-page__primary-button"
                            type="button"
                            onClick={() => navigate("/products")}
                        >
                            Shop now
                        </button>

                        <button className="home-page__secondary-button"
                            type="button"
                            onClick={() => navigate("/products")}
                        >
                            Explore categories
                        </button>

                    </div>

                </div>


                <div className="home-page__hero-visual">

                    <div className="home-page__hero-circle">
                        💻
                    </div>

                </div>

            </section>


            {/* ================= CATEGORIES ================= */}

            <section className="home-page__categories">

                <div className="home-page__section-header">

                    <div>

                        <span className="home-page__eyebrow">
                            BROWSE
                        </span>

                        <h2>
                            Shop by Category
                        </h2>

                        <p>
                            Find the technology
                            that fits your needs.
                        </p>

                    </div>

                    <button
                        type="button"
                        onClick={handleViewAllCategories}
                    >
                        View all
                    </button>

                </div>


                <div className="home-page__category-grid">

                    {mockCategories.map(category => (

                        <CategoryCard
                            key={category.id}
                            category={category}
                            onClick={handleCategoryClick}
                        />

                    ))}

                </div>

            </section>


            {/* ================= FEATURED ================= */}

            <ProductSection
                title="Featured Products"
                subtitle="Our most popular products"
                products={featuredProducts}
                onAddToCart={handleAddToCart}
            />


            {/* ================= PROMOTION ================= */}

            <section className="home-page__promotion">

                <div>

                    <span>
                        LIMITED OFFER
                    </span>

                    <h2>
                        Upgrade your setup.
                    </h2>

                    <p>
                        Save up to 30% on selected products.
                    </p>

                    <button>
                        Shop deals
                    </button>

                </div>

                <div className="home-page__promotion-icon">
                    ⚡
                </div>

            </section>


            {/* ================= NEW ARRIVALS ================= */}

            <ProductSection
                title="New Arrivals"
                subtitle="Discover our latest products"
                products={newArrivals}
                // products={mockProducts}
                onAddToCart={handleAddToCart}
            />


            {/* ================= CTA ================= */}

            <section className="home-page__cta">

                <h2>
                    Find your next device.
                </h2>

                <p>
                    Explore our complete collection
                    of technology products.
                </p>

                <button
                    type="button"
                    onClick={() => navigate("/products")}
                >
                    Explore products
                </button>

            </section>

        </main>
    );
}

export default Home;