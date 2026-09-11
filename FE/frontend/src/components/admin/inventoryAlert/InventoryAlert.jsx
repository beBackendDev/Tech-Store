import {

    AlertTriangle,
    Package

} from "lucide-react";

import "./InventoryAlert.scss";


function InventoryAlert({ products = [] }) {

    return (

        <section className="inventory-alert">


            {/* ================= HEADER ================= */}

            <div className="inventory-alert__header">

                <div>

                    <h2>
                        Inventory Alerts
                    </h2>

                    <p>
                        Products running low
                    </p>

                </div>


                <AlertTriangle
                    className="
                    inventory-alert__header-icon
                    "
                    size={22}
                />

            </div>


            {/* ================= LIST ================= */}

            <div className="inventory-alert__list">


                {products.length === 0 && (

                    <div className="inventory-alert__empty">

                        <Package size={28} />

                        <span>
                            No inventory alerts
                        </span>

                    </div>

                )}


                {products.map(product => (

                    <div
                        className="inventory-alert__item"
                        key={product.id}
                    >

                        <div className="inventory-alert__product">

                            <div
                                className="
                                inventory-alert__product-icon
                                "
                            >

                                <Package size={18} />

                            </div>


                            <div>

                                <strong>

                                    {product.name}

                                </strong>


                                <span>

                                    Low stock

                                </span>

                            </div>

                        </div>


                        <span
                            className="
                            inventory-alert__stock
                            "
                        >

                            {product.stock}

                        </span>

                    </div>

                ))}

            </div>


            {/* ================= FOOTER ================= */}

            <button
                type="button"
                className="inventory-alert__button"
            >

                View inventory

            </button>

        </section>

    );

}

export default InventoryAlert;