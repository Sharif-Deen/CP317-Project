import LoginPage from "./pages/LoginPage"
import SearchPage from "./pages/SearchPage"
import CartPage from "./pages/CartPage"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import { CartProvider } from "./context/CartContext"

function App() {
  return (
    <CartProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginPage />} />
          <Route path="/home" element={<SearchPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/cart" element={<CartPage />} />
          <Route path="/admin" element={<div>Admin Page Coming Soon</div>} />
        </Routes>
      </BrowserRouter>
    </CartProvider>
  )
}

export default App
