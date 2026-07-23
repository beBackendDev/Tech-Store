import "./AuthFooter.scss";

import { Link } from "react-router-dom";

function AuthFooter({

    text,

    linkText,

    link

}){

    return(

        <div className="auth-footer">

            <span>

                {text}

            </span>

            <Link to={link}>

                {linkText}

            </Link>

        </div>

    );

}

export default AuthFooter;