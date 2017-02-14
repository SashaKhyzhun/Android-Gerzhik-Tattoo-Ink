package com.sashakhyzhun.gerzhiktattooink.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.sashakhyzhun.gerzhiktattooink.R;

/**
 * Created by SashaKhyzhun on 2/2/17.
 */

public class FindMyOfficeFragment extends Fragment implements OnMapReadyCallback {

    private MapFragment googleMap;
    private MarkerOptions marker;
    //48.631982, 22.294839

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_my_office, container, false);


        createMapView();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        marker.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .position(new LatLng(48.631982, 22.294839))
                .draggable(false)
                .title("Hello Maps ");
        googleMap.addMarker(marker);
    }

    /**
     * Initialises the MapView
     */
    private void createMapView() {
        /** Catch the null pointer exception that may be thrown when initialising the map */
        try {
            if (null == googleMap) {
                googleMap = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.mapView);

                /** If the map is still null after attempted initialisation, show an error to the user */
                if (null == googleMap) {
                    Toast.makeText(getActivity(), "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }


    /**
     * Adds a marker to the map
     */
//    private void addMarker(){
//
//        /** Make sure that the map has been initialised **/
//        if(null != googleMap){
//            googleMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(0, 0))
//                    .title("Marker")
//                    .draggable(true)
//            );
//        }
//    }



    @Override
    public void onResume() {
        googleMap.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        googleMap.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        googleMap.onLowMemory();
        super.onLowMemory();
    }
}
