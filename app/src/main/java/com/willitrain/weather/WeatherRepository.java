package com.willitrain.weather;

import com.willitrain.RealmManager;
import com.willitrain.network.WillitRainService;
import com.willitrain.network.response.WeatherResponse;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class WeatherRepository {

    void getWeatherOptions(final WeatherCall weatherCall, double lat, double lon) {
        WillitRainService.getWeatherOptions(lat, lon)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {

                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        RealmManager.deleteOptionsFromDb();
                        RealmManager.saveOptionsInDb(weatherResponse);
                        weatherCall.success(weatherResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        weatherCall.error();
                    }
                });
    }
}
