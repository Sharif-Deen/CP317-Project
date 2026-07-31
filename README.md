# Laurier Food Services

## Project Description


## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Database](#database)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Product Data Format](#product-data-format)
- [Available Scripts](#available-scripts)
- [Troubleshooting](#troubleshooting)

## Overview

Laurier Food Services is a supply-chain and logistics application with a React client and a Java HTTP server. The system supports product discovery, shopping carts, checkout, customer orders, product returns, distributor inventory management, and sales analytics.

The client communicates with the server through JSON REST-style endpoints. When the product endpoint is unavailable, the client will fall back to its local product data.

## Features

### Customer workflows

- Create an account and sign in with an email address or username.
- Browse and search products.
- View product details, pricing, tags, location, and stock.
- Add products to a cart and complete checkout.
- View previous orders and order status information.
- Track orders and submit product returns.

### Distributor workflows

- Sign in through the distributor login page.
- View the distributor dashboard.
- Add, edit, and remove products.
- Update product stock levels.
- View sales analytics.

## Technology Stack

### Frontend

- React 19
- Vite
- React Router DOM
- React Icons
- ESLint

### Backend

- Java 17 or newer
- JDK built-in `HttpServer`
- Gson for JSON serialization
- SQLite database
- SQLite JDBC driver

## Project Structure

```text
CP317-Project/
├── laurierFS-client/       React/Vite frontend
│   ├── src/
│   │   ├── components/     Reusable UI components and modals
│   │   ├── context/        Authentication, cart, and order state
│   │   ├── data/           Local product and example data
│   │   ├── pages/          Customer and distributor pages
│   │   ├── services/       Backend API clients
│   │   └── styles/         Page and component stylesheets
│   ├── package.json
│   └── vite.config.js
├── src/
│   ├── database/            SQLite access, schema, and seed data
│   ├── features/            Domain models and application logic
│   └── server/              HTTP server and API handlers
├── lib/                     Java dependencies
│   ├── gson-2.14.0.jar
│   └── sqlite-jdbc-3.53.2.0.jar
└── README.md
```

## Prerequisites

Install the following before starting:

- JDK 17 or newer
- Node.js 18 or newer
- npm, included with Node.js
- Git, if cloning the repository
- A SQLite command-line tool or database viewer for initializing the schema (Only if you want to reset the database)

Verify the installations:

```powershell
java -version
javac -version
node --version
npm --version
```

## Getting Started

Clone the repository and enter the project root:

```bash
git clone <repository-url>
cd CP317-Project
```

Install the frontend dependencies:

```bash
cd laurierFS-client
npm install
cd ..
```

Compile the Java source files from the project root. Running this command from the root is important because the database layer uses root-relative paths.

Windows PowerShell or Command Prompt:

```powershell
New-Item -ItemType Directory -Force bin
javac -d bin -cp "lib/*" src/server/*.java src/features/*.java src/database/*.java
```

macOS or Linux:

```bash
mkdir -p bin
javac -d bin -cp "lib/*" src/server/*.java src/features/*.java src/database/*.java
```

## Database

The application uses the SQLite database file `src/database/laurierFS.db`. The schema is defined in `src/database/databaseSchema.sql`. 

**The database already contains data. You may follow these steps if you wish to refresh the database.**

Initialize or refresh the database schema before loading seed data:

```bash
sqlite3 src/database/laurierFS.db < src/database/databaseSchema.sql
```

Then run the data script from the project root:

Windows:

```powershell
java -cp "lib/*;bin" database.DataScript
```

macOS or Linux:

```bash
java -cp "lib/*:bin" database.DataScript
```

The data script loads products from `src/database/products.txt`, creates sample distributor and customer accounts, and creates sample orders. It also writes generated data to `users.txt` and `orders.txt`.

Sample customer accounts created by the seed script:

| Username | Password |
| --- | --- |
| `cust1` | `pw1234` |
| `cust2` | `abc1234` |
| `cust3` | `mypass1` |

Distributor accounts are generated from product brands. Their username is the brand, their email is `<brand>@laurierfs.com`, and their seeded password is `pass123`.

## Running the Application

### Start the server

From the project root:

```powershell
java -cp "lib/*;bin" server.Server
```

The backend starts at `http://localhost:8080`.

### Start the client

Open a second terminal and run:

```powershell
cd laurierFS-client
npm run dev -- --open
```

The Vite development server normally runs at `http://localhost:5173`.

The frontend services currently use `http://localhost:8080` as the backend base URL. Start the backend before using features that require database access.

### Run the frontend production build

```powershell
cd laurierFS-client
npm run build
npm run preview
```

## API Endpoints

All API endpoints are served from `http://localhost:8080` and use JSON request and response bodies where applicable.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/api/products` | Retrieve all products |
| `POST` | `/api/products` | Add a product |
| `PUT` | `/api/products/{id}` | Edit a product |
| `PUT` | `/api/products/{id}/stock` | Update product stock |
| `DELETE` | `/api/products/{id}` | Delete a product |
| `POST` | `/api/login` | Authenticate by email or username |
| `POST` | `/api/signup` | Create a customer or distributor account |
| `GET` | `/api/orders` | Retrieve all orders |
| `GET` | `/api/orders?email={email}` | Retrieve orders by email |
| `GET` | `/api/orders?username={username}` | Retrieve orders by username |
| `GET` | `/api/orders?brand={brand}` | Retrieve orders by brand |
| `POST` | `/api/orders` | Create an order |
| `GET` | `/api/analytics` | Retrieve sales analytics |

The server also handles CORS and browser preflight requests for these endpoints.

## Sample Data Format

Product seed data is pipe-delimited. Each non-empty line in `products.txt` must contain these fields in order:

```text
productName|price|productType|productBrand|productTags|productDescription|location|stock
```

Example:

```text
Apples|4.99|Produce|FreshFields|fruit,local|Fresh local apples|Waterloo|25
```

## Available Scripts

Run these commands from `laurierFS-client`:

| Command | Purpose |
| --- | --- |
| `npm run dev` | Start the Vite development server |
| `npm run build` | Create a production build |
| `npm run preview` | Preview the production build locally |
| `npm run lint` | Run ESLint against the frontend |

## Troubleshooting

### `SQLite JDBC driver not found`

Compile and run with the complete `lib/*` classpath. The SQLite JDBC jar must be available in `lib`.

### The server cannot find the database or seed files

Run Java commands from `CP317-Project`, not from `src` or `laurierFS-client`. The database code expects paths beginning with `src/database/`.

### The frontend cannot connect to the backend

Confirm that the Java server is running on port 8080 and that the frontend is using `http://localhost:8080`. Check the browser console and the Java terminal for error messages.

### Port 8080 or 5173 is already in use

Stop the process using the port, or start the frontend with another Vite port. The backend currently binds to port 8080 in `src/server/Server.java`.