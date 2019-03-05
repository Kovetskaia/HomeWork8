package com.example.nastya.homework4;

import android.os.Parcel;
import android.os.Parcelable;

public class News implements Parcelable{
    private String titleNews;
    private String dateNews;
    private String descriptionNews;


    News(String titleNews, String dateNews, String descriptionNews){
        this.titleNews = titleNews;
        this.dateNews = dateNews;
        this.descriptionNews = descriptionNews;
    }


    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            String titleNews = in.readString();
            String dateNews = in.readString();
            String descriptionNews = in.readString();
            return new News(titleNews, dateNews, descriptionNews);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    public String getTitleNews() {
        return titleNews;
    }

    public void setTitleNews(String titleNews) {
        this.titleNews = titleNews;
    }

    public String getDateNews() {
        return dateNews;
    }

    public void setDateNews(String dateNews) {
        this.dateNews = dateNews;
    }

    public String getDescriptionNews() {
        return descriptionNews;
    }

    public void setDescriptionNews(String descriptionNews) {
        this.descriptionNews = descriptionNews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(titleNews);
        dest.writeString(dateNews);
        dest.writeString(descriptionNews);
    }

}