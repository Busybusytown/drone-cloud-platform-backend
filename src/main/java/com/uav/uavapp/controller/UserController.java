package com.uav.uavapp.controller;

import com.uav.uavapp.util.JwtUtil;
import com.uav.uavapp.model.User;
import com.uav.uavapp.model.UserInfoBean;
import com.uav.uavapp.repository.UserRepository;
import com.uav.uavapp.response.XBaseBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/registerApp")
    public ResponseEntity<XBaseBean<String>> registerUser(@RequestParam String name,
                                                          @RequestParam int sex,
                                                          @RequestParam String account,
                                                          @RequestParam String password,
                                                          @RequestParam String idCard,
                                                          @RequestParam String telephone,
                                                          @RequestParam String departmentName,
                                                          @RequestParam int flightCardType,
                                                          @RequestParam String flightCard,
                                                          @RequestParam String flightCardDate) {
        logger.info("Received registerApp request with account: {}", account);
        XBaseBean<String> response = new XBaseBean<>();

        if (userRepository.findByAccount(account) != null) {
            response.setCode(400);
            response.setMsg("账户已经存在！");
            response.setSucc(false);
            return ResponseEntity.ok(response);
        }

        User user = new User();
        user.setName(name);
        user.setAccount(account);
        user.setPassword(password);
        user.setIdCard(idCard);
        user.setTelephone(telephone);
        user.setDepartmentName(departmentName);
        user.setSex(sex);
        user.setFlightCardType(flightCardType);
        user.setFlightCard(flightCard);
        user.setFlightCardDate(flightCardDate);

        userRepository.save(user);

        response.setCode(200);
        response.setMsg("注册成功！");
        response.setSucc(true);
        response.setData("User registered successfully"); // 设置 data 字段的内容
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<XBaseBean<UserInfoBean>> login(@RequestParam String account,
                                                         @RequestParam String password) {
        logger.info("Received login request with account: {} and password: {}", account, password);
        XBaseBean<UserInfoBean> response = new XBaseBean<>();
        try {
            User user = userRepository.findByAccount(account);

            if (user == null || !user.getPassword().equals(password)) {
                logger.warn("Login failed for account: {}", account);
                response.setCode(400);
                response.setMsg("账号或密码错误");
                response.setSucc(false);
                return ResponseEntity.ok(response);
            }

            // 生成 JWT token
            String token = jwtUtil.generateToken(user.getAccount());
            logger.info("Generated token for account: {}", account);

            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setId(user.getId().toString());
            userInfoBean.setName(user.getName());
            userInfoBean.setAccount(user.getAccount());
            userInfoBean.setSex(user.getSex());
            userInfoBean.setIdCard(user.getIdCard());
            userInfoBean.setTelephone(user.getTelephone());
            userInfoBean.setFlightCard(user.getFlightCard());
            userInfoBean.setFlightCardDate(user.getFlightCardDate());
            userInfoBean.setFlightCardType(user.getFlightCardType());
            userInfoBean.setDepartmentName(user.getDepartmentName());
            userInfoBean.setToken(token); // 设置 token

            response.setCode(200);
            response.setMsg("登录成功");
            response.setSucc(true);
            response.setData(userInfoBean);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 记录异常堆栈信息到日志
            logger.error("服务器内部错误", e);
            // 返回服务器内部错误响应
            response.setCode(500);
            response.setMsg("服务器内部错误：" + e.getMessage());
            response.setSucc(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/select")
    public ResponseEntity<XBaseBean<List<UserInfoBean>>> getUserInfo(@RequestParam String id) {
        logger.info("Received select request for user ID: {}", id);
        XBaseBean<List<UserInfoBean>> response = new XBaseBean<>();
        User user = userRepository.findById(Long.parseLong(id)).orElse(null);

        if (user == null) {
            logger.warn("User not found for ID: {}", id);
            response.setCode(400);
            response.setMsg("用户不存在");
            response.setSucc(false);
            return ResponseEntity.ok(response);
        }

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.setId(user.getId().toString());
        userInfoBean.setName(user.getName());
        userInfoBean.setAccount(user.getAccount());
        userInfoBean.setSex(user.getSex());
        userInfoBean.setIdCard(user.getIdCard());
        userInfoBean.setTelephone(user.getTelephone());
        userInfoBean.setFlightCard(user.getFlightCard());
        userInfoBean.setFlightCardDate(user.getFlightCardDate());
        userInfoBean.setFlightCardType(user.getFlightCardType());
        userInfoBean.setDepartmentName(user.getDepartmentName());

        response.setCode(200);
        response.setMsg("获取个人信息成功");
        response.setSucc(true);
        response.setData(Collections.singletonList(userInfoBean));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<XBaseBean<String>> updateUserInfo(@RequestParam String id,
                                                            @RequestParam String name,
                                                            @RequestParam int sex,
                                                            @RequestParam String telephone,
                                                            @RequestParam int flightCardType,
                                                            @RequestParam String flightCard,
                                                            @RequestParam String flightCardDate,
                                                            @RequestParam String departmentName) {
        logger.info("Received update request for user ID: {}", id);
        XBaseBean<String> response = new XBaseBean<>();
        Optional<User> optionalUser = userRepository.findById(Long.parseLong(id));

        if (!optionalUser.isPresent()) {
            logger.warn("User not found for ID: {}", id);
            response.setCode(400);
            response.setMsg("用户不存在");
            response.setSucc(false);
            return ResponseEntity.ok(response);
        }

        User user = optionalUser.get();
        user.setName(name);
        user.setSex(sex);
        user.setTelephone(telephone);
        user.setFlightCardType(flightCardType);
        user.setFlightCard(flightCard);
        user.setFlightCardDate(flightCardDate);
        user.setDepartmentName(departmentName);

        userRepository.save(user);

        response.setCode(200);
        response.setMsg("更新成功");
        response.setSucc(true);
        response.setData("User information updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<XBaseBean<String>> logout(@RequestHeader("Authorization") String token) {
        logger.info("Received logout request");
        XBaseBean<String> response = new XBaseBean<>();
        try {
            // 这里可以添加具体的退出登录逻辑，例如无效化 token 或者清除服务器端会话

            response.setCode(200);
            response.setMsg("退出登录成功");
            response.setSucc(true);
            response.setData("Logout successful");
        } catch (Exception e) {
            logger.error("服务器内部错误", e);
            response.setCode(500);
            response.setMsg("服务器内部错误：" + e.getMessage());
            response.setSucc(false);
        }
        return ResponseEntity.ok(response);
    }


    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<XBaseBean<String>> handleException(Exception e) {
            logger.error("Global exception handler caught an error", e);
            XBaseBean<String> response = new XBaseBean<>();
            response.setCode(500);
            response.setMsg("服务器内部错误：" + e.getMessage());
            response.setSucc(false);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<XBaseBean<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        logger.info("Fetching all users with pagination - page: {}, size: {}, sortBy: {}", page, size, sortBy);
        XBaseBean<Page<User>> response = new XBaseBean<>();
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<User> userPage = userRepository.findAll(pageable);

        response.setCode(200);
        response.setMsg("获取所有用户信息成功");
        response.setSucc(true);
        response.setData(userPage);
        return ResponseEntity.ok(response);
    }
}
