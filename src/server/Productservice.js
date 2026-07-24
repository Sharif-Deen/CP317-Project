import products from "../data/products"
const BASE_URL = "http://localhost:8080"

export const getDummyProducts = () => {return products}

export const getProducts = async () => {
  try {
    const response = await fetch(`${BASE_URL}/api/products`)
    if (!response.ok) throw new Error("Server error")
    const data = await response.json()
    return data
  } catch (err) {
    console.warn("Backend unavailable, using local product data:", err.message)
    return products
  }
}

// Fetch a single product by ID
export const getProductById = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/api/products/${id}`)
    if (!response.ok) throw new Error("Server error")
    return await response.json()
  } catch (err) {
    // Fallback to local data
    return products.find(p => p.id === id) || null
  }
}