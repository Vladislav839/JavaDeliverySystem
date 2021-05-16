package by.bsu.deliveryshop.data.DaoClasses;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;
import com.google.gson.Gson;
import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.*;
import com.mongodb.connection.ClusterDescription;
import com.mongodb.operation.OrderBy;
import org.atteo.evo.inflector.English;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NoSqlDao<T extends Identifiable> implements Dao<T> {
    private Class<T> clazz;
    private DB database;
    private DBCollection collection;
    private Gson gson = new Gson();

    public NoSqlDao(Class<T> clazz) {
        this.clazz = clazz;
        MongoClient client = new MongoClient("localhost", 27017);
        this.database = client.getDB("deliverysystem");
        this.collection =  database.getCollection(clazz.getAnnotation(Table.class).name());
    }

    @Override
    public void create(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            List<Field> fields = new SqlDao<T>(clazz).getFields();
            BasicDBObject document = new BasicDBObject();
            String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();
            for (Field field: fields) {
                field.setAccessible(true);
                document.put(field.getName(), field.get(obj));
            }
            collection.insert(document);
            DBCursor cursor = collection.find();
            cursor.sort(new BasicDBObject("_id", OrderBy.DESC.getIntRepresentation()));
            DBObject object = cursor.next();
            String id = object.get("_id").toString();
            BasicDBObject query = new BasicDBObject();
            query.put("_id", new ObjectId(id));
            BasicDBObject newDocument = new BasicDBObject();
            if(id.hashCode() < 0) {
                newDocument.put(idName, id.hashCode() * -1);
            } else {
                newDocument.put(idName, id.hashCode());
            }
            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);

            collection.update(query, updateObject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public T read(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException, NoSuchFieldException {
        BasicDBObject searchQuery = new BasicDBObject();
        String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();
        searchQuery.put(idName, id);
        DBCursor cursor = collection.find(searchQuery);

        T obj = gson.fromJson(cursor.next().toString(), clazz);
        return obj;
    }

    @Override
    public void update(T obj) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            List<Field> fields = new SqlDao<T>(clazz).getFields();
            BasicDBObject searchQuery  = new BasicDBObject();
            String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();
            searchQuery.put(idName, obj.getId());

            BasicDBObject newDocument = new BasicDBObject();
            for(Field field : fields) {
                field.setAccessible(true);
                newDocument.put(field.getName(), field.get(obj));
            }

            BasicDBObject updateObject = new BasicDBObject();
            updateObject.put("$set", newDocument);

            collection.update(searchQuery, updateObject);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(int id) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        try {
            BasicDBObject searchQuery  = new BasicDBObject();
            String idName = clazz.getDeclaredField(clazz.getSimpleName().toLowerCase(Locale.ROOT) + "Id").getName();
            searchQuery.put(idName, id);
            collection.remove(searchQuery);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<T> readAll() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException, JAXBException {
        List<T> entities = new ArrayList<>();
        List<Field> fields = new SqlDao<T>(clazz).getFields();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {
            DBObject object = cursor.next();
            T obj = gson.fromJson(object.toString(), clazz);
            entities.add(obj);
        }
        return entities;
    }
}
