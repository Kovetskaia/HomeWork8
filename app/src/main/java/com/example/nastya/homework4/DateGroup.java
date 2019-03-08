package com.example.nastya.homework4;

import java.util.Date;

public class DateGroup extends ListItem {
    String date;

    public DateGroup(String date) {
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
