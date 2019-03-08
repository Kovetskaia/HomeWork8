package com.example.nastya.homework4;

public class ItemDateGroup extends ListItem {
    String date;

    public ItemDateGroup(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }

}
