package com.example.nastya.homework4;

import android.app.Application;
import android.util.Log;

import com.example.nastya.homework4.Retrofit.AllNews;
import com.example.nastya.homework4.Retrofit.MyDataApi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import androidx.room.Room;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

        //if (newsDao.getCount() == 0) {
            List<ItemNews> list = new ArrayList<>();
            //list.add(new ItemNews(11, "24.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,24)).getTime()).getTime(), getString(R.string.Description7)));
            //list.add(new ItemNews(9, "24.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,24)).getTime()).getTime(), getString(R.string.Description7)));
//            list.add(new ItemNews(10, "24.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,24)).getTime()).getTime(), getString(R.string.Description7)));
            list.add(new ItemNews(8, "21.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,21)).getTime()).getTime(), getString(R.string.Description2)));
            list.add(new ItemNews(7, "23.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,23)).getTime()).getTime(), getString(R.string.Description7)));
            list.add(new ItemNews(1, "22.04.2019", ((new GregorianCalendar(2019, Calendar.APRIL, 22)).getTime()).getTime(), getString(R.string.Description1)));
            list.add(new ItemNews(2, "21.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,21)).getTime()).getTime(), getString(R.string.Description2)));
            list.add(new ItemNews(3, "20.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,20)).getTime()).getTime(), getString(R.string.Description3)));
            list.add(new ItemNews(4, "19.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,19)).getTime()).getTime(), getString(R.string.Description4)));
            list.add(new ItemNews(5, "18.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,18)).getTime()).getTime(), getString(R.string.Description5)));
            list.add(new ItemNews(6, "17.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,17)).getTime()).getTime(), getString(R.string.Description6)));
            list.add(new ItemNews(7, "16.04.2019", ((new GregorianCalendar(2018,Calendar.APRIL,16)).getTime()).getTime(), getString(R.string.Description7)));
            list.add(new ItemNews(8, "15.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,15)).getTime()).getTime(), getString(R.string.Description2)));
            list.add(new ItemNews(9, "14.04.2019", ((new GregorianCalendar(2019,Calendar.APRIL,14)).getTime()).getTime(), getString(R.string.Description6)));
            list.add(new ItemNews(10, "14.04.2019", ((new GregorianCalendar(2019, Calendar.APRIL, 14)).getTime()).getTime(), getString(R.string.Description1)));

        ArrayList<ItemNews> newsArray1 = new ArrayList<>(list.subList(0, 5));
        insertNews(newsArray1);
       // }
//        if (newsDao.getCount() == 0) {
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.tinkoff.ru/v1/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            MyDataApi myDataApi = retrofit.create(MyDataApi.class);
//
//            Call<AllNews> myData = myDataApi.myData();
//
//            myData.enqueue(new Callback<AllNews>() {
//                @Override
//                public void onResponse(Call<AllNews> call, Response<AllNews> response) {
//                    if (response.isSuccessful()) {
//                        ArrayList<ItemNews> newsArray = response.body().getPayload();
//                        ArrayList<ItemNews> newsArray1 = new ArrayList<>();
//                        Log.d("myLogs", "response " + newsArray.size() + " " + newsArray.get(0).getId());
//                        newsArray1.addAll(newsArray.subList(0,100));
//                        insertNews(newsArray1);
//                        Log.d("myLogs", "response " + newsArray1.size() + " " + newsArray.get(0).getId());
//                    } else {
//                        Log.d("myLogs", "response code " + response.code());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AllNews> call, Throwable t) {
//                    Log.d("myLogs", "failure " + t);
//                }
//            });
//        }
    }
    void insertNews(List<ItemNews> news) {
          newsDao.insert(news)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    public NewsDatabase getDatabase() {
        return database;
    }


}

