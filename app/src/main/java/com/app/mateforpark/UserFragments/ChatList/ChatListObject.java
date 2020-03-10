package com.app.mateforpark.UserFragments.ChatList;

public class ChatListObject {

    public String userId;
    public String userName;
    public String profileImageUrl, userCountry, userBio;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public ChatListObject(String userId, String userName, String profileImageUrl, String userCountry, String userBio) {
        this.userId = userId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
        this.userCountry = userCountry;
        this.userBio = userBio;
    }
}
