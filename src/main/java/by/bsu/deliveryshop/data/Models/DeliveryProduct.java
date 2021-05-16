package by.bsu.deliveryshop.data.Models;

import by.bsu.deliveryshop.annotations.CsvCreator;
import by.bsu.deliveryshop.annotations.CsvOrder;
import by.bsu.deliveryshop.data.Interfaces.Identifiable;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Table(name = "deliveryproducts")
public class DeliveryProduct implements Identifiable {
    private static int id = 1;
    @Column(name = "deliveryProductId")
    private int deliveryProductId;
    @Column(name = "productId")
    private int productId;
    @Column(name = "deliveryId")
    private int deliveryId;
    @Column(name = "productCount")
    private int productCount;
    private transient Product product;
    private transient Delivery delivery;

    public DeliveryProduct() {
        this.deliveryProductId = id++;
        this.productId = 0;
        this.product = null;
        this.productCount = 0;
    }

    @CsvCreator
    public DeliveryProduct(int deliveryProductId, int productId, int deliveryId, int count) {
        this.deliveryProductId = deliveryProductId;
        this.productId = productId;
        this.deliveryId = deliveryId;
        this.productCount = count;
    }

    public DeliveryProduct(int productId, int deliveryId) {
        this.deliveryProductId = id++;
        this.productId = productId;
        this.deliveryId = deliveryId;
    }

    public DeliveryProduct(int productId, Product product, int productCount) {
        this.deliveryProductId = id++;
        this.productId = productId;
        this.product = product;
        this.productCount = productCount;
    }

    public void setDeliveryProductId(int deliveryProductId) {
        this.deliveryProductId = deliveryProductId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        this.deliveryId = delivery.getId();
    }

    @Override
    @CsvOrder(position = 1)
    public int getId() {
        return deliveryProductId;
    }

    @CsvOrder(position = 2)
    public int getProductId() {
        return productId;
    }

    @CsvOrder(position = 3)
    public int getDeliveryId() {
        return deliveryId;
    }

    public Product getProduct() {
        return product;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    @CsvOrder(position = 4)
    public int getProductCount() {
        return productCount;
    }

    @Override
    public String toString() {
        return Integer.toString(deliveryProductId) + "," + Integer.toString(productId)
                + "," + Integer.toString(deliveryId) + "," + Integer.toString(productCount);
    }

    @Override
    public void setId(Integer id) {
        this.deliveryProductId = id;
    }
}
