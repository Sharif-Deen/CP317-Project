

export const login = (email, password) => {
    const role = (email === "admin" && password === "123") ? "admin" : "customer"
    return {success: true, role: role}
}