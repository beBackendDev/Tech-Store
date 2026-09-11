import {

    LayoutDashboard,
    ShoppingCart,
    Package,
    Warehouse,
    Users,
    RotateCcw,
    BarChart3,
    Settings

} from "lucide-react";

import { NavLink } from "react-router-dom";

import "./AdminSidebar.scss";


function AdminSidebar() {

    const menuItems = [

        {
            label: "Dashboard",
            path: "/admin",
            icon: LayoutDashboard
        },

        {
            label: "Orders",
            path: "/admin/orders",
            icon: ShoppingCart
        },

        {
            label: "Products",
            path: "/admin/products",
            icon: Package
        },

        {
            label: "Inventory",
            path: "/admin/inventory",
            icon: Warehouse
        },

        {
            label: "Customers",
            path: "/admin/customers",
            icon: Users
        },

        {
            label: "Returns",
            path: "/admin/returns",
            icon: RotateCcw
        },

        {
            label: "Analytics",
            path: "/admin/analytics",
            icon: BarChart3
        }

    ];


    return (

        <aside className="admin-sidebar">


            {/* ================= BRAND ================= */}

            <div className="admin-sidebar__brand">

                <span className="admin-sidebar__brand-primary">
                    TECH
                </span>

                <span>
                    STORE
                </span>

            </div>


            {/* ================= NAVIGATION ================= */}

            <nav className="admin-sidebar__navigation">

                <span className="admin-sidebar__label">

                    MANAGEMENT

                </span>


                <div className="admin-sidebar__menu">

                    {menuItems.map(item => {

                        const Icon = item.icon;

                        return (

                            <NavLink

                                key={item.path}

                                to={item.path}

                                end={item.path === "/admin"}

                                className={({ isActive }) =>
                                    `
                                    admin-sidebar__link
                                    ${isActive
                                        ? "admin-sidebar__link--active"
                                        : ""
                                    }
                                    `
                                }

                            >

                                <Icon
                                    size={20}
                                    strokeWidth={2}
                                />

                                <span>

                                    {item.label}

                                </span>

                            </NavLink>

                        );

                    })}

                </div>

            </nav>


            {/* ================= BOTTOM ================= */}

            <div className="admin-sidebar__footer">

                <NavLink

                    to="/admin/settings"

                    className="admin-sidebar__link"

                >

                    <Settings size={20} />

                    <span>
                        Settings
                    </span>

                </NavLink>

            </div>


        </aside>

    );

}

export default AdminSidebar;