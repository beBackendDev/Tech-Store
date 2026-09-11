import {
    Navigate,
    Outlet
} from "react-router-dom";

import useAuth from "../hooks/useAuth";


function ProtectedRoute() {

    const {
        auth,
        loading
    } = useAuth();


    // WAIT FOR AUTH INITIALIZATION

    if (loading) {

        return (
            <div>
                Loading...
            </div>
        );
    }


    // NOT AUTHENTICATED

    if (!auth.authenticated) {

        return (
            <Navigate
                to="/login"
                replace
            />
        );
    }


    // AUTHENTICATED

    return <Outlet />;
}


export default ProtectedRoute;