import {

    BrowserRouter,

    Routes,

    Route

} from "react-router-dom";


/* ===============================
   AUTH
================================ */

import Login
    from "../pages/Auth/Login";

import Register
    from "../pages/Auth/Register";


/* ===============================
   USER PAGES
================================ */

import Home
    from "../pages/Home/Home";

import Profile
    from "../pages/Profile/Profile";

import ProductDetail
    from "../pages/ProductDetail/ProductDetail";

import Categories
    from "../pages/Categories/Categories";

import ProductListing
    from "../pages/Product/ProductListing/ProductListing";

import Cart
    from "../pages/Cart/Cart";

import Checkout
    from "../pages/Checkout/Checkout";

import OrderSuccess
    from "../pages/Checkout/OrderSuccess/OrderSuccess";

import OrderFailed
    from "../pages/Checkout/OrderFailed/OrderFailed";

import OrderHistory
    from "../pages/orders/OrderHistory/OrderHistory";

import OrderDetail
    from "../pages/orders/OrderDetail/OrderDetail";


/* ===============================
   ADMIN
================================ */

import AdminDashboard
    from "../pages/adminDashboard/AdminDashboard";


/* ===============================
   ERROR
================================ */

import NotFound
    from "../pages/Error/NotFound/NotFound";


/* ===============================
   LAYOUT
================================ */

import MainLayout
    from "../layouts/MainLayout/MainLayout";

import AdminLayout
    from "../layouts/AdminLayout/AdminLayout";
/* ===============================
   ROUTES
================================ */

import ProtectedRoute
    from "./ProtectedRoute";

import RoleRoute
    from "./RoleRoute";


function AppRoutes() {

    return (

        <BrowserRouter>

            <Routes>


                {/* ===============================
                    PUBLIC AUTH ROUTES
                ================================ */}

                <Route
                    path="/login"
                    element={<Login />}
                />

                <Route
                    path="/register"
                    element={<Register />}
                />


                {/* ===============================
                    MAIN USER LAYOUT
                ================================ */}

                <Route
                    element={<MainLayout />}
                >

                    {/* HOME */}

                    <Route
                        path="/"
                        element={<Home />}
                    />


                    {/* PRODUCTS */}

                    <Route
                        path="/products"
                        element={<ProductListing />}
                    />

                    <Route
                        path="/products/:id"
                        element={<ProductDetail />}
                    />


                    {/* CATEGORIES */}

                    <Route
                        path="/categories"
                        element={<Categories />}
                    />


                    {/* ===============================
                        PROTECTED USER ROUTES
                    ================================ */}

                    <Route
                        element={<ProtectedRoute />}
                    >

                        {/* PROFILE */}

                        <Route
                            path="/profile"
                            element={<Profile />}
                        />


                        {/* CART */}

                        <Route
                            path="/cart"
                            element={<Cart />}
                        />


                        {/* CHECKOUT */}

                        <Route
                            path="/checkout"
                            element={<Checkout />}
                        />


                        {/* ORDER RESULT */}

                        <Route
                            path="/order-success"
                            element={<OrderSuccess />}
                        />

                        <Route
                            path="/order-failed"
                            element={<OrderFailed />}
                        />


                        {/* ORDERS */}

                        <Route
                            path="/orders"
                            element={<OrderHistory />}
                        />

                        <Route
                            path="/orders/:id"
                            element={<OrderDetail />}
                        />

                    </Route>

                </Route>


                {/* ===============================
                    ADMIN ROUTES
                ================================ */}

                <Route
                    element={<ProtectedRoute />}
                >

                    <Route
                        element={
                            <RoleRoute
                                allowedRoles={["ADMIN"]}
                            />
                        }
                    >

                        <Route
                            path="/admin"
                            element={<AdminLayout />}
                        >

                            {/* DASHBOARD */}

                            <Route
                                index
                                element={<AdminDashboard />}
                            />

                        </Route>

                    </Route>

                </Route>


                {/* ===============================
                    NOT FOUND
                ================================ */}

                <Route
                    path="*"
                    element={<NotFound />}
                />


            </Routes>

        </BrowserRouter>

    );
}


export default AppRoutes;