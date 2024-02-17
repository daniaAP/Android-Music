package com.example.daniaapplication;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class FeedBack {
    private String text;
    private String username;

    public FeedBack(){

    }
    public FeedBack(String text,String username) {
        this.text = text;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "FeedBack{" +
                "text='" + text + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}

