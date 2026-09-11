import "./RecentOrders.scss";


function RecentOrders({ orders = [] }) {

    return (

        <section className="recent-orders">


            {/* ================= HEADER ================= */}

            <div className="recent-orders__header">

                <div>

                    <h2>
                        Recent Orders
                    </h2>

                    <p>
                        Latest customer orders
                    </p>

                </div>


                <button
                    type="button"
                    className="recent-orders__view-all"
                >

                    View all

                </button>

            </div>


            {/* ================= TABLE ================= */}

            <div className="recent-orders__table-wrapper">

                <table className="recent-orders__table">

                    <thead>

                        <tr>

                            <th>
                                Order
                            </th>

                            <th>
                                Customer
                            </th>

                            <th>
                                Amount
                            </th>

                            <th>
                                Status
                            </th>

                        </tr>

                    </thead>


                    <tbody>

                        {orders.length === 0 && (

                            <tr>

                                <td
                                    colSpan="4"
                                    className="
                                    recent-orders__empty
                                    "
                                >

                                    No recent orders

                                </td>

                            </tr>

                        )}


                        {orders.map(order => (

                            <tr key={order.id}>

                                <td>

                                    #{order.id}

                                </td>


                                <td>

                                    {order.customerName}

                                </td>


                                <td>

                                    {order.totalAmount}

                                </td>


                                <td>

                                    <span
                                        className={`
                                        recent-orders__status
                                        recent-orders__status--${order.status?.toLowerCase()}
                                        `}
                                    >

                                        {order.status}

                                    </span>

                                </td>

                            </tr>

                        ))}

                    </tbody>

                </table>

            </div>

        </section>

    );

}

export default RecentOrders;