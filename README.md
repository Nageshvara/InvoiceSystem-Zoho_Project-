# InvoiceSystem-Zoho_Project-

### Customers.java File Description

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

#### `updateTotalPurchases(Statement stmt, int custId)`

This method updates the `TOTAL_PURCHASES` column for a specific customer.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `custId`: The ID of the customer whose `TOTAL_PURCHASES` needs to be updated.

- **Description:**
  This method constructs an SQL `UPDATE` statement that sets the `TOTAL_PURCHASES` column to the count of invoices associated with the customer specified by `custId`. It executes this statement to update the customer's total purchases.

#### `updatePaidAndRemainingBalance(Statement stmt, int custId)`

This method updates the `Paid` and `Remaining_Balance` columns for a specific customer.

- **Parameters:**
  - `stmt`: An instance of `Statement` used to execute the SQL query.
  - `custId`: The ID of the customer whose `Paid` and `Remaining_Balance` needs to be updated.

- **Description:**
  This method constructs and executes two SQL `UPDATE` statements:
  - The first statement sets the `Paid` column to the sum of all paid invoices for the customer specified by `custId`.
  - The second statement sets the `Remaining_Balance` column to the sum of all unpaid invoices for the customer specified by `custId`.
  
Each statement updates the respective column with the total amount of paid or unpaid invoices for the specified customer.
