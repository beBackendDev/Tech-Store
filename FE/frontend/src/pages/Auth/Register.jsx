import "./Register.scss";

import RegisterForm from "../../components/auth/RegisterForm";

function Register(){

    return(

        <div className="auth-page">

            <div className="auth-left">

                <div>

                    <h1>

                        Tech Store

                    </h1>

                    <p>

                        Create your account

                    </p>

                </div>

            </div>

            <div className="auth-right">

                <RegisterForm/>

            </div>

        </div>

    );

}

export default Register;