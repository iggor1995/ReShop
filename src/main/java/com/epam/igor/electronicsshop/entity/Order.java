package com.epam.igor.electronicsshop.entity;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;


public class Order extends BaseEntity {
    private User user;
    private List<OrderingItem> orderingItems = new ArrayList<>();
    private DateTime creationTime;
    private OrderStatus status;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Order order = (Order) o;

        if (user != null ? !user.equals(order.user) : order.user != null) return false;
        if (orderingItems != null ? !orderingItems.equals(order.orderingItems) : order.orderingItems != null)
            return false;
        if (creationTime != null ? !creationTime.equals(order.creationTime) : order.creationTime != null) return false;
        if (status != null ? !status.equals(order.status) : order.status != null) return false;
        return description != null ? description.equals(order.description) : order.description == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (orderingItems != null ? orderingItems.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public Order(int id) {
        setId(id);
    }

    public Order() {
        creationTime = DateTime.now();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addProduct(OrderingItem orderingItem) {
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

    public String getFormattedCreationTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.shortDateTime();
        return creationTime.toString(dateTimeFormatter);
    }

    public Money getPrice() {
        Money totalPrice = Money.zero(CurrencyUnit.getInstance("KZT"));
        for (OrderingItem orderItem : orderingItems) {
            totalPrice = totalPrice.plus(orderItem.getPrice());
        }
        return totalPrice;
    }
}
