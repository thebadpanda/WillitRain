package com.willitrain.network;

import com.willitrain.network.response.RetrofitBuilder;
import com.willitrain.network.response.WeatherResponse;

import io.reactivex.Single;

import static com.willitrain.AppConstants.API_KEY;

public class WillitRainService {

    public static Single<WeatherResponse> getWeatherOptions(double lat, double lon) {
        return RetrofitBuilder.getWillitRainApi().getWeatherOptions(lat, lon, API_KEY);
    }
}
