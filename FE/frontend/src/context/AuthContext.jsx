//main-trung tam cua toan bo auth frontend
import {
    createContext,
    useState,
    useEffect
} from "react";
import { refreshToken } from "../api/authApi";
import useRefreshToken from "../hooks/useRefreshToken";

export const AuthContext = createContext();


export function AuthProvider({ children }) {
    const refresh = useRefreshToken();

    const [auth, setAuth] = useState({
        accessToken: null,
        user: null,
        roles: [],
        authenticated: false

    });

    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const initialize = async () => {

            try {

                const accessToken = await refresh();
                setAuth(prev => ({


                    ...prev,


                    accessToken,


                    authenticated: true


                }));

            } catch (err) {

                console.log(err);

            } finally {

                setLoading(false);

            }

        };

        initialize();

    }, [refresh]);
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