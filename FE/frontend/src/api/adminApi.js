import axiosPrivate from "./axiosPrivate";

export const getAdminDashboard = async () => {
    return await axiosPrivate.get("/dashboard/admin");
};