import { useCallback, useEffect, useState } from "react";

import {
    getInventoryHistory,
} from "../../../api/adminApi";


import "./InventoryHistory.scss";
import useAxiosPrivate from "../../../hooks/useAxiosPrivate";

const PAGE_SIZE = 10;

const InventoryHistory = ({
    productId,
}) => {
    const axiosPrivate = useAxiosPrivate();

    const [transactions, setTransactions] = useState([]);

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


    const fetchHistory = useCallback(
        async (page = 0) => {
            try {
                setLoading(true);
                setError(null);

                const data = await getInventoryHistory(
                    axiosPrivate,
                    productId,
                    {
                        page,
                        size: PAGE_SIZE,
                        sort: "createdAt,desc",
                    }
                );

                setTransactions(
                    data.content ?? []
                );

                setPagination({
                    page: data.number ?? data.page ?? 0,
                    size: data.size ?? PAGE_SIZE,
                    totalElements: data.totalElements ?? 0,
                    totalPages: data.totalPages ?? 0,
                    first: data.first ?? true,
                    last: data.last ?? true,
                });

            } catch (err) {
                console.error(
                    "Failed to fetch inventory history:",
                    err
                );

                setError(
                    err?.response?.data?.message ||
                    "Failed to load inventory history."
                );
            } finally {
                setLoading(false);
            }
        },
        [axiosPrivate, productId]
    );


    useEffect(() => {
        fetchHistory(0);
    }, [fetchHistory]);


    const formatDate = (value) => {
        if (!value) {
            return "-";
        }

        return new Date(value).toLocaleString(
            "en-GB",
            {
                dateStyle: "medium",
                timeStyle: "short",
            }
        );
    };


    const getTransactionType = (type) => {
        const typeMap = {
            STOCK_IN: {
                label: "Stock In",
                className: "inventory-history__type--stock-in",
            },

            STOCK_OUT: {
                label: "Stock Out",
                className: "inventory-history__type--stock-out",
            },

            RESERVED: {
                label: "Reserved",
                className: "inventory-history__type--reserved",
            },

            RELEASED: {
                label: "Released",
                className: "inventory-history__type--released",
            },

            RETURNED: {
                label: "Returned",
                className: "inventory-history__type--returned",
            },

            ADJUSTMENT: {
                label: "Adjustment",
                className: "inventory-history__type--adjustment",
            },
        };

        return typeMap[type] ?? {
            label: type,
            className: "",
        };
    };


    if (loading) {
        return (
            <section className="inventory-history">

                <div className="inventory-history__header">
                    <h2>
                        Inventory History
                    </h2>
                </div>

                <div className="inventory-history__state">
                    Loading history...
                </div>

            </section>
        );
    }


    if (error) {
        return (
            <section className="inventory-history">

                <div className="inventory-history__header">
                    <h2>
                        Inventory History
                    </h2>
                </div>

                <div className="inventory-history__state inventory-history__state--error">
                    {error}
                </div>

            </section>
        );
    }


    return (
        <section className="inventory-history">

            <div className="inventory-history__header">

                <div>
                    <h2>
                        Inventory History
                    </h2>

                    <p>
                        Inventory transactions for this product.
                    </p>
                </div>

                <span>
                    {pagination.totalElements} transactions
                </span>

            </div>


            {!transactions.length ? (
                <div className="inventory-history__state">
                    No inventory transactions found.
                </div>
            ) : (
                <div className="inventory-history__table-wrapper">

                    <table className="inventory-history__table">

                        <thead>
                            <tr>
                                <th>Date</th>
                                <th>Type</th>
                                <th>Quantity</th>
                                <th>Stock</th>
                                <th>Reserved</th>
                                <th>Note</th>
                            </tr>
                        </thead>


                        <tbody>

                            {transactions.map(
                                (transaction) => {

                                    const type =
                                        getTransactionType(
                                            transaction.type
                                        );

                                    return (
                                        <tr
                                            key={
                                                transaction.id
                                            }
                                        >

                                            <td>
                                                {formatDate(
                                                    transaction.createdAt
                                                )}
                                            </td>

                                            <td>
                                                <span
                                                    className={`inventory-history__type ${type.className}`}
                                                >
                                                    {type.label}
                                                </span>
                                            </td>

                                            <td>
                                                {transaction.quantity}
                                            </td>

                                            <td>
                                                <div className="inventory-history__snapshot">
                                                    <span>
                                                        {transaction.stockBefore}
                                                    </span>

                                                    <span>
                                                        →
                                                    </span>

                                                    <strong>
                                                        {transaction.stockAfter}
                                                    </strong>
                                                </div>
                                            </td>

                                            <td>
                                                <div className="inventory-history__snapshot">
                                                    <span>
                                                        {transaction.reservedBefore}
                                                    </span>

                                                    <span>
                                                        →
                                                    </span>

                                                    <strong>
                                                        {transaction.reservedAfter}
                                                    </strong>
                                                </div>
                                            </td>

                                            <td>
                                                <span className="inventory-history__note">
                                                    {transaction.note || "-"}
                                                </span>
                                            </td>

                                        </tr>
                                    );
                                }
                            )}

                        </tbody>

                    </table>

                </div>
            )}


            {pagination.totalPages > 1 && (
                <HistoryPagination
                    pagination={pagination}
                    onPageChange={fetchHistory}
                />
            )}

        </section>
    );
};


const HistoryPagination = ({
    pagination,
    onPageChange,
}) => {
    const {
        page,
        totalPages,
        first,
        last,
    } = pagination;

    return (
        <div className="inventory-history__pagination">

            <button
                type="button"
                disabled={first}
                onClick={() =>
                    onPageChange(page - 1)
                }
            >
                Previous
            </button>

            <span>
                Page {page + 1} of {totalPages}
            </span>

            <button
                type="button"
                disabled={last}
                onClick={() =>
                    onPageChange(page + 1)
                }
            >
                Next
            </button>

        </div>
    );
};


export default InventoryHistory;