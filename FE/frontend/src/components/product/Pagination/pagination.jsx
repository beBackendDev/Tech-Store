const Pagination = ({
    page,
    totalPages,
    first,
    last,
    onPageChange,
}) => {
    return (
        <div className="pagination">

            <button
                disabled={first}
                onClick={() => onPageChange(page - 1)}
            >
                Previous
            </button>

            <span>
                Page {page + 1} of {totalPages}
            </span>

            <button
                disabled={last}
                onClick={() => onPageChange(page + 1)}
            >
                Next
            </button>

        </div>
    );
};

export default Pagination;