const BASE_URL = import.meta.env.VITE_API_URL || ""

export const login = async (credentials) => {
    const response = await fetch(`${BASE_URL}/api/login`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })

    const data = await response.json()
    if (!response.ok) throw new Error(data.message || "Invalid email or password")
    return data
}

export const signup = async (credentials) => {
    const response = await fetch(`${BASE_URL}/api/signup`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })

    const data = await response.json()
    if (!response.ok) throw new Error(data.message || "Username or Email not available")
    return data
}

export const loginDistributor = async (email, password) => {
    const response = await fetch(`${BASE_URL}/api/login`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        // FIXED: Java specifically requires the key "identifier" to process the login
        body: JSON.stringify({ identifier: email, password }) 
    })
    const data = await response.json()
    if (!response.ok) throw new Error(data.message || "Invalid email or password")
    return data
}
