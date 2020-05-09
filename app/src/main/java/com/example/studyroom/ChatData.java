package com.example.studyroom;

class ChatData {

    private String nickname;
    private String content;
    private String time;

    public ChatData(String nickname, String content, String time) {
        this.nickname = nickname;
        this.content = content;
        this.time = time;
    }

    public ChatData() {

    }

    public String getNickname() {
        return nickname;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }


    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

