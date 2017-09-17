package com.epam.igor.electronicsshop.entity;

import com.epam.igor.electronicsshop.entity.BaseEntity;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

/**
 * Created by User on 31.07.2017.
 */
public class User extends BaseEntity {
    private String email;
    private Role role;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;
    private Gender gender;
    private Money cash;

    public User() {
        this.role = Role.user;
        this.cash = Money.parse("KZT5000");
    }
    public User(int id){
        setId(id);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Money getCash() {
        return cash;
    }

    public void setCash(Money cash) {
        this.cash = cash;
    }

    public void spendCash(Money cashAmount) {
        this.cash = this.cash.minus(cashAmount);
    }

    public enum Role{
        admin,
        user
    }
}
