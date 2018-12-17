package com.willitrain.weather;

import com.willitrain.network.response.WeatherResponse;

interface WeatherCall {
    void success(WeatherResponse weatherResponse);

    void error();
}
