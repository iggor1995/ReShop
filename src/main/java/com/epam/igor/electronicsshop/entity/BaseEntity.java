package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 31.07.2017.
 */
public abstract class BaseEntity {
    private Integer id;
    private boolean deleted;

    public BaseEntity(){
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getId() {

        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity that = (BaseEntity) o;

        if (deleted != that.deleted) return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return  id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "id='" + id + '\'' + ", ";
    }

}
