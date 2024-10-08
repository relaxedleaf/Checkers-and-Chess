package com.lemon.check.Evacheck.Utils;



public class Users {

    public Users() {

    }

    private String names,gender,city,country,profession,profileImage,status,
    userId, message,timestamp,type,createdBy,requestId,fcmToken;
    long coins;

    long lastSeen;




    public Users(String names, String gender, String city, String country,
                 String profession, String profileImage, String status,
                 String userId, String message, String timestamp, String type,
                 String createdBy, long coins, String requestId,String fcmToken,long lastSeen) {
        this.names = names;
        this.gender = gender;
        this.city = city;
        this.country = country;
        this.profession = profession;
        this.profileImage = // Get a reference to the Firebase Database
        this.status = status;
        this.userId =userId;
        this.message=message;
        this.timestamp=timestamp;
        this.type=type;
        this.createdBy=createdBy;
        this.coins = coins;
        this.requestId=requestId;
        this.fcmToken=fcmToken;
        this.lastSeen=lastSeen;
    }

    public CharSequence getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
    public String getStatus() {
        return status;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }


    public void setStatus(String status) {
        this.status = status;
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
    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}
