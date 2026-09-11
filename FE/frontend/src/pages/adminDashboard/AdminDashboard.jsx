import DashboardStats
    from "../../components/admin/dashboardStats/DashboardStats";

import RecentOrders
    from "../../components/admin/recentOrders/RecentOrders";

import InventoryAlert
    from "../../components/admin/inventoryAlert/InventoryAlert";

import "./AdminDashboard.scss";


function AdminDashboard() {

    /*
     * TEMPORARY MOCK DATA
     *
     * Sau này thay bằng:
     *
     * GET /api/admin/dashboard
     */

    const dashboardData = {

        totalRevenue: "₫125,000,000",

        totalOrders: 1248,

        totalProducts: 1000,

        lowStockProducts: 12,

        recentOrders: [

            {
                id: 10001,

                customerName: "Nguyen Van A",

                totalAmount: "₫2,500,000",

                status: "PENDING"
            },

            {
                id: 10002,

                customerName: "Tran Van B",

                totalAmount: "₫15,000,000",

                status: "PROCESSING"
            },

            {
                id: 10003,

                customerName: "Le Van C",

                totalAmount: "₫8,500,000",

                status: "SHIPPED"
            }

        ],

        lowStockItems: [

            {
                id: 1,

                name: "MacBook Air M3",

                stock: 2
            },

            {
                id: 2,

                name: "Samsung SSD 990 Pro",

                stock: 1
            },

            {
                id: 3,

                name: "Logitech MX Master 3S",

                stock: 3
            }

        ]

    };


    return (

        <div className="admin-dashboard">


            {/* ================= PAGE HEADER ================= */}

            <header className="admin-dashboard__header">

                <div>

                    <span
                        className="
                        admin-dashboard__eyebrow
                        "
                    >

                        ADMIN PANEL

                    </span>


                    <h1>
                        Dashboard
                    </h1>


                    <p>

                        Welcome back. Here's what's
                        happening with your store today.

                    </p>

                </div>

            </header>


            {/* ================= STATISTICS ================= */}

            <DashboardStats
                stats={dashboardData}
            />


            {/* ================= MAIN CONTENT ================= */}

            <section
                className="
                admin-dashboard__content
                "
            >

                <RecentOrders
                    orders={
                        dashboardData.recentOrders
                    }
                />


                <InventoryAlert
                    products={
                        dashboardData.lowStockItems
                    }
                />

            </section>


        </div>

    );

}

export default AdminDashboard;