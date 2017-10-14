package com.epam.igor.electronicsshop.entity;


public class Address extends BaseEntity {
    private String country;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;

    private Address(AddressBuilder builder) {
        this.country = builder.country;
        this.city = builder.city;
        this.street = builder.street;
        this.buildingNumber = builder.buildingNumber;
        this.apartmentNumber = builder.apartmentNumber;
    }

    public Address(int id) {
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

    public static class AddressBuilder {
        private String country;
        private String city;
        private String street;
        private String buildingNumber;
        private String apartmentNumber;

        public AddressBuilder country(String country) {
            this.country = country;
            return this;
        }

        public AddressBuilder city(String city) {
            this.city = city;
            return this;
        }

        public AddressBuilder street(String street) {
            this.street = street;
            return this;
        }

        public AddressBuilder buildingNumber(String buildingNumber) {
            this.buildingNumber = buildingNumber;
            return this;
        }

        public AddressBuilder apartmentNumber(String apartmentNumber) {
            this.apartmentNumber = apartmentNumber;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }
}
