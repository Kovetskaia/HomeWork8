package com.example.nastya.homework4.ui;

public class ItemDateGroup implements ListItem {
    private String date;

    ItemDateGroup(String date) {
        this.date = date;
    }

    String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }

}
