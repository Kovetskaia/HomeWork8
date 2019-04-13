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
public class ItemNews implements ListItem, Serializable{

    @PrimaryKey
    private int id;
    @ColumnInfo
    private String text;
    @ColumnInfo
    private long date;
    @ColumnInfo
    private String description;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    //    @Embedded
//    private PublicationDate publicationDate;


    ItemNews(int id, String text, long date, String description){
        this.id = id;
        this.text = text;
        this.date = date;
        this.description = description;
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

    public void setText(String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//
//    public PublicationDate getPublicationDate() {
//        return publicationDate;
//    }
//
//    public void setPublicationDate(PublicationDate publicationDate) {
//        this.publicationDate = publicationDate;
//    }





//    @ColumnInfo
//    private String descriptionNews;

//    ItemNews(int id, String titleNews, PublicationDate dateNews) {
//        this.id = id;
//        this.titleNews = titleNews;
//        this.publicationDate = dateNews;
//       // this.descriptionNews = descriptionNews;
//    }

//    String getTitleNews() {
//        return titleNews;
//    }
//
//    public PublicationDate getPublicationDate() {
//        return publicationDate;
//    }
//
//    public void setPublicationDate(PublicationDate publicationDate) {
//        this.publicationDate = publicationDate;
//    }
//
////    String getDescriptionNews() {
////        return descriptionNews;
////    }
//
//    public long getId() {
//        return id;
//    }

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


class PublicationDate {
    private long milliseconds;

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }


}

@Entity
class FavouritesNews {

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