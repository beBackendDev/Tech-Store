import { jwtDecode } from "jwt-decode";

export const getAuthFromToken = (accessToken) => {

    if (!accessToken) {
        return null;
    }

    try {

        const decoded = jwtDecode(accessToken);

        console.log(
            "Decoded JWT:",
            decoded
        );

        return {
            user: {
                email: decoded.sub,
            },

            roles:
                decoded.roles || [],

            accessToken,
            authenticated: true,
        };

    } catch (error) {

        console.error(
            "Failed to decode JWT:",
            error
        );

        return null;
    }
};