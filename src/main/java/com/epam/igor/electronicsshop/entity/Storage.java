package com.epam.igor.electronicsshop.entity;

import java.util.ArrayList;
import java.util.List;


public class Storage extends EntityDescription {
    private String name;
    private List<StorageItem> storageItems = new ArrayList<>();

    public Storage() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Storage storage = (Storage) o;

        if (name != null ? !name.equals(storage.name) : storage.name != null) return false;
        return storageItems != null ? storageItems.equals(storage.storageItems) : storage.storageItems == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (storageItems != null ? storageItems.hashCode() : 0);
        return result;
    }

    public Storage(int id) {
        setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StorageItem> getStorageItems() {
        return storageItems;
    }

    public void setStorageItems(List<StorageItem> storageItems) {
        this.storageItems = storageItems;
    }

    @Override
    public String toString() {
        return "Storage{" + super.toString() + '\'' +
                "name='" + name + '\'' +
                '}';
    }
}
