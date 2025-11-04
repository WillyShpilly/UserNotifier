package com.example.user_service.notification_service.event;

import java.time.LocalDateTime;

public class UserEvent {
    private Long userId;
    private String email;
    private String eventType;
    private String message;
    private LocalDateTime timestamp;

    public UserEvent() {}

    public UserEvent(Long userId, String email, String eventType, String message) {
        this.userId = userId;
        this.email = email;
        this.eventType = eventType;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "UserEvent{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", eventType='" + eventType + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}