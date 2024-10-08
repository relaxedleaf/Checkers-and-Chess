package com.lemon.check.Evacheck.models;

public class VideoModel {
    String id;
    String createdBy;
    String playlist;
    String type;
    String video_description;
    String  video_url;
    long views;
    String video_title;
    String date;
    private String groupId;


    public VideoModel() {

    }



    public VideoModel(String id, String createdBy, String playlist, String type,
                      String video_description, String  video_url, long views,
                      String video_title, String date,String groupId) {
        this.id = id;
        this.createdBy=createdBy;
        this.playlist=playlist;
        this.type=type;
        this.video_description=video_description;
        this.video_url=video_url;
        this.views=views;
        this.video_title=video_title;
        this.date=date;
        this.groupId=groupId;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVideo_description() {
        return video_description;
    }

    public void setVideo_description(String video_description) {
        this.video_description = video_description;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public long getViews() {
        return views;
    }

    public void setViews(long views) {
        this.views = views;
    }

    public String getVideo_title() {
        return video_title;
    }

    public void setVideo_title(String video_title) {
        this.video_title = video_title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
