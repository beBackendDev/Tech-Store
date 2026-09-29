import {
    useEffect,
    useState
} from "react";

import {
    useNavigate,
    useParams
} from "react-router-dom";

import useAxiosPrivate
    from "../../../hooks/useAxiosPrivate";

import {
    getAdminProductById
} from "../../../api/adminApi";

import "./AdminProductDetail.scss";


function AdminProductDetail() {

    const {
        id
    } = useParams();


    const navigate =
        useNavigate();


    const axiosPrivate =
        useAxiosPrivate();


    const [product, setProduct] =
        useState(null);


    const [loading, setLoading] =
        useState(true);


    const [error, setError] =
        useState(null);


    useEffect(() => {

        let cancelled = false;


        const fetchProduct =
            async () => {

                try {

                    setLoading(true);

                    setError(null);


                    const data =
                        await getAdminProductById(
                            axiosPrivate,
                            id
                        );


                    if (cancelled) {
                        return;
                    }


                    setProduct(data);

                } catch (error) {

                    if (cancelled) {
                        return;
                    }


                    console.error(
                        "Failed to fetch product:",
                        error
                    );


                    if (
                        error.response?.status === 404
                    ) {

                        setError(
                            "Product not found."
                        );

                    } else {

                        setError(
                            "Unable to load product."
                        );
                    }

                } finally {

                    if (!cancelled) {
                        setLoading(false);
                    }
                }
            };


        fetchProduct();


        return () => {
            cancelled = true;
        };

    }, [axiosPrivate, id]);


    if (loading) {

        return (
            <div className="admin-product-detail">
                Loading product...
            </div>
        );
    }


    if (error) {

        return (

            <div className="admin-product-detail">

                <div className="admin-product-detail__error">

                    <h2>
                        {error}
                    </h2>

                    <button
                        type="button"
                        onClick={() =>
                            navigate(
                                "/admin/products"
                            )
                        }
                    >
                        Back to Products
                    </button>

                </div>

            </div>
        );
    }


    if (!product) {
        return null;
    }


    return (

        <div className="admin-product-detail">

            {/* ==================================
                HEADER
            ================================== */}

            <header
                className="
                    admin-product-detail__header
                "
            >

                <div>

                    <button
                        type="button"
                        className="
                            admin-product-detail__back
                        "
                        onClick={() =>
                            navigate(
                                "/admin/products"
                            )
                        }
                    >
                        ← Back to Products
                    </button>


                    <span>
                        PRODUCT MANAGEMENT
                    </span>


                    <h1>
                        Product Detail
                    </h1>

                </div>


                <button
                    type="button"
                    className="
                        admin-product-detail__edit
                    "
                    onClick={() =>
                        navigate(
                            `/admin/products/${product.id}/edit`
                        )
                    }
                >
                    Edit Product
                </button>

            </header>


            {/* ==================================
                PRODUCT OVERVIEW
            ================================== */}

            <section
                className="
                    admin-product-detail__overview
                "
            >

                <div
                    className="
                        admin-product-detail__image
                    "
                >

                    <img
                        src={product.image}
                        alt={product.name}
                    />

                </div>


                <div
                    className="
                        admin-product-detail__summary
                    "
                >

                    <div
                        className="
                            admin-product-detail__status
                        "
                    >

                        <span
                            className={
                                product.active
                                    ? "status status--active"
                                    : "status status--inactive"
                            }
                        >
                            {product.active
                                ? "Active"
                                : "Inactive"
                            }
                        </span>


                        {product.isNew && (
                            <span className="status status--new">
                                New
                            </span>
                        )}

                    </div>


                    <h2>
                        {product.name}
                    </h2>


                    <p
                        className="
                            admin-product-detail__external-id
                        "
                    >
                        ID: {product.externalId}
                    </p>


                    <p
                        className="
                            admin-product-detail__description
                        "
                    >
                        {product.description ||
                            "No description available."}
                    </p>

                </div>

            </section>


            {/* ==================================
                BASIC INFORMATION
            ================================== */}

            <section
                className="
                    admin-product-detail__section
                "
            >

                <h3>
                    Basic Information
                </h3>


                <div
                    className="
                        admin-product-detail__grid
                    "
                >

                    <DetailItem
                        label="Category"
                        value={product.category}
                    />


                    <DetailItem
                        label="Price"
                        value={`${Number(
                            product.price
                        ).toLocaleString("vi-VN")} ₫`}
                    />


                    <DetailItem
                        label="Old Price"
                        value={
                            product.oldPrice
                                ? `${Number(
                                    product.oldPrice
                                ).toLocaleString(
                                    "vi-VN"
                                )} ₫`
                                : "-"
                        }
                    />


                    <DetailItem
                        label="Stock"
                        value={product.stock}
                    />


                    <DetailItem
                        label="Rating"
                        value={
                            product.rating ?? "-"
                        }
                    />


                    <DetailItem
                        label="Reviews"
                        value={
                            product.reviewCount ?? 0
                        }
                    />

                </div>

            </section>


            {/* ==================================
                LAPTOP SPECIFICATION
            ================================== */}

            {product.laptopSpecification && (

                <section
                    className="
                        admin-product-detail__section
                    "
                >

                    <h3>
                        Laptop Specification
                    </h3>


                    <div
                        className="
                            admin-product-detail__grid
                        "
                    >

                        <DetailItem
                            label="Brand"
                            value={
                                product
                                    .laptopSpecification
                                    .brand
                            }
                        />


                        <DetailItem
                            label="Processor"
                            value={
                                product
                                    .laptopSpecification
                                    .processor
                            }
                        />


                        <DetailItem
                            label="RAM"
                            value={
                                product
                                    .laptopSpecification
                                    .ram
                            }
                        />


                        <DetailItem
                            label="SSD"
                            value={
                                product
                                    .laptopSpecification
                                    .ssd
                            }
                        />


                        <DetailItem
                            label="Hard Disk"
                            value={
                                product
                                    .laptopSpecification
                                    .hardDisk
                            }
                        />


                        <DetailItem
                            label="Operating System"
                            value={
                                product
                                    .laptopSpecification
                                    .operatingSystem
                            }
                        />


                        <DetailItem
                            label="Graphics"
                            value={
                                product
                                    .laptopSpecification
                                    .graphics
                            }
                        />


                        <DetailItem
                            label="Screen Size"
                            value={
                                product
                                    .laptopSpecification
                                    .screenSize
                            }
                        />


                        <DetailItem
                            label="Resolution"
                            value={
                                product
                                    .laptopSpecification
                                    .resolution
                            }
                        />

                    </div>

                </section>

            )}


            {/* ==================================
                SYSTEM INFORMATION
            ================================== */}

            <section
                className="
                    admin-product-detail__section
                "
            >

                <h3>
                    System Information
                </h3>


                <div
                    className="
                        admin-product-detail__grid
                    "
                >

                    <DetailItem
                        label="Product ID"
                        value={product.id}
                    />


                    <DetailItem
                        label="External ID"
                        value={product.externalId}
                    />


                    <DetailItem
                        label="Created At"
                        value={
                            product.createdAt
                        }
                    />


                    <DetailItem
                        label="Updated At"
                        value={
                            product.updatedAt
                        }
                    />

                </div>

            </section>

        </div>
    );
}


function DetailItem({
    label,
    value
}) {

    return (

        <div
            className="
                admin-product-detail__item
            "
        >

            <span>
                {label}
            </span>

            <strong>
                {value ?? "-"}
            </strong>

        </div>
    );
}


export default AdminProductDetail;