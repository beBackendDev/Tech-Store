import "./Login.scss";

import LoginForm from "../../components/auth/LoginForm";

function Login(){

    return(

        <div className="auth-page">

            <div className="auth-left">

                <div>

                    <h1>

                        Tech Store

                    </h1>

                    <p>

                        Secure Authentication

                    </p>

                </div>

            </div>

            <div className="auth-right">

                <LoginForm/>

            </div>

        </div>

    );

}

export default Login;