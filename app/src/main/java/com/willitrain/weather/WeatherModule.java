package com.willitrain.weather;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class WeatherModule {

    @Provides
    @Singleton
    public WeatherRepository getWeatherRepository() {
        return new WeatherRepository();
    }
}