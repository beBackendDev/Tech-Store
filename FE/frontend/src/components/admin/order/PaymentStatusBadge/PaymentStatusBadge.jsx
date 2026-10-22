const PAYMENT_STATUS_CONFIG = {
    PENDING: {
        label: "Pending",
        className: "pending",
    },
    PAID: {
        label: "Paid",
        className: "paid",
    },
    FAILED: {
        label: "Failed",
        className: "failed",
    },
    REFUNDED: {
        label: "Refunded",
        className: "refunded",
    },
};

export default function PaymentStatusBadge({ status }) {

    const config = PAYMENT_STATUS_CONFIG[status];

    if (!config) {
        return null;
    }

    return (
        <span className={`payment-status ${config.className}`}>
            {config.label}
        </span>
    );
}