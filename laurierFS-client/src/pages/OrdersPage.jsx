import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { useOrders } from "../context/OrderContext"
import { useCart } from "../context/CartContext"
import Logo from "../components/Logo.jsx"
import "../styles/OrdersPage.css"

const OrdersPage = () => {
    const navigate = useNavigate()
    const { orders, cancelOrder } = useOrders()
    const { addToCart, cartCount } = useCart()
    const [toast, setToast] = useState(null)
    const [confirmCancel, setConfirmCancel] = useState(null)

    const handleReorder = (order) => {
        order.items.forEach(({ product, quantity }) => {
            addToCart(product, quantity)
        })
        setToast(`Order ${order.id} items added to cart!`)
        setTimeout(() => setToast(null), 2500)
    }

    const handleCancel = (orderId) => {
        cancelOrder(orderId)
        setConfirmCancel(null)
        setToast("Order cancelled successfully")
        setTimeout(() => setToast(null), 2500)
    }

    const formatDate = (iso) => {
        return new Date(iso).toLocaleDateString("en-CA", {
            year: "numeric", month: "short", day: "numeric",
            hour: "2-digit", minute: "2-digit"
        })
    }

    return (
        <div className="orders-page">
            <header className="orders-header">
                <div className="header-left" onClick={() => navigate("/search")} style={{ cursor: "pointer" }}>
                    <Logo light={true} />
                </div>
                <span className="header-tagline">Serving the Waterloo Region</span>
                <div className="header-right">
                    <button className="cart-btn" onClick={() => navigate("/cart")}>
                        Cart ({cartCount})
                    </button>
                    <button className="back-btn" onClick={() => navigate("/search")}>
                        ← Back to Shop
                    </button>
                    <button className="profile-btn" onClick={() => navigate("/")}>👤</button>
                </div>
            </header>

            {toast && <div className="toast">✓ {toast}</div>}

            {/* Cancel confirmation modal */}
            {confirmCancel && (
                <div className="modal-overlay" onClick={() => setConfirmCancel(null)}>
                    <div className="confirm-modal" onClick={(e) => e.stopPropagation()}>
                        <h3>Cancel Order?</h3>
                        <p>Are you sure you want to cancel order <strong>{confirmCancel}</strong>? This cannot be undone.</p>
                        <div className="confirm-btns">
                            <button className="confirm-yes" onClick={() => handleCancel(confirmCancel)}>
                                Yes, Cancel Order
                            </button>
                            <button className="confirm-no" onClick={() => setConfirmCancel(null)}>
                                Keep Order
                            </button>
                        </div>
                    </div>
                </div>
            )}

            <div className="orders-body">
                <h2 className="orders-title">Order History</h2>

                {orders.length === 0 ? (
                    <div className="empty-orders">
                        <p>No orders yet</p>
                        <button className="shop-btn" onClick={() => navigate("/search")}>
                            Start Shopping
                        </button>
                    </div>
                ) : (
                    <div className="orders-list">
                        {orders.map(order => (
                            <div key={order.id} className={`order-card ${order.status}`}>
                                <div className="order-header-row">
                                    <div>
                                        <h3 className="order-id">{order.id}</h3>
                                        <p className="order-date">{formatDate(order.date)}</p>
                                    </div>
                                    <span className={`status-badge status-${order.status}`}>
                                        {order.status === "confirmed" ? "Confirmed" : "Cancelled"}
                                    </span>
                                </div>

                                <div className="order-items-list">
                                    {order.items.map(({ product, quantity }) => (
                                        <div key={product.id} className="order-item-row">
                                            <span className="oi-name">{product.name}</span>
                                            <span className="oi-qty">x{quantity}</span>
                                            <span className="oi-price">${(product.price * quantity).toFixed(2)}</span>
                                        </div>
                                    ))}
                                </div>

                                <div className="order-footer">
                                    <div className="order-totals">
                                        {order.discount > 0 && (
                                            <span className="order-discount">Bulk Discount: -${order.discount.toFixed(2)}</span>
                                        )}
                                        <span className="order-total">Total: ${order.total.toFixed(2)}</span>
                                    </div>
                                    <div className="order-actions">
                                        <button
                                            className="reorder-btn"
                                            onClick={() => handleReorder(order)}
                                        >
                                            Reorder
                                        </button>
                                        {order.status === "confirmed" && (
                                            <button
                                                className="cancel-order-btn"
                                                onClick={() => setConfirmCancel(order.id)}
                                            >
                                                Cancel Order
                                            </button>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    )
}

export default OrdersPage
