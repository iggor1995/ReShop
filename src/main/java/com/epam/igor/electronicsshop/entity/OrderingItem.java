package com.epam.igor.electronicsshop.entity;

import org.joda.money.Money;

/**
 * Created by User on 31.07.2017.
 */
public class OrderingItem extends BaseEntity {
    private Order order;
    private Product product;
    private int amount;

    public Money getPrice(){
        return product.getPrice().multipliedBy(amount);
    }
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

}
