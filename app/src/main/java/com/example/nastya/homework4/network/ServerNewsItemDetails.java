package com.example.nastya.homework4.network;

import com.google.gson.annotations.SerializedName;

public class ServerNewsItemDetails {
    @SerializedName("content")
    private String content;

    public String getContent() {
        return content;
    }
}
