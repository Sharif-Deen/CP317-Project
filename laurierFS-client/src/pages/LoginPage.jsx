import { useState } from "react";
import { useNavigate } from "react-router-dom"
import {FaEye, FaEyeSlash} from "react-icons/fa"
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"
import { login, signup } from "../services/authService"
import { useAuth } from "../context/AuthContext.jsx"

const ROLE_ROUTES = {
    distributor: "/distributor-dashboard",
    customer: "/search",
}


const LoginPage = ()=> {
    const [showpassword, setShowPassword] = useState(false)
    const [isSignUp, setIsSignUp] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState("")
    const [formData, setFormData] = useState({
        identifier: '',
        username: '',
        email: '',
        password: '',
        confirmPassword: ''
    })
    const navigate = useNavigate()
    const { setLoggedInUser } = useAuth()

    const handleChange = (e) =>{
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        })
    }
    const handleSubmit = async (e) => {
        if (e) e.preventDefault();
        setError("")

        if (isSignUp && formData.password !== formData.confirmPassword) {
            setError("Passwords do not match")
            return
        }
        try {
            const result = isSignUp
                ? await signup({
                    username: formData.username,
                    email: formData.email,
                    password: formData.password,
                })
                : await login({
                    identifier: formData.identifier,
                    password: formData.password,
                })
 
            // Persist who's logged in via context (which itself syncs to
            // localStorage, so it survives refreshes/new tabs).
            setLoggedInUser(result)
 
            const destination = ROLE_ROUTES[result.role] ?? "/"
            navigate(destination)
        } catch (err) {
            setError(err?.message || "Something went wrong. Please try again.")
        } finally {
            setIsSubmitting(false)
        }

    }
    const toggleAuth = (e) =>{
        e.preventDefault()
        setIsSignUp(!isSignUp)
        setError("")
        setFormData({
            identifier: '',
            username: '',
            email: '',
            password: '',
            confirmPassword: ''
        })
    }

    return(
    <div className="login-page">
        <div className="login-card">
            <Logo></Logo>
            <form onSubmit={handleSubmit}>
                {/* Username + email if signing up OR Username/Email if logging in */}
                {isSignUp? 
                    (<>
                        <InputField name="username" type="text" placeholder="Username" onChange={handleChange} value={formData.username}/>
                        <InputField name="email" type="email" placeholder="Email" onChange={handleChange} value={formData.email}/>
                    </>)
                :   (<>
                        <InputField name="identifier" type="text" placeholder="Username or Email" onChange={handleChange} value={formData.identifier}/>
                    </>)
                }

                {/* Password field */}
                <div className="password-wrapper">
                    <InputField name="password" type={showpassword ? "text" : "password"} placeholder="Password" onChange={handleChange} value={formData.password}/>
                    <button className="show-btn" onClick={()=>setShowPassword(!showpassword)} onMouseDown={(e)=>e.preventDefault()} type="button">{showpassword?<FaEyeSlash size={18}/>:<FaEye size={18}/>}</button>
                </div>

                {/* Confirm password (if signup) */}
                {isSignUp && 
                    <div className="password-wrapper">
                        <InputField name="confirmPassword" type={showpassword ? "text" : "password"} placeholder="Confirm Password" onChange={handleChange} value={formData.confirmPassword}/>
                        <button className="show-btn" onClick={()=>setShowPassword(!showpassword)} onMouseDown={(e)=>e.preventDefault()} type="button">{showpassword?<FaEyeSlash size={18}/>:<FaEye size={18}/>}</button>
                    </div>
                }
                {error && <div className="auth-error">{error}</div>}

                {/* Submit button */}
                <Button className="login-btn" onClick={handleSubmit} label={isSignUp?"Sign Up":"Login"} type="submit"></Button>

            </form>                
                
            
            <div>
                {isSignUp?"Already have an account? ":"Don't have an account? "} 
                <a href="#" onClick={toggleAuth}>{isSignUp?"Login":"Sign Up"}</a>
            </div>
            {!isSignUp && <a href="#">Forgot Password?</a>}
            <a href="#">Continue as Guest</a>
            {/* <a href="#" onClick={(e) => { e.preventDefault(); navigate("/distributor-login") }}>Distributor Login Page</a> */}
        </div>
    </div>
)}



export default LoginPage