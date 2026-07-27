//main-trung tam cua toan bo auth frontend
import {
    createContext,
    useState
} from "react";

export const AuthContext = createContext();
export function AuthProvider({ children }) {
    const [auth, setAuth] = useState({
        accessToken: null,
        user: null,
        roles: [],
        authenticated: false

    });
    return (
        <AuthContext.Provider

            value={{

                auth,

                setAuth

            }}

        >

            {children}

        </AuthContext.Provider>
    );
}