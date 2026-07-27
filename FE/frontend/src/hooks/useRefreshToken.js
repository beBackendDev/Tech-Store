import { useAuth } from "./useAuth";
import { refreshToken } from "../services/authService";
import { saveAccessToken } from "../services/tokenService";

export default function useRefreshToken(){

    const { auth, setAuth } = useAuth();

    const refresh = async () => {

        const response = await refreshToken();

        const accessToken = response.response;

        saveAccessToken(accessToken);

        setAuth(prev => ({

            ...prev,

            accessToken,

            authenticated:true

        }));

        return accessToken;

    };

    return refresh;

}