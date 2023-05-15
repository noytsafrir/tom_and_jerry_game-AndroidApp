package com.example.tom_and_jerry_part1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tom_and_jerry_part1.DB.Record;
import com.example.tom_and_jerry_part1.DB.RecordsList;
import com.example.tom_and_jerry_part1.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment{
    private GoogleMap googleMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_map_fragment, container, false);
        SupportMapFragment supportMapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps));
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MapFragment.this.googleMap = googleMap;
                List<Record> recordsList = RecordsList.getInstance().getTopRecords();
                if(!recordsList.isEmpty()) {
                    List<LatLng> locations = new ArrayList<>();
                    for (Record record : recordsList)
                        locations.add(new LatLng(record.getLat(), record.getLng()));
                    setMapMultipleLocations(locations);
                }
            }
        });
        return view;
    }

    public void setMapLocation(double lat,double lng,String namePlayer) {
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng)).title(namePlayer));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lng))   // Sets the center of the map to location user
                .zoom(15)                       // Sets the zoom
                .build();                       // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void setMapMultipleLocations(List<LatLng> locations) {
        googleMap.clear();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng location : locations) {
            builder.include(location); // Add marker position to builder
            googleMap.addMarker(new MarkerOptions().position(location));
        }

        LatLngBounds bounds = builder.build();
        int padding = 100; // Padding in pixels to set around the markers
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);
    }
}