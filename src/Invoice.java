import java.sql.*;

public class Invoice {
    Customer customer = new Customer();

    public void insertData(Statement stmt, int customerId, int productId, int quantity, String status)
            throws SQLException {
        // Retrieve the price and available quantity of the product
        String getProductSql = "SELECT price, available_quantity FROM products WHERE prod_id = " + productId;
        ResultSet rs = stmt.executeQuery(getProductSql);
        double price = 0;
        int availableQuantity = 0;
        if (rs.next()) {
            price = rs.getDouble("price");
            availableQuantity = rs.getInt("available_quantity");
        }
        rs.close();

        // Check if the requested quantity is available
        if (quantity <= availableQuantity) {
            // Calculate the amount
            double amount = quantity * price;

            // Check if the Invoice table is empty
            String checkEmptySql = "SELECT COUNT(*) AS count FROM Invoice";
            ResultSet countRs = stmt.executeQuery(checkEmptySql);
            int rowCount = 0;
            if (countRs.next()) {
                rowCount = countRs.getInt("count");
            }
            countRs.close();

            // If the table is empty, reset the auto-increment value
            if (rowCount == 0) {
                String resetAutoIncrementSql = "ALTER TABLE Invoice AUTO_INCREMENT = 1";
                stmt.executeUpdate(resetAutoIncrementSql);
            }

            // Insert the invoice data
            String insertSql = "INSERT INTO Invoice (customer_id, product_id, quantity, amount, status, date) VALUES ("
                    + customerId + ", " + productId + ", " + quantity + ", " + amount + ", '" + status + "',"
                    + "NOW())";
            int rowsAffected = stmt.executeUpdate(insertSql);
            System.out.println(rowsAffected + " rows inserted into Invoice table successfully.");

            // Update the remaining quantity in the products table
            int remainingQuantity = availableQuantity - quantity;
            String updateProductSql = "UPDATE products SET available_quantity = " + remainingQuantity
                    + " WHERE prod_id = "
                    + productId;
            stmt.executeUpdate(updateProductSql);
            System.out.println("Product quantity updated successfully.");

            // Update customer totals
            customer.updateTotalPurchasePaidAndRemainingBalance(stmt, customerId);
        } else {
            System.out.println("Quantity is exceeding the available quantity.");
        }
    }

    public void updateData(Statement stmt, String setClause, String whereClause) throws SQLException {
        // Update Invoice table
        String sql = "UPDATE Invoice SET " + setClause + " WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows updated in Invoice table successfully.");

        // Extract customerId and productId from whereClause
        String[] whereParts = whereClause.split("=");
        String idStr = whereParts[1].trim();
        int invoiceId = Integer.parseInt(idStr);

        // Retrieve customerId and productId from the Invoice table
        String selectSql = "SELECT customer_id, product_id FROM Invoice WHERE id = " + invoiceId;
        ResultSet rs = stmt.executeQuery(selectSql);
        int customerId = 0;
        int productId = 0;
        if (rs.next()) {
            customerId = rs.getInt("customer_id");
            productId = rs.getInt("product_id");
        }
        rs.close();

        // Check if quantity is updated
        if (setClause.contains("quantity")) {
            // Retrieve the price of the product
            String getPriceSql = "SELECT price FROM products WHERE prod_id = " + productId;
            rs = stmt.executeQuery(getPriceSql);
            double price = 0;
            if (rs.next()) {
                price = rs.getDouble("price");
            }
            rs.close();

            // Retrieve the new quantity
            String[] setParts = setClause.split("=");
            String newQuantityStr = setParts[1].trim();
            int newQuantity = Integer.parseInt(newQuantityStr);

            // Calculate the new amount
            double newAmount = newQuantity * price;

            // Update the amount in the Invoice table
            String updateAmountSql = "UPDATE Invoice SET amount = " + newAmount + " WHERE id = " + invoiceId;
            stmt.executeUpdate(updateAmountSql);
        }
        customer.updateTotalPurchasePaidAndRemainingBalance(stmt, customerId);
    }

    public void deleteData(Statement stmt, String whereClause) throws SQLException {
        // Delete from Invoice table
        String sql = "DELETE FROM Invoice WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows deleted from Invoice table successfully.");

        // Extract customerId from whereClause
        String[] whereParts = whereClause.split("=");
        String idStr = whereParts[1].trim();
        int invoiceId = Integer.parseInt(idStr);

        // Retrieve customerId from the Invoice table before deletion
        String selectSql = "SELECT customer_id FROM Invoice WHERE id = " + invoiceId;
        ResultSet rs = stmt.executeQuery(selectSql);
        int customerId = 0;
        if (rs.next()) {
            customerId = rs.getInt("customer_id");
        }
        rs.close();
        customer.updateTotalPurchasePaidAndRemainingBalance(stmt, customerId);
    }
}
