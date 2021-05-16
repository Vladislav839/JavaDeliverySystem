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
@Table(name = "products")
public class Product implements Identifiable {
    private static int id = 1;
    @Column(name = "productId")
    private int productId;
    @Column(name = "name")
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "count")
    private int count;

    public Product() {
        productId = id++;
        this.name = null;
        this.price = 0.0;
        this.count = 0;
    }

    public Product(String name, double price, int count) {
        productId = id++;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    @CsvCreator
    public Product(int productId, String name, double price, int count) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.count = count;
    }

    @Override
    public String toString() {
        return Integer.toString(productId) + "," + name + ","
                + Double.toString(price) + "," + Integer.toString(count);
    }

    public void setProductId(int id) {
        this.productId = id;
    }

    @Override
    @CsvOrder(position = 1)
    public int getId() {
        return productId;
    }

    @CsvOrder(position = 2)
    public String getName() {
        return name;
    }

    @CsvOrder(position = 3)
    public double getPrice() {
        return price;
    }

    @CsvOrder(position = 4)
    public int getCount() {
        return count;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void setId(Integer id) {
        this.productId = id;
    }
}
