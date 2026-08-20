import {
    createContext,
    useEffect,
    useMemo,
    useState
} from "react";

export const CartContext = createContext(null);

const CART_STORAGE_KEY = "techstore_cart";


export function CartProvider({ children }) {

    /*
     * ==============================
     * INITIAL STATE
     * ==============================
     *
     * Đọc cart từ localStorage ngay
     * khi CartProvider được khởi tạo.
     */

    const [items, setItems] = useState(() => {

        try {

            const savedCart =
                localStorage.getItem(
                    CART_STORAGE_KEY
                );

            if (!savedCart) {
                return [];
            }

            const parsedCart =
                JSON.parse(savedCart);

            /*
             * Đảm bảo dữ liệu đọc từ
             * localStorage thực sự là array.
             */

            return Array.isArray(parsedCart)
                ? parsedCart
                : [];

        } catch (error) {

            console.error(
                "Failed to load cart:",
                error
            );

            return [];
        }

    });


    /*
     * ==============================
     * PERSIST CART
     * ==============================
     *
     * Mỗi khi items thay đổi,
     * ghi lại vào localStorage.
     */

    useEffect(() => {

        try {

            localStorage.setItem(
                CART_STORAGE_KEY,
                JSON.stringify(items)
            );

        } catch (error) {

            console.error(
                "Failed to save cart:",
                error
            );

        }

    }, [items]);


    /*
     * ==============================
     * ADD TO CART
     * ==============================
     */
//doc lai
    // const addToCart = (product) => {

    //     setItems(prevItems => {

    //         const existingItem =
    //             prevItems.find(
    //                 item =>
    //                     item.product.id === product.id
    //             );

    //         /*
    //          * Product đã tồn tại
    //          */

    //         if (existingItem) {

    //             return prevItems.map(item => {

    //                 if (
    //                     item.product.id !== product.id
    //                 ) {
    //                     return item;
    //                 }

    //                 return {
    //                     ...item,

    //                     quantity: Math.min(
    //                         item.quantity + 1,
    //                         product.stock
    //                     )
    //                 };

    //             });

    //         }

    //         /*
    //          * Product chưa tồn tại
    //          */

    //         return [
    //             ...prevItems,

    //             {
    //                 product,
    //                 quantity: 1
    //             }
    //         ];

    //     });

    // };
    const addToCart = (product, quantity = 1) => {

        setItems(prevItems => {

            const existingItem = prevItems.find(
                item => item.product.id === product.id
            );

            if (existingItem) {

                return prevItems.map(item => {

                    if (item.product.id !== product.id) {
                        return item;
                    }

                    return {
                        ...item,
                        quantity: Math.min(
                            item.quantity + quantity,
                            product.stock
                        )
                    };

                });

            }

            return [
                ...prevItems,
                {
                    product,
                    quantity: Math.min(
                        quantity,
                        product.stock
                    )
                }
            ];

        });

    };

    /*
     * ==============================
     * REMOVE
     * ==============================
     */

    const removeFromCart = (productId) => {

        setItems(prevItems =>

            prevItems.filter(
                item =>
                    item.product.id !== productId
            )

        );

    };


    /*
     * ==============================
     * UPDATE QUANTITY
     * ==============================
     */

    const updateQuantity = (
        productId,
        quantity
    ) => {

        setItems(prevItems =>

            prevItems.map(item => {

                if (
                    item.product.id !== productId
                ) {
                    return item;
                }

                const newQuantity =
                    Math.max(
                        1,
                        Math.min(
                            quantity,
                            item.product.stock
                        )
                    );

                return {
                    ...item,
                    quantity: newQuantity
                };

            })

        );

    };


    /*
     * ==============================
     * INCREASE
     * ==============================
     */

    const increaseQuantity = (productId) => {

        const item = items.find(
            item =>
                item.product.id === productId
        );

        if (!item) {
            return;
        }

        updateQuantity(
            productId,
            item.quantity + 1
        );

    };


    /*
     * ==============================
     * DECREASE
     * ==============================
     */

    const decreaseQuantity = (productId) => {

        const item = items.find(
            item =>
                item.product.id === productId
        );

        if (!item) {
            return;
        }

        /*
         * Nếu quantity = 1
         * => remove khỏi cart
         */

        if (item.quantity === 1) {

            removeFromCart(productId);

            return;
        }

        updateQuantity(
            productId,
            item.quantity - 1
        );

    };


    /*
     * ==============================
     * CLEAR CART
     * ==============================
     */

    const clearCart = () => {

        setItems([]);

    };


    /*
     * ==============================
     * TOTAL ITEMS
     * ==============================
     */

    const totalItems = useMemo(() => {

        return items.reduce(
            (total, item) =>
                total + item.quantity,
            0
        );

    }, [items]);


    /*
     * ==============================
     * SUBTOTAL
     * ==============================
     */

    const subtotal = useMemo(() => {

        return items.reduce(
            (total, item) =>
                total +
                item.product.price *
                item.quantity,
            0
        );

    }, [items]);


    /*
     * ==============================
     * CONTEXT VALUE
     * ==============================
     */

    const value = {

        items,

        totalItems,

        subtotal,

        addToCart,

        removeFromCart,

        updateQuantity,

        increaseQuantity,

        decreaseQuantity,

        clearCart

    };


    return (

        <CartContext.Provider value={value}>

            {children}

        </CartContext.Provider>

    );

}