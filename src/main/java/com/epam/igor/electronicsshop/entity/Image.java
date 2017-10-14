package com.epam.igor.electronicsshop.entity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.io.InputStream;


public class Image extends BaseEntity {
    private String name;
    private DateTime timeModified;
    private Product product;
    private InputStream imageStream;
    private String contentType;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String toString() {
        return "Image{" +
                "name='" + name + '\'' +
                ", modifiedTime=" + DateTimeFormat.forPattern("dd-MM-yyyy hh:mm:ss").print(timeModified) +
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
        return timeModified;
    }

    public void setModifiedTime(DateTime timeModified) {
        this.timeModified = timeModified;
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
