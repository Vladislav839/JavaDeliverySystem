package by.bsu.deliveryshop.data.DaoClasses;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;
import by.bsu.deliveryshop.utils.Wrapper;
import org.atteo.evo.inflector.English;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class XmlDao<T extends Identifiable> implements Dao<T> {
    private Class<T> clazz;
    private String fileName;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public XmlDao(String fileName, Class<T> clazz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Wrapper.class, clazz);
        marshaller = context.createMarshaller();
        unmarshaller = context.createUnmarshaller();
        this.fileName = fileName;
        this.clazz = clazz;
    }
    @Override
    public List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        return unmarshal(unmarshaller, clazz, fileName);
    }

    private void write(List<T> entities) throws InvocationTargetException, IllegalAccessException, IOException, JAXBException {
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshal(marshaller, entities, clazz.getSimpleName());
    }

    private <T> List<T> unmarshal(Unmarshaller unmarshaller,
                                         Class<T> clazz, String xmlLocation) throws JAXBException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        StreamSource xml = new StreamSource(file);
        Wrapper<T> wrapper = (Wrapper<T>) unmarshaller.unmarshal(xml,
                Wrapper.class).getValue();
        return wrapper.getItems();
    }

    private void marshal(Marshaller marshaller, List<?> list, String name)
            throws JAXBException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        QName qName = new QName(English.plural(lowerCaseFirstLetter(name), 2));
        Wrapper wrapper = new Wrapper(list);
        JAXBElement<Wrapper> jaxbElement = new JAXBElement<Wrapper>(qName,
                Wrapper.class, wrapper);
        marshaller.marshal(jaxbElement, file);
    }

    private String lowerCaseFirstLetter(String input) {
        return Character.toLowerCase(input.charAt(0)) +
                (input.length() > 1 ? input.substring(1) : "");
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
