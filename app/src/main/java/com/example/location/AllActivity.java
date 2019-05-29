package com.example.location;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String [] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle extras = getIntent().getExtras();
        String content = extras.getString(ApiActivity.PAYLOAD);
        arr = content.split(",");
        /*for(String ss : arr){
            System.out.println(ss);
        }*/
        Log.e("my", arr[0]);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng  location_c=new LatLng(137,130);

        // Add a marker in Sydney and move the camera
        int i;
        int count=arr.length/3;
        for(i=0;i<count;i++) {
            location_c = new LatLng(Double.parseDouble(arr[1 + i*3]), Double.parseDouble(arr[2 + i*3]));
            mMap.addMarker(new MarkerOptions().position(location_c).title(arr[i*3]));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location_c));
    }


}
