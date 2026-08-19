import {

    BrowserRouter,

    Routes,

    Route

}

    from "react-router-dom";


import Register from "../pages/Auth/Register";

import Home from "../pages/Home/Home";

import Profile from "../pages/Profile/Profile";

import AdminDashboard from "../pages/AdminDashboard/AdminDashboard";

import ProtectedRoute from "./ProtectedRoute";

import Login from "../pages/Auth/Login";

import MainLayout from "../layouts/MainLayout/MainLayout";

import ProductDetail from "../pages/ProductDetail/ProductDetail";

import Categories from "../pages/Categories/Categories";
function AppRoutes() {

    return (

        <BrowserRouter>

            <Routes>
                <Route

                    path="/home"

                    element={<Home />}

                />
                <Route element={<ProtectedRoute />}>

                    <Route

                        path="/profile"

                        element={<Profile />}

                    />

                    <Route

                        path="/admin"

                        element={<AdminDashboard />}

                    />

                </Route>
                <Route

                    path="/login"

                    element={<Login />}

                />

                <Route

                    path="/register"

                    element={<Register />}

                />
                <Route element={<MainLayout />}>

                    <Route path="/" element={<Home />} />

                    {/* <Route path="/products" element={<Products />} /> */}

                    {/* <Route path="/categories" element={<Categories />} /> */}

                </Route>
                {/*  Route detail product */}
                <Route
                    path="/products/:id"
                    element={<ProductDetail />}
                />
                <Route
                    path="/categories"
                    element={<Categories />}
                />
            </Routes>

        </BrowserRouter>

    )

}

export default AppRoutes;