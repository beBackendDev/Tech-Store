import "./LoginForm.scss";

import { useNavigate } from 'react-router-dom';

import { useState } from "react";

import { MdEmail } from "react-icons/md";

import Input from "../common/Input/Input";

import Button from "../common/Button/Button";

import { login } from "../../services/authService";

import PasswordInput from "./PasswordInput";

import AuthCard from "./AuthCard";

import Divider from "./Divider";

import GoogleLoginButton from "./GoogleLoginButton";

import AuthFooter from "./AuthFooter";
import useAuth from "../../hooks/useAuth";
import { saveAccessToken } from "../../services/tokenService";
import { getDefaultRouteByRole } from "../../utils/roleUtils";

function LoginForm() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");

    const [loading, setLoading] = useState(false);

    const { setAuth } = useAuth();

    const [password, setPassword] = useState("");

    const submit = async (e) => {

        e.preventDefault();
        setLoading(true);
        console.log(email);

        console.log(password);
        try {

            const response = await login(email, password);
            console.log("response", response);
            //login thanh cong
            //save accesstoken 
            const redirectPath =
                getDefaultRouteByRole(
                    response.roles
                );
            saveAccessToken(response.accessToken);

            setAuth({

                accessToken: response.accessToken,

                authenticated: true

            });
            // REDIRECT BY ROLE


                navigate(
                    redirectPath,
                    {
                        replace: true
                    }
                );



        } catch (error) {

            // alert(error.response?.data?.message);
            alert("Login failed");

        }
        finally {

            setLoading(false);

        }

    }

    return (
        <AuthCard
            title="Welcome to Tech Store"
            subtitle="Sign in to continue"
        >
            <form

                className="login-card"

                onSubmit={submit}

            >

                <h2>

                    Login

                </h2>

                <Input

                    label="Email"
                    icon={<MdEmail />}


                    value={email}

                    onChange={(e) =>

                        setEmail(e.target.value)

                    }

                />

                <PasswordInput

                    label="Password"

                    type="password"

                    value={password}

                    onChange={(e) =>

                        setPassword(e.target.value)

                    }

                />

                <Button
                    loading={loading}

                    block

                    type="submit"

                >

                    Login

                </Button>

            </form>
            <Divider text="OR" />
            <GoogleLoginButton />
            <AuthFooter
                text="Don't have an account?"

                linkText="Create account"

                link="/register"

            />

        </AuthCard>


    );

}

export default LoginForm;