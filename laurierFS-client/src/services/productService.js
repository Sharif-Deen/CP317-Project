import products from "../data/products"
const BASE_URL = "http://localhost:8080"

export const getDummyProducts = () => {return products}

export const getProducts = async () => {
  const response = await fetch(`${BASE_URL}/api/products`)
  const data = await response.json()
  return data
}

