export const getDefaultRouteByRole =
    (roles = []) => {

        if (
            roles.includes("ADMIN")
        ) {

            return "/admin";

        }

        return "/";
    };