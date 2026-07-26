import "./RegisterForm.scss";

import { useState } from "react";

import { Link } from "react-router-dom";

import Input from "../common/Input";
import PasswordInput from "./PasswordInput";
import Button from "../common/Button";

import Divider from "./Divider";
import GoogleLoginButton from "./GoogleLoginButton";
import AuthCard from "./AuthCard";
import AuthFooter from "./AuthFooter";

import { register } from "../../services/authService";

function RegisterForm() {

    const [username, setUsername] = useState("");

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    const [confirmPassword, setConfirmPassword] = useState("");

    const [loading, setLoading] = useState(false);

    const [errors, setErrors] = useState({});

    const validate = () => {

        const e = {};

        if (username.length < 3)
            e.username = "Username must contain at least 3 characters.";

        if (!email.includes("@"))
            e.email = "Invalid email address.";

        if (password.length < 8)
            e.password = "Password must contain at least 8 characters.";

        if (password !== confirmPassword)
            e.confirmPassword = "Passwords do not match.";

        setErrors(e);

        return Object.keys(e).length === 0;
    };

    const submit = async (event) => {

        event.preventDefault();

        if (!validate())
            return;

        try {

            setLoading(true);

            const response = await register({

                username,

                email,

                password

            });

            alert(response.message);

        }
        catch (error) {

            alert(error.response?.data?.message || "Register failed.");

        }
        finally {

            setLoading(false);

        }

    };

    return (

        <AuthCard

            title="Create Account"

            subtitle="Start your journey with us"

        >

            <form

                className="register-form"

                onSubmit={submit}

            >

                <Input

                    label="Username"

                    icon="👤"

                    value={username}

                    error={errors.username}

                    onChange={(e) => setUsername(e.target.value)}

                />

                <Input

                    label="Email"

                    icon="📧"

                    value={email}

                    error={errors.email}

                    onChange={(e) => setEmail(e.target.value)}

                />

                <PasswordInput

                    label="Password"

                    value={password}

                    error={errors.password}

                    onChange={(e) => setPassword(e.target.value)}

                />

                <div className="password-strength">

                    {

                        password.length < 8 ?

                            "Weak"

                            :

                            password.length < 12 ?

                                "Medium"

                                :

                                "Strong"

                    }

                </div>

                <PasswordInput

                    label="Confirm Password"

                    value={confirmPassword}

                    error={errors.confirmPassword}

                    onChange={(e) =>

                        setConfirmPassword(e.target.value)

                    }

                />

                <label className="agree">

                    <input type="checkbox" required />

                    I agree to the

                    <Link to="#">

                        Terms of Service

                    </Link>

                </label>

                <Button

                    loading={loading}

                    block

                    type="submit"

                >

                    Create Account

                </Button>

            </form>

            <Divider />

            <GoogleLoginButton />

            <AuthFooter

                text="Already have an account?"

                linkText="Sign In"

                link="/login"

            />

        </AuthCard>

    );

}

export default RegisterForm;