import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null)
const STORAGE_KEY = "user"


export const AuthProvider = ({ children }) => {
    // Lazy init: read localStorage once on first render, not on every render.
    const [user, setUser] = useState(() => {
        try {
            const stored = localStorage.getItem(STORAGE_KEY)
            return stored ? JSON.parse(stored) : null
        } catch {
            return null
        }
    })

    // Keep localStorage in sync whenever `user` changes (including logout,
    // which sets it to null and clears the key below).
    useEffect(() => {
        if (user) {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(user))
        } else {
            localStorage.removeItem(STORAGE_KEY)
        }
    }, [user])

    // Call this after a successful login()/signup() with the { id, username, email, category } result.
    const setLoggedInUser = (result) => {
        setUser({...result})
    }

    const logout = () => {
        setUser(null)
    }

    const value = {
        user,
        isAuthenticated: !!user,
        setLoggedInUser,
        logout,
    }

    return (
        <AuthContext.Provider value={value}>
            {children}
        </AuthContext.Provider>
    )
}

// Usage in any component: const { user, isAuthenticated, setLoggedInUser, logout } = useAuth()
export const useAuth = () => {
    const context = useContext(AuthContext)
    if (!context) {
        throw new Error("useAuth must be used within an AuthProvider")
    }
    return context
}