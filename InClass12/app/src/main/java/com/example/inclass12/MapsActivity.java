package com.example.inclass12;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> points;
    String json;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        json= loadJSONFromAsset(this);

        Gson gson=new Gson();
        parentLocation parentLocation=gson.fromJson(json,parentLocation.class);
        ArrayList<LocationPoints> locationPoints=null;

        locationPoints=parentLocation.getPoints();

        points=new ArrayList<>();
        for(LocationPoints item:locationPoints){

            points.add(new LatLng(Double.parseDouble(item.getLatitude()),Double.parseDouble(item.getLongitude())));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PolylineOptions polyLineOptions;
        Polyline polyline=googleMap.addPolyline(new PolylineOptions().addAll(points));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

                LatLngBounds.Builder lt=new LatLngBounds.Builder();

                for(LatLng p:points){
                    lt.include(p);

                }
                int size= points.size()-1;

                mMap.addMarker(new MarkerOptions().position(points.get(0)).title("start location"));
                mMap.addMarker(new MarkerOptions().position(points.get(size)).title("end location"));

                LatLngBounds bounds=lt.build();

                mMap.setLatLngBoundsForCameraTarget(bounds);

                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,13));

            }
        });
    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("trip.json");
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}