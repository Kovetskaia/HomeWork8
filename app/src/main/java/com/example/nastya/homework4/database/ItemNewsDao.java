package com.example.nastya.homework4.database;

import com.example.nastya.homework4.ui.ItemNews;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface ItemNewsDao {

    @Query("SELECT * FROM itemnews ORDER BY date DESC LIMIT 100")
    Single<List<ItemNews>> getAll();

    @Query("SELECT * FROM itemnews WHERE id IN (:ids)")
    Single<List<ItemNews>> getByIds(List<Long> ids);

    @Query("SELECT * FROM itemnews WHERE id = :id")
    Single<ItemNews> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<ItemNews> news);

    @Query("UPDATE itemnews SET descriptionNews = :description WHERE id = :id")
    Completable insertContent(String description, int id);

    @Query("DELETE FROM itemnews WHERE date < (:date) AND id NOT IN (SELECT id FROM favouritesnews)")
    Completable delete(Long date);


}
