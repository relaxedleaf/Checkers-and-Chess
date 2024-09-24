package com.lemon.check.checkesandchess.Utils;

public class ReplyMessage {
    private String message;
    private String senderId;
    private String messageType;
    private long timestamp;
    private String messageId;

    public ReplyMessage() {
        // Default constructor required for Firebase
    }

    public ReplyMessage(String message, String senderId, String messageType, long timestamp,String messageId) {
        this.message = message;
        this.senderId = senderId;
        this.messageType = messageType;
        this.timestamp = timestamp;
        this.messageId = messageId;

    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}

