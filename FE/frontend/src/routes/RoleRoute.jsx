import {
    Navigate,
    Outlet
} from "react-router-dom";

import useAuth from "../hooks/useAuth";

function RoleRoute({
    allowedRoles = []
}) {

    const {
        auth
    } = useAuth();

console.log("auth.roles", auth);
    const hasPermission =
        auth.roles?.some(
            role =>
                allowedRoles.includes(role)
        );


    return <Outlet />

}

export default RoleRoute;