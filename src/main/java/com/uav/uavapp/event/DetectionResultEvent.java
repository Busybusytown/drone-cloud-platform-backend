package com.uav.uavapp.event; // 建议创建一个单独的 event 包

import org.springframework.context.ApplicationEvent;

public class DetectionResultEvent extends ApplicationEvent {
    private final String sessionId;
    private final String detectionResultJson;

    public DetectionResultEvent(Object source, String sessionId, String detectionResultJson) {
        super(source);
        this.sessionId = sessionId;
        this.detectionResultJson = detectionResultJson;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getDetectionResultJson() {
        return detectionResultJson;
    }
}