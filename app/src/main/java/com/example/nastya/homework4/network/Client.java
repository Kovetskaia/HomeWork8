package com.example.nastya.homework4.network;

import com.example.nastya.homework4.BuildConfig;

import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Client clientInstance = new Client();
    private static NewsService myDataApi;

    private Client() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        myDataApi = retrofit.create(NewsService.class);
    }

    public static Client getClientInstance() {
        return clientInstance;
    }

    public NewsService getMyDataApi() {
        return myDataApi;
    }

}
