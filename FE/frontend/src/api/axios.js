//public api

import axios from 'axios';

const api = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
        "Content-Type": "application/json",
    },

    //vì backend dùng HttpOnly Cookie
    withCredentials: true,
});

export default api;