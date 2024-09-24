package com.lemon.check.checkesandchess.Utils;




public class Chat {
    private String message;
    private String messageType;
    private String userId;
    private long timestamp;
    private String otherUserId;
    private boolean seen;
    private String messageId;
    private String Sender;

    private String replyToMessageId; // New field for reply feature
    private String replyToMessage; // New field for the original message content
    private String replyToUserId; // New field for the original message's sender


    public Chat() {
    }

    public Chat(String message, String messageType, String userId,
                long timestamp, boolean seen, String messageId,
                String Sender,String otherUserId,String replyToMessageId,String replyToMessage,String replyToUserId) {
        this.message = message;
        this.messageType = messageType;
        this.userId = userId;
        this.timestamp = timestamp;
        this.seen = seen;
        this.messageId = messageId;
        this.Sender=Sender;
        this.otherUserId=otherUserId;
        this.replyToMessageId=replyToMessageId;
        this.replyToMessage=replyToMessage;
        this. replyToUserId= replyToUserId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSender() {
        return Sender;
    }
    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getReplyToMessageId() {
        return replyToMessageId;
    }

    public void setReplyToMessageId(String replyToMessageId) {
        this.replyToMessageId = replyToMessageId;
    }

    public String getReplyToMessage() {
        return replyToMessage;
    }

    public void setReplyToMessage(String replyToMessage) {
        this.replyToMessage = replyToMessage;
    }

    public String getReplyToUserId() {
        return replyToUserId;
    }

    public void setReplyToUserId(String replyToUserId) {
        this.replyToUserId = replyToUserId;
    }
}
