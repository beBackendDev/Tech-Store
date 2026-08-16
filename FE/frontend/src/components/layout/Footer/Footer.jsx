import { Link } from "react-router-dom";

import Container from "../Container/Container";

import "./Footer.scss";

const Footer = () => {

    const currentYear = new Date().getFullYear();

    return (
        <footer className="footer">

            <Container>

                <div className="footer__content">

                    {/* Brand */}
                    <div className="footer__brand">

                        <Link
                            to="/"
                            className="footer__logo"
                        >
                            TechStore
                        </Link>

                        <p>
                            Your trusted technology store.
                        </p>

                    </div>


                    {/* Shop */}
                    <div className="footer__column">

                        <h3>Shop</h3>

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


                    {/* Support */}
                    <div className="footer__column">

                        <h3>Support</h3>

                        <Link to="/contact">
                            Contact
                        </Link>

                        <Link to="/faq">
                            FAQ
                        </Link>

                        <Link to="/shipping">
                            Shipping
                        </Link>

                    </div>


                    {/* Company */}
                    <div className="footer__column">

                        <h3>Company</h3>

                        <Link to="/about">
                            About us
                        </Link>

                        <Link to="/privacy">
                            Privacy Policy
                        </Link>

                        <Link to="/terms">
                            Terms
                        </Link>

                    </div>

                </div>


                <div className="footer__bottom">

                    <p>
                        © {currentYear} TechStore.
                        All rights reserved.
                    </p>

                    <div className="footer__social">

                        <a href="#" aria-label="Facebook">
                            Facebook
                        </a>

                        <a href="#" aria-label="Instagram">
                            Instagram
                        </a>

                        <a href="#" aria-label="Github">
                            GitHub
                        </a>

                    </div>

                </div>

            </Container>

        </footer>
    );
};

export default Footer;