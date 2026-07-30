const BASE_URL = "http://localhost:8080";

export const createOrder = async (orderPayload) => {
  const response = await fetch(`${BASE_URL}/api/orders`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(orderPayload),
  });

  const data = await response.json().catch(() => ({}));
  if (!response.ok) {
    throw new Error(data.message || "Failed to create order");
  }

  return data;
};

export const getOrdersByUser = async (username) => {
  const response = await fetch (`${BASE_URL}/api/orders?username=${encodeURIComponent(username)}`)
  if (!response.ok) throw new Error("Failed to fetch Orders");
  return await response.json()
}