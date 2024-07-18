import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        final String DB_URL = "jdbc:mysql://localhost:3306/invoice";
        final String USER = "root";
        final String PASS = "Nagesh@sql24";
        Scanner scanner = new Scanner(System.in);
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                Statement stmt = conn.createStatement()) {

            Customer customer = new Customer();
            Product product = new Product();
            Invoice invoice = new Invoice();
            while (true) {
                // Print the initial menu with "ABC SUPERMARKET" at the top
                System.out.println(String.format("%80s\n", "ABC SUPERMARKET"));
                System.out.println(String.format("%126s\n",
                        "--------------------------------------------------------------------------------------------------------------"));
                System.out.println("Select an option:");
                System.out.println("1. Show Databases");
                System.out.println("2. Show Tables");
                System.out.println("3. Select Table for Manipulation");
                System.out.println("4. Print Result of Query");
                System.out.println("5. Generate Invoice");
                System.out.println("6. Sales Report");
                System.out.println("7. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        showDatabases(stmt);
                        break;
                    case 2:
                        showTables(stmt);
                        break;
                    case 3:
                        manipulateTable(stmt, scanner, customer, product, invoice);
                        break;
                    case 4:
                        printQueryResult(stmt, scanner);
                        break;
                    case 5:
                        System.out.println("Enter customer ID:");
                        int customerId = scanner.nextInt();
                        generateInvoice(stmt, customerId);
                        break;
                    case 6:
                        showSalesReport(stmt, scanner);
                        break;
                    case 7:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void showDatabases(Statement stmt) throws SQLException {
        String sql = "SHOW DATABASES";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        rs.close();
    }

    public static void showTables(Statement stmt) throws SQLException {
        String sql = "SHOW TABLES";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
        rs.close();
    }

    public static void printQueryResult(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("Enter SQL query to print results:");
        String sql = scanner.nextLine();
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnsNumber = rsmd.getColumnCount();
        while (rs.next()) {
            for (int i = 1; i <= columnsNumber; i++) {
                if (i > 1)
                    System.out.print(",  ");
                String columnValue = rs.getString(i);
                System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
            }
            System.out.println("");
        }
        rs.close();
    }

    public static void manipulateTable(Statement stmt, Scanner scanner, Customer customer, Product product,
            Invoice invoice) throws SQLException {
        System.out.println("Enter table name (customers, products, invoice):");
        String tableName = scanner.nextLine();

        while (true) {
            System.out.println("Select an operation on table " + tableName + ":");
            System.out.println("1. Insert Data");
            System.out.println("2. Update Data");
            System.out.println("3. Delete Data");
            System.out.println("4. View Data");
            System.out.println("5. Back to Main Menu");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (tableName.toLowerCase()) {
                case "customers":
                    switch (choice) {
                        case 1:
                            System.out.println("Enter name:");
                            String name = scanner.nextLine();
                            System.out.println("Enter age:");
                            int age = scanner.nextInt();
                            scanner.nextLine(); // consume the newline character
                            System.out.println("Enter contact number:");
                            String contactNo = scanner.nextLine();
                            customer.insertData(stmt, name, age, contactNo);
                            break;
                        case 2:
                            printColumnNames(stmt, tableName);
                            System.out.println("Enter SET clause (e.g., cname = 'Jane'):");
                            String setClause = scanner.nextLine();
                            System.out.println("Enter WHERE clause (e.g., cust_id = 1):");
                            String whereClause = scanner.nextLine();
                            customer.updateData(stmt, setClause, whereClause);
                            break;
                        case 3:
                            System.out.println("Enter WHERE clause for deletion (e.g., cust_id = 1):");
                            whereClause = scanner.nextLine();
                            customer.deleteData(stmt, whereClause);
                            break;
                        case 4:
                            System.out.println("All customer Details (1) or Particular Customer (2)?");
                            int innerChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character after reading integer

                            if (innerChoice == 1) {
                                // Display all customers
                                String sql = "SELECT * FROM customers";
                                ResultSet rs = stmt.executeQuery(sql);
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int columnsNumber = rsmd.getColumnCount();
                                while (rs.next()) {
                                    for (int i = 1; i <= columnsNumber; i++) {
                                        if (i > 1)
                                            System.out.print(",  ");
                                        String columnValue = rs.getString(i);
                                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                    }
                                    System.out.println("");
                                }
                                rs.close();
                            } else if (innerChoice == 2) {
                                // Display particular customer details
                                System.out.println("Enter customer ID or Name:");
                                String input = scanner.nextLine().trim();

                                String sql;
                                if (input.matches("\\d+")) {
                                    // If input is numeric, assume it's customer ID
                                    int customerId = Integer.parseInt(input);
                                    sql = "SELECT * FROM customers WHERE cust_id = " + customerId;
                                } else {
                                    // Otherwise, treat it as customer name
                                    sql = "SELECT * FROM customers WHERE cname = '" + input + "'";
                                }

                                ResultSet rs = stmt.executeQuery(sql);
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int columnsNumber = rsmd.getColumnCount();

                                boolean found = false;
                                while (rs.next()) {
                                    found = true;
                                    for (int i = 1; i <= columnsNumber; i++) {
                                        if (i > 1)
                                            System.out.print(",  ");
                                        String columnValue = rs.getString(i);
                                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                    }
                                    System.out.println("");
                                }
                                if (!found) {
                                    System.out.println("Customer not found.");
                                }
                                rs.close();
                            } else {
                                System.out.println("Invalid choice.");
                            }
                            break;

                        case 5:
                            return;
                        default:
                            System.out.println("Invalid choice, please try again.");
                    }
                    break;

                case "products":
                    switch (choice) {
                        case 1:
                            System.out.println("Enter product name:");
                            String pname = scanner.nextLine();
                            System.out.println("Enter price:");
                            int price = scanner.nextInt();
                            System.out.println("Enter available quantity:");
                            int availableQuantity = scanner.nextInt();
                            scanner.nextLine(); // consume the newline character
                            System.out.println("Enter unit:");
                            String unit = scanner.nextLine();
                            product.insertData(stmt, pname, price, availableQuantity, unit);
                            break;
                        case 2:
                            printColumnNames(stmt, tableName);
                            System.out.println("Enter SET clause (e.g., price = 100):");
                            String setClause = scanner.nextLine();
                            System.out.println("Enter WHERE clause (e.g., prod_id = 1):");
                            String whereClause = scanner.nextLine();
                            product.updateData(stmt, setClause, whereClause);
                            break;
                        case 3:
                            System.out.println("Enter WHERE clause for deletion (e.g., prod_id = 1):");
                            whereClause = scanner.nextLine();
                            product.deleteData(stmt, whereClause);
                            break;
                        case 4:
                            // View Data in Products Table
                            System.out.println("All product Details (1) or Particular Product (2)?");
                            int innerChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character after reading integer

                            if (innerChoice == 1) {
                                // Display all products
                                String sql = "SELECT * FROM products";
                                ResultSet rs = stmt.executeQuery(sql);
                                ResultSetMetaData rsmd = rs.getMetaData();
                                int columnsNumber = rsmd.getColumnCount();
                                while (rs.next()) {
                                    for (int i = 1; i <= columnsNumber; i++) {
                                        if (i > 1)
                                            System.out.print(",  ");
                                        String columnValue = rs.getString(i);
                                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                    }
                                    System.out.println("");
                                }
                                rs.close();
                            } else if (innerChoice == 2) {
                                // Display particular product details
                                System.out.println("Enter product ID or Name:");
                                String input = scanner.nextLine().trim();

                                try {
                                    String sql;
                                    if (input.matches("\\d+")) {
                                        // If input is numeric, assume it's product ID
                                        int productId = Integer.parseInt(input);
                                        sql = "SELECT * FROM products WHERE prod_id = " + productId;
                                    } else {
                                        // Otherwise, treat it as product name
                                        sql = "SELECT * FROM products WHERE pname = '" + input + "'";
                                    }

                                    ResultSet rs = stmt.executeQuery(sql);
                                    ResultSetMetaData rsmd = rs.getMetaData();
                                    int columnsNumber = rsmd.getColumnCount();

                                    boolean found = false;
                                    while (rs.next()) {
                                        found = true;
                                        for (int i = 1; i <= columnsNumber; i++) {
                                            if (i > 1)
                                                System.out.print(",  ");
                                            String columnValue = rs.getString(i);
                                            System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                        }
                                        System.out.println("");
                                    }
                                    if (!found) {
                                        System.out.println("Product not found.");
                                    }
                                    rs.close();
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid product ID format.");
                                } catch (SQLException e) {
                                    System.out.println("SQL error: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Invalid choice.");
                            }
                            break;
                        case 5:
                            return;
                        default:
                            System.out.println("Invalid choice, please try again.");
                    }
                    break;

                case "invoice":
                    switch (choice) {
                        case 1:
                            System.out.println("Enter customer ID:");
                            int customerId = scanner.nextInt();
                            System.out.println("Enter product ID:");
                            int productId = scanner.nextInt();

                            // Retrieve and print the available quantity of the product
                            String getProductQuantitySql = "SELECT pname,available_quantity FROM products WHERE prod_id = "
                                    + productId;
                            ResultSet rs = stmt.executeQuery(getProductQuantitySql);
                            int availableQuantity = 0;
                            String prdname;
                            if (rs.next()) {
                                prdname = rs.getString("pname");
                                availableQuantity = rs.getInt("available_quantity");
                                System.out.println(
                                        "Available quantity for product " + prdname + ": " + availableQuantity);
                            } else {
                                System.out.println("Product ID not found.");
                                rs.close();
                                break;
                            }
                            rs.close();

                            System.out.println("Enter quantity:");
                            int quantity = scanner.nextInt();
                            scanner.nextLine(); // consume the newline character
                            System.out.println("Enter status (Paid/Not Paid):");
                            String status = scanner.nextLine();
                            invoice.insertData(stmt, customerId, productId, quantity, status);
                            break;

                        case 2:
                            printColumnNames(stmt, tableName);
                            System.out.println("Enter SET clause (e.g., quantity = 10):");
                            String setClause = scanner.nextLine();
                            System.out.println("Enter WHERE clause (e.g., id = 1):");
                            String whereClause = scanner.nextLine();
                            invoice.updateData(stmt, setClause, whereClause);
                            break;
                        case 3:
                            System.out.println("Enter WHERE clause for deletion (e.g., id = 1):");
                            whereClause = scanner.nextLine();
                            invoice.deleteData(stmt, whereClause);
                            break;
                        case 4:
                            // View Data in Invoice Table
                            System.out.println("All Invoice Details for Customer (1) or Particular Customer (2)?");
                            int innerChoice = scanner.nextInt();
                            scanner.nextLine(); // Consume newline character after reading integer

                            if (innerChoice == 1) {
                                // Display all invoices for all customers
                                String sql = "SELECT * FROM Invoice";
                                ResultSet rsb = stmt.executeQuery(sql);
                                ResultSetMetaData rsmd = rsb.getMetaData();
                                int columnsNumber = rsmd.getColumnCount();
                                while (rsb.next()) {
                                    for (int i = 1; i <= columnsNumber; i++) {
                                        if (i > 1)
                                            System.out.print(",  ");
                                        String columnValue = rsb.getString(i);
                                        System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                    }
                                    System.out.println("");
                                }
                                rsb.close();
                            } else if (innerChoice == 2) {
                                // Display invoices for a particular customer
                                System.out.println("Enter customer ID or Name:");
                                String customerInput = scanner.nextLine();

                                try {
                                    String customerIdQuery = "";
                                    if (customerInput.matches("\\d+")) {
                                        // If input is a number, treat it as customer ID
                                        customerIdQuery = "customer_id = " + Integer.parseInt(customerInput);
                                    } else {
                                        // If input is not a number, treat it as customer name
                                        customerIdQuery = "customer_id IN (SELECT cust_id FROM customers WHERE cname = '"
                                                + customerInput + "')";
                                    }

                                    String sql = "SELECT * FROM Invoice WHERE " + customerIdQuery;
                                    ResultSet rsa = stmt.executeQuery(sql);
                                    ResultSetMetaData rsmd = rsa.getMetaData();
                                    int columnsNumber = rsmd.getColumnCount();

                                    boolean found = false;
                                    while (rsa.next()) {
                                        found = true;
                                        for (int i = 1; i <= columnsNumber; i++) {
                                            if (i > 1)
                                                System.out.print(",  ");
                                            String columnValue = rsa.getString(i);
                                            System.out.print(rsmd.getColumnName(i) + ": " + columnValue);
                                        }
                                        System.out.println("");
                                    }
                                    if (!found) {
                                        System.out.println("No invoices found for this customer.");
                                    }
                                    rsa.close();
                                } catch (SQLException e) {
                                    System.out.println("SQL error: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Invalid choice.");
                            }
                            break;

                        case 5:
                            return;
                        default:
                            System.out.println("Invalid choice, please try again.");
                    }
                    break;

                default:
                    System.out.println("Invalid table name, please try again.");
            }
        }
    }

    public static void printColumnNames(Statement stmt, String tableName) throws SQLException {
        String sql = "SELECT * FROM " + tableName + " LIMIT 1";
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        System.out.println("Available columns in " + tableName + ":");
        for (int i = 1; i <= columnCount; i++) {
            System.out.println(rsmd.getColumnName(i));
        }
        rs.close();
    }

    public static void generateInvoice(Statement stmt, int customerId) throws SQLException, IOException {
        String customerSql = "SELECT * FROM customers WHERE cust_id = " + customerId;
        ResultSet customerRs = stmt.executeQuery(customerSql);
        if (!customerRs.next()) {
            System.out.println("Customer not found.");
            return;
        }

        String customerName = customerRs.getString("cname");
        String customerContact = customerRs.getString("contactno");
        customerRs.close();

        String invoiceSql = "SELECT i.id, p.pname, i.quantity, p.unit, p.price, i.amount "
                + "FROM Invoice i JOIN products p ON i.product_id = p.prod_id "
                + "WHERE i.customer_id = " + customerId;
        ResultSet invoiceRs = stmt.executeQuery(invoiceSql);

        if (!invoiceRs.next()) {
            System.out.println("No invoices found for this customer.");
            return;
        }

        // Define the directory to save the invoice
        String directoryPath = "./invoices/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = directoryPath + "invoice_" + customerId + "_Customer_" + customerName + "_"
                + LocalDate.now() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(String.format("%100s\n\n", "ABC SUPERMARKET"));
            writer.write(String.format("%-10s %s\n", "Customer Name:", customerName));
            writer.write(String.format("%-10s %s%150s %s%26s\n", "Customer ID:", customerId, "Contact No:",
                    customerContact, ""));
            writer.write(String.format("%-10s %s%150s %s%26s\n", "Invoice No:", invoiceRs.getInt("id"), "Date:",
                    LocalDate.now(), ""));
            writer.write("\n\n\n");

            // Adding dashed line before the headings
            writer.write(String.format("%154s\n",
                    "----------------------------------------------------------------------------------------------------------------------------------------"));
            writer.write("\n");
            // Adding the headings
            writer.write(
                    String.format("%30s %30s %30s %30s %30s\n", "Product Name", "Quantity", "Unit", "Price", "Amount"));
            writer.write("\n");
            // Adding dashed line after the headings
            writer.write(String.format("%154s\n",
                    "----------------------------------------------------------------------------------------------------------------------------------------"));
            double totalAmount = 0;
            do {
                String productName = invoiceRs.getString("pname");
                int quantity = invoiceRs.getInt("quantity");
                String unit = invoiceRs.getString("unit");
                double price = invoiceRs.getDouble("price");
                double amount = invoiceRs.getDouble("amount");

                writer.write(
                        String.format("%30s %30d %30s %30.2f %30.2f\n", productName, quantity, unit, price, amount));
                totalAmount += amount;
            } while (invoiceRs.next());
            writer.write("\n");
            // Adding dashed line before the total amount
            writer.write(String.format("%154s\n",
                    "----------------------------------------------------------------------------------------------------------------------------------------"));

            // Adding the total amount
            writer.write(String.format("%147s %-30.2f\n", "Total Amount:", totalAmount));
        }

        System.out.println("Invoice generated successfully: " + fileName);
        invoiceRs.close();
    }

    public static void showSalesReport(Statement stmt, Scanner scanner) throws SQLException {
        System.out.println("Select an option:");
        System.out.println("1. Today's Sales Report");
        System.out.println("2. Sales Report for a Specific Date");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                showTodaySalesReport(stmt, scanner);
                break;
            case 2:
                System.out.println("Enter date (YYYY-MM-DD format):");
                String dateStr = scanner.nextLine();
                showSalesReportForDate(stmt, dateStr, scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public static void showTodaySalesReport(Statement stmt, Scanner scanner) throws SQLException {
        LocalDate today = LocalDate.now();
        String todayStr = today.toString();
        showSalesReportForDate(stmt, todayStr, scanner);
    }

    public static void showSalesReportForDate(Statement stmt, String dateStr, Scanner scanner) throws SQLException {
        StringBuilder report = new StringBuilder();
        try {
            String sql = "SELECT pname, quantity, amount " +
                    "FROM Invoice i JOIN products p ON i.product_id = p.prod_id " +
                    "WHERE date = '" + dateStr + "'";
            ResultSet rs = stmt.executeQuery(sql);

            // Print individual product sales details in a tabular format
            System.out.println("Sales Report for " + dateStr + ":");
            report.append("Sales Report for ").append(dateStr).append(":\n");
            System.out.println("+---------------------------------------------+");
            report.append("+----------------------+------------+-----------------+\n");
            System.out.printf("| %-20s | %-10s | %-15s |\n", "Product Name", "Units Sold", "Revenue");
            report.append(String.format("| %-20s | %-10s | %-15s |\n", "Product Name", "Units Sold", "Revenue"));
            System.out.println("+----------------------+------------+-----------------+");
            report.append("+----------------------+------------+-----------------+\n");

            while (rs.next()) {
                String productName = rs.getString("pname");
                int unitsSold = rs.getInt("quantity");
                double revenue = rs.getDouble("amount");

                System.out.printf("| %-20s | %-10d | %-15.2f |\n", productName, unitsSold, revenue);
                report.append(String.format("| %-20s | %-10d | %-15.2f |\n", productName, unitsSold, revenue));
            }
            System.out.println("+----------------------+------------+-----------------+");
            report.append("+----------------------+------------+-----------------+\n");

            // Fetch total units and revenue
            sql = "SELECT SUM(quantity) AS total_units, SUM(amount) AS total_revenue " +
                    "FROM Invoice " +
                    "WHERE date = '" + dateStr + "'";
            ResultSet totalRs = stmt.executeQuery(sql);
            if (totalRs.next()) {
                int totalUnits = totalRs.getInt("total_units");
                double totalRevenue = totalRs.getDouble("total_revenue");
                System.out.printf("Total Units Sold: %-10d Total Revenue Generated: Rs. %-15.2f\n", totalUnits,
                        totalRevenue);
                report.append(String.format("Total Units Sold: %-10d Total Revenue Generated: Rs. %-15.2f\n",
                        totalUnits, totalRevenue));
                System.out.println();
                report.append("\n");
            } else {
                System.out.println("No sales found for " + dateStr);
                report.append("No sales found for ").append(dateStr).append("\n");
                System.out.println();
                report.append("\n");
            }
            System.out.print("Do you want to download the report? (yes/no): ");
            String userResponse = scanner.nextLine().trim().toLowerCase();
            if (userResponse.equals("yes")) {
                downloadReport(report.toString(), dateStr);
                System.out.println("Report downloaded successfully.");
            } else {
                System.out.println("Report not downloaded.");
            }
        } catch (SQLException e) {
            System.out.println("SQL error: " + e.getMessage());
        }
    }

    public static void downloadReport(String report, String dateStr) {
        try {
            Path reportsDir = Paths.get("./salesreports");
            if (!Files.exists(reportsDir)) {
                Files.createDirectory(reportsDir);
            }
            Path reportFile = reportsDir.resolve("Sales_Report_" + dateStr + ".txt");
            Files.write(reportFile, report.getBytes());
        } catch (IOException e) {
            System.out.println("Error writing the report: " + e.getMessage());
        }
    }

}
