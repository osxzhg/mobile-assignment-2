package com.example.location;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class AllActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //String [] arr;
    private String longitude_str;
    private String latitude_str;
    private String home_longitude;
    private String home_latitude;
    private  String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        longitude_str =extras.getString("longitude");
        latitude_str = extras.getString("latitude");
        home_latitude = extras.getString("home_latitude");
        home_longitude = extras.getString("home_longitude");
        name = extras.getString("name");
        //arr = content.split(",");
        /*for(String ss : arr){
            System.out.println(ss);
        }*/
        //Log.e("my", arr[0]);


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
        float zoom = 12.0f;
        LatLng  location_c=new LatLng(137,130);

        // Add a marker in Sydney and move the camera

        location_c = new LatLng(Double.parseDouble(latitude_str), Double.parseDouble(longitude_str));
        mMap.addMarker(new MarkerOptions().position(location_c).title(name));
        location_c = new LatLng(-36.8749844,174.7466741);
        mMap.addMarker(new MarkerOptions().position(location_c).title("now"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(location_c));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(location_c, zoom);
        mMap.moveCamera(update);
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(location_c)
                .radius(5000)
                .strokeColor(Color.RED));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(AllActivity.this,"标记被点击了，这里的纬度是:"+marker.getPosition().latitude+"",Toast.LENGTH_SHORT).show();
                return false;
            }
        });


    }


}
