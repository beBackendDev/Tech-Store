import {

    BrowserRouter,

    Routes,

    Route

}

    from "react-router-dom";

import Login from "../pages/Login";

import Register from "../pages/Register";

function AppRoutes() {

    return (

        <BrowserRouter>

            <Routes>

                <Route

                    path="/"

                    element={<Register />}

                />

                <Route

                    path="/register"

                    element={<Login />}

                />

            </Routes>

        </BrowserRouter>

    )

}

export default AppRoutes;