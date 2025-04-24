package com.uav.uavapp.repository;

import com.uav.uavapp.model.FlightRoute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRouteRepository extends JpaRepository<FlightRoute, Long> {

        List<FlightRoute> findAllByUserId(Long userId);

        Page<FlightRoute> findAll(Pageable pageable);
}
