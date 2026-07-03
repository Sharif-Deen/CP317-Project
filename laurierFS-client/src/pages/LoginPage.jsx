import { useState } from "react";
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"


const LoginPage = ()=> {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [showpassword, setShowPassword] = useState(false)

    return(
    <div className="login-page">
        <div className="login-card">
            <Logo></Logo>
            <InputField label="Email" type="email" placeholder="" onChange={(e)=> setEmail(e.target.value)} value={email}></InputField>
            <div className="password-wrapper">
                <div className="password-header">
                    <label>Password</label>
                    <Button className="show-btn" label={showpassword?"Hide":"Show"} onClick={()=>setShowPassword(!showpassword)} type="button"></Button>
                </div>
                <InputField type={showpassword ? "text" : "password"} placeholder="" onChange={(e)=> setPassword(e.target.value)} value={password}></InputField>
            </div>
            <Button className="login-btn" onClick={()=>{}} label="Login" type="button"></Button>
            <Button className="login-btn" onClick={()=>{}} label="Sign Up" type="button"></Button>
            <a href="#">Forgot Password?</a>
            <a href="#">Continue as Guest</a>
            <a href="#">Distributor Login Page</a>
        </div>
    </div>
)}

export default LoginPage