package com.willitrain;

import android.app.Application;

import com.willitrain.weather.WeatherModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class WillitRainApp extends Application {

    private static WillitRainApp willItRainApp;
    private static AppComponent appComponent;

    public static WillitRainApp newInstance() {
        return willItRainApp;
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        willItRainApp = this;
        appComponent = buildComponent();
        configurationRealm();
    }

    protected AppComponent buildComponent() {
        return DaggerAppComponent.builder()
                .weatherModule(new WeatherModule())
                .build();
    }

    private void configurationRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfig = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}