package by.bsu.deliveryshop.services;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Models.Product;
import by.bsu.deliveryshop.data.Models.Seller;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class SellerService {
    private final Dao<Seller> sellerDao;

    public SellerService(Class<?> clazz, String sellersPath) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        if(sellersPath == null) {
            this.sellerDao = (Dao<Seller>) ctor.newInstance(Seller.class);
        }
        else if(!clazz.getName().contains("Json")) {
            this.sellerDao = (Dao<Seller>) ctor.newInstance(sellersPath, Seller.class);
        } else {
            this.sellerDao = (Dao<Seller>) ctor.newInstance(sellersPath,
                    new TypeToken<List<Seller>>(){}.getType());
        }
    }

    public void addSeller(Seller seller) throws IOException, InvocationTargetException, IllegalAccessException, JAXBException, NoSuchMethodException, InstantiationException {
        sellerDao.create(seller);
    }

    public Seller getSellerById(int id) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException {
        return sellerDao.read(id);
    }

    public Optional<Seller> getSellerByName(String name) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        List<Seller> sellers = sellerDao.readAll();
        return sellers.stream().filter(sel -> sel.getName().equals(name))
                .findFirst();
    }

    public List<Seller> getAllSellers() throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        return sellerDao.readAll();
    }

    public boolean CustomerExists(String login) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        List<Seller> customers = sellerDao.readAll();
        Optional<Seller> customer = customers.stream().
                filter(cus -> cus.getName().equals(login))
                .findFirst();
        return customer.isPresent();
    }
}
