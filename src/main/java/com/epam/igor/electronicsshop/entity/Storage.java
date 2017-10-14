package com.epam.igor.electronicsshop.entity;

import java.util.ArrayList;
import java.util.List;


public class Storage extends EntityDescription {
    private String name;
    private List<StorageItem> storageItems = new ArrayList<>();

    public Storage() {
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
