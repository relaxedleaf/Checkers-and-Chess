package com.lemon.check.checkesandchess.Utils;

public class Friends {


    public Friends() {


    }


    private String profession;
    private String profileImageUrI;
    private String names;
    private long messageCount; // Add this field


    public Friends(String profession, String profileImageUrI, String names,long messageCount) {
        this.profession = profession;
        this.profileImageUrI=profileImageUrI;
        this.names =names;
        this.messageCount=messageCount;

    }
    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
    public String getProfileImageUrI() {
        return profileImageUrI;
    }

    public void setProfileImageUrI(String profileImageUrI) {
        this.profileImageUrI = profileImageUrI;
    }
    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public long getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(long messageCount) {
        this.messageCount = messageCount;
    }
}
