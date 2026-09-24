import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer
} from "recharts";

import "./InventoryStatisticsChart.scss";


function InventoryStatisticsChart({
    statistics
}) {

    const data = [
        {
            name: "Stock In",
            value: statistics?.stockInToday ?? 0
        },
        {
            name: "Stock Out",
            value: statistics?.stockOutToday ?? 0
        },
        {
            name: "Reserved",
            value: statistics?.reservedToday ?? 0
        },
        {
            name: "Released",
            value: statistics?.releasedToday ?? 0
        },
        {
            name: "Adjustment",
            value: statistics?.adjustmentToday ?? 0
        }
    ];


    return (
        <section className="inventory-statistics-chart">

            <div className="inventory-statistics-chart__header">

                <div>

                    <h2>
                        Inventory Activity
                    </h2>

                    <p>
                        Today's inventory movements
                    </p>

                </div>

            </div>


            <div className="inventory-statistics-chart__body">

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
                                "Units"
                            ]}
                        />

                        <Bar
                            dataKey="value"
                            name="Units"
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


export default InventoryStatisticsChart;