package com.uav.uavapp.controller;

import com.uav.uavapp.model.FlightRoute;
import com.uav.uavapp.model.FlightRouteAndroid;
import com.uav.uavapp.model.FlightRouteBean;
import com.uav.uavapp.model.User;
import com.uav.uavapp.response.XBaseBean;
import com.uav.uavapp.service.FlightRouteService;
import com.uav.uavapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/flight-routes")
public class FlightRouteController {

    private static final Logger logger = LoggerFactory.getLogger(FlightRouteController.class);

    @Autowired
    private FlightRouteService flightRouteService;

    @Autowired
    private UserService userService;

    // Android端创建飞行路线接口
    @PostMapping("/createFromAndroid")
    public ResponseEntity<XBaseBean<String>> createFlightRouteFromAndroid(
            @RequestParam Long userId,
            @RequestParam String routeName,
            @RequestParam String routeLocation,
            @RequestParam String createTime,
            @RequestParam String latitude,
            @RequestParam String longitude) {

        logger.info("正在为Android端创建新的飞行路线，具体信息如下:");
        logger.info("用户ID: {}", userId);
        logger.info("路线名称: {}", routeName);
        logger.info("路线位置: {}", routeLocation);
        logger.info("创建时间: {}", createTime);
        logger.info("纬度列表: {}", latitude);
        logger.info("经度列表: {}", longitude);

        XBaseBean<String> response = new XBaseBean<>();

        try {
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                // 直接存储纬度和经度字符串形式（如 "[31.322761, 31.32546]"）
                FlightRoute flightRoute = flightRouteService.createFlightRoute(
                        userId,
                        routeName,
                        routeLocation,
                        createTime,
                        latitude, // 纬度字符串
                        longitude, // 经度字符串
                        null, // 处理高度
                        null, null, null, null, null, null); // 其他参数为空

                logger.info("飞行路线创建成功，ID为: {}", flightRoute.getId());

                response.setCode(200);
                response.setMsg("飞行路线创建成功");
                response.setSucc(true);
                response.setData("飞行路线创建成功，ID为: " + flightRoute.getId());
                return ResponseEntity.ok(response);
            } else {
                logger.error("找不到ID为 {} 的用户", userId);

                response.setCode(404);
                response.setMsg("用户未找到");
                response.setData(null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("创建飞行路线时出错", e);

            response.setCode(500);
            response.setMsg("服务器错误: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<XBaseBean<String>> createFlightRoute(
            @RequestParam Long userId,
            @RequestParam String routeName,
            @RequestParam String routeLocation,
            @RequestParam String createTime,
            @RequestParam String latitude,
            @RequestParam String longitude,
            @RequestParam String altitude,
            @RequestParam String roll,
            @RequestParam String pitch,
            @RequestParam String yaw,
            @RequestParam String rollc,
            @RequestParam String pitchc,
            @RequestParam String yawc) {

        logger.info("正在创建新的飞行路线，具体信息如下:");
        logger.info("用户ID: {}", userId);
        logger.info("路线名称: {}", routeName);
        logger.info("路线位置: {}", routeLocation);
        logger.info("创建时间: {}", createTime);
        logger.info("纬度: {}", latitude);
        logger.info("经度: {}", longitude);
        logger.info("高度: {}", altitude);
        logger.info("Roll: {}", roll);
        logger.info("Pitch: {}", pitch);
        logger.info("Yaw: {}", yaw);
        logger.info("Rollc: {}", rollc);
        logger.info("Pitchc: {}", pitchc);
        logger.info("Yawc: {}", yawc);

        XBaseBean<String> response = new XBaseBean<>();

        try {
            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                FlightRoute flightRoute = flightRouteService.createFlightRoute(
                        userId, routeName, routeLocation, createTime,
                        latitude, longitude, altitude, roll, pitch, yaw, rollc, pitchc, yawc);

                logger.info("飞行路线创建成功，ID为: {}", flightRoute.getId());

                response.setCode(200);
                response.setMsg("成功");
                response.setSucc(true);
                response.setData("飞行路线创建成功，ID为: " + flightRoute.getId());
                return ResponseEntity.ok(response);
            } else {
                logger.error("找不到ID为 {} 的用户", userId);

                response.setCode(404);
                response.setMsg("用户未找到");
                response.setData(null);
                return ResponseEntity.status(404).body(response);
            }
        } catch (Exception e) {
            logger.error("创建飞行路线时出错", e);

            response.setCode(500);
            response.setMsg("服务器错误: " + e.getMessage());
            response.setData(null);
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Android客户端请求接口
     */
    @PostMapping("/getAllByUserId")
    public ResponseEntity<XBaseBean<List<FlightRouteBean>>> getAllFlightRoutesByUserId(@RequestParam Long userId) {
        logger.info("Fetching flight routes for User ID: {}", userId);

        XBaseBean<List<FlightRouteBean>> response = new XBaseBean<>();
        try {
            List<FlightRoute> flightRoutes = flightRouteService.getAllFlightRoutesByUserId(userId);
            if (flightRoutes.isEmpty()) {
                logger.info("No flight routes found for User ID: {}", userId);
                response.setCode(200);
                response.setMsg("No flight routes found");
                response.setData(null);
                return ResponseEntity.ok(response);
            }

            List<FlightRouteBean> flightRouteBeans = flightRoutes.stream().map(route -> {
                FlightRouteBean bean = new FlightRouteBean();
                bean.setId(route.getId());
                bean.setUserId(route.getUserId());
                bean.setRouteName(route.getRouteName());
                bean.setRouteLocation(route.getRouteLocation());
                bean.setCreateTime(route.getCreateTime());
                bean.setLatitude(route.getLatitude());
                bean.setLongitude(route.getLongitude());
                bean.setAltitude(route.getAltitude()); // 设置 altitude
                bean.setRoll(route.getRoll());         // 设置 roll
                bean.setPitch(route.getPitch());       // 设置 pitch
                bean.setYaw(route.getYaw());           // 设置 yaw
                bean.setRollc(route.getRollc());       // 设置 rollc
                bean.setPitchc(route.getPitchc());     // 设置 pitchc
                bean.setYawc(route.getYawc());         // 设置 yawc
                return bean;
            }).collect(Collectors.toList());

            logger.info("Fetched {} flight routes for User ID: {}", flightRouteBeans.size(), userId);
            for (FlightRouteBean bean : flightRouteBeans) {
                logger.info("FlightRouteBean - ID: {}, Route Name: {}, Route Location: {}, Create Time: {}",
                        bean.getId(), bean.getRouteName(), bean.getRouteLocation(), bean.getCreateTime());
            }

            response.setCode(200);
            response.setMsg("Flight routes fetched successfully");
            response.setData(flightRouteBeans);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching flight routes for User ID: {}", userId, e);
            response.setCode(500);
            response.setMsg("Error fetching flight routes");
            return ResponseEntity.status(500).body(response);
        }
    }


    /**
     * web请求接口
     */
    @GetMapping("/getAll")
    public ResponseEntity<XBaseBean<Page<FlightRouteBean>>> getAllFlightRoutes(
            @RequestParam int page,
            @RequestParam int size) {

        logger.info("Fetching all flight routes - Page: {}, Size: {}", page, size);

        XBaseBean<Page<FlightRouteBean>> response = new XBaseBean<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<FlightRoute> flightRoutes = flightRouteService.getAllFlightRoutes(pageable);

            Page<FlightRouteBean> flightRouteBeans = flightRoutes.map(route -> {
                FlightRouteBean bean = new FlightRouteBean();
                bean.setId(route.getId());
                bean.setUserId(route.getUserId());

                // 获取用户信息
                Optional<User> userOptional = userService.getUserById(route.getUserId());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    bean.setCreatorName(user.getName());
                    bean.setCreatorAccount(user.getAccount());
                } else {
                    bean.setCreatorName("Unknown");
                    bean.setCreatorAccount("Unknown");
                }

                // 填充其他字段
                bean.setRouteName(route.getRouteName());
                bean.setRouteLocation(route.getRouteLocation());
                bean.setCreateTime(route.getCreateTime());
                bean.setLatitude(route.getLatitude());
                bean.setLongitude(route.getLongitude());
                bean.setAltitude(route.getAltitude());
                bean.setRoll(route.getRoll());
                bean.setPitch(route.getPitch());
                bean.setYaw(route.getYaw());
                bean.setRollc(route.getRollc());
                bean.setPitchc(route.getPitchc());
                bean.setYawc(route.getYawc());

                return bean;
            });

            logger.info("Fetched page {} of flight routes with {} records", page, flightRouteBeans.getNumberOfElements());

            response.setCode(200);
            response.setMsg("Flight routes fetched successfully");
            response.setData(flightRouteBeans);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching flight routes", e);
            response.setCode(500);
            response.setMsg("Error fetching flight routes");
            return ResponseEntity.status(500).body(response);
        }
    }



}
