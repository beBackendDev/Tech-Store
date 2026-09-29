import axios from "axios";
import { getAccessToken } from "../services/tokenService";

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
        console.log("========== AXIOS PRIVATE ==========");
        console.log("METHOD:", config.method?.toUpperCase());
        console.log("URL:", config.url);
        console.log("TOKEN:", accessToken);
        console.log(
            "AUTH:",
            config.headers?.Authorization
        );
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