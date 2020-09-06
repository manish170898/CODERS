package com.example.tinderdev.Matches;

public class Matches {
    private String mName, mProfileImage, mUserId, mLastMessege;

    public Matches(String mName, String mProfileImage, String mUserId, String mLastMessege) {
        this.mName = mName;
        this.mProfileImage = mProfileImage;
        this.mUserId = mUserId;
        this.mLastMessege = mLastMessege;
    }

    public String getmLastMessege() {
        return mLastMessege;
    }

    public void setmLastMessege(String mLastMessege) {
        this.mLastMessege = mLastMessege;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }
}
