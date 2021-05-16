package by.bsu.deliveryshop.services;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Models.Delivery;
import by.bsu.deliveryshop.data.Models.Product;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class ProductService {
    private final Dao<Product> productDao;

    public ProductService(Class<?> clazz, String productsPath) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        if(productsPath == null) {
            this.productDao = (Dao<Product>) ctor.newInstance(Product.class);
        }
        else if(!clazz.getName().contains("Json")) {
            this.productDao = (Dao<Product>) ctor.newInstance(productsPath, Product.class);
        } else {
            this.productDao = (Dao<Product>) ctor.newInstance(productsPath,
                    new TypeToken<List<Product>>(){}.getType());
        }
    }

    public void addProduct(Product product) throws IOException, InvocationTargetException, IllegalAccessException, JAXBException, NoSuchMethodException, InstantiationException {
        productDao.create(product);
    }

    public Product getProductById(int id) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException {
        return productDao.read(id);
    }

    public List<Product> getAllProducts() throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        return productDao.readAll();
    }

    public void deleteProduct(int id) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        productDao.delete(id);
    }

    public void updateProduct(Product updatedProduct) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        productDao.update(updatedProduct);
    }
}
