package com.shadowstorm.metrocab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Dipesh on 04/06/16.
 */
public class DriverPge  extends FragmentActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    GPSTracker gps;
    double longitude1, latitude1;
    Thread thread;
    noti_db notiDb;
    user_database userDb;
    String myPhone;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //this publicLatLng is for universal use which has been initialized in the section below at location manager
    public LatLng publicLatLng;
    Marker marker = null;
    GPSTracker gps1 = new GPSTracker(this);


    //setUpMap onCreate mathi xa coz crash hunxa ntra coz we need publicLatLng initialized

    private void setUpMap() {
        userDb = new user_database(this);
        notiDb = new noti_db(this);
        final ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedules a task to run every 10 seconds:
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    //Looper.prepare();
                    // gps = new GPSTracker(MapsActivity.this);
//                    latitude1 = gps.getLatitude();
//                    longitude1 = gps.getLongitude();

                    LocationManager locationManager = (LocationManager)
                            getSystemService(Context.LOCATION_SERVICE);
                    Criteria criteria = new Criteria();

                    if (ActivityCompat.checkSelfPermission(DriverPge.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DriverPge.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(locationManager
                            .getBestProvider(criteria, false));
                    //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                    //works fine with location as well now
                    latitude1 = gps.getLatitude();
                    longitude1 = gps.getLongitude();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Cursor res = userDb.getUserData();
                            if (res.getCount() > 0) {
                                while (res.moveToNext()) {
                                    String user = res.getString(9);

                                    //Toast.makeText(DriverPge.this, "Lat: " + latitude1 + "Lon: " + longitude1, Toast.LENGTH_SHORT).show();

                                    Utils.updateLocationInDatabase(DriverPge.this, user, latitude1 + "", longitude1 + "");
                                    //Marker marker= new MarkerOptions().position(new LatLng(latitude1, longitude1).title("current position"));
                                    // mMap.addMarker(marker));
                                    //MarkerOptions marker = new MarkerOptions();
                                    // Marker marker ;
                                    if (marker == null) {
                                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                                    } else {
                                        marker.remove();
                                        // lol google ko blue marker le wrong  dekhaxa mero marker le accurate dekhaxa lol

                                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                                    }
                                }
                            }
                        }
                    });
                    //Looper.loop();
                }
            }
        });
        thread.start();
        //current loc
        latitude1 = gps.getLatitude();
        longitude1 = gps.getLongitude();
        //yo marker le mathi ko current bhanne marker lai override garxa coz both return the same value
        //Toast.makeText(this, "Lat: " + latitude1 + "Lon: " + longitude1, Toast.LENGTH_LONG).show();
        Cursor res = userDb.getUserData();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                String user = res.getString(9);
                Utils.updateLocationInDatabase(this, user, latitude1 + "", longitude1 + "");
            }
        }

        //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));


        // mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        //setMyLocation lai false gardiye hunxa paxi overlap nabhyera dekhauna mann nabhaye


        mMap.setMyLocationEnabled(true);


        publicLatLng = new LatLng(latitude1, longitude1);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_page);

        gps = new GPSTracker(this);

        //----------------------------------------------------------------------------------------not implemented in ashish project
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check type of intent filter
                if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                    //Registration success
                    String token = intent.getStringExtra("token");
                    //Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                    //Registration error
                    Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                } else {
                    //Tobe define
                }
            }
        };

        //Check status of Google play service in device
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (ConnectionResult.SUCCESS != resultCode) {
            //Check type of error
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(), "Google Play Service is not install/enabled in this device!", Toast.LENGTH_LONG).show();
                //So notification
                GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
            } else {
                Toast.makeText(getApplicationContext(), "This device does not support for Google Play Service!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Start service
            Intent itent = new Intent(this, GCMRegistrationIntentService.class);
            startService(itent);
        }
        /// Crouton.makeText(LogInActivity.this,)

        //------------------------------------------------------------------------------------------end

        setUpMapIfNeeded();
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        //mMap.addMarker(new MarkerOptions()
        //       .position(new LatLng(latitude, longitude))
        //       .title("current"));
        int carsCount1 = 0;
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude + carsCount1 + -0.05, carsCount1 + longitude + -0.05))
                .title("car No " + carsCount1));

        //zoomlevel
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(publicLatLng.latitude, publicLatLng.longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);


        //  LatLng myLatLng = new LatLng(latitude,longitude);
        // Location myLocation = new Location(String.valueOf(myLatLng));

        // float acc = myLocation.getAccuracy();


        //Toast.makeText(this, acc+"" ,Toast.LENGTH_SHORT).show();


        //this below  doesnt work so commented
        //current location marker by marker button
      /*  double latitudeGoogle = mMap.getMyLocation().getLatitude();
        double longitudeGoogle = mMap .getMyLocation().getLongitude();
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitudeGoogle, longitudeGoogle))
                .title("Current Position "));
        */
        //for marking different cars on map

        int carsCount;
        for (carsCount = 0; carsCount <= 10; carsCount++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(carsCount, carsCount))
                    .title("Hello world"));
        }

        //polyline between two points
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0))
                .width(5)
                .color(Color.BLUE));

        //suppose nearest cars to be around
    }

    public void checkMyNoti(View v) {
        Cursor res = notiDb.getNotiData();
        if (res.getCount() > 0) {
            Intent intent = new Intent(this,checkNotification.class);
            startActivity(intent);
        }else{
            Cursor ress = userDb.getUserData();
            if (ress.getCount() > 0) {
                while (ress.moveToNext()) {
                    myPhone = ress.getString(5);
                    String type = "checkNoti";
                    //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                    pushNotification backgroundWorkers = new pushNotification(this);
                    backgroundWorkers.execute(type, "null", myPhone, "null");   }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("LogInActivity", "onResume");
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        setUpMapIfNeeded();
    }

    @Override
    protected void onPause() {
        super.onPause();
        thread.interrupt();
        Log.w("LogInActivity", "onPause");
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    public void onZoomIna(View v) {

        if (v.getId() == R.id.idZoomInButtona) {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
    }

    public void onZoomOuta(View v) {
        if (v.getId() == R.id.idZoomOutButtona) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    public void changeTypea(View v) {
        if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
    }

    public void onSearcha(View v) {
        EditText searchBar = (EditText) findViewById(R.id.idSearchBara);
        String Location = searchBar.getText().toString();
        List<Address> addressList = null;
        try {

            if (Location != null || Location != "") {
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(Location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Address address = addressList.get(0);

                LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
                Location location = new Location(String.valueOf(latlng));

                if (location.getAccuracy() <= 5) {
                    mMap.addMarker(new MarkerOptions().position(latlng).title("Marker"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                }
                /*if (!location.hasAccuracy()) {
                    return;
                }
                if (location.getAccuracy() > 5) {
                    return;
                }*/
            } else {
                Toast.makeText(this, "Please enter a place name", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void streetSearchBara(Location location) {
        if (!location.hasAccuracy()) {
            return;
        }
        if (location.getAccuracy() > 5) {
            return;
        }
        // do something with location accurate to 5 meters here.
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }
}








