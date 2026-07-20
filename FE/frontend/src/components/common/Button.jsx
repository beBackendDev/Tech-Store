import "./Button.scss";

function Button({

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

        >

            {children}

        </button>

    );

}

export default Button;