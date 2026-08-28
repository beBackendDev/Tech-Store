import { useNavigate } from "react-router-dom";

function NotFound() {

    const navigate = useNavigate();

    return (

        <main className="not-found">

            <h1>
                404
            </h1>

            <h2>
                Page not found
            </h2>

            <p>
                The page you are looking for
                does not exist.
            </p>

            <button
                type="button"
                onClick={() => navigate("/")}
            >
                Back to home
            </button>

        </main>

    );

}

export default NotFound;