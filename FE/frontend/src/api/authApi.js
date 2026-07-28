//dungf để gọi api cho auth
import api from './axios';

export const signIn = (data) => 
    api.post("auth/sign-in", data);

export const signUp = (data) =>
    api.post("auth/sign-up", data);

export const logout = () =>
    api.post("/auth/logout");

export const refreshToken = () =>
    api.post("/auth/cookie/refresh");