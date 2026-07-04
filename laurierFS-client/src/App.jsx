import LoginPage from "./pages/LoginPage"
import SearchPage from "./pages/SearchPage"
import {BrowserRouter, Route, Routes} from "react-router-dom"
import CartPage from "./pages/CartPage"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={<div>Home Page Coming Soon</div>} />
        <Route path="/search" element={<SearchPage />} />
        <Route path="/admin" element={<div>Admin Page Coming Soon</div>} />
        <Route path="/cart" element={<CartPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App