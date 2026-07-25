For sampleProducts.txt:
productName|price|productType|productBrand|productTags|productDescription|location|stock

# How to Run the App

## Prerequisites

Ensure you have the following installed before running the application:
* **Java Development Kit (JDK 17+)**
* **Node.js (v18+)**

---

## 1. Running the Server

The server handles backend services, including product data retrieval and user authentication.

### Option A: IDE Execution

1. Open the project in **VS Code** (with the *Extension Pack for Java* installed) or your preferred IDE (e.g., IntelliJ, Eclipse).
2. Open `src/server/Server.java`.
3. Click the **Run** button directly above the `main()` method.

### Option B: Terminal Execution

Make sure your terminal is navigated to the project root directory `CP317-Project`.

1. **Compile the source files:**

    ```bash
    javac -d bin -cp "lib/*" src/server/*.java src/features/*.java src/database/*.java
    ```

2. **Launch the server**

    Windows (PowerShell/CMD):

    ```powershell
    java -cp "lib/*;bin" server.Server
    ```

    macOS / Linux:

    ```bash
    java -cp "lib/*:bin" server.Server
    ```

## 2. Running the Client

Open a new terminal window and navigate to the client directory to start the development server:

```bash
cd laurierFS-client
npm run dev -- --open
```