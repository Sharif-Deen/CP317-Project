import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Logo from "../components/Logo";
import "../styles/CheckoutPage.css";

const TAX_RATE = 0.13;

export default function CheckoutPage() {
  const navigate = useNavigate();

  // Fake Cart Data (for UI testing) 
  const items = [
    {
      product: {
        id: 1,
        name: "Wireless Mouse",
        price: 29.99,
      },
      quantity: 2,
    },
    {
      product: {
        id: 2,
        name: "Mechanical Keyboard",
        price: 89.99,
      },
      quantity: 1,
    },
    {
      product: {
        id: 3,
        name: "USB-C Hub",
        price: 49.99,
      },
      quantity: 1,
    },
  ];

  const cartTotal = items.reduce(
    (sum, item) => sum + item.product.price * item.quantity,
    0
  );

  const clearCart = () => {
    console.log("Cart cleared");
  };

  const [step, setStep] = useState(1);

  const [shipping, setShipping] = useState({
    fullName: "",
    address: "",
    city: "",
    postalCode: "",
    phone: "",
  });

  const [payment, setPayment] = useState({
    method: "credit",
    cardNumber: "",
    cardHolder: "",
    expiry: "",
    cvv: "",
  });

  const [errors, setErrors] = useState({});

  const tax = cartTotal * TAX_RATE;
  const total = cartTotal + tax;

  const validateShipping = () => {
    const e = {};

    if (!shipping.fullName.trim())
      e.fullName = "Full name is required";

    if (!shipping.address.trim())
      e.address = "Address is required";

    if (!shipping.city.trim())
      e.city = "City is required";

    if (!shipping.phone.trim())
      e.phone = "Phone number is required";

    if (
      !/^[A-Z]\d[A-Z] \d[A-Z]\d$/.test(
        shipping.postalCode.toUpperCase()
      )
    ) {
      e.postalCode = "Invalid postal code (e.g. N2L 3C5)";
    }

    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const validatePayment = () => {
    const e = {};

    if (!/^\d{16}$/.test(payment.cardNumber.replace(/\s/g, "")))
      e.cardNumber = "Card number must be 16 digits";

    if (!payment.cardHolder.trim())
      e.cardHolder = "Card holder name is required";

    if (!/^(0[1-9]|1[0-2])\/\d{2}$/.test(payment.expiry))
      e.expiry = "Invalid expiry (MM/YY)";

    if (!/^\d{3}$/.test(payment.cvv))
      e.cvv = "CVV must be 3 digits";

    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleShippingNext = () => {
    if (validateShipping()) setStep(2);
  };

  const handlePlaceOrder = () => {
    if (validatePayment()) {
      clearCart();
      setStep(3);
    }
  };

  if (items.length === 0 && step !== 3) {
    return (
      <div className="checkout-page">
        <div className="checkout-navbar">
          <Logo />
          <div className="nav-links">
            <a href="/search">Search</a>
            <a href="/cart">Cart</a>
          </div>
        </div>

        <div className="checkout-content">
          <div className="empty-checkout">
            <p>🛒 Your cart is empty</p>

            <button
              className="checkout-btn"
              onClick={() => navigate("/search")}
            >
              Browse Products
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="checkout-page">

      <div className="checkout-navbar">
        <Logo />
      <span className="header-tagline">Serving the Waterloo Region</span>
        <div className="nav-links">
  <button
    className="cancel-btn"
    onClick={() => navigate("/cart")}
  >
    Back to Cart
  </button>
</div>
      </div>

      <div className="checkout-content">
        <h2 className="checkout-title">Checkout</h2>

        <div className="checkout-steps">
          <div className={`checkout-step ${step >= 1 ? "active" : ""}`}>
            1. Shipping
          </div>

          <div className="checkout-step-divider" />

          <div className={`checkout-step ${step >= 2 ? "active" : ""}`}>
            2. Payment
          </div>

          <div className="checkout-step-divider" />

          <div className={`checkout-step ${step >= 3 ? "active" : ""}`}>
            3. Receipt
          </div>
        </div>

        <div className="checkout-body">

          <div className="checkout-form-section">

            {step === 1 && (
              <div className="checkout-card">

                <h3 className="checkout-card-title">
                  Shipping Details
                </h3>

                <div className="form-group">
                  <label>Full Name</label>

                  <input
                    type="text"
                    placeholder="John Doe"
                    value={shipping.fullName}
                    onChange={(e) =>
                      setShipping({
                        ...shipping,
                        fullName: e.target.value,
                      })
                    }
                  />

                  {errors.fullName && (
                    <p className="error">{errors.fullName}</p>
                  )}
                </div>

                <div className="form-group">
                  <label>Delivery Address</label>

                  <input
                    type="text"
                    placeholder="123 Main St"
                    value={shipping.address}
                    onChange={(e) =>
                      setShipping({
                        ...shipping,
                        address: e.target.value,
                      })
                    }
                  />

                  {errors.address && (
                    <p className="error">{errors.address}</p>
                  )}
                </div>

                <div className="form-row">

                  <div className="form-group">
                    <label>City</label>

                    <input
                      type="text"
                      placeholder="Waterloo"
                      value={shipping.city}
                      onChange={(e) =>
                        setShipping({
                          ...shipping,
                          city: e.target.value,
                        })
                      }
                    />

                    {errors.city && (
                      <p className="error">{errors.city}</p>
                    )}
                  </div>

                  <div className="form-group">
                    <label>Postal Code</label>

                    <input
                      type="text"
                      placeholder="N2L 3C5"
                      value={shipping.postalCode}
                      onChange={(e) =>
                        setShipping({
                          ...shipping,
                          postalCode: e.target.value.toUpperCase(),
                        })
                      }
                    />

                    {errors.postalCode && (
                      <p className="error">{errors.postalCode}</p>
                    )}
                  </div>

                </div>

                <div className="form-group">
                  <label>Phone Number</label>

                  <input
                    type="text"
                    placeholder="519-123-4567"
                    value={shipping.phone}
                    onChange={(e) =>
                      setShipping({
                        ...shipping,
                        phone: e.target.value,
                      })
                    }
                  />

                  {errors.phone && (
                    <p className="error">{errors.phone}</p>
                  )}
                </div>

                <button
                  className="checkout-btn"
                  onClick={handleShippingNext}
                >
                  Continue to Payment →
                </button>

              </div>
            )}

            {step === 2 && (
              <div className="checkout-card">

                <h3 className="checkout-card-title">
                  Payment Details
                </h3>

                <div className="payment-toggle">

                  <button
                    className={`toggle-btn ${
                      payment.method === "credit"
                        ? "selected"
                        : ""
                    }`}
                    onClick={() =>
                      setPayment({
                        ...payment,
                        method: "credit",
                      })
                    }
                  >
                    Credit Card
                  </button>

                  <button
                    className={`toggle-btn ${
                      payment.method === "debit"
                        ? "selected"
                        : ""
                    }`}
                    onClick={() =>
                      setPayment({
                        ...payment,
                        method: "debit",
                      })
                    }
                  >
                    Debit Card
                  </button>

                </div>
                                <div className="form-group">
                  <label>Card Number</label>

                  <input
                    type="text"
                    placeholder="1234 5678 9012 3456"
                    maxLength={16}
                    value={payment.cardNumber}
                    onChange={(e) =>
                      setPayment({
                        ...payment,
                        cardNumber: e.target.value,
                      })
                    }
                  />

                  {errors.cardNumber && (
                    <p className="error">{errors.cardNumber}</p>
                  )}
                </div>

                <div className="form-group">
                  <label>Card Holder Name</label>

                  <input
                    type="text"
                    placeholder="John Doe"
                    value={payment.cardHolder}
                    onChange={(e) =>
                      setPayment({
                        ...payment,
                        cardHolder: e.target.value,
                      })
                    }
                  />

                  {errors.cardHolder && (
                    <p className="error">{errors.cardHolder}</p>
                  )}
                </div>

                <div className="form-row">
                  <div className="form-group">
                    <label>Expiry Date</label>

                    <input
                      type="text"
                      placeholder="MM/YY"
                      maxLength={5}
                      value={payment.expiry}
                      onChange={(e) =>
                        setPayment({
                          ...payment,
                          expiry: e.target.value,
                        })
                      }
                    />

                    {errors.expiry && (
                      <p className="error">{errors.expiry}</p>
                    )}
                  </div>

                  <div className="form-group">
                    <label>CVV</label>

                    <input
                      type="password"
                      placeholder="123"
                      maxLength={3}
                      value={payment.cvv}
                      onChange={(e) =>
                        setPayment({
                          ...payment,
                          cvv: e.target.value,
                        })
                      }
                    />

                    {errors.cvv && (
                      <p className="error">{errors.cvv}</p>
                    )}
                  </div>
                </div>

                <div className="checkout-btn-row">
                  <button
                    className="checkout-btn-back"
                    onClick={() => setStep(1)}
                  >
                    ← Back
                  </button>

                  <button
                    className="checkout-btn"
                    onClick={handlePlaceOrder}
                  >
                    Place Order →
                  </button>
                </div>

              </div>
            )}

            {step === 3 && (
              <div className="checkout-card receipt-card">

                <div className="receipt-check">✓</div>

                <h3 className="receipt-title">
                  Order Confirmed!
                </h3>

                <p className="receipt-subtitle">
                  Thank you, {shipping.fullName}!
                </p>

                <div className="receipt-divider" />

                <h4 className="receipt-section-title">
                  Deliver To
                </h4>

                <p className="receipt-info">{shipping.fullName}</p>
                <p className="receipt-info">{shipping.address}</p>
                <p className="receipt-info">
                  {shipping.city}, {shipping.postalCode}
                </p>
                <p className="receipt-info">{shipping.phone}</p>

                <div className="receipt-divider" />

                <h4 className="receipt-section-title">
                  Payment
                </h4>

                <p className="receipt-info">
                  {payment.method === "credit"
                    ? "Credit Card"
                    : "Debit Card"}{" "}
                  ending in {payment.cardNumber.slice(-4)}
                </p>

                <div className="receipt-divider" />

                <h4 className="receipt-section-title">
                  Order Summary
                </h4>

                {items.map(({ product, quantity }) => (
                  <div
                    key={product.id}
                    className="receipt-item"
                  >
                    <span>
                      {product.name} x{quantity}
                    </span>

                    <span>
                      $
                      {(product.price * quantity).toFixed(2)}
                    </span>
                  </div>
                ))}

                <div className="receipt-item">
                  <span>HST (13%)</span>
                  <span>${tax.toFixed(2)}</span>
                </div>

                <div className="receipt-item receipt-total">
                  <span>Total</span>
                  <span>${total.toFixed(2)}</span>
                </div>

                <button
                  className="checkout-btn"
                  onClick={() => navigate("/search")}
                >
                  Continue Shopping
                </button>

              </div>
            )}

          </div>

          {step !== 3 && (
            <div className="checkout-summary">

              <h3 className="checkout-card-title">
                Order Summary
              </h3>

              {items.map(({ product, quantity }) => (
                <div
                  key={product.id}
                  className="summary-item"
                >
                  <div>
                    <p className="summary-item-name">
                      {product.name}
                    </p>

                    <p className="summary-item-qty">
                      Qty: {quantity}
                    </p>
                  </div>

                  <p className="summary-item-price">
                    $
                    {(product.price * quantity).toFixed(2)}
                  </p>
                </div>
              ))}

              <div className="summary-divider" />

              <div className="summary-row">
                <span>Subtotal</span>
                <span>${cartTotal.toFixed(2)}</span>
              </div>

              <div className="summary-row">
                <span>HST (13%)</span>
                <span>${tax.toFixed(2)}</span>
              </div>

              <div className="summary-row summary-total">
                <span>Total</span>
                <span>${total.toFixed(2)}</span>
              </div>

            </div>
          )}

        </div>
      </div>
    </div>
  );
}