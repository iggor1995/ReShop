package com.epam.igor.electronicsshop.entity;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 31.07.2017.
 */
public class Order extends BaseEntity {
    private User user;
    private List<OrderingItem> orderingItems = new ArrayList();
    private DateTime creationTime;
    private OrderStatus status;
    private String description;

    public Order(int id){setId(id);}
    public Order() {
        creationTime = DateTime.now();
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addProduct(OrderingItem orderingItem){
        orderingItems.add(orderingItem);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OrderingItem> getOrderingItems() {
        return orderingItems;
    }

    public void setOrderingItems(List<OrderingItem> orderingItems) {
        this.orderingItems = orderingItems;
    }

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getFormattedCreationTime(){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortDateTime();
        return creationTime.toString(dateTimeFormatter);
    }

    public Money getPrice() {
        Money totalPrice = Money.zero(CurrencyUnit.getInstance("KZT"));
        for (OrderingItem orderingItem : orderingItems) {
            totalPrice = totalPrice.plus(orderingItem.getPrice());
        }
        return totalPrice;
    }
}
