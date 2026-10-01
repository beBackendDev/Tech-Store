import { EyeOutlined } from "@ant-design/icons";

const InventoryTable = ({
    inventories,
    loading,
    error,
    onView,
}) => {
    if (loading) {
        return (
            <div className="inventory-table__state">
                Loading inventory...
            </div>
        );
    }

    if (error) {
        return (
            <div className="inventory-table__state inventory-table__state--error">
                {error}
            </div>
        );
    }

    if (!inventories.length) {
        return (
            <div className="inventory-table__state">
                No inventory found.
            </div>
        );
    }

    return (
        <div className="inventory-table-wrapper">
            <table className="inventory-table">
                <thead>
                    <tr>
                        <th>Product</th>
                        <th>Stock</th>
                        <th>Reserved</th>
                        <th>Available</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>
                    {inventories.map((inventory) => (
                        <tr key={inventory.productId}>
                            <td>
                                <div className="inventory-table__product">
                                    <div className="inventory-table__image">
                                        {inventory.image ? (
                                            <img
                                                src={inventory.image}
                                                alt={inventory.productName}
                                            />
                                        ) : (
                                            <span>No image</span>
                                        )}
                                    </div>

                                    <div className="inventory-table__product-info">
                                        <span className="inventory-table__product-name">
                                            {inventory.productName}
                                        </span>

                                        <span className="inventory-table__external-id">
                                            {inventory.externalId}
                                        </span>
                                    </div>
                                </div>
                            </td>

                            <td>{inventory.stock}</td>

                            <td>{inventory.reservedStock}</td>

                            <td>{inventory.availableStock}</td>

                            <td>
                                <InventoryStatus
                                    status={inventory.status}
                                />
                            </td>

                            <td>
                                <button
                                    type="button"
                                    className="inventory-table__view-button"
                                    onClick={() =>
                                        onView(inventory.productId)
                                    }
                                >
                                    <EyeOutlined />
                                    <span>View</span>
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
};

const InventoryStatus = ({ status }) => {
    const statusMap = {
        IN_STOCK: {
            label: "In stock",
            className: "inventory-status--in-stock",
        },

        LOW_STOCK: {
            label: "Low stock",
            className: "inventory-status--low-stock",
        },

        OUT_OF_STOCK: {
            label: "Out of stock",
            className: "inventory-status--out-of-stock",
        },
    };

    const current = statusMap[status] ?? {
        label: status,
        className: "",
    };

    return (
        <span className={`inventory-status ${current.className}`}>
            {current.label}
        </span>
    );
};

export default InventoryTable;