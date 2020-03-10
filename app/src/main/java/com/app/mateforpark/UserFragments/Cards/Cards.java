package com.app.mateforpark.UserFragments.Cards;

public class Cards {

    private String userID;
    private String userName;

    public Cards(String userID, String userName, String photoUrl) {
        this.userID = userID;
        this.userName = userName;
        this.photoUrl = photoUrl;
    }



    private String photoUrl;
    private String userAge;
    private String userCountry;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserCountry() {
        return userCountry;
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
    }

    public Cards(String userID, String userName, String photoUrl, String userAge, String userCountry) {
        this.userID = userID;
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.userAge = userAge;
        this.userCountry = userCountry;
    }

    ;
}
