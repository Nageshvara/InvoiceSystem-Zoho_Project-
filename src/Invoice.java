import java.sql.*;

public class Invoice {
    Customer customer = new Customer();

    public void insertData(Statement stmt, int customerId, int productId, int quantity, String status)
            throws SQLException {
        String getProductSql = "SELECT price, available_quantity FROM products WHERE prod_id = " + productId;
        ResultSet rs = stmt.executeQuery(getProductSql);
        double price = 0;
        int availableQuantity = 0;
        if (rs.next()) {
            price = rs.getDouble("price");
            availableQuantity = rs.getInt("available_quantity");
        }
        rs.close();

        if (quantity <= availableQuantity) {
            double amount = quantity * price;

            String checkEmptySql = "SELECT COUNT(*) AS count FROM Invoice";
            ResultSet countRs = stmt.executeQuery(checkEmptySql);
            int rowCount = 0;
            if (countRs.next()) {
                rowCount = countRs.getInt("count");
            }
            countRs.close();

            if (rowCount == 0) {
                String resetAutoIncrementSql = "ALTER TABLE Invoice AUTO_INCREMENT = 1";
                stmt.executeUpdate(resetAutoIncrementSql);
            }

            String insertSql = "INSERT INTO Invoice (customer_id, product_id, quantity, amount, status, date) VALUES ("
                    + customerId + ", " + productId + ", " + quantity + ", " + amount + ", '" + status + "',"
                    + "NOW())";
            int rowsAffected = stmt.executeUpdate(insertSql);
            System.out.println(rowsAffected + " rows inserted into Invoice table successfully.");

            int remainingQuantity = availableQuantity - quantity;
            String updateProductSql = "UPDATE products SET available_quantity = " + remainingQuantity
                    + " WHERE prod_id = "
                    + productId;
            stmt.executeUpdate(updateProductSql);
            System.out.println("Product quantity updated successfully.");

            customer.updateTotalPurchasePaidAndRemainingBalance(stmt, customerId);
        } else {
            System.out.println("Quantity is exceeding the available quantity.");
        }
    }

    public void updateData(Statement stmt, String setClause, String whereClause) throws SQLException {

        String sql = "UPDATE Invoice SET " + setClause + " WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows updated in Invoice table successfully.");

        String[] whereParts = whereClause.split("=");
        String idStr = whereParts[1].trim();
        int invoiceId = Integer.parseInt(idStr);

        String selectSql = "SELECT customer_id, product_id FROM Invoice WHERE id = " + invoiceId;
        ResultSet rs = stmt.executeQuery(selectSql);
        int customerId = 0;
        int productId = 0;
        if (rs.next()) {
            customerId = rs.getInt("customer_id");
            productId = rs.getInt("product_id");
        }
        rs.close();

        if (setClause.contains("quantity")) {
            String getPriceSql = "SELECT price FROM products WHERE prod_id = " + productId;
            rs = stmt.executeQuery(getPriceSql);
            double price = 0;
            if (rs.next()) {
                price = rs.getDouble("price");
            }
            rs.close();

            String[] setParts = setClause.split("=");
            String newQuantityStr = setParts[1].trim();
            int newQuantity = Integer.parseInt(newQuantityStr);

            double newAmount = newQuantity * price;

            String updateAmountSql = "UPDATE Invoice SET amount = " + newAmount + " WHERE id = " + invoiceId;
            stmt.executeUpdate(updateAmountSql);
        }
        customer.updateTotalPurchasePaidAndRemainingBalance(stmt, customerId);
    }

    public void deleteData(Statement stmt, String whereClause) throws SQLException {

        String sql = "DELETE FROM Invoice WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows deleted from Invoice table successfully.");

        String[] whereParts = whereClause.split("=");
        String idStr = whereParts[1].trim();
        int invoiceId = Integer.parseInt(idStr);

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
