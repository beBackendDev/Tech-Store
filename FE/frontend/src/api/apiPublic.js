//public api
import axiosPublic from "../api/axiosPublic";


export const getProducts = async () => {

    const response =
        await axiosPublic.get("/public/products");

    return response.data;
};


export const getProductById = async (id) => {

    const response =
        await axiosPublic.get(`/public/products/${id}`);

    return response.data;
};