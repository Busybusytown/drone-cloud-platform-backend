package com.uav.uavapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user") // 显式指定表名，如果省略@Table注解，默认表名是类名
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String account;
    private String password;
    private String idCard;
    private String telephone;
    private String departmentName;
    private int sex;
    private int flightCardType;
    private String flightCard;
    private String flightCardDate;

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFlightCardType() {
        return flightCardType;
    }

    public void setFlightCardType(int flightCardType) {
        this.flightCardType = flightCardType;
    }

    public String getFlightCard() {
        return flightCard;
    }

    public void setFlightCard(String flightCard) {
        this.flightCard = flightCard;
    }

    public String getFlightCardDate() {
        return flightCardDate;
    }

    public void setFlightCardDate(String flightCardDate) {
        this.flightCardDate = flightCardDate;
    }
}
