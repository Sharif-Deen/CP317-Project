import products from "../data/products";
const BASE_URL = "http://localhost:8080";

export const getDummyProducts = () => { return products; };

export const getProducts = async () => {
  try {
    const response = await fetch(`${BASE_URL}/api/products`);
    if (!response.ok) throw new Error("Server error");
    const data = await response.json();
    return data;
  } catch (err) {
    console.warn("Backend unavailable, using local product data:", err.message);
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
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(product),
  });
  if (!response.ok) throw new Error('Failed to add product');
  return response.json();
};

export const deleteProduct = async (id) => {
  const response = await fetch(`${BASE_URL}/api/products/${id}`, {
    method: 'DELETE',
  });
  if (!response.ok) throw new Error('Failed to delete product');
  return response.json();
};
```[cite: 1, 2]