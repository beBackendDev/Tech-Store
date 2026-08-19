import { useContext, useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import {
    UserOutlined,
    ShoppingOutlined,
    SettingOutlined,
    DashboardOutlined,
    LogoutOutlined,
    DownOutlined
} from "@ant-design/icons";

import { AuthContext } from "../../../context/AuthContext";

import "./UserMenu.scss";
import { logout } from "../../../services/authService";

function UserMenu() {

    const { auth, setAuth } = useContext(AuthContext);

    const navigate = useNavigate();

    const [open, setOpen] = useState(false);

    const menuRef = useRef(null);

    /*
    |--------------------------------------------------------------------------
    | User information
    |--------------------------------------------------------------------------
    */

    const user = auth?.user;

    const username = user?.username || "User";

    const email = user?.email || "";

    const roles = user?.roles || [];

    const isAdmin = roles.includes("ADMIN");

    /*
    |--------------------------------------------------------------------------
    | Avatar
    |--------------------------------------------------------------------------
    */

    const avatarLetter = username
        .charAt(0)
        .toUpperCase();

    /*
    |--------------------------------------------------------------------------
    | Toggle menu
    |--------------------------------------------------------------------------
    */

    const handleToggle = () => {
        setOpen(prev => !prev);
    };

    /*
    |--------------------------------------------------------------------------
    | Close menu when clicking outside
    |--------------------------------------------------------------------------
    */

    useEffect(() => {

        const handleClickOutside = (event) => {

            if (
                menuRef.current &&
                !menuRef.current.contains(event.target)
            ) {
                setOpen(false);
            }

        };

        document.addEventListener(
            "mousedown",
            handleClickOutside
        );

        return () => {

            document.removeEventListener(
                "mousedown",
                handleClickOutside
            );

        };

    }, []);

    /*
    |--------------------------------------------------------------------------
    | Close menu with Escape
    |--------------------------------------------------------------------------
    */

    useEffect(() => {

        const handleEscape = (event) => {

            if (event.key === "Escape") {
                setOpen(false);
            }

        };

        document.addEventListener(
            "keydown",
            handleEscape
        );

        return () => {

            document.removeEventListener(
                "keydown",
                handleEscape
            );

        };

    }, []);

    /*
    |--------------------------------------------------------------------------
    | Logout
    |--------------------------------------------------------------------------
    */

    const handleLogout = async () => {

        try {

           await logout();
            

        } catch (error) {

            console.error(
                "Logout failed:",
                error
            );

        } finally {

            /*
             * Clear authentication state
             */

            setAuth({
                accessToken: null,
                user: null,
                authenticated: false
            });

            setOpen(false);

            navigate("/login");

        }

    };

    /*
    |--------------------------------------------------------------------------
    | Don't render if not authenticated
    |--------------------------------------------------------------------------
    */

    if (!auth?.authenticated) {
        return null;
    }

    return (

        <div
            className="user-menu"
            ref={menuRef}
        >

            {/* ============================================================
                Trigger
            ============================================================ */}

            <button
                type="button"
                className="user-menu__trigger"
                onClick={handleToggle}
                aria-expanded={open}
                aria-haspopup="menu"
            >

                {/* Avatar */}

                <div className="user-menu__avatar">
                    {avatarLetter}
                </div>

                {/* Desktop user information */}

                <div className="user-menu__identity">

                    <span className="user-menu__username">
                        {username}
                    </span>

                    <span className="user-menu__email">
                        {email}
                    </span>

                </div>

                <DownOutlined
                    className={`user-menu__arrow ${
                        open
                            ? "user-menu__arrow--open"
                            : ""
                    }`}
                />

            </button>


            {/* ============================================================
                Dropdown
            ============================================================ */}

            {open && (

                <div
                    className="user-menu__dropdown"
                    role="menu"
                >

                    {/* ----------------------------------------------------
                        User information
                    ---------------------------------------------------- */}

                    <div className="user-menu__profile">

                        <div className="user-menu__profile-avatar">
                            {avatarLetter}
                        </div>

                        <div className="user-menu__profile-info">

                            <strong>
                                {username}
                            </strong>

                            <span>
                                {email}
                            </span>

                        </div>

                    </div>


                    <div className="user-menu__divider" />


                    {/* ----------------------------------------------------
                        Profile
                    ---------------------------------------------------- */}

                    <Link
                        to="/profile"
                        className="user-menu__item"
                        role="menuitem"
                        onClick={() => setOpen(false)}
                    >

                        <UserOutlined />

                        <span>
                            Profile
                        </span>

                    </Link>


                    {/* ----------------------------------------------------
                        Orders
                    ---------------------------------------------------- */}

                    <Link
                        to="/orders"
                        className="user-menu__item"
                        role="menuitem"
                        onClick={() => setOpen(false)}
                    >

                        <ShoppingOutlined />

                        <span>
                            My Orders
                        </span>

                    </Link>


                    {/* ----------------------------------------------------
                        Settings
                    ---------------------------------------------------- */}

                    <Link
                        to="/settings"
                        className="user-menu__item"
                        role="menuitem"
                        onClick={() => setOpen(false)}
                    >

                        <SettingOutlined />

                        <span>
                            Settings
                        </span>

                    </Link>


                    {/* ----------------------------------------------------
                        Admin
                    ---------------------------------------------------- */}

                    {isAdmin && (

                        <>

                            <div className="user-menu__divider" />

                            <Link
                                to="/admin"
                                className="user-menu__item user-menu__item--admin"
                                role="menuitem"
                                onClick={() => setOpen(false)}
                            >

                                <DashboardOutlined />

                                <span>
                                    Admin Dashboard
                                </span>

                            </Link>

                        </>

                    )}


                    <div className="user-menu__divider" />


                    {/* ----------------------------------------------------
                        Logout
                    ---------------------------------------------------- */}

                    <button
                        type="button"
                        className="user-menu__item user-menu__item--logout"
                        onClick={handleLogout}
                        role="menuitem"
                    >

                        <LogoutOutlined />

                        <span>
                            Logout
                        </span>

                    </button>

                </div>

            )}

        </div>

    );

}

export default UserMenu;