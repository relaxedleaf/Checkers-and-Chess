package com.lemon.check.checkesandchess.models;

public class GroupChatMessage {
    private String messageId;
    private String senderId;
    private String imageUrl; // URL of the image, if it's an image message
    private String videoUrl; // URL of the video, if it's a video message
    private long timestamp;
    private String message; // Text message content
    private String messageType; // "text" for text message, "image" for image message
    private String groupId;
    private String replyMessageId;
    private int viewCount; // Number of views for videos

    // Constructors
    public GroupChatMessage() {
        // Default constructor required for Firebase
    }



    public GroupChatMessage(String messageId, String senderId, String imageUrl,String videoUrl,
                            long timestamp, String message, String messageType,
                            String groupId, String replyMessageId,int viewCount) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.timestamp = timestamp;
        this.message = message;
        this.messageType = messageType;
        this.groupId = groupId;
        this.replyMessageId=replyMessageId;
        this.viewCount = 0; // Initialize view count to 0
    }

    // Getters and setters
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getReplyMessageId() {
        return replyMessageId;
    }

    public void setReplyMessageId(String replyMessageId) {
        this.replyMessageId = replyMessageId;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
