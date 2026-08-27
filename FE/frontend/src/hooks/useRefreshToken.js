// src/hooks/useRefreshToken.js

import {
    refreshToken
} from "../services/authService";

import {
    saveAccessToken
} from "../utils/tokenStorage";
import useAuth from "./useAuth";



const useRefreshToken = () => {

    const {
        setAuth
    } = useAuth();


    const refresh = async () => {

        const response =
            await refreshToken();


        const newAccessToken =
            response.response;


        if (!newAccessToken) {

            throw new Error(
                "Refresh token response does not contain access token"
            );

        }


        saveAccessToken(
            newAccessToken
        );


        setAuth(prev => ({

            ...prev,

            accessToken:
                newAccessToken,

            authenticated:
                true

        }));


        return newAccessToken;

    };


    return refresh;

};


export default useRefreshToken;