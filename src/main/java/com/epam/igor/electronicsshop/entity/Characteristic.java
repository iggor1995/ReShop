package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 11.08.2017.
 */
public class Characteristic extends LocaleName {
    ProductType type;

    public Characteristic() {
    }
    public Characteristic(int id) {
        setId(id);
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Characteristic{" + super.toString() +
                "type=" + type +
                '}';
    }
}
