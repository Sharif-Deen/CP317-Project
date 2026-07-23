import { useState } from "react";
import { useNavigate } from "react-router-dom"
import {FaEye, FaEyeSlash} from "react-icons/fa"
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"
import { login } from "../services/authService"

const LoginPage = ()=> {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [showpassword, setShowPassword] = useState(false)
    const navigate = useNavigate()

    const handleLogin = () => {
        const result = login(email, password)
        result.role==="admin"?navigate("/admin"):navigate("/search")
    }

    return(
    <div className="login-page">
        <div className="login-card">
            <Logo></Logo>
            <InputField type="text" placeholder="Username or Email" onChange={(e)=> setEmail(e.target.value)} value={email}></InputField>
            <div className="password-wrapper">
                <InputField type={showpassword ? "text" : "password"} placeholder="Password" onChange={(e)=> setPassword(e.target.value)} value={password}></InputField>
                
                <button className="show-btn" onClick={()=>setShowPassword(!showpassword)} onMouseDown={(e)=>e.preventDefault()} type="button">{showpassword?<FaEyeSlash size={18}/>:<FaEye size={18}/>}</button>
                
            </div>
            <Button className="login-btn" onClick={handleLogin} label="Login" type="button"></Button>
            <div>OR</div>
            <Button className="login-btn" onClick={()=>{}} label="Sign Up" type="button"></Button>
            <a href="#">Forgot Password?</a>
            <a href="#">Continue as Guest</a>
        </div>
    </div>
)}



export default LoginPage