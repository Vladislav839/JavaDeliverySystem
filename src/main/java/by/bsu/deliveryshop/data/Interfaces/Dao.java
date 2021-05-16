package by.bsu.deliveryshop.data.Interfaces;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Dao<T extends Identifiable> {
    void create(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException;
    T read(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException, NoSuchFieldException;
    void update(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException;
    void delete(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException;
    List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException;
}
