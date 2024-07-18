import java.sql.*;

public class Product {

    public void insertData(Statement stmt, String name, int price, int availableQuantity, String unit)
            throws SQLException {
        String sql = "INSERT INTO products (pname, price, available_quantity, unit) VALUES ('" + name + "', " + price
                + ", "
                + availableQuantity + ", '" + unit + "')";
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows inserted into products table successfully.");
    }

    public void updateData(Statement stmt, String setClause, String whereClause) throws SQLException {
        String sql = "UPDATE products SET " + setClause + " WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows updated in products table successfully.");
    }

    public void deleteData(Statement stmt, String whereClause) throws SQLException {
        String sql = "DELETE FROM products WHERE " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);
        System.out.println(rowsAffected + " rows deleted from products table successfully.");
    }
}
