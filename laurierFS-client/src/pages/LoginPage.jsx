import { useState } from "react";
import { useNavigate, Navigate} from "react-router-dom"
import {FaEye, FaEyeSlash, FaCheckCircle} from "react-icons/fa"
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"
import "../styles/LoginPage.css"
import { login, signup } from "../services/authService"
import { useAuth } from "../context/AuthContext.jsx"
import { useOrders } from "../context/OrderContext.jsx";

const CATEGORY_ROUTES = {
    distributor: "/distributor-dashboard",
    customer: "/search",
}
const EMPTY_FORM = {
    identifier: '',
    username: '',
    email: '',
    password: '',
    confirmPassword: '',
    category: 'customer'
}


const LoginPage = ()=> {
    const [showpassword, setShowPassword] = useState(false)
    const [isSignUp, setIsSignUp] = useState(false)
    const [isSubmitting, setIsSubmitting] = useState(false)
    const [error, setError] = useState("")
    const [fieldErrors, setFieldErrors] = useState({}) 
    const [signupSuccess, setSignupSuccess] = useState(false) 
    const [formData, setFormData] = useState(EMPTY_FORM)
    const navigate = useNavigate()
    const { user, isAuthenticated, setLoggedInUser } = useAuth()
    const { loadOrders } = useOrders()
    

    const handleChange = (e) =>{
        const { name, value } = e.target


        setFormData({
            ...formData,
            [name]: value
        })

        // Clear that field's inline error as soon as the user edits it
        if (fieldErrors[name]) {
            setFieldErrors({
                ...fieldErrors,
                [name]: ''
            })
        }

    }

    // sets formData.category from the toggle buttons (not a plain
    // input, so it doesn't go through handleChange)
    const handleCategoryChange = (category) => {
        setFormData({
            ...formData,
            category
        })
    }

    // Frontend validation for the login form — only checks that the
    // identifier field (username or email) has no spaces
    const validateLogin = () => {
        const errors = {}
        if (!formData.identifier){
            errors.identifier = "Username or Email required"
        }
        else if (/\s/.test(formData.identifier)) {
            errors.identifier = "Username or email cannot contain spaces"
        }
        if (!formData.password){
            errors.password = "Password is required"
        }
        return errors
    }
    const validateSignup = () => {
        const errors = {}
        const usernamePattern = /^[A-Za-z0-9\-_.]+$/
 
        if (!formData.username) {
            errors.username = "Username is required"
        } else if (!usernamePattern.test(formData.username)) {
            errors.username = "Only letters, numbers, and - _ . are allowed (no spaces)"
        }
        // 1+ chars before @, exactly one @, then chars, exactly
        // one '.' after @ (dots are fine before @), with chars on both
        // sides of that '.'
        const emailPattern = /^[^\s@]+@[^\s@.]+\.[^\s@.]+$/
        if (!formData.email) {
            errors.email = "Email is required"
        } else if (!emailPattern.test(formData.email)) {
            errors.email = "Please enter a valid email address"
        }
        if (!formData.password){
            errors.password = "Password is required"
        } else if (formData.password.length > 100) {
            errors.password = "Password must be 100 characters or fewer"
        }
        if (formData.password !== formData.confirmPassword) {
            errors.confirmPassword = "Passwords do not match"
        }
        return errors
    }


    const handleSubmit = async (e) => {
        if (e) e.preventDefault();
        setError("")
        setFieldErrors({})

        const errors = isSignUp ? validateSignup() : validateLogin()
        if (Object.keys(errors).length > 0) {
            setFieldErrors(errors)
            return
        }

        try {
            if (isSignUp){
                const result = await signup({
                    username: formData.username,
                    email: formData.email,
                    password: formData.password,
                    category: formData.category

                })
                setSignupSuccess(true)
            } else{
                const result = await login({
                    identifier: formData.identifier,
                    password: formData.password,
                })
                // Persist who's logged in via context (which itself syncs to
                // localStorage, so it survives refreshes/new tabs).
                setLoggedInUser(result)
                const destination = CATEGORY_ROUTES[result.category] ?? "/"
                navigate(destination)
            }            
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
        setFieldErrors({})
        setFormData(EMPTY_FORM)
    }

    // Reset the form and return to login page after successful signup
    const handleBackToLogin = (e) => {
        if (e) e.preventDefault()
        setSignupSuccess(false)
        setIsSignUp(false)
        setError("")
        setFieldErrors({})
        setFormData(EMPTY_FORM)
    }

    // if AuthContext already restored a logged-in user from
    // localStorage, skip the login form entirely and go straight to their page.
    if (isAuthenticated) {
        const destination = CATEGORY_ROUTES[user.category] ?? "/"
        return <Navigate to={destination} replace />
    }

    // Confirmation screen after signup, with a button to go back to login
    if (signupSuccess) {
        return (
            <div className="login-page">
                <div className="login-card">
                    <Logo></Logo>
                    <div className="auth-success">
                        <FaCheckCircle className="auth-success-icon" size={48}/>
                        <h2>Account created successfully!</h2>
                        <p>You can now log in with your new account.</p>
                    </div>
                    <Button className="login-btn" onClick={handleBackToLogin} label="Login" type="button"></Button>
                </div>
            </div>
        )
    }


    return(
    <div className="login-page">
        <div className="login-card">
            <Logo></Logo>
            <form onSubmit={handleSubmit}>
                {/* Username + email if signing up OR Username/Email if logging in */}
                {isSignUp? 
                    (<>
                        <div className="form-field">
                            <InputField name="username" type="text" placeholder="Username" onChange={handleChange} value={formData.username} className={fieldErrors.username ? 'has-error' : ''}/>
                            {fieldErrors.username && <span className="field-error-text">{fieldErrors.username}</span>}
                        </div>
                        <div className="form-field">
                            <InputField name="email" type="email" placeholder="Email" onChange={handleChange} value={formData.email} className={fieldErrors.email ? 'has-error' : ''}/>
                            {fieldErrors.email && <span className="field-error-text">{fieldErrors.email}</span>}
                        </div>

                    </>)
                :   (<>
                        <div className="form-field">
                            <InputField name="identifier" type="text" placeholder="Username or Email" onChange={handleChange} value={formData.identifier} className={fieldErrors.identifier ? 'has-error' : ''}/>
                            {fieldErrors.identifier && <span className="field-error-text">{fieldErrors.identifier}</span>}
                        </div>
                    </>)
                }

                {/* Password field */}
                <div>
                    <div className="password-wrapper">
                        <InputField name="password" type={showpassword ? "text" : "password"} placeholder="Password" onChange={handleChange} value={formData.password} maxLength={100} className={fieldErrors.password ? 'has-error' : ''}/>
                        <button className="show-btn" onClick={()=>setShowPassword(!showpassword)} onMouseDown={(e)=>e.preventDefault()} type="button">{showpassword?<FaEyeSlash size={18}/>:<FaEye size={18}/>}</button>
                    </div>
                    {fieldErrors.password && <span className="field-error-text">{fieldErrors.password}</span>}
                </div>


                {/* Confirm password (if signup) */}
                {isSignUp && 
                    <div>
                        <div className="password-wrapper">
                            <InputField name="confirmPassword" type={showpassword ? "text" : "password"} placeholder="Confirm Password" onChange={handleChange} value={formData.confirmPassword} maxLength={100} className={fieldErrors.confirmPassword ? 'has-error' : ''}/>
                            <button className="show-btn" onClick={()=>setShowPassword(!showpassword)} onMouseDown={(e)=>e.preventDefault()} type="button">{showpassword?<FaEyeSlash size={18}/>:<FaEye size={18}/>}</button>
                        </div>
                        {fieldErrors.confirmPassword && <span className="field-error-text">{fieldErrors.confirmPassword}</span>}
                    </div>
                }

                {/* Customer/Distributor toggle, signup only, sits above the submit button */}
                {isSignUp &&
                    <div className="category-toggle" role="group" aria-label="Account type">
                        <button
                            type="button"
                            className={`category-option ${formData.category === 'customer' ? 'active' : ''}`}
                            onClick={() => handleCategoryChange('customer')}
                        >
                            Customer
                        </button>
                        <button
                            type="button"
                            className={`category-option ${formData.category === 'distributor' ? 'active' : ''}`}
                            onClick={() => handleCategoryChange('distributor')}
                        >
                            Distributor
                        </button>
                    </div>
                }

                {error && <div className="login-error">{error}</div>}

                {/* Submit button */}
                <Button className="login-btn" onClick={handleSubmit} label={isSignUp?"Sign Up":"Login"} type="submit"></Button>

            </form>                
                
            
            <div>
                <span className="auth-toggle-text">{isSignUp?"Already have an account? ":"Don't have an account? "}</span>
                <a href="#" onClick={toggleAuth}>{isSignUp?"Login":"Sign Up"}</a>
            </div>
            <a href="#" onClick={(e)=>{e.preventDefault();navigate("/search")}}>Continue as Guest</a>
        </div>
    </div>
)}

export default LoginPage