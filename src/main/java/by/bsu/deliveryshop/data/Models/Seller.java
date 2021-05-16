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
@Table(name = "sellers")
public class Seller extends User implements Identifiable {
    private static int id = 1;
    @Column(name = "sellerId")
    private int sellerId;

    public Seller() {
        this.sellerId = id++;
    }

    public Seller(String name, String password) {
        super(name, password);
        this.sellerId = id++;
    }

    @CsvCreator
    public Seller(int sellerId, String name, String password) {
        super(name, password);
        this.sellerId = sellerId;
    }
    public void setSellerId(int id) {
        this.id = id;
    }

    @Override
    @CsvOrder(position = 1)
    public int getId() {
        return sellerId;
    }

    @Override
    public String toString() {
        return Integer.toString(sellerId) + ","
                + name + "," + password;
    }

    @Override
    public void setId(Integer id) {
        this.sellerId = id;
    }
}
