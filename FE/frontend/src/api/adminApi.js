import axiosPrivate from "./axiosPrivate";

const ADMIN_PRODUCT_URL = "/dashboard/admin";

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
            ADMIN_PRODUCT_URL + "create-product",
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