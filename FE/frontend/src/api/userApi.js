import axiosPrivate from "./axiosPrivate";

export const getUser = async () => {
    return await axiosPrivate.get("/user");
};