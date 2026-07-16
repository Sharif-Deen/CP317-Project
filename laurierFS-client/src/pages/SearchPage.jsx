import { useState, useEffect } from "react"
import { getProducts } from "../services/productService"
import { useNavigate } from "react-router-dom"
import { useCart } from "../context/CartContext"
import Logo from "../components/Logo.jsx"
import "../styles/SearchPage.css"

const SearchPage = () => {
    const navigate = useNavigate()
    const { addToCart, cartCount } = useCart()

    //fetch products from database
    const [allProducts, setAllProducts] = useState([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState(null)

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await getProducts()
                setAllProducts(data)
            } catch (err) {
                setError("Failed to load products")
            } finally {
                setLoading(false)
            }
        }
        fetchData()
    }, [])

    const [keyword, setKeyword] = useState("")
    const [filterPrice, setFilterPrice] = useState("none")
    const [filterStock, setFilterStock] = useState(false)
    const [filterLocation, setFilterLocation] = useState("")
    const [toast, setToast] = useState(null)

    const locations = [...new Set(allProducts.map(p => p.location))]

    const handleAdd = (product) => {
        addToCart(product)
        setToast(`${product.name} added to cart`)
        setTimeout(() => setToast(null), 2000)
    }

    const filtered = allProducts
        .filter(p => {
            if (!keyword.trim()) return true
            const kw = keyword.toLowerCase()
            return (
                p.name.toLowerCase().includes(kw) ||
                p.brand.toLowerCase().includes(kw) ||
                p.type.toLowerCase().includes(kw) ||
                p.tags.some(t => t.toLowerCase().includes(kw))
            )
        })
        .filter(p => !filterStock || p.stock > 0)
        .filter(p => !filterLocation || p.location === filterLocation)
        .sort((a, b) => {
            if (filterPrice === "low") return a.price - b.price
            if (filterPrice === "high") return b.price - a.price
            return 0
        })

    return (
        <div className="search-page">
            <header className="search-header">
                <div className="header-left">
                    <Logo light={true} />
                </div>
                <span className="header-tagline">Serving the Waterloo Region</span>
                <div className="header-right">
                    <button className="cart-btn" onClick={() => navigate("/cart")}>
                        🛒 Cart <span className="cart-count">{cartCount}</span>
                    </button>
                    <button className="profile-btn" onClick={() => navigate("/")}>
                        👤
                    </button>
                </div>
            </header>

            {toast && <div className="toast">✓ {toast}</div>}

            <div className="search-body">
                <input
                    className="search-bar"
                    type="text"
                    placeholder="Search products..."
                    value={keyword}
                    onChange={(e) => setKeyword(e.target.value)}
                />

                <div className="filters">
                    <select value={filterPrice} onChange={(e) => setFilterPrice(e.target.value)}>
                        <option value="none">Sort by Price</option>
                        <option value="low">Price: Low to High</option>
                        <option value="high">Price: High to Low</option>
                    </select>

                    <select value={filterLocation} onChange={(e) => setFilterLocation(e.target.value)}>
                        <option value="">All Locations</option>
                        {locations.map(loc => (
                            <option key={loc} value={loc}>{loc}</option>
                        ))}
                    </select>

                    <label className="stock-filter">
                        <input
                            type="checkbox"
                            checked={filterStock}
                            onChange={(e) => setFilterStock(e.target.checked)}
                        />
                        In Stock Only
                    </label>
                </div>

                {/* display skeleton product cards while loading */}
                {loading && (
                <div className="product-grid">
                    {[...Array(12)].map((_, i) => (
                        <div key={i} className="product-card skeleton">
                            <div className="skeleton-title"></div>
                            <div className="skeleton-line"></div>
                            <div className="skeleton-line"></div>
                        </div>
                    ))}
                </div>
                )}

                {/* display error message if product fetch failed */}
                {error && (
                <div className="error-banner">
                    <p>⚠️ {error}</p>
                    <button onClick={() => window.location.reload()}>
                        Try Again
                    </button>
                </div>
                )}

                {/* display products after done loading and no error */}
                {!loading && !error && (    
                    <>
                        <p className="result-count">{filtered.length} product(s) found</p>

                        <div className="product-grid">
                            {filtered.map(p => (
                                <div key={p.id} className="product-card">
                                    <h3>{p.name}</h3>
                                    <p className="price">${p.price.toFixed(2)}</p>
                                    <p className="meta">{p.type} | {p.brand}</p>
                                    <p className="meta">📍 {p.location}</p>
                                    <p className={p.stock > 0 ? "stock-badge" : "out-of-stock"}>
                                        {p.stock > 0 ? `In Stock (${p.stock})` : "Out of Stock"}
                                    </p>
                                    <button
                                        className="add-btn"
                                        disabled={p.stock === 0}
                                        onClick={() => handleAdd(p)}
                                    >
                                        {p.stock > 0 ? "Add to Cart" : "Unavailable"}
                                    </button>
                                </div>
                            ))}
                        </div>
                    </>
                )}
            </div>
        </div>
    )
}

export default SearchPage
