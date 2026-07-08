import { useNavigate } from "react-router-dom"
import { useCart } from "../context/CartContext"
import Logo from "../components/Logo.jsx"
import "../styles/CartPage.css"

const CartPage = () => {
    const navigate = useNavigate()
    const { items, removeFromCart, updateQuantity, clearCart, cartTotal } = useCart()

    const TAX_RATE = 0.13
    const tax = cartTotal * TAX_RATE
    const grandTotal = cartTotal + tax

    return (
        <div className="cart-page">
             <header className="cart-header">
                <div className="header-left" onClick={() => navigate("/search")} style={{ cursor: "pointer" }}>
                    <Logo />
                </div>
                <span className="header-tagline">Serving the Waterloo Region</span>
                <div className="header-right-col">
                    <button className="back-btn" onClick={() => navigate("/search")}>
                        ← Continue Shopping
                    </button>
                    <span className="page-badge">🛒 Your Cart</span>
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
                            {items.map(({ product, quantity }) => (
                                <div key={product.id} className="cart-item">
                                    <div className="item-info">
                                        <h3>{product.name}</h3>
                                        <p className="item-meta">{product.brand} | 📍 {product.location}</p>
                                        <p className="item-price">${product.price.toFixed(2)} each</p>
                                    </div>
                                    <div className="item-controls">
                                        <div className="qty-controls">
                                            <button onClick={() => updateQuantity(product.id, quantity - 1)}>−</button>
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
                            ))}
                            <button className="clear-btn" onClick={clearCart}>Clear Cart</button>
                        </div>

                        <div className="cart-summary">
                            <h3>Order Summary</h3>
                            <div className="summary-row">
                                <span>Subtotal</span>
                                <span>${cartTotal.toFixed(2)}</span>
                            </div>
                            <div className="summary-row">
                                <span>HST (13%)</span>
                                <span>${tax.toFixed(2)}</span>
                            </div>
                            <div className="summary-row total-row">
                                <span>Total</span>
                                <span>${grandTotal.toFixed(2)}</span>
                            </div>
                            <button className="checkout-btn" onClick={() => alert("Checkout page coming soon!")}>
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