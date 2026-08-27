import { useEffect } from "react";

import axiosPrivate from "../api/axiosPrivate";

import useRefreshToken from "./useRefreshToken";

import { getAccessToken } from "../services/tokenService";


export default function useAxiosPrivate() {

    const refresh = useRefreshToken();


    useEffect(() => {

        // =========================================
        // REQUEST INTERCEPTOR
        // =========================================

        const requestIntercept =
            axiosPrivate.interceptors.request.use(

                (config) => {

                    const token =
                        getAccessToken();


                    if (
                        token &&
                        !config.headers.Authorization
                    ) {

                        config.headers.Authorization =
                            `Bearer ${token}`;

                    }


                    return config;

                },

                (error) => {

                    return Promise.reject(error);

                }

            );


        // =========================================
        // RESPONSE INTERCEPTOR
        // =========================================

        const responseIntercept =
            axiosPrivate.interceptors.response.use(

                (response) => {

                    return response;

                },


                async (error) => {

                    const previousRequest =
                        error?.config;


                    // Không có request config
                    if (!previousRequest) {

                        return Promise.reject(error);

                    }


                    // Không phải 401
                    if (
                        error.response?.status !== 401
                    ) {

                        return Promise.reject(error);

                    }


                    // Request đã retry một lần
                    if (
                        previousRequest.sent
                    ) {

                        return Promise.reject(error);

                    }


                    previousRequest.sent = true;


                    try {

                        // =================================
                        // REFRESH ACCESS TOKEN
                        // =================================

                        const newToken =
                            await refresh();


                        // =================================
                        // RETRY REQUEST
                        // =================================

                        previousRequest.headers =
                            previousRequest.headers || {};


                        previousRequest.headers.Authorization =
                            `Bearer ${newToken}`;


                        return axiosPrivate(
                            previousRequest
                        );

                    } catch (refreshError) {

                        console.error(
                            "Refresh token failed:",
                            refreshError
                        );


                        return Promise.reject(
                            refreshError
                        );

                    }

                }

            );


        // =========================================
        // CLEANUP
        // =========================================

        return () => {

            axiosPrivate.interceptors.request.eject(
                requestIntercept
            );

            axiosPrivate.interceptors.response.eject(
                responseIntercept
            );

        };

    }, [refresh]);


    return axiosPrivate;

}