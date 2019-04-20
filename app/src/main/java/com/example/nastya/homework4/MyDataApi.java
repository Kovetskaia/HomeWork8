package com.example.nastya.homework4;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyDataApi {
    @GET("news")
    Call <AllNews> myData();

    @GET("news_content?")
    Call<Content> getDescription(@Query("id") int id);
}
