import { useState } from "react";
import { Link } from "react-router-dom";
import { LuShoppingCart } from "react-icons/lu";
import { CgProfile } from "react-icons/cg";
import { AiOutlineSearch } from "react-icons/ai";

import Container from "../Container/Container";

import "./Header.scss";
import useAuth from "../../../hooks/useAuth";
import UserMenu from "./UserMenu";
const Header = () => {
    const { auth, logout } = useAuth();
    const [isMenuOpen, setIsMenuOpen] = useState(false);

    return (
        <header className="header">

            <Container>

                <nav className="navbar">

                    {/* Mobile menu button */}
                    <button
                        className="navbar__menu-button"
                        onClick={() => setIsMenuOpen(!isMenuOpen)}
                        aria-label="Toggle navigation"
                    >
                        ☰
                    </button>


                    {/* Logo */}
                    <Link
                        to="/"
                        className="navbar__logo"
                    >
                        TechStore
                    </Link>


                    {/* Navigation */}
                    <div
                        className={`navbar__navigation ${isMenuOpen
                            ? "navbar__navigation--open"
                            : ""
                            }`}
                    >

                        <Link to="/">
                            Home
                        </Link>

                        <Link to="/products">
                            Products
                        </Link>

                        <Link to="/categories">
                            Categories
                        </Link>

                        <Link to="/deals">
                            Deals
                        </Link>

                    </div>


                    {/* Search */}
                    <div className="navbar__search">

                        <input
                            type="text"
                            placeholder="Search products..."
                        />

                        <button aria-label="Search">
                            <AiOutlineSearch />

                        </button>

                    </div>


                    {/* Actions */}
                    <div className="navbar__actions">

                        <Link
                            to="/cart"
                            className="navbar__action"
                            aria-label="Shopping cart"
                        >
                            <LuShoppingCart />
                        </Link>
                        {/*authentication */}
                        {auth.authenticated ? (
                            <UserMenu
                                user={auth.user}
                                onLogout={logout}
                            />
                        )
                            : (<div className="header__auth">

                                <Link to="/login">
                                    Sign in
                                </Link>

                                <Link to="/register">
                                    Sign up
                                </Link>

                            </div>)}
                        {/* <Link
                            to="/login"
                            className="navbar__action"
                            aria-label="Account"
                        >
                            <CgProfile />
                        </Link> */}

                    </div>

                </nav>

            </Container>

        </header>
    );
};

export default Header;