import { useEffect } from "react";

import axiosPrivate from "../api/axiosPrivate";

import useRefreshToken from "./useRefreshToken";

import { getAccessToken } from "../services/tokenService";

export default function useAxiosPrivate(){

    const refresh = useRefreshToken();

    useEffect(()=>{

        const requestIntercept =

        axiosPrivate.interceptors.request.use(

            config=>{

                const token=getAccessToken();

                if(

                    token &&

                    !config.headers.Authorization

                ){

                    config.headers.Authorization=

                    `Bearer ${token}`;

                }

                return config;

            }

        );

        const responseIntercept=

        axiosPrivate.interceptors.response.use(

            response=>response,

            async(error)=>{

                const previousRequest=

                error?.config;

                if(

                    error.response?.status===401 &&

                    !previousRequest.sent

                ){

                    previousRequest.sent=true;

                    const newToken=

                    await refresh();

                    previousRequest.headers.Authorization=

                    `Bearer ${newToken}`;

                    return axiosPrivate(previousRequest);

                }

                return Promise.reject(error);

            }

        );

        return ()=>{

            axiosPrivate.interceptors.request.eject(requestIntercept);

            axiosPrivate.interceptors.response.eject(responseIntercept);

        };

    },[refresh]);

    return axiosPrivate;

}