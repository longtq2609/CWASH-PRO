package com.example.cwash_pro.apis;

import com.example.cwash_pro.models.User;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    public static final String link = "http://localhost:5000/";
    public static final String link = "https://cwash-pro.herokuapp.com/";

    public static Retrofit retrofit;
    public static String JWT = "";
    public static User user;
    public static String tokenDevice = "";

    static OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder().readTimeout(20, TimeUnit.SECONDS).connectTimeout(20, TimeUnit.SECONDS).addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", JWT).build();
            return chain.proceed(request);
        }).build();
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(link)
                    .client(okHttpClient())
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .build();
        }
        return retrofit;
    }
}
