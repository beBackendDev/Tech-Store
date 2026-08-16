import "./Button.scss";

function Button({

    loading,

    children,

    type = "button",

    variant = "primary",

    block = false,

    onClick

}) {

    return (

        <button

            type={type}

            className={`btn btn-${variant} ${block ? "btn-block" : ""}`}

            onClick={onClick}

            disabled={loading}

        >

            {loading ?

                "Loading..."

                :

                children
            }

        </button>

    );

}

export default Button;