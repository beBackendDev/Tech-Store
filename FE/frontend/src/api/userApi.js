import axiosPrivate from "./axiosPrivate";
const ORDER_URL = "/orders";

export const getUser = async () => {
    return await axiosPrivate.get("/user");
};

//ORDER - APIs

export const getMyOrders = async (
    axiosPrivate,
    params = {}
) => {
    const response = await axiosPrivate.get(
        ORDER_URL,
        { params }
    );

    return response.data;
};

export const getMyOrderById = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.get(
        `${ORDER_URL}/${orderId}`
    );

    return response.data;
};

export const cancelOrder = async (
    axiosPrivate,
    orderId,
    data
) => {
    const response = await axiosPrivate.patch(
        `${ORDER_URL}/${orderId}/cancel`,
        data
    );

    return response.data;
};

export const requestReturn = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.post(
        `${ORDER_URL}/${orderId}/return-request`
    );

    return response.data;
};