package com.example.tinderdev.OneNotification;

public class OneNotification {
    private String mName,mAge,mState,mCountry,mProfileImage,mCodingL,mInterest,mKey;
    private boolean expandable;

    public OneNotification(String mName, String mAge, String mState, String mCountry, String mProfileImage, String mCodingL, String mInterest, String mKey) {
        this.mName = mName;
        this.mAge = mAge;
        this.mState = mState;
        this.mCountry = mCountry;
        this.mProfileImage = mProfileImage;
        this.mCodingL = mCodingL;
        this.mInterest = mInterest;
        this.mKey = mKey;
        this.expandable = true;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getmName() {
        return mName;
    }

    public String getmAge() {
        return mAge;
    }

    public String getmState() {
        return mState;
    }

    public String getmCountry() {
        return mCountry;
    }

    public String getmProfileImage() {
        return mProfileImage;
    }

    public String getmCodingL() {
        return mCodingL;
    }

    public String getmInterest() {
        return mInterest;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public void setmProfileImage(String mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public void setmCodingL(String mCodingL) {
        this.mCodingL = mCodingL;
    }

    public void setmInterest(String mInterest) {
        this.mInterest = mInterest;
    }

    @Override
    public String toString() {
        return "OneNotification{" +
                "mName='" + mName + '\'' +
                ", mAge='" + mAge + '\'' +
                ", mState='" + mState + '\'' +
                ", mCountry='" + mCountry + '\'' +
                ", mProfileImage='" + mProfileImage + '\'' +
                ", mCodingL='" + mCodingL + '\'' +
                ", mInterest='" + mInterest + '\'' +
                '}';
    }
}
