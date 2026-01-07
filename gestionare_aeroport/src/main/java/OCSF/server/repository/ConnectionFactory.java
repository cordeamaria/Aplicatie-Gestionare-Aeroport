package OCSF.server.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;

/**
 * The {@code ConnectionFactory} class provides methods to establish and manage
 * connections to a MySQL database. It uses a singleton pattern to ensure only
 * one instance is used for connection creation.
 */
public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
//    private static final String DBURL = "jdbc:mysql://localhost:3306/storedb";
    private static final String DBURL = "jdbc:mysql://localhost:3306/gestionare_aeroport?allowPublicKeyRetrieval=true&useSSL=false";
    private static final String USER = "root";
    private static final String PASS = "parola";
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Private constructor that loads the JDBC driver.
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new database connection using the configured parameters.
     *
     * @return a {@code Connection} to the database, or {@code null} if the connection fails
     */
    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "An error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Provides a new {@code Connection} to the database.
     *
     * @return a {@code Connection} instance
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Closes the given {@code Connection} if it is not null.
     *
     * @param connection the connection to be closed
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
            }
        }
    }

    /**
     * Closes the given {@code Statement} if it is not null.
     *
     * @param statement the statement to be closed
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Closes the given {@code ResultSet} if it is not null.
     *
     * @param resultSet the result set to be closed
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }
}
