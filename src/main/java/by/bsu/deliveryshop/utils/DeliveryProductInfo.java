package by.bsu.deliveryshop.utils;

import by.bsu.deliveryshop.data.Models.Product;

public class DeliveryProductInfo {
    private Product product;
    private Integer count;

    public DeliveryProductInfo(Product product, Integer count) {
        this.product = product;
        this.count = count;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getCount() {
        return count;
    }
}
