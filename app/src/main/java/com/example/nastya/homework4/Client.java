package com.example.nastya.homework4;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class Client {
    private static final String BASE_URL = "https://api.tinkoff.ru/v1/";
    private static Client clientInstance = new Client();
    private static MyDataApi myDataApi;

    private Client() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myDataApi = retrofit.create(MyDataApi.class);
    }

    static Client getClientInstance() {
        return clientInstance;
    }

    MyDataApi getMyDataApi() {
        return myDataApi;
    }

}
