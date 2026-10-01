import {
    LeftOutlined,
    RightOutlined,
} from "@ant-design/icons";

const InventoryPagination = ({
    pagination,
    onPageChange,
}) => {
    const {
        page,
        totalPages,
        totalElements,
        size,
        first,
        last,
    } = pagination;

    if (totalPages <= 1) {
        return null;
    }

    const start = page * size + 1;
    const end = Math.min(
        (page + 1) * size,
        totalElements
    );

    const getPages = () => {
        const pages = [];

        for (let i = 0; i < totalPages; i++) {
            pages.push(i);
        }

        return pages;
    };

    return (
        <div className="inventory-pagination">
            <div className="inventory-pagination__info">
                Showing {start}-{end} of {totalElements}
            </div>

            <div className="inventory-pagination__controls">
                <button
                    type="button"
                    disabled={first}
                    onClick={() => onPageChange(page - 1)}
                    className="inventory-pagination__button"
                >
                    <LeftOutlined />
                </button>

                {getPages().map((pageNumber) => (
                    <button
                        key={pageNumber}
                        type="button"
                        onClick={() => onPageChange(pageNumber)}
                        className={`inventory-pagination__button ${
                            pageNumber === page
                                ? "inventory-pagination__button--active"
                                : ""
                        }`}
                    >
                        {pageNumber + 1}
                    </button>
                ))}

                <button
                    type="button"
                    disabled={last}
                    onClick={() => onPageChange(page + 1)}
                    className="inventory-pagination__button"
                >
                    <RightOutlined />
                </button>
            </div>
        </div>
    );
};

export default InventoryPagination;