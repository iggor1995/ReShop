package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 31.07.2017.
 */
public class ProductType extends LocaleName {
    public ProductType(int id) {
        setId(id);
    }
    public ProductType(){}

    @Override
    public String toString() {
        return "ProductType{}" + super.toString();
    }
}
