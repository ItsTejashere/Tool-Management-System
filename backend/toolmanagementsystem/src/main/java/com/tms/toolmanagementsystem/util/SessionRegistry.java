package com.tms.toolmanagementsystem.util;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class SessionRegistry {

    private final ConcurrentMap<String, String> activeSessions = new ConcurrentHashMap<>();

    public String startSession(String username) {
        String sessionId = UUID.randomUUID().toString();
        activeSessions.put(username, sessionId);
        return sessionId;
    }

    public boolean isActive(String username, String sessionId) {
        return username != null
                && sessionId != null
                && sessionId.equals(activeSessions.get(username));
    }

    public void endSession(String username, String sessionId) {
        activeSessions.remove(username, sessionId);
    }
}
