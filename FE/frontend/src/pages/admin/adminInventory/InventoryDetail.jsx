import { useCallback, useEffect, useState } from "react";
import { ArrowLeftOutlined } from "@ant-design/icons";
import { useNavigate, useParams } from "react-router-dom";

import {
    getAdminInventoryDetail,
} from "../../../api/adminApi";

import InventoryHistory from "./InventoryHistory";

import "./InventoryDetail.scss";
import useAxiosPrivate from "../../../hooks/useAxiosPrivate";

const InventoryDetail = () => {
    const { productId } = useParams();

    const navigate = useNavigate();
    const axiosPrivate = useAxiosPrivate();

    const [inventory, setInventory] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchInventoryDetail = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);

            const data = await getAdminInventoryDetail(
                axiosPrivate,
                productId
            );

            setInventory(data);
        } catch (err) {
            console.error(
                "Failed to fetch inventory detail:",
                err
            );

            setError(
                err?.response?.data?.message ||
                "Failed to load inventory detail."
            );
        } finally {
            setLoading(false);
        }
    }, [axiosPrivate, productId]);

    useEffect(() => {
        fetchInventoryDetail();
    }, [fetchInventoryDetail]);

    const handleBack = () => {
        navigate("/admin/inventory");
    };

    if (loading) {
        return (
            <section className="inventory-detail">
                <div className="inventory-detail__state">
                    Loading inventory...
                </div>
            </section>
        );
    }

    if (error) {
        return (
            <section className="inventory-detail">
                <div className="inventory-detail__back">
                    <button
                        type="button"
                        onClick={handleBack}
                    >
                        <ArrowLeftOutlined />
                        Back to Inventory
                    </button>
                </div>

                <div className="inventory-detail__state inventory-detail__state--error">
                    {error}
                </div>
            </section>
        );
    }

    if (!inventory) {
        return null;
    }

    return (
        <section className="inventory-detail">

            {/* =========================
                HEADER
            ========================= */}

            <div className="inventory-detail__header">

                <button
                    type="button"
                    className="inventory-detail__back-button"
                    onClick={handleBack}
                >
                    <ArrowLeftOutlined />
                    Back to Inventory
                </button>

                <div className="inventory-detail__title-wrapper">
                    <h1 className="inventory-detail__title">
                        Inventory Detail
                    </h1>

                    <p className="inventory-detail__subtitle">
                        View stock information and inventory history.
                    </p>
                </div>
            </div>


            {/* =========================
                PRODUCT + INVENTORY
            ========================= */}

            <div className="inventory-detail__summary">

                <div className="inventory-detail__product">

                    <div className="inventory-detail__image">
                        {inventory.image ? (
                            <img
                                src={inventory.image}
                                alt={inventory.productName}
                            />
                        ) : (
                            <span>No image</span>
                        )}
                    </div>

                    <div className="inventory-detail__product-info">

                        <h2>
                            {inventory.productName}
                        </h2>

                        <span>
                            {inventory.externalId}
                        </span>

                        <span>
                            {inventory.category}
                        </span>

                    </div>
                </div>


                <div className="inventory-detail__stock-grid">

                    <StockItem
                        label="Stock"
                        value={inventory.stock}
                    />

                    <StockItem
                        label="Reserved"
                        value={inventory.reservedStock}
                    />

                    <StockItem
                        label="Available"
                        value={inventory.availableStock}
                    />

                    <StockStatus
                        status={inventory.status}
                    />

                </div>

            </div>


            {/* =========================
                ACTIONS
            ========================= */}

            <div className="inventory-detail__actions">

                <button
                    type="button"
                    disabled
                    className="inventory-detail__action"
                >
                    + Stock In
                </button>

                <button
                    type="button"
                    disabled
                    className="inventory-detail__action"
                >
                    - Stock Out
                </button>

                <button
                    type="button"
                    disabled
                    className="inventory-detail__action"
                >
                    Adjust Stock
                </button>

            </div>


            {/* =========================
                HISTORY
            ========================= */}

            <InventoryHistory
                productId={productId}
            />

        </section>
    );
};


const StockItem = ({
    label,
    value,
}) => {
    return (
        <div className="inventory-stock-item">

            <span className="inventory-stock-item__label">
                {label}
            </span>

            <strong className="inventory-stock-item__value">
                {value}
            </strong>

        </div>
    );
};


const StockStatus = ({
    status,
}) => {

    const statusMap = {
        IN_STOCK: {
            label: "In Stock",
            className: "inventory-stock-status--in-stock",
        },

        LOW_STOCK: {
            label: "Low Stock",
            className: "inventory-stock-status--low-stock",
        },

        OUT_OF_STOCK: {
            label: "Out of Stock",
            className: "inventory-stock-status--out-of-stock",
        },
    };

    const current = statusMap[status] ?? {
        label: status,
        className: "",
    };

    return (
        <div className="inventory-stock-item">

            <span className="inventory-stock-item__label">
                Status
            </span>

            <span
                className={`inventory-stock-status ${current.className}`}
            >
                {current.label}
            </span>

        </div>
    );
};


export default InventoryDetail;