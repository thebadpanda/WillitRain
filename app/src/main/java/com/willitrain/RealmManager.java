package com.willitrain;

import com.willitrain.network.response.WeatherResponse;

import io.realm.Realm;

public class RealmManager {

    public static void saveOptionsInDb(WeatherResponse weatherResponse) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm.copyToRealm(weatherResponse));
    }

    public static WeatherResponse getOptionsFromDb() {
        Realm realm = Realm.getDefaultInstance();
        if (!realm.where(WeatherResponse.class).findAll().isEmpty())
            return realm.where(WeatherResponse.class).findAll().get(0);
        else return null;
    }

    public static void deleteOptionsFromDb() {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm.where(WeatherResponse.class).findAll().deleteAllFromRealm());
    }
}
