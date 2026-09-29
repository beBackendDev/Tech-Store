import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import useAxiosPrivate
    from "../../../hooks/useAxiosPrivate";



import "./AdminProducts.scss";
import ProductTable from "../../../components/admin/productTable/ProductTable";
import ProductPagination from "../../../components/admin/productPagination/ProductPagination";
import { getAdminProducts, updateAdminProductStatus } from "../../../api/adminApi";
import useDebounce from "../../../hooks/useDebounce";
import ProductToolbar from "../../../components/admin/productToolbar/ProductToolbar";


function AdminProducts() {

    const axiosPrivate =
        useAxiosPrivate();

    const navigate = useNavigate();
    // ========================================
    // PRODUCTS
    // ========================================

    const [products, setProducts] =
        useState([]);

    const [pagination, setPagination] =
        useState(null);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState(null);

    // ========================================
    // FILTER STATE
    // ========================================
    const [keyword, setKeyword] =
        useState("");
    const [category, setCategory] =
        useState("");


    const [brand, setBrand] =
        useState("");


    const [minPrice, setMinPrice] =
        useState("");


    const [maxPrice, setMaxPrice] =
        useState("");


    const [minRating, setMinRating] =
        useState("");


    const [active, setActive] =
        useState("");


    // ========================================
    // SORT
    // ========================================

    const [sort, setSort] =
        useState("createdAt,desc");


    // ========================================
    // PAGE
    // ========================================
    const [page, setPage] =
        useState(0);

    const debouncedKeyword =
        useDebounce(keyword, 400);


    let cancelled = false;
    //STATUS
    const handleToggleStatus = async (
        id,
        currentStatus
    ) => {

        try {

            await updateAdminProductStatus(
                axiosPrivate,
                id,
                !currentStatus
            );

            await fetchProducts();

        } catch (error) {

            console.error(
                "Failed to update product status:",
                error
            );
        }
    };
    const fetchProducts = async () => {

        try {

            setLoading(true);
            setError(null);


            const data =
                await getAdminProducts(
                    axiosPrivate,
                    {
                        page,
                        size: 20,
                        keyword:
                            debouncedKeyword,

                        category,

                        brand,

                        minPrice,

                        maxPrice,

                        minRating,

                        active:
                            active === ""
                                ? undefined
                                : active === "true",
                        sort: "createdAt,desc"
                    }
                );
            if (cancelled) {
                return;
            }

            setProducts(
                data.content
            );


            setPagination({
                page: data.page,
                size: data.size,
                totalElements:
                    data.totalElements,
                totalPages:
                    data.totalPages,
                first:
                    data.first,
                last:
                    data.last
            });


        } catch (error) {
            if (cancelled) {
                return;
            }
            console.error(
                "Failed to fetch admin products:",
                error
            );

            setError(
                "Unable to load products."
            );

        } finally {

            if (!cancelled) {
                setLoading(false);
            }

        }

    };



    useEffect(() => {
        fetchProducts();
        return () => {
            cancelled = true;
        };
    }, [
        axiosPrivate,
        page,
        debouncedKeyword,
        category,
        brand,
        minPrice,
        maxPrice,
        minRating,
        active,
        sort]);

    // ========================================
    // FILTER HANDLERS
    // ========================================

    const handleCategoryChange =
        value => {

            setCategory(value);
            setPage(0);
        };


    const handleBrandChange =
        value => {

            setBrand(value);
            setPage(0);
        };


    const handleMinPriceChange =
        value => {

            setMinPrice(value);
            setPage(0);
        };


    const handleMaxPriceChange =
        value => {

            setMaxPrice(value);
            setPage(0);
        };


    const handleMinRatingChange =
        value => {

            setMinRating(value);
            setPage(0);
        };


    const handleActiveChange =
        value => {

            setActive(value);
            setPage(0);
        };


    const handleSortChange =
        value => {

            setSort(value);
            setPage(0);
        };


    const handleKeywordChange =
        value => {

            setKeyword(value);

            // We can reset immediately.
            setPage(0);
        };

    // ========================================
    // RESET
    // ========================================

    const handleReset =
        () => {

            setKeyword("");

            setCategory("");

            setBrand("");

            setMinPrice("");

            setMaxPrice("");

            setMinRating("");

            setActive("");

            setSort(
                "createdAt,desc"
            );

            setPage(0);
        };


    return (

        <div className="admin-products">

            {/* ==================================
                HEADER
            ================================== */}

            <header className="admin-products__header">

                <div>

                    <span>
                        PRODUCT MANAGEMENT
                    </span>

                    <h1>
                        Products
                    </h1>

                    <p>
                        Manage your product catalog.
                    </p>

                </div>


                <button
                    type="button"
                    onClick={() =>
                        navigate("/admin/products/new")
                    }
                >
                    + Add Product
                </button>

            </header>


            {/* ==================================
                TOOLBAR
            ================================== */}

            <ProductToolbar

                keyword={keyword}

                category={category}

                brand={brand}

                minPrice={minPrice}

                maxPrice={maxPrice}

                minRating={minRating}

                active={active}

                sort={sort}


                onKeywordChange={
                    handleKeywordChange
                }

                onCategoryChange={
                    handleCategoryChange
                }

                onBrandChange={
                    handleBrandChange
                }

                onMinPriceChange={
                    handleMinPriceChange
                }

                onMaxPriceChange={
                    handleMaxPriceChange
                }

                onMinRatingChange={
                    handleMinRatingChange
                }

                onActiveChange={
                    handleActiveChange
                }

                onSortChange={
                    handleSortChange
                }

                onReset={
                    handleReset
                }

            />


            {/* ==================================
                TABLE
            ================================== */}

            <section
                className="admin-products__table"
            >

                {loading ? (

                    <div className="admin-products__loading">
                        Loading products...
                    </div>

                ) : error ? (

                    <div className="admin-products__error">
                        {error}
                    </div>

                ) : (

                    <ProductTable
                        products={products}
                        onEdit={(id) =>
                            navigate(
                                `/admin/products/${id}/edit`
                            )
                        }
                        onToggleStatus={handleToggleStatus}
                    />

                )}

            </section>


            {/* ==================================
                PAGINATION
            ================================== */}

            {!loading &&
                pagination &&
                pagination.totalPages > 0 && (

                    <ProductPagination
                        pagination={pagination}
                        onPageChange={setPage}
                    />

                )}

        </div>
    );
}


export default AdminProducts;