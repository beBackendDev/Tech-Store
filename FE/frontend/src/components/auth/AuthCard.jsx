
import "./AuthCard.scss";

function AuthCard({

    title,

    subtitle,

    children

}) {

    return (

        <div className="auth-card">

            <div className="auth-card__logo">

                <div className="logo-circle">

                    TS

                </div>

            </div>

            <h2 className="auth-card__title">

                {title}

            </h2>

            {

                subtitle &&

                <p className="auth-card__subtitle">

                    {subtitle}

                </p>

            }

            <div className="auth-card__body">

                {children}

            </div>

        </div>

    );

}

export default AuthCard;