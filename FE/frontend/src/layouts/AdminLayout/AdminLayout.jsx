import {
    Outlet
} from "react-router-dom";

import AdminSidebar
    from "../../components/admin/adminSidebar/AdminSidebar";

import AdminHeader
    from "../../components/admin/adminHeader/AdminHeader";

import "./AdminLayout.scss";


function AdminLayout() {

    return (

        <div className="admin-layout">

            {/* SIDEBAR */}

            <AdminSidebar />


            {/* MAIN */}

            <div className="admin-layout__main">

                {/* HEADER */}

                <AdminHeader />


                {/* PAGE */}

                <main className="admin-layout__content">

                    <Outlet />

                </main>

            </div>

        </div>

    );
}


export default AdminLayout;