import "./PasswordInput.scss";
import { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa";
import { FcGoogle } from "react-icons/fc";
function PasswordInput({

    label,

    value,

    onChange,

    placeholder,

    error

}){

    const [show,setShow]=useState(false);

    return(

        <div className="password-group">

            {label &&

                <label>

                    {label}

                </label>

            }

            <div className="password-wrapper">

                <input

                    type={show?"text":"password"}

                    className={`input ${error?"input-error":""}`}

                    value={value}

                    placeholder={placeholder}

                    onChange={onChange}

                />

                <button

                    type="button"

                    className="toggle-password"

                    onClick={()=>setShow(!show)}

                >

                    {show ? <FaEyeSlash /> : <FaEye />}

                </button>

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

export default PasswordInput;