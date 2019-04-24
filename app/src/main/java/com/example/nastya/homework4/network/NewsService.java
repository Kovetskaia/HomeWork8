package com.example.nastya.homework4.network;

import com.example.nastya.homework4.ui.ItemNews;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

//public interface MyDataApi {
//    @GET("news")
//    Call <AllNews> myData();
//
//    @GET("news_content?")
//    Call<Content> getDescription(@Query("id") int id);
//}


public interface NewsService {
    @GET("news")
    Single<ServerResponse<List<ItemNews>>> getNewsList();

    @GET("news_content")
    Single<ServerResponse<ServerNewsItemDetails>> getDescription(@Query("id") int id);
}