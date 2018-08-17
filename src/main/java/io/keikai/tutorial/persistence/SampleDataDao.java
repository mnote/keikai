package io.keikai.tutorial.persistence;

import org.hsqldb.cmdline.*;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.*;

/**
 * Create adn close a connection for every query.
 */
public class SampleDataDao {
    /**
     * http://hsqldb.org/doc/guide/dbproperties-chapt.html
     * shutdown=true, Automatic Shutdown, shut down the database when the last connection is closed
     */
    public static final String HSQLDB_CONNECTION_STRING = "jdbc:hsqldb:file:database/tutorial;shutdown=true";
    static String TABLE_NAME = "tutorial";

    static public void initDatabase() {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
            try (Connection con = createConnection();) {
                executeSqlFile(con);
                System.out.println("-> initialized the database");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Connection createConnection() {
        try {
            return DriverManager.getConnection(HSQLDB_CONNECTION_STRING, "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    static private void executeSqlFile(Connection con) throws IOException, URISyntaxException, SqlToolError, SQLException {
        File inputFile = new File(SampleDataDao.class.getResource("/tutorial.sql").toURI());
        SqlFile file = new SqlFile(inputFile);
        file.setConnection(con);
        file.execute();
    }

    static public List<Expense> queryAll() throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection con = createConnection();
             Statement statement = con.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery(sql);
        ) {
            LinkedList<Expense> list = new LinkedList<>();
            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setId(resultSet.getInt("id"));
                expense.setCategory(resultSet.getString("category"));
                expense.setQuantity(resultSet.getInt("quantity"));
                expense.setSubtotal(resultSet.getInt("subtotal"));
                list.add(expense);
            }
            return list;
        }
    }

    static public List<Expense> queryByCategory() {
        String sql = "SELECT category, sum(quantity) as quantity, sum(subtotal) as subtotal FROM " + TABLE_NAME + " GROUP BY category";
        LinkedList<Expense> list = new LinkedList<>();
        try (Connection con = createConnection();
             Statement statement = con.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
        ) {
            while (resultSet.next()) {
                Expense expense = new Expense();
                expense.setCategory(resultSet.getString("category"));
                expense.setQuantity(resultSet.getInt("quantity"));
                expense.setSubtotal(resultSet.getInt("subtotal"));
                list.add(expense);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    static public void insert(Expense expense) {
        String sql = "INSERT INTO " + TABLE_NAME + " (category, quantity, subtotal) VALUES( ?, ?, ?)";
        try (Connection con = createConnection();
             PreparedStatement statement = con.prepareStatement(sql);
        ) {
            statement.setString(1, expense.getCategory());
            statement.setInt(2, expense.getQuantity());
            statement.setInt(3, expense.getSubtotal());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}