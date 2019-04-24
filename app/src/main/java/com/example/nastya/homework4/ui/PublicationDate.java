package com.example.nastya.homework4.ui;

import java.io.Serializable;

import androidx.room.ColumnInfo;

public class PublicationDate implements Serializable {
    @ColumnInfo(name = "date")
    private long milliseconds;

    public PublicationDate(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

}
