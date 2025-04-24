package com.uav.uavapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "flight_route")
public class FlightRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String routeName;
    private String routeLocation;

    private String createTime; // 修改字段类型为 String

    @Column(length = 100000)
    private String latitude;

    @Column(length = 100000)
    private String longitude;

    @Column(length = 100000)
    private String altitude;

    @Column(length = 100000)
    private String roll;

    @Column(length = 100000)
    private String pitch;

    @Column(length = 100000)
    private String yaw;

    @Column(length = 100000)
    private String rollc;

    @Column(length = 100000)
    private String pitchc;

    @Column(length = 100000)
    private String yawc;

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
