import { createContext, useContext, useState } from "react"

const OrderContext = createContext()

export const OrderProvider = ({ children }) => {
    const [orders, setOrders] = useState([])

    const placeOrder = (orderData) => {
        const newOrder = {
            id: "ORD-" + Date.now(),
            items: orderData.items,
            subtotal: orderData.subtotal,
            discount: orderData.discount || 0,
            tax: orderData.tax,
            total: orderData.total,
            shipping: orderData.shipping,
            payment: orderData.payment,
            status: "confirmed",
            date: new Date().toISOString(),
        }
        setOrders(prev => [newOrder, ...prev])
        return newOrder
    }

    const cancelOrder = (orderId) => {
        setOrders(prev =>
            prev.map(o =>
                o.id === orderId && o.status === "confirmed"
                    ? { ...o, status: "cancelled" }
                    : o
            )
        )
    }

    return (
        <OrderContext.Provider value={{ orders, placeOrder, cancelOrder }}>
            {children}
        </OrderContext.Provider>
    )
}

export const useOrders = () => useContext(OrderContext)
