package com.epam.igor.electronicsshop.entity;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StorageItem that = (StorageItem) o;

        if (amount != that.amount) return false;
        if (storage != null ? !storage.equals(that.storage) : that.storage != null) return false;
        return product != null ? product.equals(that.product) : that.product == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (storage != null ? storage.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + amount;
        return result;
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
