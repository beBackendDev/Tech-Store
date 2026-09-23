import {
    Search,
    Bell,
    User
} from "lucide-react";



import "./AdminHeader.scss";
import useAuth from "../../../hooks/useAuth";


function AdminHeader() {

    const { auth } = useAuth();


    const username =
        auth?.user?.username
        || auth?.user?.email
        || "Admin";


    const role =
        auth?.roles?.[0]
        || "ADMIN";


    return (

        <header className="admin-header">


            {/* ================= SEARCH ================= */}

            <div className="admin-header__search">

                <Search size={20} />

                <input
                    type="text"
                    placeholder="Search orders, products..."
                />

            </div>


            {/* ================= ACTIONS ================= */}

            <div className="admin-header__actions">


                {/* NOTIFICATION */}

                <button
                    type="button"
                    className="admin-header__notification"
                >

                    <Bell size={21} />

                    <span
                        className="
                            admin-header__notification-dot
                        "
                    />

                </button>


                {/* PROFILE */}

                <div className="admin-header__profile">

                    <div className="admin-header__avatar">

                        <User size={18} />

                    </div>


                    <div className="admin-header__user">

                        <strong>
                            {username}
                        </strong>

                        <span>
                            {role}
                        </span>

                    </div>

                </div>


            </div>


        </header>

    );

}

export default AdminHeader;