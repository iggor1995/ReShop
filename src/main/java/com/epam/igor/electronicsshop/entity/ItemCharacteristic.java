package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 11.08.2017.
 */
public class ItemCharacteristic extends BaseEntity {
    private Product product;
    private Characteristic characteristic;
    private String price;

    public ItemCharacteristic() {
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ItemCharacteristic{" + super.toString() +
                "product=" + product +
                ", characteristic=" + characteristic +
                ", price='" + price + '\'' +
                '}';
    }
}
