const ORDER_STATUS_CONFIG = {
    PENDING: {
        label: "Pending",
        className: "pending",
    },
    CONFIRMED: {
        label: "Confirmed",
        className: "confirmed",
    },
    PROCESSING: {
        label: "Processing",
        className: "processing",
    },
    SHIPPING: {
        label: "Shipping",
        className: "shipping",
    },
    DELIVERED: {
        label: "Delivered",
        className: "delivered",
    },
    COMPLETED: {
        label: "Completed",
        className: "completed",
    },
    CANCELLED: {
        label: "Cancelled",
        className: "cancelled",
    },
    RETURN_REQUESTED: {
        label: "Return requested",
        className: "return-requested",
    },
    RETURNED: {
        label: "Returned",
        className: "returned",
    },
};

export default function OrderStatusBadge({ status }) {

    const config = ORDER_STATUS_CONFIG[status];

    if (!config) {
        return null;
    }

    return (
        <span className={`order-status ${config.className}`}>
            {config.label}
        </span>
    );
}