package com.uav.uavapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("http://localhost:5173","http://localhost:8082","http://192.168.1.100:8080",  "http://192.168.31.114:8081","http://192.168.1.102:8082","http://localhost:5173/dashboard/video") // 允许的前端来源
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的请求方法
				.allowedHeaders("*") // 允许的请求头
				.allowCredentials(true); // 允许发送带有认证信息的请求
	}
}
