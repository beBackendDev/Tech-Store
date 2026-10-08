import axiosPrivate from "./axiosPrivate";

const ADMIN_PRODUCT_URL = "/dashboard/admin";

const ADMIN_INVENTORY_URL = "/admin/inventory";

const ADMIN_ORDER_URL = "/admin/orders";

export const getAdminDashboard = async () => {
    return await axiosPrivate.get(ADMIN_PRODUCT_URL);
};
//get all products with pagination and filters
export const getAdminProducts = async (
    axiosPrivate,
    params = {}
) => {

    const response =
        await axiosPrivate.get(
            ADMIN_PRODUCT_URL + "/products",
            {
                params: {
                    page: params.page ?? 0,
                    size: params.size ?? 20,
                    keyword: params.keyword || undefined,
                    category: params.category || undefined,
                    brand: params.brand || undefined,
                    minPrice: params.minPrice || undefined,
                    maxPrice: params.maxPrice || undefined,
                    active:
                        params.active !== undefined
                            ? params.active
                            : undefined,
                    sort:
                        params.sort || "createdAt,desc"
                }
            }
        );


    return response.data.response;
};

//get product with id
export const getAdminProductById = async (
    axiosPrivate,
    id
) => {

    const response =
        await axiosPrivate.get(
            ADMIN_PRODUCT_URL + `/products/${id}`
        );

    return response.data.response;
};

//create
export const createAdminProduct = async (
    axiosPrivate,
    product
) => {

    const response =
        await axiosPrivate.post(
            `${ADMIN_PRODUCT_URL}/create-product`,
            product
        );
    return response.data.response;
};
//update
export const updateAdminProduct = async (
    axiosPrivate,
    id,
    product
) => {

    const response =
        await axiosPrivate.put(
            `${ADMIN_PRODUCT_URL}/update-product/${id}`,
            product
        );

    return response.data.response;
};
export const updateAdminProductStatus = async (
    axiosPrivate,
    id,
    active
) => {

    const response =
        await axiosPrivate.patch(
            `${ADMIN_PRODUCT_URL}/update-product/${id}/status`,
            {
                active
            }
        );

    return response.data.response;
};

//INVENTORY - APIs
export const getAdminInventory = async (
    axiosPrivate,
    {
        page = 0,
        size = 20,
        sort = "name,asc",
    } = {}
) => {
    const response = await axiosPrivate.get(ADMIN_INVENTORY_URL, {
        params: {
            page,
            size,
            sort,
        },
    });

    return response.data.response;
};
//INVENTORY DETAIL - API
export const getAdminInventoryDetail = async (
    axiosPrivate,
    productId
) => {
    const response = await axiosPrivate.get(
        `${ADMIN_INVENTORY_URL}/${productId}`
    );

    return response.data.response;
};
//INVENTORY HISTORY - API
export const getInventoryHistory = async (
    axiosPrivate,
    productId,
    {
        page = 0,
        size = 20,
        sort = "createdAt,desc",
    } = {}
) => {
    const response = await axiosPrivate.get(
        `${ADMIN_INVENTORY_URL}/${productId}/history`,
        {
            params: {
                page,
                size,
                sort,
            },
        }
    );

    return response.data.response;
};
//INVENTORY STOCK-IN
export const stockIn = async (
    axiosPrivate,
    productId,
    data
) => {
    const response = await axiosPrivate.post(
        `${ADMIN_INVENTORY_URL}/${productId}/stock-in`,
        data
    );

    return response.data;
};
// INVENTORY STOCK -OUT 
export const stockOut = async (
    axiosPrivate,
    productId,
    data
) => {
    const response = await axiosPrivate.post(
        `${ADMIN_INVENTORY_URL}/${productId}/stock-out`,
        data
    );

    return response.data;
};

// INVENTORY ADJUSTMENT
export const adjustInventory = async (
    axiosPrivate,
    productId,
    data
) => {
    const response = await axiosPrivate.post(
        `${ADMIN_INVENTORY_URL}/${productId}/adjust`,
        data
    );

    return response.data;
};

//ORDER || PAYMENT - APIs
export const getAdminOrders = async (
    axiosPrivate,
    params = {}
) => {
    const response = await axiosPrivate.get(
        ADMIN_ORDER_URL,
        { params }
    );

    return response.data;
};

export const getAdminOrderById = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.get(
        `${ADMIN_ORDER_URL}/${orderId}`
    );

    return response.data;
};

export const confirmOrder = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/confirm`
    );

    return response.data;
};

export const processOrder = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/process`
    );

    return response.data;
};

export const shipOrder = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/ship`
    );

    return response.data;
};

export const deliverOrder = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/deliver`
    );

    return response.data;
};

export const completeOrder = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/complete`
    );

    return response.data;
};

export const cancelAdminOrder = async (
    axiosPrivate,
    orderId,
    reason
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/cancel`,
        null,
        {
            params: { reason }
        }
    );

    return response.data;
};

//PAYMENT - APIs
export const markPaymentSuccess = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/payment/success`
    );

    return response.data;
};

export const markPaymentFailed = async (
    axiosPrivate,
    orderId,
    reason
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/payment/failed`,
        { reason }
    );

    return response.data;
};

export const completeCodPayment = async (
    axiosPrivate,
    orderId
) => {
    const response = await axiosPrivate.patch(
        `${ADMIN_ORDER_URL}/${orderId}/payment/cod/complete`
    );

    return response.data;
};