package com.uav.uavapp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.uav.uavapp.websocket.VideoStreamHandler;

@Configuration
@EnableWebSocket // 启用 WebSocket 支持
public class WebSocketConfig implements WebSocketConfigurer {

    // 注入你的 WebSocket Handler，用于处理前端消息
    private final VideoStreamHandler videoStreamHandler;

    public WebSocketConfig(VideoStreamHandler videoStreamHandler) {
        this.videoStreamHandler = videoStreamHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(videoStreamHandler, "/video-stream") // 注册 WebSocket 端点 "/video-stream"
                .setAllowedOrigins("*"); // 允许所有来源连接 (开发环境，生产环境应更严格)
    }
}
