package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.locationhelper.GPSTracker;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class NewsFragment extends Fragment {

    private GPSTracker gpsTracker;
    //private LocationUtil locationHelper;
    private TextView tvLatitude, tvLongitude, tvSpeed;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        gpsTracker = new GPSTracker(getActivity());

        tvLatitude = (TextView) view.findViewById(R.id.text_view_latitude);
        tvLongitude = (TextView) view.findViewById(R.id.text_view_longitude);
        tvSpeed = (TextView) view.findViewById(R.id.text_view_speed);

        //locationHelper = new LocationUtil(getActivity());
        //locationHelper.invokeLocationPermission();



        if (ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(getContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            tvLatitude.setText(String.format("Latitude: %s", gpsTracker.getLatitude()));
            tvLongitude.setText(String.format("Longitude: %s", gpsTracker.getLongitude()));
            tvSpeed.setText(String.format("Speed: %s", gpsTracker.hasSpeed()));
        } else {
            gpsTracker.showSettingsAlert();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }





}
