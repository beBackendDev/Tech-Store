// import {
// useContext
// } from "react";

// import {
// AuthContext
// } from "../context/AuthContext";

// export default function useAuth(){

//     return useContext(AuthContext);

// }

import {
    useContext
} from "react";

import {
    AuthContext
} from "../context/AuthContext";


function useAuth() {

    const context =
        useContext(AuthContext);

    if (!context) {

        throw new Error(
            "useAuth must be used inside AuthProvider"
        );

    }

    return context;

}


export default useAuth;