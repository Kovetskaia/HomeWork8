package com.example.nastya.homework4;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemNews implements ListItem, Serializable {

    @PrimaryKey
    private int id;
    @ColumnInfo
    private String titleNews;
    @ColumnInfo
    private String dateNews;
    @ColumnInfo
    private String descriptionNews;

    ItemNews(int id, String titleNews, String dateNews, String descriptionNews) {
        this.id = id;
        this.titleNews = titleNews;
        this.dateNews = dateNews;
        this.descriptionNews = descriptionNews;
    }

    String getTitleNews() {
        return titleNews;
    }

    String getDateNews() {
        return dateNews;
    }

    String getDescriptionNews() {
        return descriptionNews;
    }

    int getId() {
        return id;
    }

    @Override
    public int getType() {
        return TYPE_NEWS;
    }
}

@Entity
class FavouritesNews {

    @PrimaryKey
    private int idFavourites;

    FavouritesNews(int idFavourites) {
        this.idFavourites = idFavourites;
    }

    int getIdFavourites() {
        return idFavourites;
    }
}