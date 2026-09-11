// import {
//     signIn, signUp,
//     refreshToken as refreshApi,
//     logout as logoutApi,
// } from "../api/authApi";
// import {
//     saveAccessToken,
//     removeAccessToken
// } from "./tokenService";

// export const login = async (email, password) => {
//     const response = await signIn({
//         email,
//         password
//     });
//     const accessToken = response.data.response;
//     console.log("accessToken", accessToken);
//     saveAccessToken(accessToken);

//     return response.data;


// };

// // export const register = async (username, email, password) => {
// export const register = async (user) => {
//     const response = await signUp(
//         user
//     );

//     return response.data;
// };

// export const refreshToken = async () => {
//     console.log("refreshToken started:");

//     const response = await refreshApi();

//     const accessToken = response.data.response;
//     saveAccessToken(accessToken);

//     return accessToken;

// };

// export const logout = async () => {

//     await logoutApi();

//     removeAccessToken();

// };

import {
    signIn,
    signUp,
    refreshToken as refreshApi,
    logout as logoutApi,
} from "../api/authApi";

import {
    saveAccessToken,
    removeAccessToken
} from "./tokenService";

import {
    getAuthFromToken
} from "../utils/jwtUtils";


// =========================================
// LOGIN
// =========================================

export const login = async (
    email,
    password
) => {

    const response = await signIn({
        email,
        password
    });

    const accessToken =
        response.data.response;

    if (!accessToken) {

        throw new Error(
            "Login response does not contain access token"
        );

    }

    saveAccessToken(accessToken);

    const authData =
        getAuthFromToken(accessToken);

    if (!authData) {

        throw new Error(
            "Failed to decode access token"
        );

    }

    return authData;
};


// =========================================
// REGISTER
// =========================================

export const register = async (
    user
) => {

    const response =
        await signUp(user);

    return response.data;
};


// =========================================
// REFRESH TOKEN
// =========================================

export const refreshToken = async () => {

    console.log(
        "Refresh token started"
    );

    const response =
        await refreshApi();

    const accessToken =
        response.data.response;

    if (!accessToken) {

        throw new Error(
            "Refresh token response does not contain access token"
        );

    }

    saveAccessToken(accessToken);

    const authData =
        getAuthFromToken(accessToken);

    if (!authData) {

        throw new Error(
            "Failed to decode refreshed token"
        );

    }

    return authData;
};


// =========================================
// LOGOUT
// =========================================

export const logout = async () => {

    try {

        await logoutApi();

    } finally {

        removeAccessToken();

    }

};