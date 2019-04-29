package com.example.nastya.homework4.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.nastya.homework4.ui.ItemNews;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ItemNewsDao {

    @Query("SELECT * FROM itemnews ORDER BY date DESC LIMIT 100")
    Single<List<ItemNews>> getAll();

    @Query("SELECT * FROM itemnews WHERE id IN (:ids)")
    Single<List<ItemNews>> getByIds(List<Long> ids);

    @Query("SELECT * FROM itemnews WHERE id = :id")
    Maybe<ItemNews> getById(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<ItemNews> news);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOneNews(ItemNews news);

    @Query("UPDATE itemnews SET descriptionNews = :description WHERE id = :id")
    Completable insertContent(String description, int id);

    @Query("DELETE FROM itemnews WHERE date < (:date) AND id NOT IN (SELECT id FROM favouritesnews)")
    Completable delete(Long date);


}
