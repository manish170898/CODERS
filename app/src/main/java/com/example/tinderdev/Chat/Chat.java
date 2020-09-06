package com.example.tinderdev.Chat;

public class Chat {
    private String messege;
    private Boolean currentUser;

    public Chat(String messege, Boolean currentUser) {
        this.messege = messege;
        this.currentUser = currentUser;
    }

    public String getMessege() {
        return messege;
    }

    public void setMessege(String messege) {
        this.messege = messege;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }
}