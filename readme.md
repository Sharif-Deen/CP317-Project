For sampleProducts.txt:
productName|price|productType|productBrand|productTags|productDescription|location|stock

## Running the Server
### Prerequisites

Make sure your terminal is navigated to the project root directory `CP317-Project`.

### Step 1: Compile the Project

Compile all source files into the bin directory:

```bash
javac -d bin -cp "lib/*" src/server/*.java src/features/*.java src/database/*.java
```

### Step 2: Launch the Server

Run the compiled `server.Server` class using the command for your OS:

Windows:

```powershell
java -cp "lib/*;bin" server.Server
```

macOS / Linux:

```bash
java -cp "lib/*:bin" server.Server
```

### Alternative: IDE Execution (VS Code)

If you are using __VS Code__ with the __Extension Pack for Java__ or __Code Runner__ installed, open `src/server/Server.java` and click the `Run` button directly above the `main()` method.