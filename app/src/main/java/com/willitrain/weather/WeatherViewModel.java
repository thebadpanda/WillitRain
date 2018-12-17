package com.willitrain.weather;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.willitrain.Status;
import com.willitrain.WillitRainApp;
import com.willitrain.network.response.WeatherResponse;

import javax.inject.Inject;

public class WeatherViewModel extends ViewModel {

    @Inject
    public WeatherRepository weatherRepository;

    void inject(){
        WillitRainApp.getAppComponent().inject(this);
    }

    MutableLiveData<WeatherResponse> weatherOptions = new MutableLiveData<>();
    MutableLiveData<Status> isGetWeatherSuccess = new MutableLiveData<>();

    MutableLiveData<WeatherResponse> getWeatherOptions(double lat, double lon) {
        WeatherCall weatherCall = new WeatherCall() {
            @Override
            public void success(WeatherResponse weatherbitResponse) {
                weatherOptions.setValue(weatherbitResponse);
                isGetWeatherSuccess.setValue(Status.SUCCESS);
            }

            @Override
            public void error() {
                isGetWeatherSuccess.setValue(Status.ERROR);
            }
        };
        weatherRepository.getWeatherOptions(weatherCall, lat, lon);
        return weatherOptions;
    }
}
