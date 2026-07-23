import { signIn, signUp } from "../api/authApi";
import { saveAccessToken } from "./tokenService";
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