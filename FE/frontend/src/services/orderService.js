import axiosPrivate from "../api/axiosPrivate";

export const createOrder = async (orderData) => {

    const response = await axiosPrivate.post(
        "/orders",
        orderData
    );

    return response.data;
};