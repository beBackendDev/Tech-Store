import "./GoogleLoginButton.scss";

function GoogleLoginButton() {

    const handleGoogleLogin = () => {

        // Spring Security OAuth2 Endpoint
        window.location.href =
            "http://localhost:8080/oauth2/authorization/google";

    };

    return (

        <button

            type="button"

            className="google-btn"

            onClick={handleGoogleLogin}

        >

            <img

                src="https://www.gstatic.com/firebasejs/ui/2.0.0/images/auth/google.svg"

                alt="Google"

            />

            <span>

                Continue with Google

            </span>

        </button>

    );

}

export default GoogleLoginButton;