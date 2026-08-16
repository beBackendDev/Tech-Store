import { Outlet } from "react-router-dom";

import Header from "../../components/layout/Header/Header";
import Footer from "../../components/layout/Footer/Footer";

import "./MainLayout.scss";

const MainLayout = () => {

    return (
        <div className="main-layout">

            <Header />

            <main className="main-layout__content">
                <Outlet />
            </main>

            <Footer />

        </div>
    );
};

export default MainLayout;