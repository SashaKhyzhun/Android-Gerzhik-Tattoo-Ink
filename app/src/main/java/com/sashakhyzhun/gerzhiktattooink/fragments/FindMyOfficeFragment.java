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
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.sashakhyzhun.gerzhiktattooink.R;

import java.util.Date;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class FindMyOfficeFragment extends Fragment implements OnMapReadyCallback {

    MapView mMapView;
    private GoogleMap googleMap;
    //48.631982, 22.294839

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_my_office, container, false);


        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        //mMapView.onResume(); // needed to get the map to display immediately
        mMapView.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        // For dropping a marker at a point on the Map
        LatLng uzh = new LatLng(48.631982, 22.294839);
        googleMap.addMarker(new MarkerOptions().position(uzh).title("Gerzhik Tattoo Ink").snippet("Top Ukraine Tattoo Salon"));

        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &
            ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            /************************ for API >= 23 *************************/
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
            }, 1);
            /****************************************************************/

            return;
        }
        googleMap.setMyLocationEnabled(true);


        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(uzh).zoom(6).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }


}
