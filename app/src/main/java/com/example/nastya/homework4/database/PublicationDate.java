package com.example.nastya.homework4.database;

import java.io.Serializable;

import androidx.room.ColumnInfo;

public class PublicationDate implements Serializable {
    @ColumnInfo(name = "date")
    private long milliseconds;

    PublicationDate(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

}
