import LoginPage from "./pages/LoginPage"
import SearchPage from "./pages/SearchPage"
import CartPage from "./pages/CartPage"
import CheckoutPage from "./pages/CheckoutPage"
import OrdersPage from "./pages/OrdersPage"
import DistributorLoginPage from "./pages/DistributorLoginPage"
import { BrowserRouter, Route, Routes } from "react-router-dom"
import { CartProvider } from "./context/CartContext"
import { OrderProvider } from "./context/OrderContext"

function App() {
  return (
    <OrderProvider>
      <CartProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/home" element={<SearchPage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/cart" element={<CartPage />} />
            <Route path="/checkout" element={<CheckoutPage />} />
            <Route path="/orders" element={<OrdersPage />} />
            <Route path="/distributor-login" element={<DistributorLoginPage />} />
            <Route path="/admin" element={<div>Admin Page Coming Soon</div>} />
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </OrderProvider>
  )
}

export default App
