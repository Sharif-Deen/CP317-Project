import { useState } from "react"
import { getProducts } from "../services/productService"
import { useNavigate } from "react-router-dom"
import { useCart } from "../context/CartContext"
import "../styles/SearchPage.css"
import Logo from "../components/Logo"

const SearchPage = () => {
    const allProducts = getProducts()
    const [keyword, setKeyword] = useState("")
    const [filterPrice, setFilterPrice] = useState("none")
    const [filterStock, setFilterStock] = useState(false)
    const [filterLocation, setFilterLocation] = useState("")

    const locations = [...new Set(allProducts.map(p => p.location))]

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
    const navigate = useNavigate()
    const { totalItems } = useCart()

    return (
        <div className="search-page">
            <div className="search-header">
    <Logo />
    <span className="tagline">Serving the Waterloo Region</span>
    <div className="header-right">
        <button className="cart-btn" onClick={() => navigate("/cart")}>
            🛒 <span className="cart-count">{totalItems}</span>
        </button>
        <button className="logout-btn" onClick={() => navigate("/")}>Logout</button>
    </div>
</div>
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

                <p className="result-count">{filtered.length} product(s) found</p>

                
                </div>
            </div>
        
    )
}

export default SearchPage