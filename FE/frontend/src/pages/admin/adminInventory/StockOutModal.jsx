import { useEffect, useState } from "react";

import "./StockOutModal.scss";

const StockOutModal = ({
    isOpen,
    inventory,
    loading,
    error,
    onClose,
    onSubmit,
}) => {
    const [quantity, setQuantity] = useState("");
    const [note, setNote] = useState("");

    useEffect(() => {
        if (isOpen) {
            setQuantity("");
            setNote("");
        }
    }, [isOpen]);

    if (!isOpen || !inventory) {
        return null;
    }

    const availableStock = inventory.availableStock ?? 0;

    const numericQuantity =
        quantity === ""
            ? 0
            : Number(quantity);

    const isQuantityInvalid =
        quantity !== "" &&
        (
            !Number.isInteger(numericQuantity) ||
            numericQuantity <= 0 ||
            numericQuantity > availableStock
        );

    const canSubmit =
        !loading &&
        quantity !== "" &&
        Number.isInteger(numericQuantity) &&
        numericQuantity > 0 &&
        numericQuantity <= availableStock;

    const handleSubmit = (event) => {
        event.preventDefault();

        if (!canSubmit) {
            return;
        }

        onSubmit({
            quantity: numericQuantity,
            note: note.trim() || null,
        });
    };

    return (
        <div
            className="stock-out-modal__overlay"
            onMouseDown={onClose}
        >
            <div
                className="stock-out-modal"
                onMouseDown={(event) =>
                    event.stopPropagation()
                }
            >
                <div className="stock-out-modal__header">
                    <div>
                        <h2>
                            Stock Out
                        </h2>

                        <p>
                            Remove physical stock from inventory.
                        </p>
                    </div>

                    <button
                        type="button"
                        className="stock-out-modal__close"
                        onClick={onClose}
                        disabled={loading}
                    >
                        ×
                    </button>
                </div>


                <form
                    className="stock-out-modal__form"
                    onSubmit={handleSubmit}
                >

                    <div className="stock-out-modal__product">
                        <div className="stock-out-modal__image">
                            {inventory.image ? (
                                <img
                                    src={inventory.image}
                                    alt={inventory.productName}
                                />
                            ) : (
                                <span>
                                    No image
                                </span>
                            )}
                        </div>

                        <div>
                            <strong>
                                {inventory.productName}
                            </strong>

                            <span>
                                {inventory.externalId}
                            </span>
                        </div>
                    </div>


                    <div className="stock-out-modal__available">
                        <span>
                            Available stock
                        </span>

                        <strong>
                            {availableStock}
                        </strong>
                    </div>


                    <div className="stock-out-modal__field">
                        <label htmlFor="stock-out-quantity">
                            Quantity
                        </label>

                        <input
                            id="stock-out-quantity"
                            type="number"
                            min="1"
                            max={availableStock}
                            step="1"
                            value={quantity}
                            onChange={(event) =>
                                setQuantity(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                            autoFocus
                        />

                        {isQuantityInvalid && (
                            <span className="stock-out-modal__field-error">
                                Quantity must be between 1 and{" "}
                                {availableStock}.
                            </span>
                        )}
                    </div>


                    <div className="stock-out-modal__field">
                        <label htmlFor="stock-out-note">
                            Note
                            <span>
                                (optional)
                            </span>
                        </label>

                        <textarea
                            id="stock-out-note"
                            rows="4"
                            maxLength="500"
                            value={note}
                            onChange={(event) =>
                                setNote(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                            placeholder="Reason for stock out..."
                        />
                    </div>


                    {error && (
                        <div className="stock-out-modal__error">
                            {error}
                        </div>
                    )}


                    <div className="stock-out-modal__actions">

                        <button
                            type="button"
                            className="stock-out-modal__cancel"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            className="stock-out-modal__submit"
                            disabled={!canSubmit}
                        >
                            {loading
                                ? "Processing..."
                                : "Confirm Stock Out"}
                        </button>

                    </div>

                </form>
            </div>
        </div>
    );
};

export default StockOutModal;