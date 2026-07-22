import { useNavigate } from "react-router-dom"
import { useCart, getBulkDiscount } from "../context/CartContext"
import Logo from "../components/Logo.jsx"
import "../styles/CartPage.css"

const CartPage = () => {
    const navigate = useNavigate()
    const { items, removeFromCart, updateQuantity, clearCart, cartTotal, bulkDiscount, BULK_DISCOUNT_THRESHOLD } = useCart()

    const TAX_RATE = 0.13
    const discountedSubtotal = cartTotal - bulkDiscount
    const tax = discountedSubtotal * TAX_RATE
    const grandTotal = discountedSubtotal + tax

    return (
        <div className="cart-page">
             <header className="cart-header">
                <div className="header-left" onClick={() => navigate("/search")} style={{ cursor: "pointer" }}>
                    <Logo light={true} />
                </div>
                <span className="header-tagline">Serving the Waterloo Region</span>
                <div className="header-right">
                    <button className="back-btn" onClick={() => navigate("/search")}>
                        ← Continue Shopping
                    </button>
                    <button className="profile-btn" onClick={() => navigate("/")}>👤</button>
                </div>
            </header>

            <div className="cart-body">
                {items.length === 0 ? (
                    <div className="empty-cart">
                        <p>🛒 Your cart is empty</p>
                        <button className="shop-btn" onClick={() => navigate("/search")}>
                            Browse Products
                        </button>
                    </div>
                ) : (
                    <div className="cart-layout">
                        <div className="cart-items">
                            {items.map(({ product, quantity }) => {
                                const itemDiscount = getBulkDiscount(quantity, product.price)
                                return (
                                    <div key={product.id} className="cart-item">
                                        <div className="item-info">
                                            <h3>{product.name}</h3>
                                            <p className="item-meta">{product.brand} | 📍 {product.location}</p>
                                            <p className="item-price">${product.price.toFixed(2)} each</p>
                                            {quantity >= BULK_DISCOUNT_THRESHOLD && (
                                                <p className="bulk-discount-badge">
                                                    Bulk discount: -${itemDiscount.toFixed(2)} (10% off)
                                                </p>
                                            )}
                                            {quantity < BULK_DISCOUNT_THRESHOLD && quantity >= BULK_DISCOUNT_THRESHOLD - 3 && (
                                                <p className="bulk-hint">
                                                    Add {BULK_DISCOUNT_THRESHOLD - quantity} more for 10% bulk discount!
                                                </p>
                                            )}
                                        </div>
                                        <div className="item-controls">
                                            <div className="qty-controls">
                                                <button onClick={() => updateQuantity(product.id, quantity - 1)}>-</button>
                                                <span>{quantity}</span>
                                                <button
                                                    onClick={() => updateQuantity(product.id, quantity + 1)}
                                                    disabled={quantity >= product.stock}
                                                >+</button>
                                            </div>
                                            <p className="item-total">${(product.price * quantity).toFixed(2)}</p>
                                            <button className="remove-btn" onClick={() => removeFromCart(product.id)}>
                                                Remove
                                            </button>
                                        </div>
                                    </div>
                                )
                            })}
                            <button className="clear-btn" onClick={clearCart}>Clear Cart</button>
                        </div>

                        <div className="cart-summary">
                            <h3>Order Summary</h3>
                            <div className="summary-row">
                                <span>Subtotal</span>
                                <span>${cartTotal.toFixed(2)}</span>
                            </div>
                            {bulkDiscount > 0 && (
                                <div className="summary-row discount-row">
                                    <span>Bulk Discount</span>
                                    <span>-${bulkDiscount.toFixed(2)}</span>
                                </div>
                            )}
                            <div className="summary-row">
                                <span>HST (13%)</span>
                                <span>${tax.toFixed(2)}</span>
                            </div>
                            <div className="summary-row total-row">
                                <span>Total</span>
                                <span>${grandTotal.toFixed(2)}</span>
                            </div>
                            <button className="checkout-btn" onClick={() => navigate("/checkout")}>
                                Proceed to Checkout
                            </button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    )
}

export default CartPage
