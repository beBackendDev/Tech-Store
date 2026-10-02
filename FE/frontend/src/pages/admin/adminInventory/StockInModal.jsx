import { useEffect, useState } from "react";

import "./StockInModal.scss";

const StockInModal = ({
    isOpen,
    inventory,
    loading,
    error,
    onClose,
    onSubmit,
}) => {
    const [quantity, setQuantity] = useState("");
    const [note, setNote] = useState("");
    const [validationError, setValidationError] = useState("");

    useEffect(() => {
        if (!isOpen || !inventory) {
            return;
        }

        setQuantity("");
        setNote("");
        setValidationError("");
    }, [isOpen, inventory]);

    if (!isOpen || !inventory) {
        return null;
    }

    const handleQuantityChange = (event) => {
        const value = event.target.value;

        // Cho phép input rỗng khi user đang nhập
        if (value === "") {
            setQuantity("");
            setValidationError("");
            return;
        }

        // Chỉ cho phép số nguyên không âm
        if (!/^\d+$/.test(value)) {
            return;
        }

        setQuantity(value);
        setValidationError("");
    };

    const handleNoteChange = (event) => {
        const value = event.target.value;

        if (value.length <= 500) {
            setNote(value);
        }

        setValidationError("");
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const parsedQuantity = Number(quantity);

        if (!quantity || !Number.isInteger(parsedQuantity)) {
            setValidationError(
                "Quantity must be a valid integer."
            );
            return;
        }

        if (parsedQuantity <= 0) {
            setValidationError(
                "Quantity must be greater than zero."
            );
            return;
        }

        if (note.trim().length > 500) {
            setValidationError(
                "Note must not exceed 500 characters."
            );
            return;
        }

        onSubmit({
            quantity: parsedQuantity,
            note: note.trim() || null,
        });
    };

    const handleOverlayClick = (event) => {
        if (
            event.target === event.currentTarget &&
            !loading
        ) {
            onClose();
        }
    };

    return (
        <div
            className="stock-in-modal__overlay"
            onMouseDown={handleOverlayClick}
        >
            <div
                className="stock-in-modal"
                role="dialog"
                aria-modal="true"
                aria-labelledby="stock-in-modal-title"
            >
                {/* =========================
                    HEADER
                ========================= */}

                <div className="stock-in-modal__header">
                    <div>
                        <h2 id="stock-in-modal-title">
                            Add Stock
                        </h2>

                        <p>
                            {inventory.productName}
                        </p>
                    </div>

                    <button
                        type="button"
                        className="stock-in-modal__close"
                        onClick={onClose}
                        disabled={loading}
                        aria-label="Close"
                    >
                        ×
                    </button>
                </div>

                {/* =========================
                    FORM
                ========================= */}

                <form
                    className="stock-in-modal__form"
                    onSubmit={handleSubmit}
                >
                    {/* =========================
                        PRODUCT
                    ========================= */}

                    <div className="stock-in-modal__product">
                        <div className="stock-in-modal__image">
                            {inventory.image ? (
                                <img
                                    src={inventory.image}
                                    alt={inventory.productName}
                                />
                            ) : (
                                <span>No image</span>
                            )}
                        </div>

                        <div>
                            <strong>
                                {inventory.productName}
                            </strong>

                            <span>
                                {inventory.externalId}
                            </span>

                            <span>
                                {inventory.category}
                            </span>
                        </div>
                    </div>

                    {/* =========================
                        CURRENT STOCK
                    ========================= */}

                    <div className="stock-in-modal__current">
                        <div className="stock-in-modal__current-row">
                            <span>Current stock</span>

                            <strong>
                                {inventory.stock}
                            </strong>
                        </div>

                        <div className="stock-in-modal__current-row">
                            <span>Reserved</span>

                            <strong>
                                {inventory.reservedStock}
                            </strong>
                        </div>

                        <div className="stock-in-modal__current-row">
                            <span>Available</span>

                            <strong>
                                {inventory.availableStock}
                            </strong>
                        </div>
                    </div>

                    {/* =========================
                        QUANTITY
                    ========================= */}

                    <div className="stock-in-modal__field">
                        <label htmlFor="stock-in-quantity">
                            Quantity
                        </label>

                        <input
                            id="stock-in-quantity"
                            type="number"
                            min="1"
                            step="1"
                            value={quantity}
                            onChange={handleQuantityChange}
                            disabled={loading}
                            placeholder="Enter quantity"
                        />

                        {validationError && (
                            <span className="stock-in-modal__field-error">
                                {validationError}
                            </span>
                        )}
                    </div>

                    {/* =========================
                        NOTE
                    ========================= */}

                    <div className="stock-in-modal__field">
                        <label htmlFor="stock-in-note">
                            Reason / Note
                            <span>(optional)</span>
                        </label>

                        <textarea
                            id="stock-in-note"
                            rows="4"
                            maxLength="500"
                            value={note}
                            onChange={handleNoteChange}
                            disabled={loading}
                            placeholder="Explain why the inventory is being increased..."
                        />

                        <span className="stock-in-modal__character-count">
                            {note.length}/500
                        </span>
                    </div>

                    {/* =========================
                        ERROR
                    ========================= */}

                    {error && (
                        <div
                            className="stock-in-modal__error"
                            role="alert"
                        >
                            {error}
                        </div>
                    )}

                    {/* =========================
                        ACTIONS
                    ========================= */}

                    <div className="stock-in-modal__actions">
                        <button
                            type="button"
                            className="stock-in-modal__cancel"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            className="stock-in-modal__submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Adding..."
                                : "Confirm Stock In"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default StockInModal;