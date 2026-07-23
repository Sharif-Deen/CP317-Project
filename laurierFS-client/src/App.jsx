import LoginPage from "./pages/LoginPage"
import SearchPage from "./pages/SearchPage"
import CartPage from "./pages/CartPage"
import CheckoutPage from "./pages/CheckoutPage"
import OrdersPage from "./pages/OrdersPage"
import DistributorLoginPage from "./pages/DistributorLoginPage"
import DistributorDashboardPage from './pages/DistributorDashboardPage';
import { BrowserRouter, Route, Routes } from "react-router-dom"
import { CartProvider } from "./context/CartContext"
import { OrderProvider } from "./context/OrderContext"
import ProductReturnsPage from "./pages/ProductReturnsPage";

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
            <Route path="/returns" element={<ProductReturnsPage />} />
            <Route path="/distributor-login" element={<DistributorLoginPage />} />
            <Route path="/distributor-dashboard" element={<DistributorDashboardPage />} />
            
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </OrderProvider>
  )
}

export default App
