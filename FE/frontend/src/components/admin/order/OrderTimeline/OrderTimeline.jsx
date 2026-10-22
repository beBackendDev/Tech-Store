const TIMELINE = [
    "PENDING",
    "CONFIRMED",
    "PROCESSING",
    "SHIPPING",
    "DELIVERED",
    "COMPLETED",
];

const STATUS_INDEX = {
    PENDING: 0,
    CONFIRMED: 1,
    PROCESSING: 2,
    SHIPPING: 3,
    DELIVERED: 4,
    COMPLETED: 5,
};

export default function OrderTimeline({ status }) {

    const currentIndex = STATUS_INDEX[status] ?? -1;

    return (
        <div className="order-timeline">

            {TIMELINE.map((item, index) => {

                const completed = index <= currentIndex;

                return (
                    <div
                        key={item}
                        className={`timeline-item ${
                            completed ? "completed" : ""
                        }`}
                    >
                        <div className="timeline-item__dot">
                            {completed ? "✓" : ""}
                        </div>

                        <div className="timeline-item__content">
                            {item}
                        </div>
                    </div>
                );
            })}
        </div>
    );
}