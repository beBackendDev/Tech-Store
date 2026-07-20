import "./LoginForm.scss";

import { useState } from "react";

import Input from "../common/Input";

import Button from "../common/Button";

function LoginForm() {

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    const submit = (e) => {

        e.preventDefault();

        console.log(email);

        console.log(password);

    }

    return (

        <form

            className="login-card"

            onSubmit={submit}

        >

            <h2>

                Login

            </h2>

            <Input

                label="Email"

                value={email}

                onChange={(e) =>

                    setEmail(e.target.value)

                }

            />

            <Input

                label="Password"

                type="password"

                value={password}

                onChange={(e) =>

                    setPassword(e.target.value)

                }

            />

            <Button

                block

                type="submit"

            >

                Login

            </Button>

        </form>

    );

}

export default LoginForm;