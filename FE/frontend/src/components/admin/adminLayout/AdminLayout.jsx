import { Outlet } from "react-router-dom";

import AdminSidebar
    from "../AdminSidebar/AdminSidebar";

import AdminHeader
    from "../AdminHeader/AdminHeader";

import "./AdminLayout.scss";


function AdminLayout() {

    return (

        <div className="admin-layout">

            {/* ================= SIDEBAR ================= */}

            <AdminSidebar />


            {/* ================= MAIN ================= */}

            <div className="admin-layout__main">

                <AdminHeader />

                <main className="admin-layout__content">

                    <Outlet />

                </main>

            </div>

        </div>

    );

}

export default AdminLayout;