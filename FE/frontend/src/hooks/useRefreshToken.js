// import { refreshToken } from "../services/authService";
// import { saveAccessToken } from "../services/tokenService";
// import  useAuth  from "./useAuth";

// export default function useRefreshToken(){

//     const { auth, setAuth } = useAuth();

//     const refresh = async () => {
//         const response = await refreshToken();

//         const accessToken = response.response;

//         saveAccessToken(accessToken);

//         setAuth(prev => ({

//             ...prev,

//             accessToken,

//             authenticated:true

//         }));

//         return accessToken;

//     };

//     return refresh;

// }



import { useCallback } from "react";

import { refreshToken } from "../services/authService";


// useRefreshToken
//        ↓
// authService.refreshToken()
//        ↓
// authApi.refreshToken()
//        ↓
// Backend

export default function useRefreshToken() {


    const refresh = useCallback(async () => {


        const accessToken = await refreshToken();


        return accessToken;


    }, []);


    return refresh;

}