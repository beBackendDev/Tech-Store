import "./LoginForm.scss";

import { useState } from "react";

import { MdEmail } from "react-icons/md";

import Input from "../common/Input";

import Button from "../common/Button";

import { login } from "../../services/authService";

import PasswordInput from "./PasswordInput";

import AuthCard from "./AuthCard";

import Divider from "./Divider";

import GoogleLoginButton from "./GoogleLoginButton";

import AuthFooter from "./AuthFooter";

function LoginForm() {

    const [email, setEmail] = useState("");

    const [loading, setLoading] = useState(false);

    const [password, setPassword] = useState("");

    const submit = async (e) => {

        e.preventDefault();
        setLoading(true);
        console.log(email);

        console.log(password);
        try {

            const response = await login(email, password);
            console.log("response" + response);
            alert(response.message);

            console.log(response);

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