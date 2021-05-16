package by.bsu.deliveryshop.data.DaoClasses;

import by.bsu.deliveryshop.annotations.CsvCreator;
import by.bsu.deliveryshop.annotations.CsvOrder;
import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;
import by.bsu.deliveryshop.data.Models.Product;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CsvDao<T extends Identifiable> implements Dao<T> {
    private final static Object lock = new Object();
    private String fileName;
    private Class<T> clazz;

    public CsvDao(String fileName, Class<T> clazz) {
        this.fileName = fileName;
        this.clazz = clazz;
    }

    private String objectToCsv(T instance) throws InvocationTargetException, IllegalAccessException {
        TreeMap<Integer, Method> treeMap = new TreeMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        Class<T> superclass = (Class<T>) clazz.getSuperclass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(CsvOrder.class)) {
                CsvOrder csvOrder = method.getAnnotation(CsvOrder.class);
                treeMap.put(csvOrder.position(), method);
            }
        }
        if(superclass.getName() != "java.lang.Object") {
            Method[] inheredMethods = superclass.getDeclaredMethods();
            for (Method method : inheredMethods) {
                if (method.isAnnotationPresent(CsvOrder.class)) {
                    CsvOrder csvOrder = method.getAnnotation(CsvOrder.class);
                    treeMap.put(csvOrder.position(), method);
                }
            }
        }
        for (Map.Entry<Integer, Method> entry : treeMap.entrySet()) {
            stringBuilder.append(entry.getValue().invoke(instance));
            stringBuilder.append(',');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private T getObjectFromString(String info) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        Optional<Constructor> ctor = Arrays.stream(constructors).
                filter(c -> c.isAnnotationPresent(CsvCreator.class))
                .findFirst();
        if(ctor.isPresent()) {
            Type[] types = ctor.get().getParameterTypes();
            ArrayList<Object> ctorParams = new ArrayList<>();
            String[] attributes = info.split(",");
            for(int i = 0; i < attributes.length; i++) {
                if(types[i] == int.class) {
                    ctorParams.add(Integer.parseInt(attributes[i]));
                } else if(types[i] == double.class) {
                    ctorParams.add(Double.parseDouble(attributes[i]));
                } else if(types[i] == LocalDateTime.class) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    ctorParams.add(LocalDateTime.parse(attributes[i], formatter));
                } else if(types[i] == String.class) {
                    ctorParams.add(attributes[i]);
                }
            }
            Object instance = ctor.get().newInstance(ctorParams.toArray());
            return (T) instance;
        }
        return null;
    }

    @Override
    public List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        synchronized (lock) {
            ArrayList<T> entities = new ArrayList<T>();
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            if (file.exists()) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        entities.add(getObjectFromString(line));
                    }
                } catch (IOException e) {
                    throw new IOException("File error");
                }
            }
            return entities;
        }
    }

    private void write(List<T> entities) throws InvocationTargetException, IllegalAccessException, IOException {
        synchronized (lock) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, false))) {
                for (T entity : entities) {
                    printWriter.println(objectToCsv(entity));
                }
            } catch (IOException e) {
                throw new IOException("File error");
            }
        }
    }

    @Override
    public void create(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        int id = entities.size() - 1 >= 0 ? entities.get(entities.size() - 1).getId() + 1 : 1;
        obj.setId(id);
        entities.add(obj);
        write(entities);
    }

    @Override
    public T read(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        Optional<T> obj = entities.stream().filter(item -> item.getId() == id).findFirst();
        return obj.get();
    }

    @Override
    public void update(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        Optional<T> o = entities.stream()
                .filter(p -> p.getId() == obj.getId())
                .findFirst();
        if(o.isPresent()) {
           delete(obj.getId());
           entities.add(obj);
           write(entities);
        }
    }

    @Override
    public void delete(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i).getId() == id) {
                entities.remove(i);
                break;
            }
        }
        write(entities);
    }
}
