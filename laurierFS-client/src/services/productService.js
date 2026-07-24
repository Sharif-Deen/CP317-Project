import products from "../data/products"
const BASE_URL = "http://localhost:8080"

export const getDummyProducts = () => {return products}

export const getProducts = async () => {
  const response = await fetch(`${BASE_URL}/api/products`)
  const data = await response.json()
  return data
}

export const addProduct = async (product) => {
  const response = await fetch(`${BASE_URL}/api/products`, {
    method: "POST",
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product)
  })
  return await response.json()
}

export const deleteProduct = async (productId) => {
  const response = await fetch(`${BASE_URL}/api/products`, {
    method: "DELETE",
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({id: productId})
  })
}
