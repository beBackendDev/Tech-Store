// import {
//     Navigate,
//     Outlet
// } from "react-router-dom";

// import useAuth from "../hooks/useAuth";

// function RoleRoute({
//     allowedRoles = []
// }) {

//     const {
//         auth
//     } = useAuth();

// console.log("auth.roles", auth);
//     const hasPermission =
//         auth.roles?.some(
//             role =>
//                 allowedRoles.includes(role)
//         );


//     return <Outlet />

// }

// export default RoleRoute;

import {
    Navigate,
    Outlet
} from "react-router-dom";

import useAuth from "../hooks/useAuth";

function RoleRoute({
    allowedRoles = []
}) {

    const {
        auth,
        loading
    } = useAuth();


    if (loading) {

        return (
            <div>
                Loading...
            </div>
        );

    }


    const hasPermission =
        auth.roles?.some(
            role =>
                allowedRoles.includes(role)
        );


    console.log(
        "========== ROLE ROUTE =========="
    );

    console.log(
        "User roles:",
        auth.roles
    );

    console.log(
        "Allowed roles:",
        allowedRoles
    );

    console.log(
        "Has permission:",
        hasPermission
    );

    console.log(
        "================================"
    );


    return hasPermission

        ? <Outlet />

        : (
            <Navigate
                to="/"
                replace
            />
        );

}

export default RoleRoute;