package com.example.nastya.homework4.network;

import com.google.gson.annotations.SerializedName;

public class ServerResponse<T> {
    @SerializedName("payload")
    private T payload;

    public T getPayload() {
        return payload;
    }
}

