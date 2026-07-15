-- SQLite
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS product (
    productNumber INTEGER PRIMARY KEY AUTOINCREMENT,
    productName TEXT NOT NULL,
    price REAL NOT NULL,
    productCategory TEXT,
    productBrand TEXT,
    productShortDescription TEXT
);

CREATE TABLE IF NOT EXISTS users (
    accountNumber INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL,
    username TEXT NOT NULL,
    userPassword TEXT NOT NULL,
    phone TEXT NOT NULL,
    userType INTEGER,
    UNIQUE(email, phone)
);

CREATE TABLE IF NOT EXISTS orders (
    orderNumber INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL,
    phone TEXT NOT NULL,
    totalPrice REAL NOT NULL,
    orderDate DATE NOT NULL,
    orderStatus TEXT NOT NULL,
    deliveryDate DATE,
    FOREIGN KEY (email, phone) REFERENCES users(email, phone)
);

CREATE TABLE IF NOT EXISTS orderDetails (
    orderNumber INTEGER NOT NULL,
    productNumber INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price REAL NOT NULL,
    PRIMARY KEY (orderNumber, productNumber),
    FOREIGN KEY (orderNumber) REFERENCES orders(orderNumber),
    FOREIGN KEY (productNumber) REFERENCES product(productNumber)
);

CREATE TABLE IF NOT EXISTS locations (
    locationNumber INTEGER PRIMARY KEY AUTOINCREMENT,
    city TEXT NOT NULL,
    country TEXT NOT NULL,
    province TEXT
);

CREATE TABLE IF NOT EXISTS productLocation (
    productNumber INTEGER NOT NULL,
    locationNumber INTEGER NOT NULL,
    productName TEXT NOT NULL,
    locationName TEXT NOT NULL,
    PRIMARY KEY (productNumber, locationNumber),
    FOREIGN KEY (productNumber) REFERENCES product(productNumber),
    FOREIGN KEY (locationNumber) REFERENCES locations(locationNumber),
    FOREIGN KEY (locationName) REFERENCES locations(city)
);

CREATE TABLE IF NOT EXISTS tag (
    tagNumber INTEGER PRIMARY KEY AUTOINCREMENT,
    tagName TEXT NOT NULL,
    tagDescription TEXT
);

CREATE TABLE IF NOT EXISTS productTag (
    productNumber INTEGER NOT NULL,
    tagNumber INTEGER NOT NULL,
    PRIMARY KEY (productNumber, tagNumber),
    FOREIGN KEY (productNumber) REFERENCES product(productNumber),
    FOREIGN KEY (tagNumber) REFERENCES tag(tagNumber)
);

CREATE TABLE IF NOT EXISTS productStock (
    productNumber INTEGER NOT NULL,
    locationNumber INTEGER NOT NULL,
    locationName TEXT NOT NULL,
    stockQuantity INTEGER NOT NULL,
    PRIMARY KEY (productNumber, locationNumber),
    FOREIGN KEY (productNumber) REFERENCES product(productNumber),
    FOREIGN KEY (locationNumber) REFERENCES locations(locationNumber),
    FOREIGN KEY (locationName) REFERENCES locations(city)
);
