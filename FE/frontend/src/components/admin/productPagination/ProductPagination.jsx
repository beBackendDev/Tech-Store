import "./ProductPagination.scss";
function ProductPagination({
    pagination,
    onPageChange
}) {

    const {
        page,
        totalPages,
        first,
        last
    } = pagination;


    return (

        <div className="product-pagination">

            <button
                disabled={first}
                onClick={() =>
                    onPageChange(page - 1)
                }
            >
                Previous
            </button>


            <span>
                Page {page + 1} of {totalPages}
            </span>


            <button
                disabled={last}
                onClick={() =>
                    onPageChange(page + 1)
                }
            >
                Next
            </button>

        </div>

    );

}
export default ProductPagination;