package com.example.studyroom;

import java.text.SimpleDateFormat;

class ChatData {

    private String nickname;
    private String content;
    private String time;
    private String chat_time;
    public ChatData(String nickname, String content, String time, String chat_time) {
        this.nickname = nickname;
        this.content = content;
        this.time = time;
        this.chat_time = chat_time;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
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

