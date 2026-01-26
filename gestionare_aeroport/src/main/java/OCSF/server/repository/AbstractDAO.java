package OCSF.server.repository;//package DAO;

import OCSF.server.repository.ConnectionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.lang.*;
import java.util.stream.Collectors;
import java.lang.reflect.Modifier;

/**
 * Generic Data Access Object (DAO) providing basic CRUD operations for any entity type.
 *
 * <p>This class uses reflection to dynamically construct SQL queries and map ResultSet data
 * to objects of type T. It expects the entity class to have an 'id' field as a primary key.</p>
 *
 * @param <T> the type of the entity this DAO handles
 */
public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Constructs an AbstractDAO, inferring the class type T via reflection.
     */
    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * Creates a SELECT SQL query string to find an object by a given field.
     *
     * @param field the field name to be used in the WHERE clause
     * @return a SQL SELECT query string
     */
    private String createSelectQuery(String field) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        sb.append(" * ");
        sb.append(" FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " =?");
        return sb.toString();
    }

    /**
     * Creates a SELECT SQL query string to find all objects of type T.
     *
     * @return a SQL SELECT * query string
     */
    private String createSelectAllQuery() {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Creates an INSERT SQL query string using provided field names.
     *
     * @param fieldNames list of field names to insert
     * @return a SQL INSERT query string
     */
    private String createInsertQuery(List<String> fieldNames) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        sb.append(type.getSimpleName().toLowerCase());
        sb.append(" (");
        sb.append(String.join(", ", fieldNames));
        sb.append(") VALUES (");
        sb.append(fieldNames.stream().map(e -> "?").collect(Collectors.joining(", ")));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Creates an UPDATE SQL query string to update the specified fields of an object.
     *
     * @param fields list of field names to update
     * @param idField the name of the ID field for the WHERE clause
     * @return a SQL UPDATE query string
     */
    private String createUpdateQuery(List<String> fields, String idField) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ").append(type.getSimpleName()).append(" SET ");

        for (String field : fields) {
            sb.append(field).append("=?,");
        }
        sb.setLength(sb.length() - 1);
        sb.append(" WHERE ").append(idField).append("=?");

        return sb.toString();
    }


    /**
     * Creates a DELETE SQL query string to delete an object by ID.
     *
     * @param idField the name of the ID field
     * @return a SQL DELETE query string
     */
    private String createDeleteQuery(String idField) {
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ").append(type.getSimpleName());
        sb.append(" WHERE ").append(idField).append("=?");
        return sb.toString();
    }


    /**
     * Maps a JDBC ResultSet to a list of objects of type T by invoking setters via reflection.
     *
     * @param resultSet the ResultSet returned by a query
     * @return a list of mapped objects of type T
     */

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();



                for (Field field : type.getDeclaredFields()) {

                    if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                            java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
                            field.getName().endsWith("Cached")) {
                        continue;
                    }

                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();

                    if (value != null) {
                        Class<?> expectedType = method.getParameterTypes()[0];

                        if (Number.class.isAssignableFrom(value.getClass())) {
                            Number num = (Number) value;
                            if (expectedType == Long.class || expectedType == long.class) {
                                value = num.longValue();
                            } else if (expectedType == Integer.class || expectedType == int.class) {
                                value = num.intValue();
                            } else if (expectedType == Double.class || expectedType == double.class) {
                                value = num.doubleValue();
                            }
                        }

                        if (expectedType == java.time.LocalDateTime.class && value instanceof java.sql.Timestamp) {
                            value = ((java.sql.Timestamp) value).toLocalDateTime();
                        }
                        if (expectedType == java.time.LocalDate.class && value instanceof java.sql.Date) {
                            value = ((java.sql.Date) value).toLocalDate();
                        }
                    }

                    method.invoke(instance, value);
                }

                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }



    /**
     * Retrieves all objects of type T from the database.
     *
     * @return a list of all objects of type T, or null if an error occurs
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuery();

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    /**
     * Retrieves an object of type T by its ID.
     *
     * @param id the ID of the object to find
     * @return the object found by ID
     * @throws RuntimeException if no object is found or a database error occurs
     */
    public T findById(int id) throws RuntimeException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            List<T> results = createObjects(resultSet);
            if (results.isEmpty()) {
                throw new RuntimeException("No object found with the provided ID.");
            }
            return results.get(0);

        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findById " + e.getMessage());
            throw new RuntimeException("Error retrieving object by ID: " + e.getMessage(), e);
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    /**
     * Inserts a new object of type T into the database.
     *
     * @param t the object to insert
     * @return the inserted object with its ID set (if applicable)
     * @throws RuntimeException if an error occurs during insertion
     */
    public T insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();


        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);

            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                    java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
                    field.getName().endsWith("Cached")) {
                continue;
            }

            try {
                Object value = field.get(t);
                if (value != null) {
                    fieldNames.add(field.getName());
                    fieldValues.add(value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        String query = createInsertQuery(fieldNames);

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            for (int i = 0; i < fieldValues.size(); i++) {
                statement.setObject(i + 1, fieldValues.get(i));
            }

            statement.executeUpdate();

            Field idField = type.getDeclaredField("id");
            idField.setAccessible(true);
            Object idValue = idField.get(t);
            if (idValue == null || ((Integer) idValue) == 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    idField.set(t, generatedId);
                }
            }


        } catch (SQLException | NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:insert " + e.getMessage(), e);
            throw new RuntimeException("Error inserting into " + type.getSimpleName() + ": " + e.getMessage(), e);
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Updates an existing object of type T in the database.
     *
     * @param t the object with updated fields
     * @return the updated object
     * @throws RuntimeException if an error occurs during update
     */

    public T update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;

        List<String> fieldNames = new ArrayList<>();
        List<Object> fieldValues = new ArrayList<>();
        Object idValue = null;

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);


            if (java.lang.reflect.Modifier.isStatic(field.getModifiers()) ||
                    java.lang.reflect.Modifier.isFinal(field.getModifiers()) ||
                    field.getName().endsWith("Cached")) { // <--- ADD THIS LINE
                continue;
            }

            try {
                Object value = field.get(t);

                if ("id".equalsIgnoreCase(field.getName())) {
                    idValue = value;
                } else {

                    if (value != null) {
                        fieldNames.add(field.getName());
                        fieldValues.add(value);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (fieldNames.isEmpty()) {
            return t;
        }

        String query = createUpdateQuery(fieldNames, "id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);

            for (int i = 0; i < fieldValues.size(); i++) {
                statement.setObject(i + 1, fieldValues.get(i));
            }
            statement.setObject(fieldValues.size() + 1, idValue);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:update " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }

        return t;
    }

    /**
     * Deletes an object by its ID from the database.
     *
     * @param id the ID of the object to delete
     * @throws RuntimeException if an error occurs during deletion or no record is found
     */
    public void delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        String query = createDeleteQuery("id");

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No record found with the given ID.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:delete " + e.getMessage(), e);
            throw new RuntimeException("Error deleting from " + type.getSimpleName() + ": " + e.getMessage(), e);
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

}
