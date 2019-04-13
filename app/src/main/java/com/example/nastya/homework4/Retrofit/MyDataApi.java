package com.example.nastya.homework4.Retrofit;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyDataApi {
    @GET("news")
    Call <AllNews> myData();
}
