package com.epam.igor.electronicsshop.entity;

import org.joda.time.DateTime;

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
    public Order(){}
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
