import java.sql.*;

public class Customer {

    public void insertData(Statement stmt, String name, int age, String contactNo) throws SQLException {
        String sql = "INSERT INTO customers (cname, cage, contactno) VALUES ('" + name + "', " + age + ", '" + contactNo
                + "')";
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows inserted into customers table successfully.");
    }

    public void updateData(Statement stmt, String setClause, String whereClause) throws SQLException {

        if (setClause.contains("TOTAL_PURCHASES") || setClause.contains("Paid")
                || setClause.contains("Remaining_Balance")) {
            if (setClause.contains("TOTAL_PURCHASES")) {
                System.out.println(
                        "The TOTAL_PURCHASES column is updated automatically according to your invoices, you can't update it.");
            }
            if (setClause.contains("Paid") || setClause.contains("Remaining_Balance")) {
                System.out.println(
                        "The Paid and Remaining_Balance columns are related to your purchases, you can't update them!");
            }
            return; 
        }

        String sql = "UPDATE customers SET " + setClause + " WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows updated in customers table successfully.");
    }

    public void deleteData(Statement stmt, String whereClause) throws SQLException {
        String sql = "DELETE FROM customers WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows deleted from customers table successfully.");
    }

    public void updateTotalPurchasePaidAndRemainingBalance(Statement stmt, int custId) throws SQLException {
        String sqlPaid = "UPDATE customers c "
                + "SET c.Paid = (SELECT COALESCE(SUM(i.amount), 0) FROM invoice i WHERE i.customer_id = c.cust_id AND i.status = 'Paid') "
                + "WHERE c.cust_id = " + custId;
        stmt.executeUpdate(sqlPaid);

        String sqltotalPurchases = "UPDATE customers c "
                + "SET c.TOTAL_PURCHASES = (SELECT COUNT(*) FROM invoice i WHERE i.customer_id = c.cust_id) "
                + "WHERE c.cust_id = " + custId;
        stmt.executeUpdate(sqltotalPurchases);

        String sqlRemainingBalance = "UPDATE customers c "
                + "SET c.Remaining_Balance = (SELECT COALESCE(SUM(i.amount), 0) FROM invoice i WHERE i.customer_id = c.cust_id AND i.status = 'Not Paid') "
                + "WHERE c.cust_id = " + custId;
        stmt.executeUpdate(sqlRemainingBalance);
    }
}
