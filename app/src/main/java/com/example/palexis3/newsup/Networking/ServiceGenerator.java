package com.example.palexis3.newsup.Networking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
   The ServiceGenerator class uses Retrofit’s Retrofit builder to
   create a new REST client with the given API base url (BASE_URL)
*/

public class ServiceGenerator {

    private static final String BASE_URL = "https://newsapi.org/";

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();


    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    private static Retrofit retrofit = builder.build();

    // setting logging functionality to know what's being sent
    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder();

    // a generic method that creates a rest client given a class/interface
    public static <T> T createService(Class<T> serviceClass) {

        // check the logs of what Retrofit is sending
        if(!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
