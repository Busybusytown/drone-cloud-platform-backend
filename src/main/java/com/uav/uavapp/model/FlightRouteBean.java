package com.uav.uavapp.model;

public class FlightRouteBean {
    private Long id;
    private Long userId;
    private String creatorName;
    private String creatorAccount;
    private String routeName;
    private String routeLocation;
    private String createTime;
    private String latitude;  // JSON 字符串，保存所有点的纬度
    private String longitude; // JSON 字符串，保存所有点的经度
    private String altitude;  // JSON 字符串，保存所有点的高度
    private String roll;      // JSON 字符串，保存所有点的Roll
    private String pitch;     // JSON 字符串，保存所有点的Pitch
    private String yaw;       // JSON 字符串，保存所有点的Yaw
    private String rollc;     // JSON 字符串，保存所有点的Rollc
    private String pitchc;    // JSON 字符串，保存所有点的Pitchc
    private String yawc;      // JSON 字符串，保存所有点的Yawc

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorAccount() {
        return creatorAccount;
    }

    public void setCreatorAccount(String creatorAccount) {
        this.creatorAccount = creatorAccount;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteLocation() {
        return routeLocation;
    }

    public void setRouteLocation(String routeLocation) {
        this.routeLocation = routeLocation;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getYaw() {
        return yaw;
    }

    public void setYaw(String yaw) {
        this.yaw = yaw;
    }

    public String getRollc() {
        return rollc;
    }

    public void setRollc(String rollc) {
        this.rollc = rollc;
    }

    public String getPitchc() {
        return pitchc;
    }

    public void setPitchc(String pitchc) {
        this.pitchc = pitchc;
    }

    public String getYawc() {
        return yawc;
    }

    public void setYawc(String yawc) {
        this.yawc = yawc;
    }
}
