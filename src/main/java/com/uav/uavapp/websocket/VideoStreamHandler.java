package com.uav.uavapp.websocket;

import com.uav.uavapp.event.DetectionResultEvent;
import com.uav.uavapp.service.PythonDetectionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VideoStreamHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(VideoStreamHandler.class);

    // 所有前端会话管理
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 注入与 Python 通信的客户端
    private final PythonDetectionClient pythonDetectionClient;

    public VideoStreamHandler(PythonDetectionClient pythonDetectionClient) {
        this.pythonDetectionClient = pythonDetectionClient;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        logger.info("🟢 前端 WebSocket 连接建立，Session ID: {}", session.getId());

        // 确保 Python WebSocket 保持连接
        pythonDetectionClient.connect();
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String payload = message.getPayload();
        logger.info("📥 收到前端发送的图像 Base64，长度: {}, Session ID: {}", payload.length(), session.getId());

        // 转发到 Python 推理服务
        pythonDetectionClient.sendImageForDetectionWithId(payload, session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        logger.warn("🔴 前端连接关闭，Session ID: {}, 状态: {}", session.getId(), status);

        if (sessions.isEmpty()) {
            logger.info("🔌 所有前端连接断开，正在断开 Python WebSocket...");
            pythonDetectionClient.disconnect();
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("❌ 前端通信错误，Session ID: {}, 错误: {}", session.getId(), exception.getMessage(), exception);
        try {
            if (session.isOpen()) {
                session.close(CloseStatus.SERVER_ERROR);
            }
        } catch (IOException e) {
            logger.error("❌ 尝试关闭异常连接失败: {}", e.getMessage(), e);
        }
    }

    // 接收事件回调，将检测结果发送给对应前端 Session
    @EventListener
    public void handleDetectionResultEvent(DetectionResultEvent event) {
        String sessionId = event.getSessionId();
        String detectionResultJson = event.getDetectionResultJson();

        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(detectionResultJson));
                logger.info("📤 成功发送检测结果到前端 Session ID: {}", sessionId);
            } catch (IOException e) {
                logger.error("❌ 发送检测结果失败，Session ID: {}, 错误: {}", sessionId, e.getMessage(), e);
            }
        } else {
            logger.warn("⚠️ 检测结果丢弃：找不到活跃的前端 Session ID: {}", sessionId);
        }
    }
}
