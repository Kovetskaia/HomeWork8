package com.example.nastya.homework4;

import java.util.ArrayList;

class AllNews {
    private ArrayList<ItemNews> payload = new ArrayList<>();

    ArrayList<ItemNews> getPayload() {
        return payload;
    }

}

class Content {
    private Payload payload;

    Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}

class Payload {
    private String content;

    String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

