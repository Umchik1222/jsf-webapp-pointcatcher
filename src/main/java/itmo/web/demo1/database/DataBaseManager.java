package itmo.web.demo1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import itmo.web.demo1.database.models.Point;


public class DataBaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5433/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void createPointsTable(Connection connection) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS points ("
                + "ID SERIAL PRIMARY KEY, "
                + "x REAL NOT NULL, "
                + "y REAL NOT NULL,"
                + "r REAL NOT NULL, "
                + "result BOOLEAN NOT NULL"
                + ");";
        try (Statement statement = connection.createStatement()) {
            statement.execute(createTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertPoint(Connection connection, Point point) {
        String insertSQL = "INSERT INTO points (x, y, r, result) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setDouble(1, point.getX());
            preparedStatement.setDouble(2, point.getY());
            preparedStatement.setDouble(3, point.getR());
            preparedStatement.setBoolean(4, point.getResult());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Point> getLastPoints(Connection connection) {
        ArrayList<Point> points = new ArrayList<Point>();
        String query = "SELECT * FROM points ORDER BY id DESC LIMIT 100";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                double r = resultSet.getDouble("r");
                boolean result = resultSet.getBoolean("result");

                Point point = new Point(x, y, r, result);
                points.add(point);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return points;
    }
    public static void clearPoints(Connection connection) {
        String deleteSQL = "DELETE FROM points";
        try (Statement statement = connection.createStatement()) {
            int deletedRows = statement.executeUpdate(deleteSQL);
            System.out.println("Удалено точек: " + deletedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
