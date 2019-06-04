package com.example.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView textView;
    private LocationManager locationManager;
    private double longitude;
    private double latitude;
    private Geocoder geocoder;
    private List<Address> addressList;
    private TextView tvAddress;
    private StringBuilder sb;
    private String cityName = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_api:
                    Intent api = new Intent();
                    api.setClass(MainActivity.this,ApiActivity.class);
                    //api.putExtra("longitude", Double.toString(longitude));
                    //api.putExtra("latitude",Double.toString(latitude));

                    if (api.resolveActivity(getPackageManager()) != null) {
                        startActivity(api);
                    }

                    return true;
                case R.id.navigation_map:
                    Intent mesg = new Intent();
                    mesg.setClass(MainActivity.this,MapsActivity.class);
                    mesg.putExtra("longitude", Double.toString(longitude));
                    mesg.putExtra("latitude",Double.toString(latitude));



                    if (mesg.resolveActivity(getPackageManager()) != null) {
                        startActivity(mesg);
                    }

                    return true;
                case R.id.navigation_all:
                    Intent apiIntent = new Intent();
                    apiIntent.setClass(MainActivity.this, ThirdActivity.class);
                    apiIntent.putExtra("longitude", Double.toString(longitude));
                    apiIntent.putExtra("latitude",Double.toString(latitude));
                    apiIntent.putExtra("cityName",cityName);

                    if (apiIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(apiIntent);
                    }
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.id_textview);
        tvAddress = (TextView) findViewById(R.id.address_textview);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        final Button btnGetLocation = (Button) findViewById(R.id.button);
        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locIntent = new Intent();
                locIntent.setClass(MainActivity.this, LocationActivity.class);


                if (locIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(locIntent);
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        if(location!=null) {
            onLocationChanged(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("debug","onlocation");
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        textView.setText("Longitude: " + longitude + "\n" + "Latitude: " + latitude);
        sb = new StringBuilder();
        geocoder = new Geocoder(MainActivity.this,Locale.getDefault());
        addressList = new ArrayList<Address>();


        try {
            // return address
            addressList= geocoder.getFromLocation(latitude,longitude,10);


            if (addressList.size() > 0) {
                for(Address adr:addressList){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                    }
                }

            }
            tvAddress.setText(cityName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
