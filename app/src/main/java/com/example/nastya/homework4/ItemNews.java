package com.example.nastya.homework4;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ItemNews extends ListItem implements Parcelable {
    public static final Creator<ItemNews> CREATOR = new Creator<ItemNews>() {
        @Override
        public ItemNews createFromParcel(Parcel in) {
            int id = in.readInt();
            String titleNews = in.readString();
            String dateNews = in.readString();
            String descriptionNews = in.readString();
            return new ItemNews(id, titleNews, dateNews, descriptionNews);
        }

        @Override
        public ItemNews[] newArray(int size) {
            return new ItemNews[size];
        }
    };
    @PrimaryKey
    private int id;
    @ColumnInfo
    private String titleNews;
    @ColumnInfo
    private String dateNews;
    @ColumnInfo
    private String descriptionNews;

    public ItemNews(int id, String titleNews, String dateNews, String descriptionNews) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(titleNews);
        dest.writeString(dateNews);
        dest.writeString(descriptionNews);
    }

    @Override
    public int getType() {
        return TYPE_NEWS;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

@Entity
class FavouritesNews {

    @PrimaryKey
    private int idFavourites;

    FavouritesNews(int idFavourites) {
        this.idFavourites = idFavourites;
    }

    public int getIdFavourites() {
        return idFavourites;
    }
}