package com.uav.uavapp.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.uav.uavapp.event.DetectionResultEvent;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Component
public class PythonDetectionClient extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PythonDetectionClient.class);
    private static final String PYTHON_WEBSOCKET_URL = "ws://localhost:5001";

    private WebSocketSession pythonSession;
    private WebSocketClient webSocketClient;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    private final Map<String, String> pendingRequests = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        logger.info("🛠️ Initializing PythonDetectionClient...");
        webSocketClient = new StandardWebSocketClient();
        connect();
    }

    @PreDestroy
    public void cleanup() {
        logger.info("🧹 Cleaning up PythonDetectionClient...");
        disconnect();
        pendingRequests.clear();
        logger.info("🧺 Pending requests cleared during cleanup. Size: {}", pendingRequests.size());
    }

    public void connect() {
        if (pythonSession == null || !pythonSession.isOpen()) {
            logger.info("🔌 Attempting to connect to Python WebSocket service at {}...", PYTHON_WEBSOCKET_URL);
            try {
                Future<WebSocketSession> future = webSocketClient.doHandshake(this, PYTHON_WEBSOCKET_URL);
                pythonSession = future.get(10, TimeUnit.SECONDS);
                logger.info("✅ Connected to Python WebSocket service: {}", pythonSession.getId());
                pendingRequests.clear();
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                logger.error("❌ Failed to connect to Python WebSocket: {}", e.getMessage());
                pythonSession = null;
            } catch (Exception e) {
                logger.error("❌ Unexpected connection error:", e);
                pythonSession = null;
            }
        }
    }

    public void disconnect() {
        if (pythonSession != null && pythonSession.isOpen()) {
            try {
                pythonSession.close(CloseStatus.NORMAL);
                logger.info("🔌 Disconnected from Python WebSocket.");
            } catch (IOException e) {
                logger.error("❌ Error disconnecting from Python WebSocket: {}", e.getMessage(), e);
            } finally {
                pythonSession = null;
                pendingRequests.clear();
                logger.info("🧺 Pending requests cleared on disconnect. Size: {}", pendingRequests.size());
            }
        }
    }

    public void sendImageForDetectionWithId(String imageDataBase64, String sessionId) {
        if (pythonSession != null && pythonSession.isOpen()) {
            String requestId = UUID.randomUUID().toString();
            pendingRequests.put(requestId, sessionId);

            try {
                logger.info("📥 Received image from frontend. Base64 length: {}, sessionId: {}", imageDataBase64.length(), sessionId);

                String jsonPayload = String.format("{\"requestId\":\"%s\",\"image\":\"%s\"}", requestId, imageDataBase64);
                logger.info("📤 Sending image to Python. requestId: {}, payload length: {}", requestId, jsonPayload.length());

                pythonSession.sendMessage(new TextMessage(jsonPayload));
                logger.info("✅ Image sent to Python successfully. Waiting for detection result...");

            } catch (IOException e) {
                logger.error("❌ Failed to send image to Python. requestId: {}, error: {}", requestId, e.getMessage(), e);
                pendingRequests.remove(requestId);
                disconnect();
            } catch (Exception e) {
                logger.error("❌ Unexpected error while sending image. requestId: {}", requestId, e);
                pendingRequests.remove(requestId);
            }
        } else {
            logger.warn("⚠️ Python WebSocket is not connected. Cannot send image for sessionId: {}. Reconnecting...", sessionId);
            connect();
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        String responseJson = message.getPayload();

        try {
            Map<String, Object> result = objectMapper.readValue(responseJson, new TypeReference<Map<String, Object>>() {});
            String requestId = (String) result.get("requestId");
            logger.info("📨 Received result from Python for requestId: {}", requestId);

            if (requestId != null) {
                String sessionId = pendingRequests.remove(requestId);
                if (sessionId != null) {
                    result.remove("requestId");
                    String detectionResult = objectMapper.writeValueAsString(result);
                    logger.info("📤 Forwarding detection result to frontend session: {}", sessionId);
                    eventPublisher.publishEvent(new DetectionResultEvent(this, sessionId, detectionResult));
                } else {
                    logger.warn("⚠️ Received result for unknown or expired requestId: {}", requestId);
                }
            } else {
                logger.warn("⚠️ Python response missing requestId: {}", responseJson);
            }
        } catch (IOException e) {
            logger.error("❌ Failed to parse response from Python: {}", e.getMessage(), e);
            logger.debug("🚫 Malformed response: {}", responseJson);
        } catch (Exception e) {
            logger.error("❌ Unexpected error while processing Python response:", e);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        logger.info("✅ WebSocket connection established with Python service: {}", session.getId());
        this.pythonSession = session;
        pendingRequests.clear();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        logger.warn("❌ Python WebSocket closed. sessionId: {}, status: {}", session.getId(), status);
        this.pythonSession = null;
        pendingRequests.clear();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        logger.error("❌ WebSocket transport error with Python. sessionId: {}, error: {}", session.getId(), exception.getMessage(), exception);
        if (session.isOpen()) {
            try {
                session.close(CloseStatus.SERVER_ERROR);
            } catch (IOException e) {
                logger.error("❌ Error closing session after transport error: {}", e.getMessage(), e);
            }
        }
    }
}
