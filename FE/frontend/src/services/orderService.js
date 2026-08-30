// import axiosPrivate from "../api/axiosPrivate";

export const createOrder = async (orderData, axiosPrivate) => {

    const response = await axiosPrivate.post(
        "/orders",
        orderData
    );

    return response.data;
};

export const getOrders = async (axiosPrivate) => {

    const response = await axiosPrivate.get(
        "/orders"
    );

    return response.data;

};


export const getOrderById = async (orderId , axiosPrivate) => {

    const response = await axiosPrivate.get(
        `/orders/${orderId}`
    );

    return response.data;

};