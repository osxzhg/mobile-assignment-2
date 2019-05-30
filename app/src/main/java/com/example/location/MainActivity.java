package com.example.location;

import android.Manifest;
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
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView textView;
    private LocationManager locationManager;
    private double longtitude;
    private double latitude;
    private Geocoder geocoder;
    private List<Address> addressList;
    private TextView tvAddress;
    private StringBuilder sb;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // ...

                    return true;
                case R.id.navigation_api:
                    Intent api = new Intent();
                    api.setClass(MainActivity.this,ApiActivity.class);
                    api.putExtra("longtitude", Double.toString(longtitude));
                    api.putExtra("latitude",Double.toString(latitude));

                    if (api.resolveActivity(getPackageManager()) != null) {
                        startActivity(api);
                    }

                    return true;
                case R.id.navigation_map:
                    Intent mesg = new Intent();
                    mesg.setClass(MainActivity.this,MapsActivity.class);
                    mesg.putExtra("longtitude", Double.toString(longtitude));
                    mesg.putExtra("latitude",Double.toString(latitude));



                    if (mesg.resolveActivity(getPackageManager()) != null) {
                        startActivity(mesg);
                    }

                    return true;
                case R.id.navigation_all:
                    Intent apiIntent = new Intent();
                    apiIntent.setClass(MainActivity.this, ThirdActivity.class);

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
        onLocationChanged(location);
    }

    @Override
    public void onLocationChanged(Location location) {
        longtitude = location.getLongitude();
        latitude = location.getLatitude();
        textView.setText("Longitude: " + longtitude + "\n" + "Latitude: " + latitude);
        sb = new StringBuilder();
        geocoder = new Geocoder(MainActivity.this);
        addressList = new ArrayList<Address>();

        try {
            // 返回集合对象泛型address
            addressList= geocoder.getFromLocation(latitude,longtitude,1);


            if (addressList.size() > 0) {
                Address address = addressList.get(0);
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    Log.e("add",address.getAddressLine(i));
                    sb.append(address.getAddressLine(i)).append("\n");
                }
                sb.append(address.getFeatureName());//周边地址
            }
            tvAddress.setText(sb.toString());
            ;
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
