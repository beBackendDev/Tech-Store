//main-trung tam cua toan bo auth frontend
import {
    createContext,
    useState,
    useEffect,
    useCallback
} from "react";
import { saveAccessToken } from "../services/tokenService";
import { refreshToken } from "../services/authService";

export const AuthContext = createContext(null);


export function AuthProvider({ children }) {
    const [auth, setAuth] = useState({
        accessToken: null,
        user: null,
        roles: [],
        authenticated: false

    });

    const [loading, setLoading] = useState(true);
    const initializeAuth = useCallback(async () => {

        try {

            const accessToken =
                await refreshToken();

            if (!accessToken) {

                throw new Error(
                    "No access token returned"
                );

            }
            saveAccessToken(accessToken);

            setAuth(prev => ({
                ...prev,
                accessToken,
                authenticated: true
            }));

        } catch (error) {

            console.log(
                "No valid refresh token."
            );

            setAuth({
                accessToken: null,
                user: null,
                roles: [],
                authenticated: false
            });

        } finally {

            setLoading(false);

        }

    }, []);


    useEffect(() => {

        initializeAuth();

    }, []);


    return (
        <AuthContext.Provider

            value={{

                auth,

                setAuth,
                loading

            }}

        >

            {children}

        </AuthContext.Provider>
    );
}
export default AuthContext;