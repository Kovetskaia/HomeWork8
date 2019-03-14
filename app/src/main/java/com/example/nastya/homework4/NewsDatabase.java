package com.example.nastya.homework4;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomDatabase;

@Dao
interface NewsDao {
    @Query("SELECT * FROM itemnews")
    List<ItemNews> getAll();

    @Query("SELECT * FROM itemnews WHERE id = :id")
    ItemNews getById(long id);

    @Query("SELECT COUNT(*) FROM itemnews")
    int getCount();

    @Insert
    void insert(ItemNews news);
}

@Dao
interface FavouritesNewsDao {
    @Query("SELECT * FROM favouritesnews")
    List<FavouritesNews> getAll();

    @Query("SELECT * FROM favouritesnews WHERE idFavourites = :id")
    FavouritesNews getById(long id);

    @Insert
    void insert(FavouritesNews favouritesNews);

    @Delete
    void delete(FavouritesNews favouritesNews);

}

@Database(entities = {ItemNews.class, FavouritesNews.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public abstract FavouritesNewsDao favouritesNewsDao();
}

