package com.epam.igor.electronicsshop.entity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;

/**
 * Created by User on 31.07.2017.
 */
public class Image extends BaseEntity {
    private String name;
    private DateTime TimeModified;
    private Product product;
    private InputStream imageStream;

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", modifiedTime=" + DateTimeFormat.forPattern("dd-MM-yyyy hh:mm:ss").print(TimeModified) +
                ", product=" + product +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getTimeModified() {
        return TimeModified;
    }

    public void setModifiedTime(DateTime timeModified) {
        this.TimeModified = TimeModified;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public InputStream getImageStream() {
        return imageStream;
    }

    public void setImageStream(InputStream imageStream) {
        this.imageStream = imageStream;
    }
}
