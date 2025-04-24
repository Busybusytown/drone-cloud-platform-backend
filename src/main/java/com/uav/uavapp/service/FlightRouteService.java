package com.uav.uavapp.service;

import com.uav.uavapp.model.FlightRoute;
import com.uav.uavapp.repository.FlightRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightRouteService {

    @Autowired
    private FlightRouteRepository flightRouteRepository;

    public FlightRoute createFlightRoute(Long userId, String routeName, String routeLocation, String createTime,
                                         String latitude, String longitude, String altitude, String roll,
                                         String pitch, String yaw, String rollc, String pitchc, String yawc) {
        FlightRoute flightRoute = new FlightRoute();
        flightRoute.setUserId(userId);
        flightRoute.setRouteName(routeName);
        flightRoute.setRouteLocation(routeLocation);
        flightRoute.setCreateTime(createTime);
        flightRoute.setLatitude(latitude);
        flightRoute.setLongitude(longitude);
        flightRoute.setAltitude(altitude);
        flightRoute.setRoll(roll);
        flightRoute.setPitch(pitch);
        flightRoute.setYaw(yaw);
        flightRoute.setRollc(rollc);
        flightRoute.setPitchc(pitchc);
        flightRoute.setYawc(yawc);
        return flightRouteRepository.save(flightRoute);
    }

    public List<FlightRoute> getAllFlightRoutesByUserId(Long userId) {
        return flightRouteRepository.findAllByUserId(userId);
    }

    public Page<FlightRoute> getAllFlightRoutes(Pageable pageable) {
        return flightRouteRepository.findAll(pageable);
    }
}
