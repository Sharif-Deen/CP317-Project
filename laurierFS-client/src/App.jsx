import LoginPage from "./pages/LoginPage"
import CheckoutPage from "./pages/CheckoutPage"
import {BrowserRouter, Route, Routes} from "react-router-dom"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/home" element={<div>Home Page Coming Soon</div>} />
        <Route path="/admin" element={<div>Admin Page Coming Soon</div>} />
        <Route path="/checkout" element={<CheckoutPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App