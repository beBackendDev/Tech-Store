import { useEffect } from "react";
import { getUser } from "../api/userApi";

function Profile() {

    useEffect(() => {

        const loadUser = async () => {

            try {

                const response = await getUser();

                console.log("USER API:", response.data);

            } catch (error) {

                console.error(
                    "USER API ERROR:",
                    error
                );

            }
        };

        loadUser();

    }, []);

    return <h1>Profile</h1>;
}

export default Profile;