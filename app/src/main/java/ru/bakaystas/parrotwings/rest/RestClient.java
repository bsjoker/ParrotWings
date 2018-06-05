package ru.bakaystas.parrotwings.rest;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 1 on 13.04.2018.
 */

public class RestClient {
    private final static String BASE_URL = "http://193.124.114.46:3001/";
    private static Retrofit mRetrofit = null;

    public RestClient(){
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor((new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)))
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <S> S createService(Class<S> serviceClass){
        return mRetrofit.create(serviceClass);
    }

}
