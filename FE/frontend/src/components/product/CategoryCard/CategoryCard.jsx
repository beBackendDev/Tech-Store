import "./CategoryCard.scss";

function CategoryCard({ category, onClick }) {

    return (
        <button
            className="category-card"
            onClick={() => onClick?.(category)}
        >

            <div className="category-card__icon">
                {category.icon}
            </div>

            <div className="category-card__content">

                <h3>
                    {category.name}
                </h3>

                <p>
                    {category.description}
                </p>

                <span>
                    {category.productCount} products
                </span>

            </div>

        </button>
    );
}

export default CategoryCard;