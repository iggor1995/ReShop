package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 31.07.2017.
 */
public class StorageItem extends BaseEntity {
    private Storage storage;
    private Product product;
    private int amount;

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Storage getStorage() {

        return storage;
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "StorageItem{" +
                "storage=" + storage +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }
}
