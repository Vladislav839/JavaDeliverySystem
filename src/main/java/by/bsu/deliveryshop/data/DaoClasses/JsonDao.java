package by.bsu.deliveryshop.data.DaoClasses;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonDao<T extends Identifiable> implements Dao<T> {
    private final static Object lock = new Object();
    private Gson gson;
    private String fileName;
    private Type listType;

    public JsonDao(String fileName, Type listType) {
        gson = new GsonBuilder().setPrettyPrinting().create();
        this.fileName = fileName;
        this.listType = listType;
    }

    @Override
    public List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        synchronized (lock) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            StringBuilder stringBuilder = new StringBuilder();
            if (file.exists()) {
                try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                } catch (IOException e) {
                    throw new IOException("File error");
                }
            }
            return gson.fromJson(stringBuilder.toString(), listType);
        }
    }

    private void write(List<T> entities) throws InvocationTargetException, IllegalAccessException, IOException {
        synchronized (lock) {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(file, false))) {
                printWriter.print(gson.toJson(entities.toArray()));
            } catch (IOException e) {
                throw new IOException("File error");
            }
        }
    }

    @Override
    public void create(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = this.readAll();
        int id = entities.size() - 1 >= 0 ? entities.get(entities.size() - 1).getId() + 1 : 1;
        obj.setId(id);
        entities.add(obj);
        this.write(entities);
    }

    @Override
    public T read(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = readAll();
        Optional<T> obj = entities.stream().filter(item -> item.getId() == id).findFirst();
        return obj.get();
    }

    @Override
    public void update(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = this.readAll();
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
        List<T> entities = this.readAll();
        for(int i = 0; i < entities.size(); i++) {
            if(entities.get(i).getId() == id) {
                entities.remove(i);
                break;
            }
        }
        write(entities);
    }
}
