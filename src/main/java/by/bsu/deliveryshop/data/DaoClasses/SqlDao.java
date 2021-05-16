package by.bsu.deliveryshop.data.DaoClasses;

import by.bsu.deliveryshop.annotations.CsvCreator;
import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SqlDao<T extends Identifiable> implements Dao<T> {
    private static final String URL = "jdbc:mysql://localhost/deliverysystem?useUnicode=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "project";

    private Class<T> clazz;

    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SqlDao(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void create(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            Statement statement = connection.createStatement();

            String tableName = clazz.getAnnotation(Table.class).name();
            String SQL = "INSERT INTO " + tableName + " (";

            List<Field> fieldsToGet = getFields();

            for (Field field: fieldsToGet) {
                if(!field.getName().contains("Id")) {
                    SQL += field.getName() + ",";
                }
            }

            SQL = SQL.substring(0, SQL.length() - 1);
            SQL += ") VALUES (";

            for (Field field: fieldsToGet) {
                if(!field.getName().contains("Id")) {
                    field.setAccessible(true);
                    Object res = field.get(obj);
                    if (res.getClass() == Integer.class || res.getClass() == Double.class) {
                        SQL += Objects.toString(res) + ",";
                    } else if (res.getClass() == String.class || res.getClass() == LocalDateTime.class) {
                        SQL += "'" + res.toString() + "',";
                    }
                }
            }

            SQL = SQL.substring(0, SQL.length() - 1);
            SQL += ")";
            statement.executeUpdate(SQL);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public T read(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        return entities.stream().filter(e -> e.getId() == id).findFirst().get();
    }

    @Override
    public void update(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            Statement statement = connection.createStatement();
            String tableName = clazz.getAnnotation(Table.class).name();
            String SQL = "UPDATE " + tableName + " SET ";

            List<Field> fieldsToGet = getFields();

            for (Field field: fieldsToGet) {
                if(!field.getName().contains("Id")) {
                    field.setAccessible(true);
                    Object res = field.get(obj);
                    if (res.getClass() == Integer.class || res.getClass() == Double.class) {
                        SQL += field.getName() + "=" + res + ",";
                    } else if (res.getClass() == String.class || res.getClass() == LocalDateTime.class) {
                        SQL += field.getName() + "='" + res + "',";
                    }
                }
            }

            SQL = SQL.substring(0, SQL.length() - 1);
            SQL += " WHERE ";

            String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();

            SQL += idName + "=" + obj.getId();

            statement.executeUpdate(SQL);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            String tableName = clazz.getAnnotation(Table.class).name();
            String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("DELETE FROM " + tableName + " WHERE " + idName + "=?");

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = new ArrayList<>();

        try {
            String tableName = clazz.getAnnotation(Table.class).name();
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM " + tableName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Constructor[] constructors = clazz.getDeclaredConstructors();
                Optional<Constructor> ctor = Arrays.stream(constructors).
                        filter(c -> c.getParameterCount() == 0)
                        .findFirst();

                T instance = (T)ctor.get().newInstance();

                List<Field> fieldToSet = getFields();

                for (Field field: fieldToSet) {
                    field.setAccessible(true);
                    if(field.getType() == int.class) {
                        field.set(instance, resultSet.getInt(field.getName()));
                    } else if(field.getType() == String.class) {
                        field.set(instance, resultSet.getString(field.getName()));
                    } else if(field.getType() == double.class) {
                        field.set(instance, resultSet.getDouble(field.getName()));
                    } else if(field.getType() == LocalDateTime.class) {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        field.set(instance, LocalDateTime.parse(resultSet.getString(field.getName()), formatter));
                    }
                }

                entities.add(instance);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return entities;
    }

    public List<Field> getFields() {
        List<Field> fieldToSet = new ArrayList<>();

        Class<?> superclass = clazz.getSuperclass();
        if(superclass.getName() != "java.lang.Object") {
            Field[] fields = superclass.getDeclaredFields();
            for(Field field : fields) {
                if(field.isAnnotationPresent(Column.class)) {
                    fieldToSet.add(field);
                }
            }
        }

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(field.isAnnotationPresent(Column.class)) {
                fieldToSet.add(field);
            }
        }
        return fieldToSet;
    }
}
