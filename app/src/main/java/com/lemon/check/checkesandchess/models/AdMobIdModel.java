package com.lemon.check.checkesandchess.models;

public class AdMobIdModel {

    private String groupId;
    private String creatorId;
    private String adMobId;

    public AdMobIdModel() {
        // Default constructor required for Firebase
    }

    public AdMobIdModel(String groupId, String creatorId, String adMobId) {
        this.groupId = groupId;
        this.creatorId = creatorId;
        this.adMobId = adMobId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public String getAdMobId() {
        return adMobId;
    }
}
