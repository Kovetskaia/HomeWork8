package com.example.nastya.homework4;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private NewsDatabase database;

    NewsDao newsDao;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDatabase.class, "db")
                .allowMainThreadQueries()
                .build();

        newsDao = database.newsDao();

    }

    public NewsDatabase getDatabase() {
        return database;
    }


}

