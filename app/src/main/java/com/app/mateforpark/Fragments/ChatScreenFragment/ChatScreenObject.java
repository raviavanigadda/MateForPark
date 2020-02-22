package com.app.mateforpark.Fragments.ChatScreenFragment;

public class ChatScreenObject {

    private String userId;
    private String userName;
    private String profileImageUrl;


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

    public ChatScreenObject(String userId, String userName, String profileImageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.profileImageUrl = profileImageUrl;
    }


    //opposite user that we want. the matches that we got appear here and those who have will be displayed in the profile/chat with user
    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
}
