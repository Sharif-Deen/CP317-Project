
const product = 
{
    "id": 1,
    "name": "Bulk Roma Tomatoes",
    "price": 24.5,
    "type": "food",
    "brand": "FreshFields",
    "tags": [
      "vegetable",
      "produce",
      "bulk"
    ],
    "description": "Ripe Roma tomatoes packed for restaurants and catering.",
    "location": "Waterloo",
    "stock": 200
}

const user = 
{
    "id": 1,
    "username": "helloworld",
    "email": "example@gmail.com",
    "category": "customer"  //or "distributor"
}

const order =
{
  "orderId": 100,
  "email": "user@example.com",
  "phone": "519-123-4567",
  "totalPrice": 9.25,
  "orderDate": "2026-07-26",
  "orderStatus": "confirmed",
  "items": [
    {
      "product": {
        "id": 1,
        "name": "Milk",
        "price": 3.5,
        "brand": "FreshFields",
        "stock": 0
      },
      "quantity": 2
    },
    {
      "product": {
        "id": 2,
        "name": "Bread",
        "price": 2.25,
        "brand": "FreshFields",
        "stock": 0
      },
      "quantity": 1
    }
  ]
}