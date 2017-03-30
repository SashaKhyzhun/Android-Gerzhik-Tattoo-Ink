package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sashakhyzhun.gerzhiktattooink.R;
import com.sashakhyzhun.locationhelper.GPSTracker;
import com.sashakhyzhun.locationhelper.MyLocationHelper;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class NewsFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);


        GPSTracker gpsTracker = new GPSTracker(getActivity());

        TextView text = (TextView) view.findViewById(R.id.text_view_news);

        MyLocationHelper locationHelper = new MyLocationHelper(getActivity());
        locationHelper.invokeLocationPermission();

        if (gpsTracker.canGetLocation()) {
            text.setText("Lat: " + gpsTracker.getLatitude() + " Lon: " + gpsTracker.getLongitude());
        } else {
            text.setText("Unable to find");
            gpsTracker.showSettingsAlert();
            System.out.println("unable");
        }


        return view;
    }



}
