package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.locationhelper.GPSTracker;
import com.sashakhyzhun.locationhelper.LocationService;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class NewsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "NewsFragment";
    private GPSTracker gpsTracker;
    //private LocationUtil locationHelper;
    private TextView tvLatitude, tvLongitude, tvHasSpeed, tvSpeed;
    private Button buttonStart, buttonStop;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        tvLatitude = (TextView) view.findViewById(R.id.text_view_latitude);
        tvLongitude = (TextView) view.findViewById(R.id.text_view_longitude);
        tvHasSpeed = (TextView) view.findViewById(R.id.text_view_has_speed);
        tvSpeed = (TextView) view.findViewById(R.id.text_view_speed);
        buttonStart = (Button) view.findViewById(R.id.button_start);
        buttonStop = (Button) view.findViewById(R.id.button_stop);

        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
        //locationHelper = new LocationUtil(getActivity());
        //locationHelper.invokeLocationPermission();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
        //setCurrentData();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
        if (gpsTracker != null) gpsTracker.stopUsingGPS();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_start:
                getActivity().startService(new Intent(getActivity(), LocationService.class));
                gpsTracker = new GPSTracker(getActivity());
                setCurrentData();
                break;
            case R.id.button_stop:
                getActivity().stopService(new Intent(getActivity(), LocationService.class));
                break;
        }
    }

    private void setCurrentData() {
        if (ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            tvLatitude.setText(String.format("Latitude: %s", gpsTracker.getLatitude()));
            tvLongitude.setText(String.format("Longitude: %s", gpsTracker.getLongitude()));
            tvHasSpeed.setText(String.format("Has Speed: %s", gpsTracker.hasSpeed()));
            tvSpeed.setText(String.format("Speed: %s", gpsTracker.getSpeed()));

        } else {
            gpsTracker.showSettingsAlert();
        }
    }





}
