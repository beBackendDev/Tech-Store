//private api

import axios from "axios";

const axiosPrivate = axios.create({

    baseURL:"http://localhost:8080/api",

    withCredentials:true

});

export default axiosPrivate;

//hoan thien o giai doan 6