package com.willitrain.network;

import com.willitrain.network.response.WeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WillitRainApi {

    @GET("current?")
    Single<WeatherResponse>
    getWeatherOptions(@Query("lat") double lat, @Query("lon") double lon, @Query("key") String key);
}
