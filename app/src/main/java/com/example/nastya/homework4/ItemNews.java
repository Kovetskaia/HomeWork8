package com.example.nastya.homework4;

import java.io.Serializable;

public class ItemNews implements ListItem, Serializable {

    private String titleNews;
    private String dateNews;
    private String descriptionNews;

    ItemNews(String titleNews, String dateNews, String descriptionNews) {
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

    @Override
    public int getType() {
        return TYPE_NEWS;
    }
}
