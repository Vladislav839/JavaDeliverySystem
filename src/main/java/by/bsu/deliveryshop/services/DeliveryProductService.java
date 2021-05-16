package by.bsu.deliveryshop.services;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Models.Customer;
import by.bsu.deliveryshop.data.Models.DeliveryProduct;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class DeliveryProductService {
    private final Dao<DeliveryProduct> deliveryProductDao;

    public DeliveryProductService(Class<?> clazz, String deliveryProductsPath) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        if(deliveryProductsPath == null) {
            this.deliveryProductDao = (Dao<DeliveryProduct>) ctor.newInstance(DeliveryProduct.class);
        }
        else  if(!clazz.getName().contains("Json")) {
            this.deliveryProductDao = (Dao<DeliveryProduct>) ctor.newInstance(deliveryProductsPath, DeliveryProduct.class);
        } else {
            this.deliveryProductDao = (Dao<DeliveryProduct>) ctor.newInstance(deliveryProductsPath,
                    new TypeToken<List<DeliveryProduct>>(){}.getType());
        }
    }

    public void add(DeliveryProduct deliveryProduct) throws IOException, InvocationTargetException, IllegalAccessException, JAXBException, NoSuchMethodException, InstantiationException {
        deliveryProductDao.create(deliveryProduct);
    }

    public List<DeliveryProduct> getAllDeliveryProducts() throws IOException, NoSuchMethodException, JAXBException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return deliveryProductDao.readAll();
    }
}
