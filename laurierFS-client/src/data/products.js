const products = [
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
  },
  {
    "id": 2,
    "name": "Frozen Chicken Breast (10kg)",
    "price": 79.99,
    "type": "food",
    "brand": "ChefPro",
    "tags": [
      "meat",
      "frozen",
      "bulk",
      "protein"
    ],
    "description": "Lean boneless chicken breasts, individually quick-frozen for long shelf life.",
    "location": "Kitchener",
    "stock": 150
  },
  {
    "id": 3,
    "name": "All-Purpose Flour 25kg",
    "price": 32,
    "type": "food",
    "brand": "KitchenMax",
    "tags": [
      "baking",
      "pantry",
      "bulk"
    ],
    "description": "High-protein all-purpose flour ideal for breads and pastries.",
    "location": "Waterloo",
    "stock": 300
  },
  {
    "id": 4,
    "name": "Disposable Paper Food Trays (1000ct)",
    "price": 18.75,
    "type": "packaging",
    "brand": "BistroLine",
    "tags": [
      "disposable",
      "packaging",
      "eco-friendly"
    ],
    "description": "Sturdy paper food trays for serving and takeout, compostable.",
    "location": "Toronto",
    "stock": 500
  },
  {
    "id": 5,
    "name": "Stainless Steel Chef Knife 10in",
    "price": 59.95,
    "type": "utensils",
    "brand": "SteelChef",
    "tags": [
      "cutlery",
      "knife",
      "kitchen"
    ],
    "description": "High-carbon stainless chef knife for precision slicing and dicing.",
    "location": "Waterloo",
    "stock": 75
  },
  {
    "id": 6,
    "name": "Commercial Non-stick Fry Pan 12in",
    "price": 44.99,
    "type": "cookware",
    "brand": "KitchenMax",
    "tags": [
      "pan",
      "cookware",
      "non-stick"
    ],
    "description": "Durable non-stick fry pan for daily restaurant use.",
    "location": "Kitchener",
    "stock": 100
  },
  {
    "id": 7,
    "name": "Reusable Nylon Spatula Set (3pc)",
    "price": 9.5,
    "type": "utensils",
    "brand": "ChefPro",
    "tags": [
      "spatula",
      "utensil",
      "heat-resistant"
    ],
    "description": "Flexible nylon spatulas safe for non-stick surfaces.",
    "location": "Toronto",
    "stock": 250
  },
  {
    "id": 8,
    "name": "Bulk White Sugar 50lb",
    "price": 27,
    "type": "food",
    "brand": "FreshFields",
    "tags": [
      "sugar",
      "pantry",
      "bulk"
    ],
    "description": "Refined granulated sugar for baking and beverage service.",
    "location": "Waterloo",
    "stock": 400
  },
  {
    "id": 9,
    "name": "Glass Beverage Dispenser 5L",
    "price": 22,
    "type": "serveware",
    "brand": "BistroLine",
    "tags": [
      "beverage",
      "dispense",
      "glass"
    ],
    "description": "Clear glass dispenser with spigot, ideal for cold beverages and infusions.",
    "location": "Toronto",
    "stock": 60
  },
  {
    "id": 10,
    "name": "Heavy-Duty Oven Mitts (pair)",
    "price": 12.99,
    "type": "safety",
    "brand": "SteelChef",
    "tags": [
      "safety",
      "gloves",
      "heat-resistant"
    ],
    "description": "Insulated oven mitts for handling hot trays and pans.",
    "location": "Kitchener",
    "stock": 180
  },
  {
    "id": 11,
    "name": "Restaurant-Grade Food Thermometer",
    "price": 18.49,
    "type": "equipment",
    "brand": "ChefPro",
    "tags": [
      "thermometer",
      "food-safety",
      "tool"
    ],
    "description": "Digital instant-read thermometer for safe cooking temperatures.",
    "location": "Waterloo",
    "stock": 90
  },
  {
    "id": 12,
    "name": "Parchment Paper Rolls 12in x 500ft",
    "price": 15.25,
    "type": "packaging",
    "brand": "KitchenMax",
    "tags": [
      "baking",
      "disposable",
      "liner"
    ],
    "description": "Non-stick parchment rolls for baking and lining trays.",
    "location": "Toronto",
    "stock": 350
  },
  {
    "id": 13,
    "name": "Canned San Marzano Tomatoes (24 cans)",
    "price": 48,
    "type": "food",
    "brand": "FreshFields",
    "tags": [
      "canned",
      "tomato",
      "pantry"
    ],
    "description": "Authentic San Marzano tomatoes, peeled and packed in juice.",
    "location": "Waterloo",
    "stock": 120
  },
  {
    "id": 14,
    "name": "Porcelain Dinner Plates (12in, 24pc)",
    "price": 129.99,
    "type": "serveware",
    "brand": "BistroLine",
    "tags": [
      "dinnerware",
      "ceramic",
      "service"
    ],
    "description": "Durable porcelain plates designed for high-volume service.",
    "location": "Toronto",
    "stock": 40
  },
  {
    "id": 15,
    "name": "Commercial Stock Pot 40qt",
    "price": 199.99,
    "type": "cookware",
    "brand": "SteelChef",
    "tags": [
      "pot",
      "stock",
      "large"
    ],
    "description": "Heavy-gauge stainless steel stock pot for soups and stocks.",
    "location": "Kitchener",
    "stock": 25
  },
  {
    "id": 16,
    "name": "Squeeze Bottle Set (12oz, 12pc)",
    "price": 11.5,
    "type": "utensils",
    "brand": "ChefPro",
    "tags": [
      "squeeze",
      "sauce",
      "dispense"
    ],
    "description": "Plastic squeeze bottles for sauces, dressings, and plating.",
    "location": "Toronto",
    "stock": 200
  },
  {
    "id": 17,
    "name": "Espresso Roast Coffee Beans 5kg",
    "price": 89,
    "type": "beverage",
    "brand": "FreshFields",
    "tags": [
      "coffee",
      "beans",
      "roast"
    ],
    "description": "Medium-dark roast coffee beans tailored for espresso machines.",
    "location": "Waterloo",
    "stock": 80
  },
  {
    "id": 18,
    "name": "Insulated Food Delivery Bags LARGE",
    "price": 34.99,
    "type": "packaging",
    "brand": "BistroLine",
    "tags": [
      "delivery",
      "insulated",
      "transport"
    ],
    "description": "Thermal delivery bag to keep hot foods at serving temperature.",
    "location": "Toronto",
    "stock": 150
  },
  {
    "id": 19,
    "name": "Braided Cotton Kitchen Towels (50ct)",
    "price": 29.99,
    "type": "cleaning",
    "brand": "KitchenMax",
    "tags": [
      "towel",
      "cleaning",
      "linen"
    ],
    "description": "Absorbent cotton towels for wiping and polishing in kitchens.",
    "location": "Kitchener",
    "stock": 300
  },
  {
    "id": 20,
    "name": "Professional Immersion Blender 1.5hp",
    "price": 249,
    "type": "equipment",
    "brand": "SteelChef",
    "tags": [
      "blender",
      "immersion",
      "foodprep"
    ],
    "description": "High-power immersion blender for soups, sauces, and purees.",
    "location": "Waterloo",
    "stock": 30
  },
  {
    "id": 21,
    "name": "Frozen French Fries 10kg Bag",
    "price": 21.99,
    "type": "food",
    "brand": "ChefPro",
    "tags": [
      "frozen",
      "side",
      "deli"
    ],
    "description": "Crisp-cut frozen fries suitable for fryers and ovens.",
    "location": "Kitchener",
    "stock": 200
  },
  {
    "id": 22,
    "name": "Clamshell To-Go Containers 500ct",
    "price": 39.5,
    "type": "packaging",
    "brand": "BistroLine",
    "tags": [
      "takeout",
      "disposable",
      "plastic"
    ],
    "description": "Durable hinged clamshell containers for hot and cold foods.",
    "location": "Toronto",
    "stock": 400
  },
  {
    "id": 23,
    "name": "Olive Oil Extra Virgin 20L",
    "price": 149.99,
    "type": "food",
    "brand": "FreshFields",
    "tags": [
      "oil",
      "pantry",
      "bulk"
    ],
    "description": "Cold-pressed extra virgin olive oil for cooking and finishing.",
    "location": "Waterloo",
    "stock": 50
  },
  {
    "id": 24,
    "name": "Bamboo Chopsticks 1000 pairs",
    "price": 16.75,
    "type": "utensils",
    "brand": "KitchenMax",
    "tags": [
      "chopsticks",
      "disposable",
      "wood"
    ],
    "description": "Eco-friendly single-use bamboo chopsticks for sushi and Asian cuisine.",
    "location": "Toronto",
    "stock": 600
  },
  {
    "id": 25,
    "name": "Commercial Dish Rack (Pro Series)",
    "price": 89.95,
    "type": "equipment",
    "brand": "SteelChef",
    "tags": [
      "dishwashing",
      "rack",
      "organization"
    ],
    "description": "Heavy-duty dish rack for fast drying and storage in busy kitchens.",
    "location": "Kitchener",
    "stock": 45
  }
]
export default products