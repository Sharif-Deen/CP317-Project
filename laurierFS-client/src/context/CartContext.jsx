import { createContext, useContext, useState } from "react"

const CartContext = createContext()

// Bulk discount thresholds
const BULK_DISCOUNT_THRESHOLD = 10  // 10+ items of same product
const BULK_DISCOUNT_RATE = 0.10     // 10% discount

export const getBulkDiscount = (quantity, price) => {
    if (quantity >= BULK_DISCOUNT_THRESHOLD) {
        return price * quantity * BULK_DISCOUNT_RATE
    }
    return 0
}

export const CartProvider = ({ children }) => {
    const [items, setItems] = useState([])

    const addToCart = (product, qty = 1) => {
        setItems(prev => {
            const existing = prev.find(i => i.product.id === product.id)
            if (existing) {
                return prev.map(i =>
                    i.product.id === product.id
                        ? { ...i, quantity: i.quantity + qty }
                        : i
                )
            }
            return [...prev, { product, quantity: qty }]
        })
    }

    const removeFromCart = (productId) => {
        setItems(prev => prev.filter(i => i.product.id !== productId))
    }

    const updateQuantity = (productId, quantity) => {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        setItems(prev =>
            prev.map(i =>
                i.product.id === productId ? { ...i, quantity } : i
            )
        )
    }

    const clearCart = () => setItems([])

    const cartCount = items.reduce((sum, i) => sum + i.quantity, 0)
    const cartTotal = items.reduce((sum, i) => sum + i.product.price * i.quantity, 0)
    const bulkDiscount = items.reduce((sum, i) => sum + getBulkDiscount(i.quantity, i.product.price), 0)

    return (
        <CartContext.Provider value={{
            items, addToCart, removeFromCart, updateQuantity, clearCart,
            cartCount, cartTotal, bulkDiscount,
            BULK_DISCOUNT_THRESHOLD, BULK_DISCOUNT_RATE
        }}>
            {children}
        </CartContext.Provider>
    )
}

export const useCart = () => useContext(CartContext)
