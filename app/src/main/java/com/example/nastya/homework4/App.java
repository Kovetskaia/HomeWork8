package com.example.nastya.homework4;

import android.app.Application;

import androidx.room.Room;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

public class App extends Application {

    public static App instance;

    private NewsDatabase database;

    NewsDao newsDao;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDatabase.class, "db")
                .allowMainThreadQueries()
                .build();

        newsDao = database.newsDao();
        if (newsDao.getCount() == 0) {
            ItemNews itemNews1 = new ItemNews(1, getString(R.string.Title1), getString(R.string.Date1), getString(R.string.Description1));
                insertNews(itemNews1);
            ItemNews itemNews2 = new ItemNews(2, getString(R.string.Title2), getString(R.string.Date2), getString(R.string.Description2));
            insertNews(itemNews2);

            ItemNews itemNews3 = new ItemNews(3, getString(R.string.Title3), getString(R.string.Date3), getString(R.string.Description3));
            insertNews(itemNews3);

            ItemNews itemNews4 = new ItemNews(4, getString(R.string.Title4), getString(R.string.Date4), getString(R.string.Description4));
            insertNews(itemNews4);

            ItemNews itemNews5 = new ItemNews(5, getString(R.string.Title5), getString(R.string.Date5), getString(R.string.Description5));
            insertNews(itemNews5);

            ItemNews itemNews6 = new ItemNews(6, getString(R.string.Title6), getString(R.string.Date6), getString(R.string.Description6));
            insertNews(itemNews6);

            ItemNews itemNews7 = new ItemNews(7, getString(R.string.Title7), getString(R.string.Date7), getString(R.string.Description7));
            insertNews(itemNews7);
        }
    }
    void insertNews(ItemNews news) {
        newsDao.insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    public NewsDatabase getDatabase() {
        return database;
    }
}