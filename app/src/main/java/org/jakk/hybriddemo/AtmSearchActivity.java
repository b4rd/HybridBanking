package org.jakk.hybriddemo;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jakk.hybriddemo.model.Location;
import org.jakk.hybriddemo.model.response.AtmSearchResponse;

public class AtmSearchActivity extends BaseActivity implements OnMapReadyCallback {

    private static final String TAG = AtmSearchActivity.class.getSimpleName();
    private static final String PATH_LOCATION = "/atm";
    private static final Location MY_LOCATION = new Location(48.082090, 20.766857);
    private static final LatLng MY_LOCATION_LAT_LNG = new LatLng(MY_LOCATION.getLat(), MY_LOCATION.getLng());

    private SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atm_search);
        setTitle(R.string.atm_search_title);

        mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction()
            .add(R.id.map_container, mapFragment)
            .commit();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addCircle(new CircleOptions()
                .center(MY_LOCATION_LAT_LNG)
                .radius(20)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(MY_LOCATION_LAT_LNG));
        callService(PATH_LOCATION, MY_LOCATION, AtmSearchResponse.class);
    }

    @Override
    protected void onServiceResponse(String path, Object response) {
        AtmSearchResponse atmSearchResponse = (AtmSearchResponse) response;
        for (Location location : atmSearchResponse.getItems()) {
            mapFragment.getMap().addMarker(
                    new MarkerOptions().position(new LatLng(location.getLat(), location.getLng())));
        }
    }
}
