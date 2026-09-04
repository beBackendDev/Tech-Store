import axios from "axios";

const API_URL =
    "http://localhost:8080/api/public";


export const getProducts = async ({
    page = 0,
    size = 20,
    sort = ""
} = {}) => {

    const response = await axios.get(
        `${API_URL}/products-list`,
        {
            params: {
                page,
                size,
                sort
            }
        }
    );

    return response.data.response;
};


export const getProductById = async (id) => {

    const response = await axios.get(
        `${API_URL}/${id}`
    );

    return response.data;
};