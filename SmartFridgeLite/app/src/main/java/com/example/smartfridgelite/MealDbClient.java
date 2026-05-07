package com.example.smartfridgelite;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealDbClient {

    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static Retrofit instance;

    public static MealDbApi getApi() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return instance.create(MealDbApi.class);
    }
}
