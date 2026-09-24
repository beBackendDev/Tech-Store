import { Outlet } from "react-router-dom";

import AdminSidebar
    from "../adminSidebar/AdminSidebar";

import AdminHeader
    from "../adminHeader/AdminHeader";

import "./AdminLayout.scss";


function AdminLayout() {

    return (

        <div className="admin-layout">

            <AdminSidebar />


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