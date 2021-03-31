package com.example.chatbot;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String name;
    private int userId;
    private int id;
    private String title;

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    @SerializedName("body")
    private String text;

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Post(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }




}
