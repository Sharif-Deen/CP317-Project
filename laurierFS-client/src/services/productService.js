import products from "../data/products"
const BASE_URL = "http://localhost:8080"


export const getProducts = async () => {
  try {
    const response = await fetch(`${BASE_URL}/api/products`);
    if (!response.ok) throw new Error("Failed to fetch products");
    return await response.json()

  } catch (err) {
    console.warn("Server unavailable, using local product data:", err.message);
    return products;
  }
};


// Fetch a single product by ID
export const getProductById = async (id) => {
  try {
    const response = await fetch(`${BASE_URL}/api/products/${id}`);
    if (!response.ok) throw new Error("Server error");
    return await response.json();
  } catch (err) {
    return products.find(p => p.id === id) || null;
  }
};


export const updateProductStock = async (id, newStock) => {
  const response = await fetch(`${BASE_URL}/api/products/${id}/stock`, {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ stock: newStock }),
  });
  if (!response.ok) throw new Error('Failed to update stock');
  return response.json();
};


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
  const response = await fetch(`${BASE_URL}/api/products/${productId}`, {
    method: "DELETE"
  })
  if (!response.ok) throw new Error(data.message || "Failed to delete product")
  return await response.json()
}
