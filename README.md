# InvoiceSystem-Zoho_Project-

#### App.java:

The `main` method serves as the entry point for the Java application, providing a command-line interface for interacting with a MySQL database managing customer, product, and invoice records.

- **Functionality:**
  - Establishes a connection to the MySQL database defined by `DB_URL`, `USER`, and `PASS`.
  - Initializes instances of `Customer`, `Product`, and `Invoice` classes to manage corresponding database tables.
  - Displays a menu (`ABC SUPERMARKET`) allowing users to perform operations like viewing databases, tables, querying data, generating invoices, and viewing sales reports.
  - Uses `Scanner` for user input and handles exceptions.

#### `showDatabases(Statement stmt)`

This method executes an SQL query to retrieve and display all database names available in the MySQL server.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.

- **Description:**
  Executes the SQL query `"SHOW DATABASES"` using the provided `Statement` object. It iterates through the `ResultSet` to print each database name retrieved from the query result.

#### `showTables(Statement stmt)`

This method executes an SQL query to retrieve and display all table names in the currently selected database.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.

- **Description:**
  Executes the SQL query `"SHOW TABLES"` using the provided `Statement` object. It iterates through the `ResultSet` to print each table name retrieved from the query result.

#### `printQueryResult(Statement stmt, Scanner scanner)`

This method executes a user-provided SQL query and prints the result set obtained from the database.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `scanner`: A `Scanner` object to read the user-provided SQL query.

- **Description:**
  Prompts the user to enter an SQL query via the `Scanner`. Executes the SQL query using the provided `Statement` object. It prints the column names and corresponding values for each row in the result set retrieved from the database query.

#### `manipulateTable(Statement stmt, Scanner scanner, Customer customer, Product product, Invoice invoice)`

This method provides a menu-driven interface to manipulate data in specified database tables (`customers`, `products`, `invoice`) based on user input.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `scanner`: A `Scanner` object for user input.
  - `customer`: An instance of `Customer` class for database operations related to customers.
  - `product`: An instance of `Product` class for database operations related to products.
  - `invoice`: An instance of `Invoice` class for database operations related to invoices.

- **Description:**
  Displays a menu prompting the user to select a table (`customers`, `products`, `invoice`) and then choose an operation (`Insert Data`, `Update Data`, `Delete Data`, `View Data`, `Back to Main Menu`). Depending on the chosen table and operation, it interacts with the database through corresponding methods of the `Customer`, `Product`, or `Invoice` classes.

  - **For Customers Table:**
    - Supports operations like inserting customer data (`insertData` method of `Customer` class), updating data (`updateData` method of `Customer` class), deleting data (`deleteData` method of `Customer` class), and viewing all or specific customer details.
  
  - **For Products Table:**
    - Allows inserting product data (`insertData` method of `Product` class), updating data (`updateData` method of `Product` class), deleting data (`deleteData` method of `Product` class), and viewing all or specific product details.
  
  - **For Invoice Table:**
    - Provides functionalities for generating invoices (`insertData` method of `Invoice` class), updating invoice data (`updateData` method of `Invoice` class), deleting invoices (`deleteData` method of `Invoice` class), and viewing all invoices or invoices for a specific customer.

  - **Navigation:**
    - Allows the user to return to the main menu (`Back to Main Menu`) to choose a different operation or exit the application.

### `printColumnNames(Statement stmt, String tableName)`
This method retrieves and prints the column names of a specified database table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `tableName`: A `String` representing the name of the table whose column names are to be printed.

- **Description:**
  Executes a query to fetch metadata for the specified table (`tableName`) and prints each column name. This method helps in identifying the structure of the table dynamically.

---

### `generateInvoice(Statement stmt, int customerId)`
Generates an invoice for a specified customer including customer details, purchased products, and total amount.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `customerId`: An `int` representing the ID of the customer for whom the invoice is generated.

- **Description:**
  Retrieves customer details (`name`, `contact number`) and invoice details (`product name`, `quantity`, `unit`, `price`, `amount`) by executing SQL queries joining the `customers`, `products`, and `Invoice` tables. Generates a text file formatted as an invoice and saves it to a directory (`./invoices/`) with a filename based on customer and date information.

---

### `showSalesReport(Statement stmt, Scanner scanner)`
Displays sales reports based on user input: today's sales report or a sales report for a specific date.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `scanner`: A `Scanner` object for user input.

- **Description:**
  Presents a menu to select between today's sales report or a report for a specific date (`YYYY-MM-DD` format). Executes corresponding methods based on user choice (`showTodaySalesReport` or `showSalesReportForDate`).

---

### `showTodaySalesReport(Statement stmt, Scanner scanner)`
Displays today's sales report using the `showSalesReportForDate` method.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `scanner`: A `Scanner` object for user input.

- **Description:**
  Fetches today's date and invokes `showSalesReportForDate` with the formatted date string (`YYYY-MM-DD`).

---

### `showSalesReportForDate(Statement stmt, String dateStr, Scanner scanner)`
Displays sales report for a specified date, including individual product sales details and total units sold with revenue.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute SQL queries.
  - `dateStr`: A `String` representing the date (`YYYY-MM-DD` format) for which the report is generated.
  - `scanner`: A `Scanner` object for user input.

- **Description:**
  Executes SQL queries to fetch sales data (`product name`, `quantity`, `amount`) for the specified date. Prints a tabular format report with individual product sales details and calculates total units sold and revenue. Offers the option to download the generated report as a text file (`Sales_Report_YYYY-MM-DD.txt`) in a specified directory (`./salesreports/`).

---

### `downloadReport(String report, String dateStr)`
Downloads a sales report as a text file.

- **Parameters:**
  - `report`: A `String` containing the formatted sales report content.
  - `dateStr`: A `String` representing the date for which the report is generated (`YYYY-MM-DD` format).

- **Description:**
  Creates a directory (`./salesreports/`) if it doesn't exist and writes the provided `report` content into a text file (`Sales_Report_YYYY-MM-DD.txt`). This method facilitates saving and accessing generated sales reports.

---

These methods collectively facilitate database operations, invoice generation, and sales reporting functionalities in a Java application using JDBC for SQL interactions and user input handling through a console interface.

### Customers.java:

This class defines methods for managing customer data in the `customers` table. The `customers` table includes columns for customer ID, name, age, contact number, total purchases, paid amount, and remaining balance. Below are detailed descriptions of each method:

#### `insertData(Statement stmt, String name, int age, String contactNo)`

This method inserts a new customer record into the `customers` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `name`: The name of the customer.
  - `age`: The age of the customer.
  - `contactNo`: The contact number of the customer.

- **Description:**
  This method constructs an SQL `INSERT` statement using the provided customer details and executes it. It reports the number of rows affected by the insertion.

#### `updateData(Statement stmt, String setClause, String whereClause)`

This method updates existing customer records in the `customers` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `setClause`: The SQL `SET` clause specifying which columns to update and their new values.
  - `whereClause`: The SQL `WHERE` clause specifying which records to update.

- **Description:**
  Before executing the update, this method checks if the `setClause` contains restricted columns (`TOTAL_PURCHASES`, `Paid`, or `Remaining_Balance`). If these columns are present, it prints a message indicating that these columns cannot be updated manually and terminates without executing the update. Otherwise, it constructs and executes an SQL `UPDATE` statement and reports the number of rows affected.

#### `deleteData(Statement stmt, String whereClause)`

This method deletes customer records from the `customers` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `whereClause`: The SQL `WHERE` clause specifying which records to delete.

- **Description:**
  This method constructs an SQL `DELETE` statement using the provided `whereClause` and executes it. It reports the number of rows affected by the deletion.

#### `updateTotalPurchasePaidAndRemainingBalance(Statement stmt, int custId)`

This method updates the `TOTAL_PURCHASES`, `Paid`, and `Remaining_Balance` columns for a specific customer.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `custId`: The ID of the customer whose `TOTAL_PURCHASES`, `Paid`, and `Remaining_Balance` needs to be updated.

- **Description:**
  This method constructs and executes three SQL `UPDATE` statements:
  - The first statement sets the `Paid` column to the sum of all paid invoices for the customer specified by `custId`.
  - The second statement sets the `TOTAL_PURCHASES` column to the count of invoices associated with the customer specified by `custId`.
  - The third statement sets the `Remaining_Balance` column to the sum of all unpaid invoices for the customer specified by `custId`.

Each statement updates the respective column with the total amount of paid or unpaid invoices and the total number of invoices for the specified customer.

### Products.java:

This class manages operations related to product data in the `products` table. The `products` table includes columns for product ID, product name, price, available quantity, and unit. Below are detailed descriptions of each method:

#### `createTable(Statement stmt)`

This method creates the `products` table in the database.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.

- **Description:**
  Executes an SQL `CREATE TABLE` statement to create the `products` table with columns for product ID (auto-incremented primary key), product name (`pname`), price (`price`), available quantity (`available_quantity`), and unit (`unit`). Upon successful execution, it prints a confirmation message.

#### `insertData(Statement stmt, String name, int price, int availableQuantity, String unit)`

This method inserts new product data into the `products` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `name`: The name of the product to insert (`pname`).
  - `price`: The price of the product (`price`).
  - `availableQuantity`: The available quantity of the product (`available_quantity`).
  - `unit`: The unit of measurement for the product (`unit`).

- **Description:**
  Constructs and executes an SQL `INSERT INTO` statement to insert a new record into the `products` table with the provided product details. It prints the number of rows affected upon successful insertion.

#### `updateData(Statement stmt, String setClause, String whereClause)`

This method updates existing product data in the `products` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `setClause`: The SQL `SET` clause specifying which columns to update and their new values.
  - `whereClause`: The SQL `WHERE` clause specifying which records to update.

- **Description:**
  Constructs and executes an SQL `UPDATE` statement to update records in the `products` table based on the provided `setClause` and `whereClause`. It prints the number of rows affected upon successful update.

#### `deleteData(Statement stmt, String whereClause)`

This method deletes product data from the `products` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `whereClause`: The SQL `WHERE` clause specifying which records to delete.

- **Description:**
  Constructs and executes an SQL `DELETE FROM` statement to delete records from the `products` table based on the provided `whereClause`. It prints the number of rows affected upon successful deletion.

These methods provide essential functionalities for managing product data within the application, ensuring CRUD (Create, Read, Update, Delete) operations can be performed effectively on the `products` table in the database.

### Invoice.java:

This class defines methods for managing invoice data in the `Invoice` table. The `Invoice` table includes columns for invoice ID, customer ID, product ID, quantity, amount, status, and date. Below are detailed descriptions of each method:

#### `insertData(Statement stmt, int customerId, int productId, int quantity, String status)`

This method inserts a new invoice record into the `Invoice` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `customerId`: The ID of the customer associated with the invoice.
  - `productId`: The ID of the product associated with the invoice.
  - `quantity`: The quantity of the product in the invoice.
  - `status`: The status of the invoice (e.g., 'Paid', 'Not Paid').

- **Description:**
  This method first retrieves the price and available quantity of the specified product. If the requested quantity is available, it calculates the total amount for the invoice and inserts a new record into the `Invoice` table. It also updates the remaining quantity in the `products` table and the customer totals (total purchases, paid amount, and remaining balance). If the requested quantity exceeds the available quantity, it prints an error message and does not insert the record.

#### `updateData(Statement stmt, String setClause, String whereClause)`

This method updates existing invoice records in the `Invoice` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `setClause`: The SQL `SET` clause specifying which columns to update and their new values.
  - `whereClause`: The SQL `WHERE` clause specifying which records to update.

- **Description:**
  This method constructs and executes an SQL `UPDATE` statement using the provided `setClause` and `whereClause`. It retrieves the customer ID and product ID from the `Invoice` table based on the invoice ID in the `whereClause`. If the `quantity` is updated, it recalculates the `amount` based on the new quantity and updates the `amount` column. Finally, it updates the customer totals (total purchases, paid amount, and remaining balance).

#### `deleteData(Statement stmt, String whereClause)`

This method deletes invoice records from the `Invoice` table.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `whereClause`: The SQL `WHERE` clause specifying which records to delete.

- **Description:**
  This method constructs and executes an SQL `DELETE` statement using the provided `whereClause`. It retrieves the customer ID from the `Invoice` table based on the invoice ID in the `whereClause` before deletion. After deleting the record, it updates the customer totals (total purchases, paid amount, and remaining balance).

#### `customer.updateTotalPurchasePaidAndRemainingBalance(Statement stmt, int custId)`

- **Note:**
  This method is called within the `insertData`, `updateData`, and `deleteData` methods to ensure that the customer's total purchases, paid amount, and remaining balance are always up-to-date after any modifications to the `Invoice` table.
