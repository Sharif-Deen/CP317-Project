import products from "../data/products"
const BASE_URL = "http://localhost:8080"

export const getDummyProducts = () => {return products}

export const getProducts = async () => {
  const response = await fetch(`${BASE_URL}/api/products`)
  if (!response.ok) throw new Error("Failed to fetch products")
  return await response.json()
}

export const addProduct = async (product) => {
  const response = await fetch(`${BASE_URL}/api/products`, {
    method: "POST",
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product)
  })
  const data = await response.json()
  if (!response.ok) throw new Error(data.message || "Failed to add product")
  return data
}

export const deleteProduct = async (productId) => {
  const response = await fetch(`${BASE_URL}/api/products`, {
    method: "DELETE",
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({id: productId})
  })
  const data = await response.json()
  if (!response.ok) throw new Error(data.message || "Failed to delete product")
  return data
}
