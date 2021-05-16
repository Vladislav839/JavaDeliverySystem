package by.bsu.deliveryshop.services;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Models.Customer;
import by.bsu.deliveryshop.data.Models.Delivery;
import by.bsu.deliveryshop.data.Models.DeliveryProduct;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DeliveryService {
    private final Dao<Delivery> deliveryDao;

    public DeliveryService(Class<?> clazz, String deliveryPath) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        if(deliveryPath == null) {
            this.deliveryDao = (Dao<Delivery>) ctor.newInstance(Delivery.class);
        }
        else if(!clazz.getName().contains("Json")) {
            this.deliveryDao = (Dao<Delivery>) ctor.newInstance(deliveryPath, Delivery.class);
        } else {
            this.deliveryDao = (Dao<Delivery>) ctor.newInstance(deliveryPath,
                    new TypeToken<List<Delivery>>(){}.getType());
        }
    }

    public void addDelivery(Delivery delivery) throws IOException, InvocationTargetException, IllegalAccessException, JAXBException, NoSuchMethodException, InstantiationException {
        deliveryDao.create(delivery);
    }

    public Delivery getDeliveryById(int id) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException {
        return deliveryDao.read(id);
    }

    public List<Delivery> getAllDeliveries() throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        return deliveryDao.readAll();
    }

    public List<Delivery> getUserDeliveries(int customerId) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        List<Delivery> deliveries = new ArrayList<>();
        for(Delivery delivery : deliveryDao.readAll()) {
            if(delivery.getCustomerId() == customerId) {
                deliveries.add(delivery);
            }
        }
        return deliveries;
    }
}
