# Laurier Food Services

[Final Demo Video Link](https://lauriercloud-my.sharepoint.com/:v:/g/personal/sami5647_mylaurier_ca/IQCasnZUHVVuQqBkIktUvqF8ATZSpUphMKXUOBpCWkRg6O0?nav=eyJyZWZlcnJhbEluZm8iOnsicmVmZXJyYWxBcHAiOiJPbmVEcml2ZUZvckJ1c2luZXNzIiwicmVmZXJyYWxBcHBQbGF0Zm9ybSI6IldlYiIsInJlZmVycmFsTW9kZSI6InZpZXciLCJyZWZlcnJhbFZpZXciOiJNeUZpbGVzTGlua0NvcHkifX0&e=hWSeva)

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Quick Start](#quick-start)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Database](#database)
- [Sample Data Format](#sample-data-format)
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

## Quick Start

Codespaces can prepare and run the complete application without requiring the installation of Java or Node.js locally. See [Prerequisites](#prerequisites), [Getting Started](#getting-started), and [Running the Application](#running-the-application) if you wish to run the application locally.

1. Open the repository on GitHub.
2. Click the green **Code** button, open the **Codespaces** tab, and choose **Create codespace on main**.
3. Wait for setup to finish (this may take a few minutes). Dependencies are installed, Java is compiled, and both servers start automatically. The application should open automatically.

The client uses the Vite proxy to reach the Java API on port `8080`, so all features work inside the Codespace just as they would if ran locally. The SQLite database is stored in the Codespace and should be treated as development data, not permanent production storage.

To start the processes again from the Codespace terminal:

```bash
bash .devcontainer/start.sh
```

Your browser may block the Codespace from opening a pop-up window. If the application did not open, within the Codespace click on the PORTS tab and open port 5173.

To close the Codespace, click the `Codespaces` button in the bottom left corner, then click `Stop Current Codespace` from the dropdown. Alternatively, go back to the repository home page, click the **Code** button and stop the Codespace from there. Otherwise, Codespaces will stop after a period of inactivity. Reopening the Codespace starts the services again.

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
git clone https://github.com/Sharif-Deen/CP317-Project
cd CP317-Project
```

Install the frontend dependencies:

```bash
cd laurierFS-client
npm install
cd ..
```

Compile the Java source files from the project root. Running this command from the root is important because the database layer uses root-relative paths.

```bash
javac -d bin -cp "lib/*" src/server/*.java src/features/*.java src/database/*.java
```


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

During local development, Vite proxies frontend `/api` requests to the backend on port `8080`. Start the backend before using features that require database access.

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

## Database

**The database already contains data. You may follow these steps if you wish to completely reset the database with seeded data, otherwise it is not necessary.**

The application uses the SQLite database file `src/database/laurierFS.db`. The schema is defined in `src/database/databaseSchema.sql`. 

**Complete the following commands from the root directory:**

Delete the existing database:

Windows Powershell:

```powershell
Remove-Item .\src\database\laurierFS.db
```

macOS / Linux:

```bash
rm src/database/laurierFS.db
```

Initialize or refresh the database schema before loading seed data:

Windows Powershell:

```powershell
sqlite3 src/database/laurierFS.db ".read src/database/databaseSchema.sql"
```

macOS / Linux / Windows CMD:

```bash
sqlite3 src/database/laurierFS.db < src/database/databaseSchema.sql
```

Then run the data script:

Windows:

```powershell
java -cp "lib/*;bin" database.DataScript
```

macOS / Linux:

```bash
java -cp "lib/*:bin" database.DataScript
```

The data script loads products from `src/database/products.txt`, creates sample distributor and customer accounts, and creates sample orders. It also writes generated data to `users.txt` and `orders.txt` for viewing.

Sample customer accounts created by the seed script:

| Username | Password |
| --- | --- |
| `cust1` | `pw1234` |
| `cust2` | `abc1234` |
| `cust3` | `mypass1` |

Distributor accounts are generated from product brands. Their username is the brand, their email is `<brand>@laurierfs.com`, and their seeded password is `pass123`.

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

### Codespaces setup appears finished, but no application opens

The terminal prompt only means that the Codespace shell is ready. Check the **PORTS** tab and open port `5173` (LaurierFS client); port `8080` is the backend API and is not the application page.

If neither port is available, run this from the repository root:

```bash
bash .devcontainer/start.sh
```

Then check the startup logs:

```bash
cat /tmp/laurierfs/server.log
cat /tmp/laurierfs/client.log
```

If all else fails, run **Codespaces: Rebuild Container** from the VS Code Command Palette (Open with `F1` in the Codespace). A normal reconnect does not necessarily rebuild the container or rerun `postCreateCommand`.


### The server cannot find the database or seed files

Run Java commands from `CP317-Project`, not from `src` or `laurierFS-client`. The database code expects paths beginning with `src/database/`.

### The frontend cannot connect to the backend ('Failed to fetch')

Confirm that the Java server is running on port 8080. In Codespaces, open the frontend through forwarded port `5173`, not port `8080`. Vite proxies `/api` requests to the Java server. Check `/tmp/laurierfs/server.log` and `/tmp/laurierfs/client.log` for error messages.

### Port 8080 or 5173 is already in use

Stop the process using the port, or restart the Codespace. The backend currently binds to port 8080 in `src/server/Server.java`, and the frontend uses port 5173. If the Java server reports an exit code of 1, inspect its terminal output or `/tmp/laurierfs/server.log`, an occupied port is a common cause.
