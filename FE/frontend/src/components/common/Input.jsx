import "./Input.scss";

function Input({

    label,

    type = "text",

    value,

    onChange,

    placeholder,

    error

}) {

    return (

        <div className="input-group">

            {label &&

                <label>

                    {label}
                </label>

            }

            <input

                className={`input ${error ? "input-error" : ""}`}

                type={type}

                value={value}

                placeholder={placeholder}

                onChange={onChange}

            />

            {

                error &&

                <small className="text-danger">

                    {error}

                </small>

            }

        </div>

    );

}

export default Input;