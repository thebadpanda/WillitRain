package com.willitrain;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.willitrain.weather.WeatherActivity;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng startPosition = new LatLng(50.460, 30.480);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startPosition));
        mMap.setMinZoomPreference(4);
        mMap.setMaxZoomPreference(10);

        if (RealmManager.getOptionsFromDb() == null) {
            final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if (activeNetwork == null || !activeNetwork.isConnected()) {
                showOptions(this);
            }
        }

        mMap.setOnMapClickListener(latLng -> {
            if (mMarker != null) {
                mMarker.setPosition(latLng);
            } else {
                mMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            double lat = marker.getPosition().latitude;
            double lon = marker.getPosition().longitude;
            startWeatherActivity(lat, lon);
            return true;
        });
    }

    private void startWeatherActivity(double lat, double lon) {
        Intent intent = new Intent(this, WeatherActivity.class);
        intent.putExtra("lat", lat);
        intent.putExtra("lon", lon);
        startActivity(intent);
    }

    private void showOptions(Context context) {
        try {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.popup_no_network, null);
            PopupWindow popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setAnimationStyle(R.style.Animation);
            popupWindow.setClippingEnabled(false);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.update(0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            new Handler().postDelayed(() -> popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0), 100L);

            Button quitBtn = layout.findViewById(R.id.btn_quit);
            quitBtn.setOnClickListener(v -> {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
