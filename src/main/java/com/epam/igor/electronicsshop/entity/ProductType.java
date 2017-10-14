package com.epam.igor.electronicsshop.entity;


public class ProductType extends LocaleName {
    public ProductType(int id) {
        setId(id);
    }

    public ProductType() {
    }

    @Override
    public String toString() {
        return "ProductType{}" + super.toString();
    }
}
