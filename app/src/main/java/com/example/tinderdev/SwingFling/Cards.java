package com.example.tinderdev.SwingFling;

public class Cards {

    private String userId;
    private String name;
    private String profileImageUrl;
    private String interest;
    private String coding;
    private String age;

    public Cards(String userId, String name, String profileImageUrl, String interest, String coding, String age){
        this.userId = userId;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.interest = interest;
        this.coding = coding;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCoding() {
        return coding;
    }

    public void setCoding(String coding) {
        this.coding = coding;
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
