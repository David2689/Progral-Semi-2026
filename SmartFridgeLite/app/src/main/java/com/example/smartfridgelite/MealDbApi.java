package com.example.smartfridgelite;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealDbApi {

    @GET("filter.php")
    Call<MealResponse> searchByIngredient(
            @Query("i") String ingredient
    );

    // Clases para parsear la respuesta
    class MealResponse {
        public List<Meal> meals;
    }

    class Meal {
        public String idMeal;
        public String strMeal;
        public String strMealThumb;
    }
}
