import "./Input.scss";

function Input({

    label,

    icon,

    type="text",

    value,

    onChange,

    placeholder,

    error

}){

    return(

        <div className="input-group">

            {label &&

                <label>

                    {label}

                </label>

            }

            <div className="input-wrapper">

                {

                    icon &&

                    <span className="input-icon">

                        {icon}

                    </span>

                }

                <input

                    className={`input ${error?"input-error":""}`}

                    type={type}

                    value={value}

                    placeholder={placeholder}

                    onChange={onChange}

                />

            </div>

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