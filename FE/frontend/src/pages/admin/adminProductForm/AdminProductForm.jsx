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
    createAdminProduct,
    getAdminProductById,
    updateAdminProduct
} from "../../../api/adminApi";

import "./AdminProductForm.scss";
import { getAccessToken } from "../../../services/tokenService";


const initialForm = {
    name: "",
    description: "",
    category: "",
    price: "",
    oldPrice: "",
    image: "",
    isNew: false,

    initialStock: 0,
    active: true,

    laptopSpecification: {
        brand: "",
        processor: "",
        ram: "",
        ssd: "",
        hardDisk: "",
        operatingSystem: "",
        graphics: "",
        screenSize: "",
        resolution: ""
    }
};


function AdminProductForm() {

    const axiosPrivate =
        useAxiosPrivate();

    const navigate =
        useNavigate();

    const { id } =
        useParams();


    const isEditMode =
        Boolean(id);


    const [form, setForm] =
        useState(initialForm);


    const [loading, setLoading] =
        useState(isEditMode);


    const [submitting, setSubmitting] =
        useState(false);


    const [error, setError] =
        useState(null);


    const [errors, setErrors] =
        useState({});


    // ============================================
    // LOAD PRODUCT WHEN EDITING
    // ============================================

    useEffect(() => {

        if (!isEditMode) {
            return;
        }


        const fetchProduct =
            async () => {

                try {

                    setLoading(true);
                    setError(null);


                    const product =
                        await getAdminProductById(
                            axiosPrivate,
                            id
                        );


                    setForm({
                        name:
                            product.name ?? "",

                        description:
                            product.description ?? "",

                        category:
                            product.category ?? "",

                        price:
                            product.price ?? "",

                        oldPrice:
                            product.oldPrice ?? "",

                        image:
                            product.image ?? "",

                        isNew:
                            product.isNew ?? false,

                        /*
                         * Stock is intentionally NOT
                         * editable in edit mode.
                         */

                        initialStock: 0,

                        active:
                            product.active ?? true,


                        laptopSpecification: {
                            brand:
                                product.laptopSpecification?.brand
                                ?? "",

                            processor:
                                product.laptopSpecification?.processor
                                ?? "",

                            ram:
                                product.laptopSpecification?.ram
                                ?? "",

                            ssd:
                                product.laptopSpecification?.ssd
                                ?? "",

                            hardDisk:
                                product.laptopSpecification?.hardDisk
                                ?? "",

                            operatingSystem:
                                product.laptopSpecification?.operatingSystem
                                ?? "",

                            graphics:
                                product.laptopSpecification?.graphics
                                ?? "",

                            screenSize:
                                product.laptopSpecification?.screenSize
                                ?? "",

                            resolution:
                                product.laptopSpecification?.resolution
                                ?? ""
                        }
                    });


                } catch (error) {

                    console.error(
                        "Failed to load product:",
                        error
                    );


                    setError(
                        "Unable to load product."
                    );


                } finally {

                    setLoading(false);

                }
            };


        fetchProduct();

    }, [
        isEditMode,
        id,
        axiosPrivate
    ]);


    // ============================================
    // HANDLE BASIC FIELD
    // ============================================

    const handleChange = event => {

        const {
            name,
            value,
            type,
            checked
        } = event.target;


        setForm(previous => ({
            ...previous,

            [name]:
                type === "checkbox"
                    ? checked
                    : value
        }));


        setErrors(previous => ({
            ...previous,
            [name]: undefined
        }));
    };


    // ============================================
    // HANDLE LAPTOP SPECIFICATION
    // ============================================

    const handleSpecificationChange =
        event => {

            const {
                name,
                value
            } = event.target;


            setForm(previous => ({
                ...previous,

                laptopSpecification: {
                    ...previous.laptopSpecification,

                    [name]: value
                }
            }));


            setErrors(previous => ({
                ...previous,
                [`laptopSpecification.${name}`]:
                    undefined
            }));
        };


    // ============================================
    // VALIDATION
    // ============================================

    const validate = () => {

        const validationErrors = {};


        if (!form.name.trim()) {

            validationErrors.name =
                "Product name is required.";
        }


        if (!form.category.trim()) {

            validationErrors.category =
                "Category is required.";
        }


        if (
            form.price === ""
            || Number(form.price) <= 0
        ) {

            validationErrors.price =
                "Price must be greater than 0.";
        }


        if (
            form.oldPrice !== ""
            && Number(form.oldPrice)
                < Number(form.price)
        ) {

            validationErrors.oldPrice =
                "Old price must be greater than or equal to current price.";
        }


        if (
            !isEditMode
            && Number(form.initialStock) < 0
        ) {

            validationErrors.initialStock =
                "Initial stock cannot be negative.";
        }


        setErrors(validationErrors);


        return (
            Object.keys(validationErrors)
                .length === 0
        );
    };


    // ============================================
    // SUBMIT
    // ============================================

    const handleSubmit =
        async event => {

            event.preventDefault();


            if (!validate()) {
                return;
            }


            try {

                setSubmitting(true);
                setError(null);


                const payload = {

                    name:
                        form.name.trim(),

                    description:
                        form.description.trim(),

                    category:
                        form.category.trim(),

                    price:
                        Number(form.price),

                    oldPrice:
                        form.oldPrice === ""
                            ? null
                            : Number(form.oldPrice),

                    image:
                        form.image.trim(),

                    isNew:
                        form.isNew,


                    laptopSpecification: {
                        brand:
                            form.laptopSpecification.brand.trim(),

                        processor:
                            form.laptopSpecification.processor.trim(),

                        ram:
                            form.laptopSpecification.ram,

                        ssd:
                            form.laptopSpecification.ssd,

                        hardDisk:
                            form.laptopSpecification.hardDisk,

                        operatingSystem:
                            form.laptopSpecification.operatingSystem.trim(),

                        graphics:
                            form.laptopSpecification.graphics.trim(),

                        screenSize:
                            form.laptopSpecification.screenSize,

                        resolution:
                            form.laptopSpecification.resolution.trim()
                    }
                    
                };

                let product;


                // ====================================
                // CREATE
                // ====================================

                if (!isEditMode) {
console.log(
    "🔥 ACCESS TOKEN BEFORE CREATE:",
    getAccessToken()
);

console.log(
    "🔥 ACCESS TOKEN TYPE:",
    typeof getAccessToken()
);
                    product =
                        await createAdminProduct(
                            axiosPrivate,
                            {
                                ...payload,

                                initialStock:
                                    Number(
                                        form.initialStock
                                    ),

                                active:
                                    form.active
                            }
                        );

                }

                // ====================================
                // UPDATE
                // ====================================

                else {

                    product =
                        await updateAdminProduct(
                            axiosPrivate,
                            id,
                            payload
                        );
                }


                // ====================================
                // REDIRECT DETAIL
                // ====================================

                navigate(
                    `/admin/products/${product.id}`
                );


            } catch (error) {

                console.error(
                    "Failed to save product:",
                    error
                );


                setError(
                    error.response?.data?.message
                    || "Unable to save product."
                );


            } finally {

                setSubmitting(false);

            }
        };


    // ============================================
    // CANCEL
    // ============================================

    const handleCancel = () => {

        if (isEditMode) {

            navigate(
                `/admin/products/${id}`
            );

        } else {

            navigate(
                "/admin/products"
            );
        }
    };


    // ============================================
    // LOADING
    // ============================================

    if (loading) {

        return (
            <div className="admin-product-form__loading">
                Loading product...
            </div>
        );
    }


    // ============================================
    // RENDER
    // ============================================

    return (

        <div className="admin-product-form">

            {/* ================================= */}
            {/* HEADER */}
            {/* ================================= */}

            <header className="admin-product-form__header">

                <div>

                    <span>
                        PRODUCT MANAGEMENT
                    </span>

                    <h1>
                        {isEditMode
                            ? "Edit Product"
                            : "Create Product"
                        }
                    </h1>

                    <p>
                        {isEditMode
                            ? "Update product information."
                            : "Add a new product to your catalog."
                        }
                    </p>

                </div>

            </header>


            {/* ================================= */}
            {/* ERROR */}
            {/* ================================= */}

            {error && (

                <div className="admin-product-form__error">

                    {error}

                </div>

            )}


            {/* ================================= */}
            {/* FORM */}
            {/* ================================= */}

            <form
                className="admin-product-form__form"
                onSubmit={handleSubmit}
            >

                {/* ================================= */}
                {/* BASIC INFORMATION */}
                {/* ================================= */}

                <section className="form-section">

                    <div className="form-section__header">

                        <h2>
                            Basic Information
                        </h2>

                        <p>
                            General information about this product.
                        </p>

                    </div>


                    <div className="form-grid">

                        {/* NAME */}

                        <div className="form-field form-field--full">

                            <label>
                                Product Name
                            </label>

                            <input
                                name="name"
                                value={form.name}
                                onChange={handleChange}
                                placeholder="Enter product name"
                            />

                            {errors.name && (
                                <span className="field-error">
                                    {errors.name}
                                </span>
                            )}

                        </div>


                        {/* CATEGORY */}

                        <div className="form-field">

                            <label>
                                Category
                            </label>

                            <input
                                name="category"
                                value={form.category}
                                onChange={handleChange}
                                placeholder="Laptop"
                            />

                            {errors.category && (
                                <span className="field-error">
                                    {errors.category}
                                </span>
                            )}

                        </div>


                        {/* PRICE */}

                        <div className="form-field">

                            <label>
                                Price
                            </label>

                            <input
                                type="number"
                                min="0"
                                step="0.01"
                                name="price"
                                value={form.price}
                                onChange={handleChange}
                                placeholder="0"
                            />

                            {errors.price && (
                                <span className="field-error">
                                    {errors.price}
                                </span>
                            )}

                        </div>


                        {/* OLD PRICE */}

                        <div className="form-field">

                            <label>
                                Old Price
                            </label>

                            <input
                                type="number"
                                min="0"
                                step="0.01"
                                name="oldPrice"
                                value={form.oldPrice}
                                onChange={handleChange}
                                placeholder="Optional"
                            />

                            {errors.oldPrice && (
                                <span className="field-error">
                                    {errors.oldPrice}
                                </span>
                            )}

                        </div>


                        {/* IMAGE */}

                        <div className="form-field form-field--full">

                            <label>
                                Image URL
                            </label>

                            <input
                                name="image"
                                value={form.image}
                                onChange={handleChange}
                                placeholder="https://..."
                            />

                        </div>


                        {/* DESCRIPTION */}

                        <div className="form-field form-field--full">

                            <label>
                                Description
                            </label>

                            <textarea
                                name="description"
                                value={form.description}
                                onChange={handleChange}
                                rows="5"
                                placeholder="Enter product description"
                            />

                        </div>

                    </div>

                </section>


                {/* ================================= */}
                {/* LAPTOP SPECIFICATION */}
                {/* ================================= */}

                <section className="form-section">

                    <div className="form-section__header">

                        <h2>
                            Laptop Specification
                        </h2>

                        <p>
                            Technical specifications for this laptop.
                        </p>

                    </div>


                    <div className="form-grid">

                        <SpecificationField
                            label="Brand"
                            name="brand"
                            value={
                                form.laptopSpecification.brand
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Processor"
                            name="processor"
                            value={
                                form.laptopSpecification.processor
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="RAM"
                            name="ram"
                            value={
                                form.laptopSpecification.ram
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="SSD"
                            name="ssd"
                            value={
                                form.laptopSpecification.ssd
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Hard Disk"
                            name="hardDisk"
                            value={
                                form.laptopSpecification.hardDisk
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Operating System"
                            name="operatingSystem"
                            value={
                                form.laptopSpecification.operatingSystem
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Graphics"
                            name="graphics"
                            value={
                                form.laptopSpecification.graphics
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Screen Size"
                            name="screenSize"
                            value={
                                form.laptopSpecification.screenSize
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />


                        <SpecificationField
                            label="Resolution"
                            name="resolution"
                            value={
                                form.laptopSpecification.resolution
                            }
                            onChange={
                                handleSpecificationChange
                            }
                        />

                    </div>

                </section>


                {/* ================================= */}
                {/* INVENTORY */}
                {/* ================================= */}

                {!isEditMode && (

                    <section className="form-section">

                        <div className="form-section__header">

                            <h2>
                                Inventory
                            </h2>

                            <p>
                                Initial inventory for this product.
                            </p>

                        </div>


                        <div className="form-grid">

                            <div className="form-field">

                                <label>
                                    Initial Stock
                                </label>

                                <input
                                    type="number"
                                    min="0"
                                    name="initialStock"
                                    value={
                                        form.initialStock
                                    }
                                    onChange={
                                        handleChange
                                    }
                                />

                                {errors.initialStock && (

                                    <span className="field-error">
                                        {errors.initialStock}
                                    </span>

                                )}

                            </div>

                        </div>

                    </section>

                )}


                {/* ================================= */}
                {/* OPTIONS */}
                {/* ================================= */}

                <section className="form-section">

                    <div className="form-section__header">

                        <h2>
                            Product Options
                        </h2>

                    </div>


                    <div className="form-options">

                        <label className="checkbox-field">

                            <input
                                type="checkbox"
                                name="isNew"
                                checked={form.isNew}
                                onChange={handleChange}
                            />

                            <span>
                                Mark as new product
                            </span>

                        </label>


                        {!isEditMode && (

                            <label className="checkbox-field">

                                <input
                                    type="checkbox"
                                    name="active"
                                    checked={form.active}
                                    onChange={handleChange}
                                />

                                <span>
                                    Product is active
                                </span>

                            </label>

                        )}

                    </div>

                </section>


                {/* ================================= */}
                {/* ACTIONS */}
                {/* ================================= */}

                <footer className="admin-product-form__actions">

                    <button
                        type="button"
                        className="button button--secondary"
                        onClick={handleCancel}
                        disabled={submitting}
                    >
                        Cancel
                    </button>


                    <button
                        type="submit"
                        className="button button--primary"
                        disabled={submitting}
                    >

                        {submitting
                            ? "Saving..."
                            : isEditMode
                                ? "Save Changes"
                                : "Create Product"
                        }

                    </button>

                </footer>

            </form>

        </div>
    );
}


// ============================================
// SPECIFICATION FIELD
// ============================================

function SpecificationField({
    label,
    name,
    value,
    onChange
}) {

    return (

        <div className="form-field">

            <label>
                {label}
            </label>

            <input
                name={name}
                value={value}
                onChange={onChange}
                placeholder={`Enter ${label.toLowerCase()}`}
            />

        </div>
    );
}


export default AdminProductForm;