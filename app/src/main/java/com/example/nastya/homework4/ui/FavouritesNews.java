package com.example.nastya.homework4.ui;

import com.example.nastya.homework4.database.News;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FavouritesNews implements News {

    @PrimaryKey
    private int id;

    public FavouritesNews(int id) {
        this.id = id;
    }

    public int getId() {

        return id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        return this.id == ((News) obj).getId();
    }

}
