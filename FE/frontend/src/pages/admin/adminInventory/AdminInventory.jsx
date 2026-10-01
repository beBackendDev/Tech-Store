import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getAdminInventory } from "../../../api/adminApi";

import InventoryTable from "./InventoryTable";
import InventoryPagination from "./InventoryPagination";

import "./AdminInventory.scss";
import useAxiosPrivate from "../../../hooks/useAxiosPrivate";

const PAGE_SIZE = 20;

const AdminInventory = () => {
    const axiosPrivate = useAxiosPrivate();
    const navigate = useNavigate();

    const [inventories, setInventories] = useState([]);
    const [pagination, setPagination] = useState({
        page: 0,
        size: PAGE_SIZE,
        totalElements: 0,
        totalPages: 0,
        first: true,
        last: true,
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchInventory = useCallback(
        async (page = 0) => {
            try {
                setLoading(true);
                setError(null);

                const data = await getAdminInventory(axiosPrivate, {
                    page,
                    size: PAGE_SIZE,
                    sort: "name,asc",
                });

                setInventories(data.content ?? []);

                setPagination({
                    page: data.number ?? data.page ?? 0,
                    size: data.size ?? PAGE_SIZE,
                    totalElements: data.totalElements ?? 0,
                    totalPages: data.totalPages ?? 0,
                    first: data.first ?? true,
                    last: data.last ?? true,
                });
            } catch (err) {
                console.error("Failed to fetch inventory:", err);

                setError(
                    err?.response?.data?.message ||
                    "Failed to load inventory."
                );
            } finally {
                setLoading(false);
            }
        },
        [axiosPrivate]
    );

    useEffect(() => {
        fetchInventory(0);
    }, [fetchInventory]);

    const handlePageChange = (page) => {
        fetchInventory(page);
    };

    const handleView = (productId) => {
        navigate(`/admin/inventory/${productId}`);
    };

    return (
        <section className="admin-inventory">
            <div className="admin-inventory__header">
                <div>
                    <h1 className="admin-inventory__title">
                        Inventory
                    </h1>

                    <p className="admin-inventory__subtitle">
                        Manage product stock and inventory levels.
                    </p>
                </div>
            </div>

            <div className="admin-inventory__card">
                <InventoryTable
                    inventories={inventories}
                    loading={loading}
                    error={error}
                    onView={handleView}
                />

                {!loading && !error && pagination.totalPages > 0 && (
                    <InventoryPagination
                        pagination={pagination}
                        onPageChange={handlePageChange}
                    />
                )}
            </div>
        </section>
    );
};

export default AdminInventory;