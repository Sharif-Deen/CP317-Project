import { createContext, useContext, useState } from "react"

const CartContext = createContext()

export const CartProvider = ({ children }) => {
    const [items, setItems] = useState([])

    const addToCart = (product) => {
        setItems(prev => {
            const existing = prev.find(i => i.product.id === product.id)
            if (existing) {
                return prev.map(i =>
                    i.product.id === product.id
                        ? { ...i, quantity: i.quantity + 1 }
                        : i
                )
            }
            return [...prev, { product, quantity: 1 }]
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

    return (
        <CartContext.Provider value={{ items, addToCart, removeFromCart, updateQuantity, clearCart, cartCount, cartTotal }}>
            {children}
        </CartContext.Provider>
    )
}

export const useCart = () => useContext(CartContext)