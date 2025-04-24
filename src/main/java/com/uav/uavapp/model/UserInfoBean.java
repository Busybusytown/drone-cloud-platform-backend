package com.uav.uavapp.model;

public class UserInfoBean {

    private String id;
    private String name;
    private String account;
    private int sex;
    private String idCard;
    private String telephone;
    private String flightCard;
    private String flightCardDate;
    private int flightCardType;
    private String departmentName;
    private String token; // 新增 token 字段

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public int getFlightCardType() {
        return flightCardType;
    }

    public void setFlightCardType(int flightCardType) {
        this.flightCardType = flightCardType;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
