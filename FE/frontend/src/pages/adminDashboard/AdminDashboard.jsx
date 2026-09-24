import { useEffect, useState } from "react";

import DashboardStats
    from "../../components/admin/dashboardStats/DashboardStats";

import RecentOrders
    from "../../components/admin/recentOrders/RecentOrders";

import InventoryAlert
    from "../../components/admin/inventoryAlert/InventoryAlert";

import useAxiosPrivate
    from "../../hooks/useAxiosPrivate";

import { getAdminDashboard }
    from "../../api/adminApi";

import "./AdminDashboard.scss";
import OrderStatisticsChart from "../../components/admin/chart/OrderStatisticsChart/OrderStatisticsChart";
import InventoryStatisticsChart from "../../components/admin/chart/InventoryStatisticsChart/InventoryStatisticsChart";


function AdminDashboard() {

    const axiosPrivate = useAxiosPrivate();

    const [dashboardData, setDashboardData] =
        useState(null);

    const [loading, setLoading] =
        useState(true);

    const [error, setError] =
        useState(null);


    useEffect(() => {

        let mounted = true;


        const fetchDashboard = async () => {

            try {

                setLoading(true);
                setError(null);

                const response =
                    await getAdminDashboard(axiosPrivate);
                console.log("Admin Dashboard Response:", response.data.response);
                if (!mounted) {
                    return;
                }

                setDashboardData(
                    response.data.response
                );

            } catch (error) {

                if (!mounted) {
                    return;
                }

                console.error(
                    "Failed to fetch admin dashboard:",
                    error
                );

                setError(
                    "Unable to load dashboard data."
                );

            } finally {

                if (mounted) {
                    setLoading(false);
                }

            }

        };


        fetchDashboard();


        return () => {
            mounted = false;
        };

    }, [axiosPrivate]);


    /*
     * ============================
     * LOADING
     * ============================
     */

    if (loading) {

        return (

            <div className="admin-dashboard">

                <div className="admin-dashboard__loading">

                    Loading dashboard...

                </div>

            </div>

        );

    }


    /*
     * ============================
     * ERROR
     * ============================
     */

    if (error) {

        return (

            <div className="admin-dashboard">

                <div className="admin-dashboard__error">

                    {error}

                </div>

            </div>

        );

    }


    /*
     * ============================
     * EMPTY
     * ============================
     */

    if (!dashboardData) {
        return null;
    }


    /*
     * ============================
     * BACKEND RESPONSE
     *
     * dashboardData:
     *
     * {
     *     overview,
     *     orderStatistics,
     *     productStatistics,
     *     inventoryStatistics,
     *     recentOrders,
     *     lowStockProducts
     * }
     *
     * ============================
     */


    return (

        <div className="admin-dashboard">


            {/* ================= PAGE HEADER ================= */}

            <header className="admin-dashboard__header">

                <div>

                    <span className="admin-dashboard__eyebrow">

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
                overview={
                    dashboardData.overview
                }

                productStatistics={
                    dashboardData.productStatistics
                }
            />
            {/* ================= CHARTS ================= */}

            <section className="admin-dashboard__charts">

                <OrderStatisticsChart
                    statistics={dashboardData.orderStatistics}
                />

                <InventoryStatisticsChart
                    statistics={dashboardData.inventoryStatistics}
                />

            </section>
            {/* ================= MAIN CONTENT ================= */}

            <section className="admin-dashboard__content">


                {/* ================= RECENT ORDERS ================= */}

                <RecentOrders
                    orders={
                        dashboardData.recentOrders
                    }
                />


                {/* ================= INVENTORY ALERT ================= */}

                <InventoryAlert
                    products={
                        dashboardData.lowStockProducts
                    }
                />


            </section>


        </div>

    );

}

export default AdminDashboard;