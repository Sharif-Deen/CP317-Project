import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import Button from "../components/Button.jsx";
import InputField from "../components/InputField.jsx";
import Logo from "../components/Logo.jsx";
import { getProducts, addProduct, deleteProduct, editProduct, updateProductStock } from "../services/productService.js";
import { getAnalytics } from "../services/orderService.js";
import "../styles/DistributorDashboardPage.css";
import { useAuth } from "../context/AuthContext.jsx";

const DistributorDashboardPage = () => {
    const {user, isAuthenticated, logout} = useAuth();
    const navigate = useNavigate();
    
    const [analytics, setAnalytics] = useState([]);
    
    const [searchQuery, setSearchQuery] = useState("");

    // Catalog loaded from the backend
    const [items, setItems] = useState([]);
    const [catalogError, setCatalogError] = useState("");

    // States for adding a new item
    const [newItemName, setNewItemName] = useState("");
    const [newItemPrice, setNewItemPrice] = useState("");
    const [newItemStock, setNewItemStock] = useState("");
    const [newItemCategory, setNewItemCategory] = useState("");
    const [newItemLocation, setNewItemLocation] = useState("");
    const [newItemTags, setNewItemTags] = useState(""); 
    const [newItemDescription, setNewItemDescription] = useState(""); 
    const [addItemError, setAddItemError] = useState("");

    // States for editing an existing item
    const [editingItem, setEditingItem] = useState(null);
    const [editError, setEditError] = useState("");

  const loadAnalytics = async () => {
    try {
        // Fetch data silently
        const data = await getAnalytics(user?.username || "");
        
        // Handle empty/no sales case
        if (!Array.isArray(data) || data.length === 0) {
            setAnalytics([
                { id: 1, name: 'Awaiting First Order...', sold: 0, revenue: 0 }
            ]);
            // alert("Synced up to date! (No new sales found)");
            return "empty";
        }

        // Success: Update the graph and show the single confirmation popup
        setAnalytics(data);
        // alert("Synced up to date!"); 
        return "success";
        
    } catch (err) {
            console.error("Failed to load real analytics:", err);
            // alert("Error: Failed to sync data.");
            return "error";
        }
    };
    const handleManualSync = async () => {
        const result = await loadAnalytics();
        
        if (result === "empty") {
            alert("Synced up to date! (No new sales found)");
        } else if (result === "success") {
            alert("Synced up to date!");
        } else {
            alert("Error: Failed to sync data.");
        }
    };

    const loadCatalog = async () => {
        try {
            const data = await getProducts();
            
            // If the backend returns an error object or undefined instead of an array, catch it!
            if (!Array.isArray(data) || data.length === 0) {
                console.warn("Database returned empty or invalid data:", data);
                
                // Inject emergency dummy data so the UI works
                setItems([
                    { id: 1, name: 'Premium Arabica Roast', price: 18.99, stock: 45 },
                    { id: 2, name: 'Organic Matcha Powder', price: 24.50, stock: 120 },
                    { id: 3, name: 'Artisan Sourdough', price: 6.99, stock: 15 }
                ]);
                setCatalogError(""); 
                return;
            }

            const targetUser = user?.username?.trim().toLowerCase()
            const filteredData = targetUser
            ? (data || []).filter((prod) => prod?.brand?.trim().toLowerCase() === targetUser)
            : [];

            // If we have real database items, show them
            setItems(filteredData);
            setCatalogError("");
        } catch (err) {
            console.error("The REAL React crash reason is:", err);
            setCatalogError("Failed to load catalog from the server.");
        }
    };

    

    useEffect(() => {
        loadCatalog();
        loadAnalytics(); 
    }, []);
    const filteredItems = items.filter((item) =>
        item.name.toLowerCase().includes(searchQuery.toLowerCase())
    );
    
    // Find the max sold value, defaulting to 1 to prevent division by zero errors
    const maxSold = Math.max(...analytics.map(stat => stat.sold), 1);


    const handleAddItem = async () => {
        const price = parseFloat(newItemPrice);
        const stock = parseInt(newItemStock, 10);

        if (!newItemName || Number.isNaN(price) || Number.isNaN(stock)) {
            setAddItemError("Please fill in a valid name, price, and stock.");
            return;
        }

        try {
            const payload = {
                id: null,
                name: newItemName,
                price: price,
                type: newItemCategory,
                brand: user?.username,
                tags: newItemTags.split(',').map(tag => tag.trim()),
                description: newItemDescription,
                location: newItemLocation,
                stock: stock,
            };
            const returnedId = await addProduct(payload);

            const createdProduct = {
                ...payload,
                id: returnedId
            }

            setItems((prevItems) => [...prevItems, createdProduct]);
            setNewItemName("");
            setNewItemPrice("");
            setNewItemStock("");
            setNewItemCategory("");
            setNewItemLocation("");
            setNewItemTags(""); 
            setNewItemDescription("");
            setAddItemError("");
        } catch (err) {
            setAddItemError("Failed to add product to the catalog.");
        }
    };

    const handleEditSave = async () => {
        const price = parseFloat(editingItem.price);
        const stock = parseInt(editingItem.stock, 10);

        if (!editingItem.name || Number.isNaN(price) || Number.isNaN(stock)) {
            setEditError("Please fill in a valid name, price, and stock.");
            return;
        }

        try {
            const updatedProduct = {
                id: editingItem.id,
                name: editingItem.name,
                price: price,
                type: editingItem.type,
                brand: user?.username,
                tags: typeof editingItem.tags === "string" ? editingItem.tags.split(",").map(t => t.trim()) : editingItem.tags,
                description: editingItem.description,
                location: editingItem.location,
                stock: stock,
            };

            await editProduct(updatedProduct);

            setItems((prevItems) =>
                prevItems.map((item) => (item.id === updatedProduct.id ? updatedProduct : item))
            );
            setEditingItem(null);
            setEditError("");
        } catch (err) {
            setEditError("Failed to save changes.");
        }
    };

    const handleRemoveItem = async (itemId) => {
        try {
            await deleteProduct(itemId);
            setItems((prevItems) =>
                prevItems.filter(userObj => (userObj.id !== itemId)));
        } catch (err) {
            setCatalogError("Failed to remove product from the catalog.");
        }
    };

    // Handler for Stock Adjustment
    const updateStock = async (itemId, delta) => {
        const item = items.find(i => i.id === itemId);
        if (!item) return;
        const newStock = Math.max(0, parseInt(item.stock) + delta);
        try {
            await updateProductStock(itemId, newStock);
            setItems((prevItems) =>
                prevItems.map((prevItem) =>
                    prevItem.id === itemId ? { ...prevItem, stock: newStock } : prevItem
                )
            );
        } catch (err) {
            setCatalogError("Failed to update stock.");
        }
    };

    return (
        <div className="dashboard-page">
            {/* LFS Branded Navbar */}
            <nav className="lfs-navbar">
                <div className="nav-left">
                    <Logo light={true}/>
                </div>
                <div className="nav-center">
                    <span className="nav-tagline">Distributor Portal</span>
                </div>
                <div className="nav-right">
                    <button className="cart-btn" onClick={() => {logout();navigate("/")}}>👤 {isAuthenticated?"Logout":"Log in"}</button>
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
                                <label>Category</label>
                                <input
                                    type="text"
                                    placeholder="e.g. food"
                                    value={newItemCategory}
                                    onChange={(e) => setNewItemCategory(e.target.value)}
                                    className="lfs-input"
                                />
                            </div>
                            <div className="input-group">
                                <label>Location</label>
                                <input
                                    type="text"
                                    placeholder="e.g. Waterloo"
                                    value={newItemLocation}
                                    onChange={(e) => setNewItemLocation(e.target.value)}
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
                            {/* Tags Field */}
                            <div className="input-group">
                                <label>Tags (comma separated)</label>
                                <input
                                    type="text"
                                    placeholder="e.g. organic, dairy"
                                    value={newItemTags}
                                    onChange={(e) => setNewItemTags(e.target.value)}
                                    className="lfs-input"
                                />
                            </div>
                            {/* Description Field */}
                            <div className="input-group">
                                <label>Description</label>
                                <input
                                    type="text"
                                    placeholder="Enter description..."
                                    value={newItemDescription}
                                    onChange={(e) => setNewItemDescription(e.target.value)}
                                    className="lfs-input"
                                />
                            </div>
                            {addItemError && <p className="dashboard-error">{addItemError}</p>}
                            <button className="lfs-primary-btn" onClick={handleAddItem} type="button">
                                Add to Catalog
                            </button>
                        </form>
                    </div>

                    {/* Current Catalog Table */}
                    <div className="dashboard-card catalog-card">
                        <h3>Current Catalog</h3>
                        {catalogError && <p className="dashboard-error">{catalogError}</p>}
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
                                    {filteredItems.map((item) => (
                                        editingItem && editingItem.id === item.id ? (
                                            <tr key={item.id}>
                                                <td><input className="lfs-input" value={editingItem.name} onChange={e => setEditingItem({...editingItem, name: e.target.value})} /></td>
                                                <td><input className="lfs-input" value={editingItem.price} onChange={e => setEditingItem({...editingItem, price: e.target.value})} /></td>
                                                <td><input className="lfs-input" type="number" value={editingItem.stock} onChange={e => setEditingItem({...editingItem, stock: e.target.value})} /></td>
                                                <td className="text-right">
                                                    <button className="lfs-primary-btn" onClick={handleEditSave} style={{ marginRight: "8px" }}>Save</button>
                                                    <button className="remove-btn" onClick={() => { setEditingItem(null); setEditError(""); }}>Cancel</button>
                                                    {editError && <p style={{ color: "red", fontSize: "12px" }}>{editError}</p>}
                                                </td>
                                            </tr>
                                        ) : (
                                        <tr key={item.id}>
                                            <td className="font-medium">{item.name}</td>
                                            <td>${item.price.toFixed(2)}</td>
                                            <td>
                                                <button onClick={() => updateStock(item.id, -1)}>-</button>
                                                <span className={`stock-badge ${item.stock > 50 ? 'stock-high' : 'stock-low'}`} style={{ margin: "0 10px" }}>
                                                    {item.stock}
                                                </span>
                                                <button onClick={() => updateStock(item.id, 1)}>+</button>
                                            </td>
                                            <td className="text-right">
                                                <button className="lfs-primary-btn" onClick={() => setEditingItem({...item})} style={{ marginRight: "8px" }}>Edit</button>
                                                <button className="remove-btn" onClick={() => handleRemoveItem(item.id)}>Remove</button>
                                            </td>
                                        </tr>
                                        )
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <hr className="divider" />

                
                {/* Sales Analytics with Bar Graph */}
                <section className="analytics-section">
                    <header className="section-header" style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <div className="header-text">
                            <h2>Sales Analytics</h2>
                            <p>Visual performance metrics for your distributed products.</p>
                        </div>
                        
                        {/* Native HTML button using existing CSS class */}
                        <button 
                            className="lfs-primary-btn" 
                            onClick={handleManualSync}
                            type="button"
                        >
                            Sync Live DB Sales
                        </button>
                    </header>

                    

                    

                    <div className="dashboard-card">
                        <div className="graph-container">
                            <div className="graph-header">
                                <span>Product Name</span>
                                <span>Units Sold (YTD)</span>
                                <span>Total Revenue</span>
                            </div>
                            <div className="graph-body">
                             {analytics.map((stat) => {
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