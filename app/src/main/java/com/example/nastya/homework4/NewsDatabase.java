package com.example.nastya.homework4;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
interface NewsDao {

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

@Dao
interface FavouritesNewsDao {
    @Query("SELECT * FROM favouritesnews")
    Flowable<List<FavouritesNews>> getAll();

    @Query("SELECT * FROM favouritesnews WHERE id = :id")
    Single<FavouritesNews> getById(long id);

    @Insert
    Completable insert(FavouritesNews favouritesNews);

    @Delete
    Completable delete(FavouritesNews favouritesNews);

}

@Database(entities = {ItemNews.class, FavouritesNews.class}, version = 1, exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {
    public abstract NewsDao newsDao();

    public abstract FavouritesNewsDao favouritesNewsDao();
}

