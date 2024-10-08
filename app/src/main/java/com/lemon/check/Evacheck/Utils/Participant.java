package com.lemon.check.Evacheck.Utils;

public class Participant {
    private String names;
    private String profileImage;
    private String uid;
    private String createdBy;
    String Participant;


    public Participant() {
        // Default constructor required for calls to DataSnapshot.getValue(Participant.class)
    }


    public Participant(String names, String profileImage, String uid, String createdBy, String Participant) {
        this.names = names;
        this.profileImage = profileImage;
        this.uid=uid;
        this.createdBy=createdBy;
        this.Participant= Participant;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

}

