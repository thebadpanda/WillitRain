package com.willitrain;

import com.willitrain.weather.WeatherModule;
import com.willitrain.weather.WeatherViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = WeatherModule.class)

@Singleton
public interface AppComponent {

    void
    inject(WeatherViewModel weatherViewModel);
}
