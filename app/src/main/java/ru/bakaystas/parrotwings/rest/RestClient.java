package ru.bakaystas.parrotwings.rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1 on 13.04.2018.
 */

public class RestClient {
    private final static String BASE_URL = "http://193.124.114.46:3001/";
    private static Retrofit mRetrofit = null;

    public static Retrofit getClient(){
        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        return mRetrofit;
    }

}
