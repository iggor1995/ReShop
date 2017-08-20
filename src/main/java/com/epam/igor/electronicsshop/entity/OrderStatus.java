package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 02.08.2017.
 */
public class OrderStatus extends LocaleName {
    public OrderStatus(){}
    public OrderStatus(int id){setId(id);}
    @Override
    public String toString() {
        return "OrderStatus " + super.toString();
    }
}
