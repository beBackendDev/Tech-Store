import { useEffect, useMemo, useState } from "react";
import "./AdjustmentModal.scss";

const AdjustmentModal = ({
    isOpen,
    inventory,
    loading,
    error,
    onClose,
    onSubmit,
}) => {
    const [actualStock, setActualStock] = useState("");
    const [note, setNote] = useState("");
    const [validationError, setValidationError] = useState("");

    useEffect(() => {
        if (!isOpen || !inventory) {
            return;
        }

        setActualStock(String(inventory.stock));
        setNote("");
        setValidationError("");
    }, [isOpen, inventory]);

    const difference = useMemo(() => {
        if (!inventory || actualStock === "") {
            return null;
        }

        const value = Number(actualStock);

        if (!Number.isInteger(value)) {
            return null;
        }

        return value - inventory.stock;
    }, [actualStock, inventory]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        setValidationError("");

        const value = Number(actualStock);

        if (actualStock === "") {
            setValidationError(
                "Actual stock is required."
            );
            return;
        }

        if (!Number.isInteger(value) || value < 0) {
            setValidationError(
                "Actual stock must be a non-negative integer."
            );
            return;
        }

        if (!note.trim()) {
            setValidationError(
                "Adjustment note is required."
            );
            return;
        }

        if (note.trim().length > 500) {
            setValidationError(
                "Note must not exceed 500 characters."
            );
            return;
        }

        if (difference === 0) {
            setValidationError(
                "Actual stock is the same as current stock."
            );
            return;
        }

        if (
            inventory.reservedStock != null &&
            value < inventory.reservedStock
        ) {
            setValidationError(
                "Actual stock cannot be lower than reserved stock."
            );
            return;
        }

        await onSubmit({
            actualStock: value,
            note: note.trim(),
        });
    };

    if (!isOpen || !inventory) {
        return null;
    }

    return (
        <div
            className="adjustment-modal__overlay"
            onMouseDown={onClose}
        >
            <div
                className="adjustment-modal"
                onMouseDown={(event) => event.stopPropagation()}
            >
                <div className="adjustment-modal__header">
                    <div>
                        <h2>Adjust Inventory</h2>

                        <p>
                            {inventory.productName}
                        </p>
                    </div>

                    <button
                        type="button"
                        className="adjustment-modal__close"
                        onClick={onClose}
                        disabled={loading}
                    >
                        ×
                    </button>
                </div>

                <form
                    className="adjustment-modal__form"
                    onSubmit={handleSubmit}
                >
                    <div className="adjustment-modal__current">
                        <div className="adjustment-modal__current-row">
                            <span>Current stock</span>
                            <strong>{inventory.stock}</strong>
                        </div>

                        <div className="adjustment-modal__current-row">
                            <span>Reserved</span>
                            <strong>{inventory.reservedStock}</strong>
                        </div>

                        <div className="adjustment-modal__current-row">
                            <span>Available</span>
                            <strong>{inventory.availableStock}</strong>
                        </div>
                    </div>

                    <div className="adjustment-modal__field">
                        <label htmlFor="actual-stock">
                            Actual stock
                        </label>

                        <input
                            id="actual-stock"
                            type="number"
                            min="0"
                            step="1"
                            value={actualStock}
                            onChange={(event) =>
                                setActualStock(
                                    event.target.value
                                )
                            }
                            disabled={loading}
                        />
                    </div>

                    {difference !== null && (
                        <div className="adjustment-modal__difference">
                            <span>Difference</span>

                            <strong
                                className={
                                    difference > 0
                                        ? "is-positive"
                                        : "is-negative"
                                }
                            >
                                {difference > 0
                                    ? `+${difference}`
                                    : difference}
                            </strong>
                        </div>
                    )}

                    <div className="adjustment-modal__field">
                        <label htmlFor="adjustment-note">
                            Reason / Note
                        </label>

                        <textarea
                            id="adjustment-note"
                            rows="4"
                            maxLength="500"
                            value={note}
                            onChange={(event) =>
                                setNote(event.target.value)
                            }
                            placeholder="Explain why the inventory is being adjusted..."
                            disabled={loading}
                        />

                        <small>
                            {note.length}/500
                        </small>
                    </div>

                    {(validationError || error) && (
                        <div className="adjustment-modal__error">
                            {validationError || error}
                        </div>
                    )}

                    <div className="adjustment-modal__actions">
                        <button
                            type="button"
                            className="adjustment-modal__cancel"
                            onClick={onClose}
                            disabled={loading}
                        >
                            Cancel
                        </button>

                        <button
                            type="submit"
                            className="adjustment-modal__submit"

                            disabled={
                                loading ||
                                difference === 0
                            }
                        >
                            {loading
                                ? "Adjusting..."
                                : "Confirm Adjustment"}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AdjustmentModal;