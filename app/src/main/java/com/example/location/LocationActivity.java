package com.example.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class LocationActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 99;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private int flag=0;
    private double longitude;
    private double latitude;
    private Geocoder geocoder;
    private List<Address> addressList;
    private String cityName = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {

                case R.id.navigation_api:
                    Intent api = new Intent();
                    api.setClass(LocationActivity.this,ApiActivity.class);
                    //api.putExtra("longitude", Double.toString(longitude));
                    //api.putExtra("latitude",Double.toString(latitude));

                    if (api.resolveActivity(getPackageManager()) != null) {
                        startActivity(api);
                    }

                    return true;
                case R.id.navigation_map:
                    Intent mesg = new Intent();
                    mesg.setClass(LocationActivity.this,MapsActivity.class);
                    mesg.putExtra("longitude", Double.toString(longitude));
                    mesg.putExtra("latitude",Double.toString(latitude));



                    if (mesg.resolveActivity(getPackageManager()) != null) {
                        startActivity(mesg);
                    }

                    return true;
                case R.id.navigation_all:
                    Intent apiIntent = new Intent();
                    apiIntent.setClass(LocationActivity.this, ThirdActivity.class);
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
        Log.e("onCreate","onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        final TextView txtLatitude = (TextView)findViewById(R.id.txtLatitude);
        final TextView txtLongitude = (TextView)findViewById(R.id.txtLongitude);




        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //final FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


                try {
                    Task<Location> location = fusedLocationClient.getLastLocation();

                    //TODO don't do this!
                    //double lat = location.getResult().getLatitude();
                    location.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {

                            txtLatitude.setText(Double.toString(task.getResult().getLatitude()));
                            txtLongitude.setText(Double.toString(task.getResult().getLongitude()));
                            latitude=task.getResult().getLatitude();
                            longitude=task.getResult().getLongitude();
                            System.err.println(task.getResult().getLatitude());
                            geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
                            addressList = new ArrayList<Address>();


                            try {
                                // return address
                                addressList= geocoder.getFromLocation(latitude,longitude,10);


                                if (addressList.size() > 0) {
                                    for(Address adr:addressList){
                                        if(adr.getLocality() != null && adr.getLocality().length() > 0){
                                            cityName = adr.getLocality();
                                            Log.e("location:",cityName);
                                        }
                                    }

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
                catch(SecurityException ex)
                {
                    ex.printStackTrace();
                }




        LocationRequest req = new LocationRequest();
        req.setInterval(10000); // 2 seconds
        req.setFastestInterval(10000); // 500 milliseconds
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
            flag=1;
            fusedLocationClient.requestLocationUpdates(req,locationCallback=new LocationCallback(){
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Log.e("location:",locationResult.getLastLocation().toString());

                    //Toast.makeText(MainActivity.this,"location callback:"+ locationResult.getLastLocation().toString(), Toast.LENGTH_LONG).show();
                }
            },null);

        }

    }
    protected void onPause() {
        super.onPause();
        Log.e("pause","pause");


        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);

        locationCallback = null;
    }

    protected void onResume() {
        super.onResume();

        Log.e("resume","onResume");

        if(flag==1) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        LocationRequest req = new LocationRequest();
        req.setInterval(10000); // 2 seconds
        req.setFastestInterval(10000); // 500 milliseconds
        req.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

            if(locationCallback == null) {

                fusedLocationClient.requestLocationUpdates(req, locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        Log.e("location:", locationResult.getLastLocation().toString());

                        //Toast.makeText(MainActivity.this, "other" + locationResult.getLastLocation().toString(), Toast.LENGTH_LONG).show();
                    }
                }, null);
            }

        }
    }
}
