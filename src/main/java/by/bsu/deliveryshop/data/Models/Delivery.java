package by.bsu.deliveryshop.data.Models;

import by.bsu.deliveryshop.annotations.CsvCreator;
import by.bsu.deliveryshop.annotations.CsvOrder;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;
import by.bsu.deliveryshop.utils.LocalDateTimeAdapter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "deliveries")
public class Delivery implements Identifiable {
    private static int id = 1;
    @Column(name = "deliveryId")
    private int deliveryId;
    @Column(name = "customerId")
    private int customerId;
    private transient Customer customer;
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    @Column(name = "date")
    private LocalDateTime date;
    @Column(name = "totalPrice")
    private double totalPrice;
    private transient List<DeliveryProduct> deliveryProducts;

    public Delivery() {
        this.deliveryId = id++;
        this.customerId = 0;
        this.date = null;
        this.totalPrice = 0;
        deliveryProducts = null;
    }

    public Delivery(int customerId, LocalDateTime date, double totalPrice) {
        this.deliveryId = id++;
        this.customerId = customerId;
        this.date = date;
        this.totalPrice = totalPrice;
        deliveryProducts = new ArrayList<>();
    }

    @CsvCreator
    public Delivery(int deliveryId, int customerId, LocalDateTime date, double totalPrice) {
        this.deliveryId = deliveryId;
        this.customerId = customerId;
        this.date = date;
        this.totalPrice = totalPrice;
        deliveryProducts = new ArrayList<>();
    }

    public Delivery(int customerId, Customer customer, LocalDateTime date) {
        deliveryId = id++;
        this.customerId = customerId;
        this.customer = customer;
        this.date = date;
    }

    public void setDeliveryId(int id) {
        this.deliveryId = id;
    }

    @Override
    @CsvOrder(position = 1)
    public int getId() {
        return deliveryId;
    }

    @CsvOrder(position = 2)
    public int getCustomerId() {
        return customerId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @CsvOrder(position = 3)
    public String getDateAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    @CsvOrder(position = 4)
    public double getTotalPrice() {
        return totalPrice;
    }

    public List<DeliveryProduct> getDeliveryProducts() {
        return deliveryProducts;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setDeliveryProducts(List<DeliveryProduct> deliveryProducts) {
        this.deliveryProducts = deliveryProducts;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Integer.toString(deliveryId) + "," + Integer.toString(customerId) + ","
                + date.format(formatter) + "," + Double.toString(totalPrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return deliveryId == delivery.deliveryId &&
                customerId == delivery.customerId &&
                Double.compare(delivery.totalPrice, totalPrice) == 0 &&
                Objects.equals(customer, delivery.customer) &&
                Objects.equals(date, delivery.date) &&
                Objects.equals(deliveryProducts, delivery.deliveryProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deliveryId, customerId, customer, date, totalPrice, deliveryProducts);
    }

    @Override
    public void setId(Integer id) {
        this.deliveryId = id;
    }
}
