import "./OrderTimeline.scss";

const TIMELINE = [
    { status: "PENDING", label: "Order Placed", shortLabel: "Placed", dateKey: "createdAt" },
    { status: "CONFIRMED", label: "Order Confirmed", shortLabel: "Confirmed" },
    { status: "PROCESSING", label: "Processing", shortLabel: "Processing" },
    { status: "SHIPPING", label: "Order Shipped", shortLabel: "Shipped" },
    { status: "DELIVERED", label: "Delivered", shortLabel: "Delivered" },
    { status: "COMPLETED", label: "Order Completed", shortLabel: "Completed" },
];

const STATUS_INDEX = Object.fromEntries(TIMELINE.map((step, index) => [step.status, index]));

const ICONS = {
    PENDING: "▤",
    CONFIRMED: "✓",
    PROCESSING: "◷",
    SHIPPING: "⇢",
    DELIVERED: "□",
    COMPLETED: "★",
};

const EXCEPTIONAL_STATUSES = new Set(["CANCELLED", "RETURN_REQUESTED", "RETURNED"]);

export default function OrderTimeline({ status, order }) {
    const currentIndex = STATUS_INDEX[status] ?? -1;
    const isExceptional = EXCEPTIONAL_STATUSES.has(status);
    const progress = currentIndex < 0 ? 0 : (currentIndex / (TIMELINE.length - 1)) * 100;

    return (
        <div
            className={`order-timeline${isExceptional ? " order-timeline--exceptional" : ""}`}
            aria-label={`Order progress: ${String(status || "unknown").replaceAll("_", " ")}`}
        >
            <div className="order-timeline__track" aria-hidden="true">
                <span
                    className="order-timeline__track-fill"
                    style={{ width: `${progress}%` }}
                />
            </div>

            <ol className="order-timeline__steps">
                {TIMELINE.map((step, index) => {
                    const complete = currentIndex >= index;
                    const current = currentIndex === index;
                    const date = step.dateKey && order?.[step.dateKey];

                    return (
                        <li
                            key={step.status}
                            className={[
                                "order-timeline__step",
                                complete ? "is-complete" : "",
                                current ? "is-current" : "",
                            ].filter(Boolean).join(" ")}
                            aria-current={current ? "step" : undefined}
                        >
                            <span className="order-timeline__node" aria-hidden="true">
                                <span className="order-timeline__icon">
                                    {complete && index < currentIndex ? "✓" : ICONS[step.status]}
                                </span>
                            </span>
                            <span className="order-timeline__label">{step.label}</span>
                            {date && (
                                <time className="order-timeline__date" dateTime={date}>
                                    {new Date(date).toLocaleString(undefined, {
                                        day: "2-digit",
                                        month: "short",
                                        year: "numeric",
                                        hour: "2-digit",
                                        minute: "2-digit",
                                    })}
                                </time>
                            )}
                        </li>
                    );
                })}
            </ol>

            {isExceptional && (
                <p className="order-timeline__exception" role="status">
                    {status === "CANCELLED" && "This order has been cancelled."}
                    {status === "RETURN_REQUESTED" && "A return has been requested for this order."}
                    {status === "RETURNED" && "This order has been returned."}
                </p>
            )}
        </div>
    );
}
