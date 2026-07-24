const BASE_URL = "http://localhost:8080"

export const login = (email, password) => {
    const role = (email === "admin" && password === "123") ? "admin" : "customer"
    return {success: true, role: role}
}

export const loginDistributor = async (email, password) => {
    const response = await fetch(`${BASE_URL}/api/login`, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: email, password, category: "distributor", email })
    })
    const data = await response.json()
    if (!response.ok) throw new Error(data.message || "Invalid email or password")
    return data
}