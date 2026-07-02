import { useState } from "react";
import Button from "../components/Button.jsx"
import InputField from "../components/InputField.jsx"
import Logo from "../components/Logo.jsx"


const LoginPage = ()=> {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")

    return(
    <div>
        <Logo></Logo>
        <InputField label="Enter your email" type="email" placeholder="email@example.com" onChange={(e)=> setEmail(e.target.value)} value={email}></InputField>
        <InputField label="Enter your password" type="password" placeholder="" onChange={(e)=> setPassword(e.target.value)} value={password}></InputField>
        <a href="#">Forgot Password</a>
        <Button onClick={()=>{}} label="Login" type="button"></Button>
        <Button onClick={()=>{}} label="Sign Up" type="button"></Button>
        <a href="#">Continue as Guest</a>
        <a href="#">Distributor Login Page</a>
    </div>
)}

export default LoginPage