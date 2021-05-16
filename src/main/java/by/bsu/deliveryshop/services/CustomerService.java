package by.bsu.deliveryshop.services;

import by.bsu.deliveryshop.data.Interfaces.Dao;
import by.bsu.deliveryshop.data.Models.Customer;
import com.google.gson.reflect.TypeToken;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class CustomerService {
    private final Dao<Customer> customerDao;

    public CustomerService(Class<?> clazz, String connectionString) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<?> ctor = clazz.getDeclaredConstructors()[0];
        if(clazz.getName().contains("Sql") && connectionString == null) {
            this.customerDao = (Dao<Customer>) ctor.newInstance(Customer.class);
        }
        else if(!clazz.getName().contains("Json")) {
            this.customerDao = (Dao<Customer>) ctor.newInstance(connectionString, Customer.class);
        } else {
            this.customerDao = (Dao<Customer>) ctor.newInstance(connectionString,
                    new TypeToken<List<Customer>>(){}.getType());
        }
    }

    public void addCustomer(Customer customer) throws IOException, InvocationTargetException, IllegalAccessException, JAXBException, NoSuchMethodException, InstantiationException {
       customerDao.create(customer);
    }

    public Customer getCustomerById(int id) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException, NoSuchFieldException {
        return customerDao.read(id);
    }

    public List<Customer> getAllCustomers() throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        return customerDao.readAll();
    }

    public Optional<Customer> getCustomerByName(String name) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        List<Customer> customers = customerDao.readAll();
        return customers.stream().filter(cus -> cus.getName().equals(name))
                .findFirst();
    }

    public boolean CustomerExists(String login) throws NoSuchMethodException, InstantiationException, JAXBException, IllegalAccessException, InvocationTargetException, IOException {
        List<Customer> customers = customerDao.readAll();
        Optional<Customer> customer = customers.stream().
                filter(cus -> cus.getName().equals(login))
                .findFirst();
        return customer.isPresent();
    }
}
