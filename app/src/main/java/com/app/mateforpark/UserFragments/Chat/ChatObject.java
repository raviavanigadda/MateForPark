package com.app.mateforpark.UserFragments.Chat;

public class ChatObject {

    private String message;
    private Boolean currentUser;
    private String profileImgUrl;

    public ChatObject(String message, Boolean currentUser, String profileImgUrl) {
        this.message = message;
        this.currentUser = currentUser;
        this.profileImgUrl = profileImgUrl;
    }

    public String getProfileImgUrl() {
        return profileImgUrl;
    }

    public void setProfileImgUrl(String profileImgUrl) {
        this.profileImgUrl = profileImgUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        this.currentUser = currentUser;
    }

    public ChatObject(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }
}
