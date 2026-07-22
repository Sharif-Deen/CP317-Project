import { useState } from "react";
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"
import { useNavigate } from "react-router-dom"

const DistributorLoginPage = () => {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [showpassword, setShowPassword] = useState(false)
    const navigate = useNavigate()

    const handleLogin = () => {
        // TODO: add distributor auth logic
        navigate("/search")
    }

    return (
        <div className="login-page">
            <div className="login-card">
                <Logo />
                <p className="distributor-label">Distributor Portal</p>
                <InputField label="Business Email" type="email" placeholder="" onChange={(e) => setEmail(e.target.value)} value={email} />
                <div className="password-wrapper">
                    <div className="password-header">
                        <label>Password</label>
                        <Button className="show-btn" label={showpassword ? "Hide" : "Show"} onClick={() => setShowPassword(!showpassword)} type="button" />
                    </div>
                    <InputField type={showpassword ? "text" : "password"} placeholder="" onChange={(e) => setPassword(e.target.value)} value={password} />
                </div>
                <Button className="login-btn" onClick={handleLogin} label="Distributor Login" type="button" />
                <a href="#" onClick={(e) => { e.preventDefault(); navigate("/") }}>← Back to Customer Login</a>
            </div>
        </div>
    )
}

export default DistributorLoginPage
