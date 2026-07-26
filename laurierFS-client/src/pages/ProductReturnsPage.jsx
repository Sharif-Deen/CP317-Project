import { useEffect, useState } from "react";
import "../styles/ProductReturnsPage.css";
import Logo from "../components/Logo";
import { useNavigate } from "react-router-dom";
import { useCart } from "../context/CartContext";
import { useAuth } from "../context/AuthContext";

const RETURN_REASONS = [
  "Damaged item",
  "Incorrect item",
  "Missing parts",
  "Product quality issue",
  "No longer needed",
  "Other",
];

function ProductReturnsPage() {
  const [orderId, setOrderId] = useState("");
  const [productName, setProductName] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [reason, setReason] = useState("");
  const [description, setDescription] = useState("");
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [returnRequests, setReturnRequests] = useState([]);
  const navigate = useNavigate();
const { cartCount } = useCart();
const { user, isAuthenticated, logout } = useAuth();

  useEffect(() => {
    try {
      const savedReturns = JSON.parse(
        localStorage.getItem("productReturns")
      );

      if (Array.isArray(savedReturns)) {
        setReturnRequests(savedReturns);
      }
    } catch (error) {
      console.error("Could not load saved return requests:", error);
    }
  }, []);

  const saveReturns = (updatedReturns) => {
    setReturnRequests(updatedReturns);

    localStorage.setItem(
      "productReturns",
      JSON.stringify(updatedReturns)
    );
  };

  const clearForm = () => {
    setOrderId("");
    setProductName("");
    setQuantity(1);
    setReason("");
    setDescription("");
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (!orderId.trim()) {
      setMessage("Please enter an order ID.");
      setMessageType("error");
      return;
    }

    if (!productName.trim()) {
      setMessage("Please enter a product name.");
      setMessageType("error");
      return;
    }

    if (Number(quantity) < 1) {
      setMessage("Return quantity must be at least 1.");
      setMessageType("error");
      return;
    }

    if (!reason) {
      setMessage("Please select a return reason.");
      setMessageType("error");
      return;
    }

    const newReturnRequest = {
      returnId: `RET-${Date.now()}`,
      orderId: orderId.trim(),
      productName: productName.trim(),
      quantity: Number(quantity),
      reason,
      description: description.trim(),
      status: "Pending",
      submittedDate: new Date().toLocaleDateString(),
    };

    const updatedReturns = [
      newReturnRequest,
      ...returnRequests,
    ];

    saveReturns(updatedReturns);

    setMessage("Return request submitted successfully.");
    setMessageType("success");

    clearForm();
  };

  const handleClear = () => {
    clearForm();
    setMessage("");
    setMessageType("");
  };

  const handleDelete = (returnId) => {
    const updatedReturns = returnRequests.filter(
      (returnRequest) =>
        returnRequest.returnId !== returnId
    );

    saveReturns(updatedReturns);
  };

  return (
    <main className="returns-page">
  <header className="search-header">
    <div className="header-left">
      <Logo light={true} />
    </div>

    <span className="header-tagline">
      Welcome, {isAuthenticated ? `${user.username}!` : "Guest!"}
    </span>

    <div className="header-right">
      <button
        className="cart-btn"
        type="button"
        onClick={() => navigate("/orders")}
      >
        Orders
      </button>

      <button
        className="cart-btn"
        type="button"
        onClick={() => navigate("/cart")}
      >
        Cart
        <span className="cart-count">{cartCount}</span>
      </button>

      <button
        className="cart-btn"
        type="button"
        onClick={() => {
          logout();
          navigate("/");
        }}
      >
        {isAuthenticated ? "Logout" : "Log in"}
      </button>
    </div>
  </header>

  <div className="returns-content">
    <section className="returns-header">
      <h1>Product Returns</h1>

      <p>
        Submit a return request for a product from one of your completed
        orders.
      </p>
    </section>

    <section className="return-form-card">
      <h2>New Return Request</h2>

      <form className="return-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="orderId">
            Order ID <span>*</span>
          </label>

          <input
            id="orderId"
            type="text"
            placeholder="Example: ORD-105"
            value={orderId}
            onChange={(event) => setOrderId(event.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="productName">
            Product Name <span>*</span>
          </label>

          <input
            id="productName"
            type="text"
            placeholder="Enter the product name"
            value={productName}
            onChange={(event) => setProductName(event.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="quantity">
            Return Quantity <span>*</span>
          </label>

          <input
            id="quantity"
            type="number"
            min="1"
            value={quantity}
            onChange={(event) => setQuantity(event.target.value)}
          />
        </div>

        <div className="form-group">
          <label htmlFor="reason">
            Return Reason <span>*</span>
          </label>

          <select
            id="reason"
            value={reason}
            onChange={(event) => setReason(event.target.value)}
          >
            <option value="">Select a reason</option>

            {RETURN_REASONS.map((returnReason) => (
              <option
                key={returnReason}
                value={returnReason}
              >
                {returnReason}
              </option>
            ))}
          </select>
        </div>

        <div className="form-group full-width">
          <label htmlFor="description">
            Additional Details
          </label>

          <textarea
            id="description"
            rows="4"
            placeholder="Provide additional information about the return..."
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          />
        </div>

        {message && (
          <p className={`form-message ${messageType}`}>
            {message}
          </p>
        )}

        <div className="form-buttons">
          <button
            className="submit-return-button"
            type="submit"
          >
            Submit Return
          </button>

          <button
            className="clear-return-button"
            type="button"
            onClick={handleClear}
          >
            Clear
          </button>
        </div>
      </form>
    </section>

    <section className="return-history-card">
      <h2>My Return Requests</h2>

      {returnRequests.length === 0 ? (
        <p className="empty-message">
          You have not submitted any return requests.
        </p>
      ) : (
        <div className="return-table-container">
          <table className="return-table">
            <thead>
              <tr>
                <th>Return ID</th>
                <th>Order ID</th>
                <th>Product</th>
                <th>Quantity</th>
                <th>Reason</th>
                <th>Date</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {returnRequests.map((returnRequest) => (
                <tr key={returnRequest.returnId}>
                  <td>{returnRequest.returnId}</td>
                  <td>{returnRequest.orderId}</td>
                  <td>{returnRequest.productName}</td>
                  <td>{returnRequest.quantity}</td>
                  <td>{returnRequest.reason}</td>
                  <td>{returnRequest.submittedDate}</td>

                  <td>
                    <span className="status-pending">
                      {returnRequest.status}
                    </span>
                  </td>

                  <td>
                    <button
                      className="delete-return-button"
                      type="button"
                      onClick={() =>
                        handleDelete(returnRequest.returnId)
                      }
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </section>
  </div>
</main>
    
  );
}

export default ProductReturnsPage;
