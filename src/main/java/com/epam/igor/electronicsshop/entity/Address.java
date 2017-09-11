package com.epam.igor.electronicsshop.entity;

/**
 * Created by User on 31.07.2017.
 */
public class Address extends BaseEntity {
    private String country;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;
    public Address(){}
    public Address(int id){
        setId(id);
    }
    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }

    public String getCountry() {

        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }
}
