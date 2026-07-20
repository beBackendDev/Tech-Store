import "./RegisterForm.scss";

import { useState } from "react";

import Input from "../common/Input";

import Button from "../common/Button";

function RegisterForm() {

    const [username, setUsername] = useState("");

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    return (

        <form

            className="register-card"

        >

            <h2>

                Register

            </h2>

            <Input

                label="Username"

                value={username}

                onChange={(e)=>

                    setUsername(e.target.value)

                }

            />

            <Input

                label="Email"

                value={email}

                onChange={(e)=>

                    setEmail(e.target.value)

                }

            />

            <Input

                label="Password"

                type="password"

                value={password}

                onChange={(e)=>

                    setPassword(e.target.value)

                }

            />

            <Button

                block

                type="submit"

            >

                Register

            </Button>

        </form>

    );

}

export default RegisterForm;