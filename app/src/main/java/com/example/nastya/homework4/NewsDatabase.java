package com.example.nastya.homework4;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomDatabase;
import androidx.room.Transaction;
import androidx.room.Update;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

@Dao
interface NewsDao {

    @Query("SELECT * FROM itemnews ORDER BY date DESC LIMIT 5")
    Flowable<List<ItemNews>> getAll();

    @Query("SELECT * FROM itemnews WHERE id IN (:ids)")
    Single<List<ItemNews>> getByIds(List<Long> ids);

    @Query("SELECT COUNT(*) FROM itemnews")
    int getCount();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Completable insert(List<ItemNews> news);

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

