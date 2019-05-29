package com.example.location;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView textView;
    private LocationManager locationManager;
    private double longtitude;
    private double latitude;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    // ...

                    return true;
                case R.id.navigation_dashboard:
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
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.id_textview);
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
