package com.lemon.check.checkesandchess.models;

public class User {
    private String userId;
    private String names;
    private String profileImage;
    private String city;
    private String country;
    private String gender;
    private String profession;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String names, String profileImage,
                String city, String country, String gender, String profession) {
        this.userId = userId;
        this.names = names;
        this.profileImage = profileImage;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.profession = profession;

    }

    // Getters and Setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }


}
