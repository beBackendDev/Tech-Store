import {

    DollarSign,
    ShoppingCart,
    Package,
    AlertTriangle

} from "lucide-react";

import "./DashboardStats.scss";


function DashboardStats({ stats }) {

    const dashboardStats = [

        {
            title: "Total Revenue",
            value: stats?.totalRevenue ?? "₫0",
            subtitle: "Revenue overview",
            icon: DollarSign,
            type: "revenue"
        },

        {
            title: "Total Orders",
            value: stats?.totalOrders ?? 0,
            subtitle: "All customer orders",
            icon: ShoppingCart,
            type: "orders"
        },

        {
            title: "Products",
            value: stats?.totalProducts ?? 0,
            subtitle: "Active products",
            icon: Package,
            type: "products"
        },

        {
            title: "Low Stock",
            value: stats?.lowStockProducts ?? 0,
            subtitle: "Needs attention",
            icon: AlertTriangle,
            type: "warning"
        }

    ];


    return (

        <section className="dashboard-stats">

            {dashboardStats.map(stat => {

                const Icon = stat.icon;

                return (

                    <article
                        className="
                        dashboard-stats__card
                        "
                        key={stat.title}
                    >

                        <div className="dashboard-stats__content">

                            <span className="dashboard-stats__title">

                                {stat.title}

                            </span>


                            <h3 className="dashboard-stats__value">

                                {stat.value}

                            </h3>


                            <span className="dashboard-stats__subtitle">

                                {stat.subtitle}

                            </span>

                        </div>


                        <div
                            className={`
                            dashboard-stats__icon
                            dashboard-stats__icon--${stat.type}
                            `}
                        >

                            <Icon size={24} />

                        </div>

                    </article>

                );

            })}

        </section>

    );

}

export default DashboardStats;