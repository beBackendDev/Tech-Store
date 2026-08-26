import axios from "axios";
import { getAccessToken } from "../utils/tokenStorage";

const axiosPrivate = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true,
    headers: {
        "Content-Type": "application/json"
    }
});

axiosPrivate.interceptors.request.use(
    (config) => {

        const accessToken = getAccessToken();

        if (accessToken) {

            config.headers.Authorization =
                `Bearer ${accessToken}`;

        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

export default axiosPrivate;