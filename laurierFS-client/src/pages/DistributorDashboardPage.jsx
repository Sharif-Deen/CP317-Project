import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../components/Button.jsx";
import InputField from "../components/InputField.jsx";
import Logo from "../components/Logo.jsx";
import "../styles/DistributorDashboardPage.css";

const DistributorDashboardPage = () => {
    const navigate = useNavigate();
    const [searchQuery, setSearchQuery] = useState("");
    
    // States for adding a new item
    const [newItemName, setNewItemName] = useState("");
    const [newItemPrice, setNewItemPrice] = useState("");
    const [newItemStock, setNewItemStock] = useState("");

    // Mock data for the UI
    const mockItems = [
        { id: 1, name: 'Premium Coffee Beans', price: '$12.99', stock: 150 },
        { id: 2, name: 'Organic Green Tea', price: '$9.49', stock: 85 },
        { id: 3, name: 'Raw Honey Extract', price: '$15.00', stock: 40 },
    ];

    const mockAnalytics = [
        { id: 1, name: 'Premium Coffee Beans', sold: 342, revenue: '$4,442.58' },
        { id: 2, name: 'Organic Green Tea', sold: 128, revenue: '$1,214.72' },
        { id: 3, name: 'Raw Honey Extract', sold: 89, revenue: '$1,335.00' },
        { id: 4, name: 'Artisan Bread', sold: 210, revenue: '$1,050.00' },
    ];

    // Find the max sold value to scale the graph bars relatively
    const maxSold = Math.max(...mockAnalytics.map(stat => stat.sold));

    const handleLogout = () => {
        navigate("/distributor-login");
    };

    const handleAddItem = () => {
        console.log("Adding item:", newItemName, newItemPrice, newItemStock);
        // Backend logic goes here later
    };

    return (
        <div className="dashboard-page">
            {/* LFS Branded Navbar */}
            <nav className="lfs-navbar">
                <div className="nav-left">
                    <Logo />
                </div>
                <div className="nav-center">
                    <span className="nav-tagline">Distributor Portal</span>
                </div>
                <div className="nav-right">
                    <button className="lfs-outline-btn" onClick={handleLogout}>
                        Logout
                    </button>
                </div>
            </nav>

            <main className="dashboard-main">
                {/* Inventory & Search Header */}
                <header className="section-header">
                    <div className="header-text">
                        <h2>Inventory Management</h2>
                        <p>Search, add, or remove items from your catalog.</p>
                    </div>
                    <div className="search-bar-container">
                        <input 
                            type="text" 
                            placeholder="Search products..." 
                            value={searchQuery}
                            onChange={(e) => setSearchQuery(e.target.value)}
                            className="lfs-search-input"
                        />
                    </div>
                </header>

                <div className="dashboard-grid">
                    {/* Add Item Form */}
                    <div className="dashboard-card add-item-card">
                        <h3>Add New Item</h3>
                        <form className="add-item-form">
                            <div className="input-group">
                                <label>Product Name</label>
                                <input 
                                    type="text" 
                                    placeholder="e.g. Arabica Roast" 
                                    value={newItemName}
                                    onChange={(e) => setNewItemName(e.target.value)} 
                                    className="lfs-input"
                                />
                            </div>
                            <div className="input-group">
                                <label>Price</label>
                                <input 
                                    type="text" 
                                    placeholder="$0.00" 
                                    value={newItemPrice}
                                    onChange={(e) => setNewItemPrice(e.target.value)} 
                                    className="lfs-input"
                                />
                            </div>
                            <div className="input-group">
                                <label>Initial Stock</label>
                                <input 
                                    type="number" 
                                    placeholder="0" 
                                    value={newItemStock}
                                    onChange={(e) => setNewItemStock(e.target.value)} 
                                    className="lfs-input"
                                />
                            </div>
                            <button className="lfs-primary-btn" onClick={handleAddItem} type="button">
                                Add to Catalog
                            </button>
                        </form>
                    </div>

                    {/* Current Catalog Table */}
                    <div className="dashboard-card catalog-card">
                        <h3>Current Catalog</h3>
                        <div className="table-container">
                            <table className="data-table">
                                <thead>
                                    <tr>
                                        <th>Item Name</th>
                                        <th>Price</th>
                                        <th>Stock</th>
                                        <th className="text-right">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {mockItems.map((item) => (
                                        <tr key={item.id}>
                                            <td className="font-medium">{item.name}</td>
                                            <td>{item.price}</td>
                                            <td>
                                                <span className={`stock-badge ${item.stock > 50 ? 'stock-high' : 'stock-low'}`}>
                                                    {item.stock} units
                                                </span>
                                            </td>
                                            <td className="text-right">
                                                <button className="remove-btn">Remove</button>
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <hr className="divider" />

                {/* Sales Analytics with Bar Graph */}
                <section className="analytics-section">
                    <header className="section-header">
                        <div className="header-text">
                            <h2>Sales Analytics</h2>
                            <p>Visual performance metrics for your distributed products.</p>
                        </div>
                    </header>

                    <div className="dashboard-card">
                        <div className="graph-container">
                            <div className="graph-header">
                                <span>Product Name</span>
                                <span>Units Sold (YTD)</span>
                                <span>Total Revenue</span>
                            </div>
                            <div className="graph-body">
                                {mockAnalytics.map((stat) => {
                                    const barWidth = `${(stat.sold / maxSold) * 100}%`;
                                    return (
                                        <div className="graph-row" key={stat.id}>
                                            <div className="graph-label">
                                                <span className="font-medium">{stat.name}</span>
                                            </div>
                                            <div className="graph-bar-wrapper">
                                                <div className="graph-bar" style={{ width: barWidth }}>
                                                    <span className="graph-bar-text">{stat.sold}</span>
                                                </div>
                                            </div>
                                            <div className="graph-revenue-cell">
                                                <span className="graph-revenue">{stat.revenue}</span>
                                            </div>
                                        </div>
                                    );
                                })}
                            </div>
                        </div>
                    </div>
                </section>
            </main>
        </div>
    );
};

export default DistributorDashboardPage;