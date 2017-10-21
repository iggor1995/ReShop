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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Image image = (Image) o;

        if (name != null ? !name.equals(image.name) : image.name != null) return false;
        if (timeModified != null ? !timeModified.equals(image.timeModified) : image.timeModified != null) return false;
        if (product != null ? !product.equals(image.product) : image.product != null) return false;
        if (imageStream != null ? !imageStream.equals(image.imageStream) : image.imageStream != null) return false;
        return contentType != null ? contentType.equals(image.contentType) : image.contentType == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (timeModified != null ? timeModified.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + (imageStream != null ? imageStream.hashCode() : 0);
        result = 31 * result + (contentType != null ? contentType.hashCode() : 0);
        return result;
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
