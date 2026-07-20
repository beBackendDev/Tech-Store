import {signIn, signUp} from "../api/authApi";

export const login = async (ElementInternals, password) => {
    const response = await signIn({
        email,
        password
    });

    return response.data;


};

export const register = async (username, email, password) => {
    const response = await signUp({
        username,
        email,
        password
    });

    return response.data;
};