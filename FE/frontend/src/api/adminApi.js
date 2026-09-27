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
