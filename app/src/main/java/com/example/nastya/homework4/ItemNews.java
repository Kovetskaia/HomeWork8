package com.example.nastya.homework4;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

interface News {
    int getId();
}

@Entity
public class ItemNews implements News, ListItem, Serializable {

    @PrimaryKey
    private int id;
    @ColumnInfo
    private String text;
    @Embedded
    private PublicationDate publicationDate;
    @ColumnInfo
    private String descriptionNews;

    ItemNews(int id, String text, PublicationDate publicationDate, String descriptionNews) {
        this.id = id;
        this.text = text;
        this.publicationDate = publicationDate;
        this.descriptionNews = descriptionNews;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    PublicationDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(PublicationDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    String getDescriptionNews() {
        return descriptionNews;
    }

    void setDescriptionNews(String descriptionNews) {
        this.descriptionNews = descriptionNews;
    }

    @Override
    public int getType() {
        return TYPE_NEWS;
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


class PublicationDate implements Serializable {
    @ColumnInfo(name = "date")
    private long milliseconds;

    PublicationDate(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

}

@Entity
class FavouritesNews implements News {

    @PrimaryKey
    private int id;

    FavouritesNews(int id) {
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