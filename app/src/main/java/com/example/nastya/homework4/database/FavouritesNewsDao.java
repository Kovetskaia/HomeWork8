package com.example.nastya.homework4.database;

import com.example.nastya.homework4.ui.FavouritesNews;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface FavouritesNewsDao {
    @Query("SELECT * FROM favouritesnews")
    Flowable<List<FavouritesNews>> getAll();

    @Query("SELECT * FROM favouritesnews WHERE id = :id")
    Single<FavouritesNews> getById(long id);

    @Insert
    Completable insert(FavouritesNews favouritesNews);

    @Delete
    Completable delete(FavouritesNews favouritesNews);

}
