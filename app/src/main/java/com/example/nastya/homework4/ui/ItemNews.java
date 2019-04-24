package com.example.nastya.homework4.ui;

import java.io.Serializable;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

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

    public ItemNews(int id, String text, PublicationDate publicationDate, String descriptionNews) {
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

    public String getText() {
        return text;
    }

    public PublicationDate getPublicationDate() {
        return publicationDate;
    }

    public String getDescriptionNews() {
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


