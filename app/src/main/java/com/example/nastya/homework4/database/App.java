package com.example.nastya.homework4.database;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private NewsDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDatabase.class, "db")
                .build();

    }

    public NewsDatabase getDatabase() {
        return database;
    }


}

