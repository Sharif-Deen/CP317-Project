import "../styles/ProductDetailModal.css"

const ProductDetailModal = ({ product, onClose, onAddToCart }) => {
    if (!product) return null

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                <button className="modal-close" onClick={onClose}>&times;</button>

                <h2 className="modal-title">{product.name}</h2>

                <div className="modal-body">
                    <div className="modal-price">${product.price.toFixed(2)}</div>

                    <div className="modal-meta-grid">
                        <div className="meta-item">
                            <span className="meta-label">Category</span>
                            <span className="meta-value">{product.type}</span>
                        </div>
                        <div className="meta-item">
                            <span className="meta-label">Brand</span>
                            <span className="meta-value">{product.brand}</span>
                        </div>
                        <div className="meta-item">
                            <span className="meta-label">Location</span>
                            <span className="meta-value">📍 {product.location}</span>
                        </div>
                        <div className="meta-item">
                            <span className="meta-label">Stock</span>
                            <span className={`meta-value ${product.stock > 0 ? "in-stock" : "no-stock"}`}>
                                {product.stock > 0 ? `${product.stock} units` : "Out of Stock"}
                            </span>
                        </div>
                    </div>

                    <div className="modal-section">
                        <h4>Description</h4>
                        <p>{product.description}</p>
                    </div>

                    <div className="modal-section">
                        <h4>Tags</h4>
                        <div className="tag-list">
                            {product.tags.map((tag, i) => (
                                <span key={i} className="tag-chip">{tag}</span>
                            ))}
                        </div>
                    </div>

                    <button
                        className="modal-add-btn"
                        disabled={product.stock === 0}
                        onClick={() => onAddToCart(product)}
                    >
                        {product.stock > 0 ? "Add to Cart" : "Unavailable"}
                    </button>
                </div>
            </div>
        </div>
    )
}

export default ProductDetailModal
