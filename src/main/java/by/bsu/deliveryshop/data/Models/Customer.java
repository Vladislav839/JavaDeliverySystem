package by.bsu.deliveryshop.data.Models;

import by.bsu.deliveryshop.annotations.CsvCreator;
import by.bsu.deliveryshop.annotations.CsvOrder;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "customers")
public class Customer extends User implements Identifiable {
    private static int id = 1;
    @Column(name = "customerId")
    private int customerId;
    @Column(name = "address")
    private String address;

    public Customer() {
        super();
        this.customerId = id++;
        this.address = null;
    }

    public Customer(String name, String password, String address) {
        super(name, password);
        this.customerId = id++;
        this.address = address;
    }

    @CsvCreator
    public Customer(int customerId, String name, String password, String address) {
        super(name, password);
        this.customerId = customerId;
        this.address = address;
    }

    @Override
    @CsvOrder(position = 1)
    public int getId() {
        return customerId;
    }

    public void setCustomerId(int id) {
        this.customerId = id;
    }

    @CsvOrder(position = 4)
    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return Integer.toString(customerId) + ","
                + name + "," + password + "," + address;
    }

    @Override
    public void setId(Integer id) {
        this.customerId = id;
    }
}
