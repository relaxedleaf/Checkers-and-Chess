package com.lemon.check.Evacheck.Utils;

public class Group {
    String message,timestamp,type, names,groupTitle,groupDescription,groupIcon,groupId, createdBy,Participant,senderId;

    public Group() {
    }




    public Group(String message, String timestamp,
                 String type, String names, String groupTitle,
                 String groupDescription, String groupIcon,
                 String groupId, String createdBy, String Participant,String senderId) {
        this.message = message;
        this.timestamp = timestamp;
        this.type = type;
        this.names = names;
        this.groupTitle=groupTitle;
        this.groupDescription=groupDescription;
        this.groupIcon=groupIcon;
        this.groupId=groupId;
        this.createdBy=createdBy;
        this.Participant=Participant;
        this.senderId=senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
    public String getGroupDescription() {
        return groupDescription;
    }

    public void setGroupDescription(String groupDescription) {
        this.groupDescription = groupDescription;
    }

    public String getGroupIcon() {
        return groupIcon;
    }

    public void setGroupIcon(String groupIcon) {
        this.groupIcon = groupIcon;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getParticipant() {
        return Participant;
    }

    public void setParticipant(String participant) {
        Participant = participant;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
