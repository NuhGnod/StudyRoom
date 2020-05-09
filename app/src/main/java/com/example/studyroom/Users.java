package com.example.studyroom;

public class Users {
    private String userID;
    private String userPW;
    private String userName;
    private String userNumber;
    private String userNickName;
    public Users(String id, String pw, String name, String number, String nickname) {
        this.userID = id;
        this.userPW = pw;
        this.userName = name;
        this.userNumber = number;
        this.userNickName = nickname;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPW() {
        return userPW;
    }

    public void setUserPW(String userPW) {
        this.userPW = userPW;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
