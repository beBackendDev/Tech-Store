import {
    signIn, signUp,
    refreshToken as refreshApi,
    logout as logoutApi,
} from "../api/authApi";
import {
    saveAccessToken,
    removeAccessToken
} from "./tokenService";

export const login = async (email, password) => {
    const response = await signIn({
        email,
        password
    });
    const accessToken = response.data.response;

    saveAccessToken(accessToken);

    return response.data;


};

// export const register = async (username, email, password) => {
export const register = async (user) => {
    const response = await signUp(
        user
    );

    return response.data;
};

export const refreshToken = async () => {
    console.log("refreshToken started:");

    const response = await refreshApi();

    const accessToken = response.data.response;
    saveAccessToken(accessToken);

    return accessToken;

};

export const logout = async () => {

    await logoutApi();

    removeAccessToken();

};