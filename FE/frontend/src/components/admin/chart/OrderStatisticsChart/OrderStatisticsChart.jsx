import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer
} from "recharts";

import "./OrderStatisticsChart.scss";


const formatLabel = (value) => {
    return value
        .replace(/([A-Z])/g, " $1")
        .replace(/^./, char => char.toUpperCase());
};


function OrderStatisticsChart({
    statistics
}) {

    const data = [
        {
            name: "Pending",
            value: statistics?.pendingOrders ?? 0
        },
        {
            name: "Confirmed",
            value: statistics?.confirmedOrders ?? 0
        },
        {
            name: "Processing",
            value: statistics?.processingOrders ?? 0
        },
        {
            name: "Shipped",
            value: statistics?.shippedOrders ?? 0
        },
        {
            name: "Delivered",
            value: statistics?.deliveredOrders ?? 0
        },
        {
            name: "Cancelled",
            value: statistics?.cancelledOrders ?? 0
        },
        {
            name: "Return Requested",
            value: statistics?.returnRequested ?? 0
        },
        {
            name: "Returned",
            value: statistics?.returnedOrders ?? 0
        }
    ];

    return (
        <section className="order-statistics-chart">

            <div className="order-statistics-chart__header">

                <div>
                    <h2>
                        Order Statistics
                    </h2>

                    <p>
                        Orders by current status
                    </p>
                </div>

                <span className="order-statistics-chart__total">
                    {statistics?.total ?? 0} orders
                </span>

            </div>


            <div className="order-statistics-chart__body">

                <ResponsiveContainer
                    width="100%"
                    height="100%"
                >

                    <BarChart
                        data={data}
                        margin={{
                            top: 10,
                            right: 10,
                            left: 0,
                            bottom: 10
                        }}
                    >

                        <CartesianGrid
                            strokeDasharray="3 3"
                            vertical={false}
                        />

                        <XAxis
                            dataKey="name"
                            tick={{
                                fontSize: 12
                            }}
                            tickFormatter={formatLabel}
                            interval={0}
                            angle={-25}
                            textAnchor="end"
                            height={65}
                        />

                        <YAxis
                            allowDecimals={false}
                            tick={{
                                fontSize: 12
                            }}
                        />

                        <Tooltip
                            formatter={(value) => [
                                value,
                                "Orders"
                            ]}
                        />

                        <Bar
                            dataKey="value"
                            name="Orders"
                            radius={[
                                6,
                                6,
                                0,
                                0
                            ]}
                        />

                    </BarChart>

                </ResponsiveContainer>

            </div>

        </section>
    );
}


export default OrderStatisticsChart;