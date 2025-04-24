package com.uav.uavapp.model;

import java.util.List;

public class FlightRouteAndroid {
    private static Long userId;
    private static String routeName;
    private static String routeLocation;
    private static String createTime;
    private static List<Double> latitude;
    private static List<Double> longitude;

    // Getters and Setters
    public static Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public static String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public static String getRouteLocation() {
        return routeLocation;
    }

    public void setRouteLocation(String routeLocation) {
        this.routeLocation = routeLocation;
    }

    public static String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public static List<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(List<Double> latitude) {
        this.latitude = latitude;
    }

    public static List<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<Double> longitude) {
        this.longitude = longitude;
    }
}
