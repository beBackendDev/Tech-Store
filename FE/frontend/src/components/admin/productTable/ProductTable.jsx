import { formatCurrency } from "../../../utils/formatCurrency";
import "./ProductTable.scss";
import { useNavigate } from "react-router-dom";
function ProductTable({
    products = [],
    onEdit,
    onToggleStatus
}) {
    const navigate = useNavigate();

    if (products.length === 0) {

        return (
            <div className="admin-products__empty">
                No products found.
            </div>
        );

    }


    return (

        <table className="product-table">

            <thead>

                <tr>

                    <th>Product</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                    <th>Actions</th>

                </tr>

            </thead>


            <tbody>

                {products.map(product => (

                    <tr key={product.id}>

                        <td>

                            <div className="product-cell">

                                <img
                                    src={product.image}
                                    alt={product.name}
                                />

                                <div>

                                    <strong>
                                        {product.name}
                                    </strong>

                                    <span>
                                        ID: {product.externalId}
                                    </span>

                                </div>

                            </div>

                        </td>


                        <td>
                            {product.category}
                        </td>


                        <td>
                            {formatCurrency(
                                product.price
                            )}
                        </td>


                        <td>
                            {product.stock}
                        </td>


                        <td>

                            <button
                                type="button"
                                onClick={() =>
                                    onEdit(product.id)
                                }
                            >
                                Edit
                            </button>


                            <button
                                type="button"
                                onClick={() =>
                                    onToggleStatus(
                                        product.id,
                                        product.active
                                    )
                                }
                            >
                                {product.active
                                    ? "Deactivate"
                                    : "Activate"
                                }
                            </button>

                        </td>


                        <td>

                            <button
                                type="button"
                                onClick={() =>
                                    navigate(`/admin/products/${product.id}`)
                                }
                            >
                                View
                            </button>

                        </td>

                    </tr>

                ))}

            </tbody>

        </table>

    );

}
export default ProductTable;
