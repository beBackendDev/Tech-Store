import {

    BrowserRouter,

    Routes,

    Route

}

    from "react-router-dom";

import Login from "../pages/Login";

import Register from "../pages/Register";

import Home from "../pages/Home";

import Profile from "../pages/Profile";

import AdminDashboard from "../pages/AdminDashboard";

import ProtectedRoute from "./ProtectedRoute";

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

            </Routes>

        </BrowserRouter>

    )

}

export default AppRoutes;