import {

    BrowserRouter,

    Routes,

    Route

}

    from "react-router-dom";

import Login from "../pages/Login";

import Register from "../pages/Register";

import Home from "../components/auth/Home";

function AppRoutes() {

    return (

        <BrowserRouter>

            <Routes>
                <Route

                    path="/home"

                    element={<Home />}

                />
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