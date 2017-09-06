package com.epam.igor.electronicsshop.entity;

import org.joda.money.Money;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.07.2017.
 */
public class Product extends EntityDescription {
    private String name;
    private Money price;
    private ProductType type;

    public List<ItemCharacteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<ItemCharacteristic> characteristics) {
        this.characteristics = characteristics;
    }

    private List<ItemCharacteristic> characteristics = new ArrayList<>();
    private List<Image> images = new ArrayList<>();

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public Product(){}
    public Product(int id) {
        setId(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Money getPrice() {
        return price;
    }

    public void setPrice(Money price) {
        this.price = price;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

}
