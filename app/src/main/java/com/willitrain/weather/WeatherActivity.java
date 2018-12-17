package com.willitrain.weather;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.willitrain.MapsActivity;
import com.willitrain.R;
import com.willitrain.RealmManager;
import com.willitrain.Status;
import com.willitrain.databinding.ActivityWeatherBinding;
import com.willitrain.network.response.WeatherResponse;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class WeatherActivity extends AppCompatActivity {

    private static final String TAG = WeatherActivity.class.getSimpleName();
    private ActivityWeatherBinding binding;
    private static WeatherViewModel weatherViewModel;
    private double mSavedLat;
    private double mSavedLon;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.inject();

        if (RealmManager.getOptionsFromDb() != null) {
            mSavedLat = Double.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getLat());
            mSavedLon = Double.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getLon());
            setData();
        } else {
            mSavedLat = 0;
            mSavedLon = 0;
        }

        Intent intent = getIntent();
        double lat = intent.getDoubleExtra("lat", mSavedLat);
        double lon = intent.getDoubleExtra("lon", mSavedLon);

        weatherViewModel.getWeatherOptions(lat, lon).observe(this, this::checkGetWeatherOptions);

        binding.btnMap.setOnClickListener(v -> {
            startActivity(new Intent(this, MapsActivity.class));
        });

        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) {
            Toast.makeText(this, getString(R.string.you_are_offline), Toast.LENGTH_LONG).show();
        }
    }

    private void checkGetWeatherOptions(WeatherResponse weatherResponse) {
        weatherViewModel.isGetWeatherSuccess.observe(this, aBoolean -> {
            if (aBoolean == Status.SUCCESS) {
                setData();
            } else if (aBoolean == Status.ERROR) {
                Log.i(TAG, "ERROR with Weather response");
            }
        });
    }

    private void setData() {
        binding.city.setText(RealmManager.getOptionsFromDb().getData().get(0).getCityName());
        int iconResId = getResources().getIdentifier(RealmManager.getOptionsFromDb().getData().get(0).getWeather().getIcon(), "drawable", getPackageName());
        binding.weatherIcon.setImageResource(iconResId);
        binding.weatherDescription.setText(RealmManager.getOptionsFromDb().getData().get(0).getWeather().getDescription());
        binding.temperature.setText(String.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getTemp().toString()));
        binding.windSpeed.setText(String.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getWindSpd()));
        binding.windDirection.setText(String.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getWindCdirFull()));
        binding.feelsLike.setText(String.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getAppTemp()));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(RealmManager.getOptionsFromDb().getData().get(0).getTimezone()));

        binding.sunrise.setText(simpleDateFormat.format(Time.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getSunrise() + ":00")));
        binding.sunset.setText(simpleDateFormat.format(Time.valueOf(RealmManager.getOptionsFromDb().getData().get(0).getSunset() + ":00")));
    }
}
