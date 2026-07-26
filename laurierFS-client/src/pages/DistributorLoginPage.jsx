import { useState } from "react";
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"
import { useNavigate } from "react-router-dom"
// Notice: The broken import line is completely gone.

const DistributorLoginPage = () => {
    const [isSignUp, setIsSignUp] = useState(false)
    const [username, setUsername] = useState("")
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [showpassword, setShowPassword] = useState(false)
    const [authMessage, setAuthMessage] = useState("") 
    const navigate = useNavigate()
// disable for time being 
    const handleSubmit = async () => {
        //setAuthMessage("") 
        //try {
            // Decide which endpoint to hit based on the toggle
            //const endpoint = isSignUp ? "register" : "login";
            
            // Build the data object
            //const bodyData = isSignUp 
                //? { username, email, password, category: "distributor" }
                //: { username: email, email, password, category: "distributor" };

            // Fetch directly from here
            //const response = await fetch(`http://localhost:8080/api/${endpoint}`, {
            //    method: "POST",
            //    headers: { "Content-Type": "application/json" },
            //    body: JSON.stringify(bodyData)
            //});

            //const data = await response.json();

            //if (!response.ok || data.status === "error") {
            //    throw new Error(data.message || "Authentication failed");
            //}

            //if (isSignUp) {
            //    setAuthMessage("Account created successfully! Please log in.")
            //    setIsSignUp(false) // switch back to login screen
            //} else {
            navigate("/distributor-dashboard")
            //}
        //} catch (err) {
            //setAuthMessage(err.message)
        //}
    }

    return (
        <div className="login-page">
            <div className="login-card">
                <Logo />
                <p className="distributor-label">
                    Distributor Portal {isSignUp ? "- Sign Up" : "- Login"}
                </p>
                
                {isSignUp && (
                    <InputField 
                        label="Username" 
                        type="text" 
                        placeholder="Choose a username" 
                        onChange={(e) => setUsername(e.target.value)} 
                        value={username} 
                    />
                )}

                <InputField 
                    label="Business Email" 
                    type="email" 
                    placeholder="" 
                    onChange={(e) => setEmail(e.target.value)} 
                    value={email} 
                />
                
                <div className="password-wrapper">
                    <div className="password-header">
                        <label>Password</label>
                        <Button 
                            className="show-btn" 
                            label={showpassword ? "Hide" : "Show"} 
                            onClick={() => setShowPassword(!showpassword)} 
                            type="button" 
                        />
                    </div>
                    <InputField 
                        type={showpassword ? "text" : "password"} 
                        placeholder="" 
                        onChange={(e) => setPassword(e.target.value)} 
                        value={password} 
                    />
                </div>
                
                {authMessage && (
                    <p className={authMessage.includes("success") ? "success-message" : "login-error"}>
                        {authMessage}
                    </p>
                )}
                
                <Button 
                    className="login-btn" 
                    onClick={handleSubmit} 
                    label={isSignUp ? "Create Account" : "Distributor Login"} 
                    type="button" 
                />
                
                <p className="toggle-signup">
                    {isSignUp ? "Already have an account? " : "Don't have an account? "}
                    <a href="#" onClick={(e) => { e.preventDefault(); setIsSignUp(!isSignUp); }}>
                        {isSignUp ? "Log In" : "Sign Up"}
                    </a>
                </p>

                <br/>
                <a href="#" onClick={(e) => { e.preventDefault(); navigate("/") }}>← Back to Customer Login</a>
            </div>
        </div>
    )
}

export default DistributorLoginPage