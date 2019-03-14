package com.example.nastya.homework4;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private NewsDatabase database;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, NewsDatabase.class, "n5")
                .allowMainThreadQueries()
                .build();

        NewsDao newsDao = database.newsDao();
        if (newsDao.getCount() == 0) {
            ItemNews itemNews1 = new ItemNews(1, getString(R.string.Title1), getString(R.string.Date1), getString(R.string.Description1));
            newsDao.insert(itemNews1);

            ItemNews itemNews2 = new ItemNews(2, getString(R.string.Title2), getString(R.string.Date2), getString(R.string.Description2));
            if (newsDao.getById(itemNews2.getId()) == null)
                newsDao.insert(itemNews2);

            ItemNews itemNews3 = new ItemNews(3, getString(R.string.Title3), getString(R.string.Date3), getString(R.string.Description3));
            if (newsDao.getById(itemNews3.getId()) == null)
                newsDao.insert(itemNews3);

            ItemNews itemNews4 = new ItemNews(4, getString(R.string.Title4), getString(R.string.Date4), getString(R.string.Description4));
            if (newsDao.getById(itemNews4.getId()) == null)
                newsDao.insert(itemNews4);

            ItemNews itemNews5 = new ItemNews(5, getString(R.string.Title5), getString(R.string.Date5), getString(R.string.Description5));
            if (newsDao.getById(itemNews5.getId()) == null)
                newsDao.insert(itemNews5);
        }
    }

    public NewsDatabase getDatabase() {
        return database;
    }
}