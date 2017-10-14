package com.epam.igor.electronicsshop.entity;


public class OrderStatus extends LocaleName {
    public OrderStatus() {
    }

    public OrderStatus(int id) {
        setId(id);
    }

    @Override
    public String toString() {
        return "OrderStatus " + super.toString();
    }
}
