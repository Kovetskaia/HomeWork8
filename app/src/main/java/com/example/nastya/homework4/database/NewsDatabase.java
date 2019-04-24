package com.example.nastya.homework4.database;

import com.example.nastya.homework4.ui.FavouritesNews;
import com.example.nastya.homework4.ui.ItemNews;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ItemNews.class, FavouritesNews.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract ItemNewsDao newsDao();

    public abstract FavouritesNewsDao favouritesNewsDao();
}

