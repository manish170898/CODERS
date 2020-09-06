package com.example.tinderdev.SwingFling;

public class Cards {

    private String userId;
    private String name;
    private String profileImageUrl;
    public Cards(String userId, String name, String profileImageUrl){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
    public String getUserId() {
        return this.userId;
    }

    public String getName() {
        return this.name;
    }

    public String getProfileImageUrl() { return this.profileImageUrl; }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
}
