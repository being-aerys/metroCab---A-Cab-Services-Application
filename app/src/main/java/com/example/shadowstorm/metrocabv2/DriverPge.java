package com.example.shadowstorm.metrocabv2;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by Dipesh on 04/06/16.
 */
public class DriverPge  extends FragmentActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    GPSTracker gps;
    double longitude1, latitude1,latitude2,longitude2,latChangeCheck,longChangeCheck;


    Thread thread;
    private Noti_db notiDb;

    int markerNum=2;
    User_database userDb;
    String myPhone;
    Boolean camUpdateSet,lat1Check;
    String passMyPhone;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //this publicLatLng is for universal use which has been initialized in the section below at location manager
    public LatLng publicLatLng;
    Marker marker = null;
    Marker marker1=null;
    int updateNumber;
    PolylineOptions polylineOptions;
    Polyline line = null;
    GPSTracker gps1 = new GPSTracker(this);
    String distance="";
    String duration="";
    Boolean busyMark;
    int count = 0;
    ArrayList listTest = new ArrayList( );
    public Button status ;
    String statusString = "n/a";



    //setUpMap onCreate mathi xa coz crash hunxa ntra coz we need publicLatLng initialized

    private void setUpMap() {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            Log.e("update bhayo lat5", String.valueOf(latitude1));

            userDb = new User_database(this);
        /*
        Thread thre = new Thread(new Runnable() {
            @Override
            public void run() {
                while (0 == 0){

                    getStatus();
                    try {
                        sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thre.start();
        */

            String s = getStatus();
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Log.e("update bhayo lat4", String.valueOf(latitude1));

                            sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            break;
                        }
                        //========================
                        getStatus();
                        //========================
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("update bhayo lat2", String.valueOf(latitude1));

                                Cursor res = userDb.getUserData();
                                if (res.getCount() > 0) {
                                    while (res.moveToNext()) {
                                        //take the 10th item from the SQLite db
                                        String user = res.getString(9);

                                        GlobalValues.globalDriverUseId = user;
                                        //ALL THE MAP LOCARION UPDATES COME FROM HERE
                                        try {

                                            LocationManager locationManager = (LocationManager)
                                                    getSystemService(Context.LOCATION_SERVICE);
                                            Criteria criteria = new Criteria();
                                            Location location = locationManager.getLastKnownLocation(locationManager
                                                    .getBestProvider(criteria, false));
                                            //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                                            //works fine with location as well now
                                            latitude1 = location.getLatitude();
                                            Log.e("LocationServiceNext", String.valueOf(latitude1));
                                            longitude1 = location.getLongitude();




                                            //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("location.position"));

                                      /*  GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
                                        latitude1 = gpsTracker.getLatitude();
                                        longitude1 = gpsTracker.getLongitude();
                                        //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                                        Toast.makeText(MapsActivity.this, "Lat: " + latitude1 + "Lon: " + longitude1, Toast.LENGTH_SHORT).show();
                                        publicLatLng = new LatLng(latitude1, longitude1);
                                        Toast.makeText(MapsActivity.this,"yei ta ho ni",Toast.LENGTH_SHORT);
                                        Log.e("update bhayo lat",String.valueOf(latitude1));
                                        */

                                        } catch (Exception e) {
                                            GPSTracker gpsTracker = new GPSTracker(DriverPge.this);
                                            latitude1 = gpsTracker.getLatitude();
                                            longitude1 = gpsTracker.getLongitude();
                                            //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                                            Log.e("DriverPage", "---------------------Latitude-------" + latitude1);
                                            Log.e("DriverPage", "---------------------Longitude-------" + longitude2);
                                            publicLatLng = new LatLng(latitude1, longitude1);
                                            Log.e("DriverPage", "---------------------Lat Long Updated with above value-------");
                                        }

                                        //Toast.makeText(DriverPge.this,"COMBDVRPGE"+String.valueOf(latitude1),Toast.LENGTH_LONG).show();



                                        //while true bhako xa so auto update xa

                                        if (latitude1 * 2 == 0){

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(DriverPge.this,"Turn ON the GPS",Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                        }
                                        else {
                                            Utils.updateLocationInDatabase(DriverPge.this, user, latitude1 + "", longitude1 + "");

                                        }

                                        if (marker == null) {
                                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("foo position"));
                                        } else {
                                            marker.remove();

                                            // lol google ko blue marker le wrong  dekhaxa mero marker le accurate dekhaxa lol
                                            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("updateValue " + String.valueOf(updateNumber)));
                                            updateNumber = updateNumber + 1;
                                            Log.e("updateNumber", String.valueOf(updateNumber));
                                        }
                                    }
                                }
                            }
                        });

                    } //
                }
            });
            thread.start();

            //locationChanged


        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_page);
        notiDb = new Noti_db(this);
        gps = new GPSTracker(this);
        status = (Button)findViewById(R.id.statusButton);

        //Log.e("AAYO DriverPge ma",String.valueOf(notiDb));

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            //----------------------------------------------------------------------------------------not implemented in ashish project
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Check type of intent filter
                    if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        //Registration success
                        String token = intent.getStringExtra("token");
                        Log.e("DriverPage", "---------------------GCM token-------" + token);
                    } else if (intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)) {
                        //Registration errortoken
                        Log.e("DriverPage", "------------------------GCM registration error !!!-------");
                    } else {
                        Log.e("DriverPage", "---------------------DRIVER pge GCM registration else herr!!!-------");
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
                Log.e("DriverPage", "---------------------Has Google Play Service!-------");
                startService(itent);
            }
            /// Crouton.makeText(LogInActivity.this,)

            //------------------------------------------------------------------------------------------end

            setUpMapIfNeeded();
            //THE FIRST MAP LOCATION IS FETCHED BY THE FOLLWING
            try {
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                //works fine with location as well now
                latitude1 = location.getLatitude();
                latChangeCheck = location.getLatitude();
                Log.e("1stLocationService", String.valueOf(latitude1));
                //Toast.makeText(MapsActivity.this,"UpdatedLat",Toast.LENGTH_LONG).show();
                longitude1 = location.getLongitude();
                longChangeCheck = location.getLongitude();
                mMap.setMyLocationEnabled(true);

                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("location.position"));
                publicLatLng = new LatLng(latitude1, longitude1);
                //zoomlevel
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude1, longitude1));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);
            /*  GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
             latitude1 = gpsTracker.getLatitude();
              longitude1 = gpsTracker.getLongitude();
               //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
               Toast.makeText(MapsActivity.this, "Lat: " + latitude1 + "Lon: " + longitude1, Toast.LENGTH_SHORT).show();
                publicLatLng = new LatLng(latitude1, longitude1);
               Toast.makeText(MapsActivity.this,"yei ta ho ni",Toast.LENGTH_SHORT);
                Log.e("update bhayo lat",String.valueOf(latitude1));
                                        */

            } catch (Exception e) {
                GPSTracker gpsTracker = new GPSTracker(DriverPge.this);
                latitude1 = gpsTracker.getLatitude();
                longitude1 = gpsTracker.getLongitude();
                //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                Log.e("DriverPge", "---------------------Latitude" + latitude1);
                Log.e("DriverPge", "---------------------Longitude" + longitude1);
                publicLatLng = new LatLng(latitude1, longitude1);
                Log.e("DriverPge", "---------------------Longitude Longitude updated with above value");
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("location.position"));

                publicLatLng = new LatLng(latitude1, longitude1);
                //zoomlevel
                CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude1, longitude1));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);


            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkMyNoti(View v) {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            try {
                Cursor res1 = notiDb.getNotiData();
                Log.e("NOtification db rows", "--------------------------------" + res1.getCount());

                if (res1.getCount() > 0) {
                    Intent intent = new Intent(this, CheckNotification.class);
                   // Toast.makeText(this, "AAYO 2", Toast.LENGTH_SHORT).show();

                    startActivity(intent);
                } else {
                    A.t(this, "No Requests");
                    Cursor ress = userDb.getUserData();
                    if (ress.getCount() > 0) {
                        while (ress.moveToNext()) {
                            myPhone = ress.getString(5);

                            String type = "checkNoti";
                            //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                            PushNotification backgroundWorkers = new PushNotification(this);
                            backgroundWorkers.execute(type, myPhone);
                        }
                    }
                }

            } catch (NullPointerException n) {
                n.printStackTrace();
                Log.e("DriverPge", "-----------------------------------CHECK MY NOTI KAAM GARENA");
                //Toast.makeText(this,"Caught.",Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }

    }
    public void changeStatus(View v) {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            Cursor ress = userDb.getUserData();
            if (ress.getCount() > 0) {
                while (ress.moveToNext()) {
                    String id = ress.getString(0);
                    final String phone = ress.getString(5);
                    String status = ress.getString(11);
                    Log.e("Current Status  = ", status);
                    final String free = "FREE";
                    final String busy = "BUSY";
                    if (status.equals(free)) {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DriverPge.this);
                        myAlert.setMessage("Confirm to change status to " + busy + ".")
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String type = "changeStatus";


                                        //------------------------------------------TO CLEAR ALL REQUESTS BY BUSY


                                        busyMark = true;
                                        //CheckNotification cn = new CheckNotification();
                                        busySoClearAll(busyMark);
                                        // Noti_db no = new Noti_db(this);


                                        //---------------------------


                                        LoginCheck backgroundWorkers = new LoginCheck(DriverPge.this);
                                        backgroundWorkers.execute(type, phone, busy);
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("Change Status")
                                .setIcon(R.drawable.myridec1)
                                .create();
                        myAlert.show();


                        //----------------------------------------------------
                    } else {
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(DriverPge.this);
                        myAlert.setMessage("Confirm to change status to " + free + ".")
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String type = "changeStatus";


                                        LoginCheck backgroundWorkers = new LoginCheck(DriverPge.this);
                                        backgroundWorkers.execute(type, phone, free);
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();


                                    }


                                })
                                .setTitle("Change Status")
                                .setIcon(R.drawable.myridec1)
                                .create();
                        myAlert.show();
                    }
                }
            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    public void busySoClearAll( Boolean busyMark){

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            //Boolean t = busyMark;
            if (busyMark) {
                notiDb = new Noti_db(this);

                notiDb.getWritableDatabase();
                userDb.getWritableDatabase();
                Cursor c = notiDb.getNotiData();
                Cursor c2 = userDb.getUserData();

                while (c.moveToNext()) {
                    String phonePassen = c.getString(4);

                    while (c2.moveToNext()) {
                        String myPhone = c2.getString(5);
                        passMyPhone = myPhone;

                    }


                    Boolean check1 = listTest.add(c.getString(0));


                    //-------------------------java bata delete
                    if (listTest.get(count) != null) {
                        int done = notiDb.deleteNotiData(String.valueOf(listTest.get(count))); //arraylist ma bhako value db_id ho delete those

                        //---------------------------------aba sab lai notification pathai
                        ListView myList = (ListView) findViewById(R.id.listDrivernoti);

                        //------------------------

                /* VIEW CLEAR XODDE---------------BECAUSE ARRAYLIST BATA DELETE BHAYESI SEE REQUESTS GARDA AQLITE ma bhetdaina ani NO REQUESTS
                //bhanera dekhauxa ...SO no baal

                    //---------------------------Aba View clear gareko paile ani balla java bata
                    //ListView myList = (ListView) findViewById(R.id.listDrivernoti);

                        myList.removeAllViews();
                        Log.e("RAJESH","VIEWS REMOVED");

            */

                        String type = "cancelReq";
                        PushNotification backgroundWorkers = new PushNotification(this);
                        backgroundWorkers.execute(type, String.valueOf(listTest.get(count)), phonePassen, passMyPhone, "demo");
                        count = count + 1;
                    }


                    //NO FOR LOOP REQUIRED COZ ALREADY moveToNext bhitra xa
                    //int done = notiDb.deleteNotiData(String.valueOf(21));


                }








            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            Log.w("LogInActivity", "onResume");
            //=========set status
            final String foobar = getStatus();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(DriverPge.this, "Status found: " + foobar, Toast.LENGTH_SHORT).show();
                }
            });
            //=========================
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
            setUpMapIfNeeded();
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //
        //
        // thread.interrupt();
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

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            EditText searchBar = (EditText) findViewById(R.id.idSearchBara);
            String input = searchBar.getText().toString();
            List<Address> addressList = null;
            camUpdateSet = true;
            lat1Check = true;


            Thread thread3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    setUpMap();
                    Log.e("here hai", "hiiii");
                }
            });
            thread3.start();
            try {

                if (input != null || input != "") {
                    Geocoder geocoder = new Geocoder(this);
                    try {
                        addressList = geocoder.getFromLocationName(input, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    latitude2 = address.getLatitude();
                    longitude2 = address.getLongitude();

                    LatLng latlng = new LatLng(latitude2, longitude2);
                    Location location = new Location(String.valueOf(latlng));
                    //for route


                    Thread thread2 = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (true) {
                                if (lat1Check == true) {


                                    Log.e("aaaaaaaaaaa", "aaa");
                                    String url = null;
                                    try {
                                        url = getMapsApiDirectionsUrl();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    ReadTask downloadTask = new ReadTask();
                                    downloadTask.execute(url);
                                    lat1Check = false;
                                    Log.e("check falseBanayo", "j");

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (camUpdateSet.equals(true)) {
                                                Log.e("here", "bbbbbbbbbbbb");
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude2, longitude2), 8));
                                                camUpdateSet = false;


                                            } else {
                                                Log.e("boolean", String.valueOf(camUpdateSet));

                                            }

                                            if (marker1 == null) {
                                                Log.e("nullmarker1", "eeee");
                                                marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));

                                            } else {
                                                marker1.remove();
                                                Log.e("remove", "REMOVE");
                                                marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("LocUpdate" + String.valueOf(markerNum)));
                                                markerNum = markerNum + 1;
                                            }

                                        }
                                    });
                                    try {
                                        sleep(15000);
                                        Log.e("Sleep BHayo 15s", "");

                                    } catch (InterruptedException e) {
                                        Log.e("Sleep BHayena", "");
                                    }
                                } else {

                                    if (latitude1 == latChangeCheck && longitude2 == longChangeCheck) {
                                        //to be sure the lat values equal we divide to get 1
                                        Double d = (latitude1 / latChangeCheck);
                                        Log.e("valueOfD", String.valueOf(d));
                                        Log.e("lat1check ra lat eq", "ccccc");
                                        Toast.makeText(DriverPge.this, "Latitude Longitude Values matched. No update required.", Toast.LENGTH_SHORT);

                                    } else {
                                        Double d = (latitude1 / latChangeCheck);
                                        Log.e("valueOfD", String.valueOf(d));
                                        Log.e("lat1check ra lat not eq", "ddddd");
                                        //Toast.makeText(DriverPge.this,"farak BHAYEXA", Toast.LENGTH_SHORT);
                                        String url = null;
                                        try {
                                            url = getMapsApiDirectionsUrl();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        ReadTask downloadTask = new ReadTask();
                                        downloadTask.execute(url);
                                        //lat1Check = false;

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (camUpdateSet.equals(true)) {
                                                    Log.e("here", "true");
                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude2, longitude2), 8));
                                                    camUpdateSet = false;
                                                } else {
                                                    Log.e("boolean", String.valueOf(camUpdateSet));
                                                }
                                                if (marker1 == null) {
                                                    Log.e("nullmarker1", "eeee");
                                                    marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));

                                                } else {
                                                    marker1.remove();
                                                    Log.e("remove", "REMOVE");
                                                    marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("LocUpdate" + String.valueOf(markerNum)));
                                                    markerNum = markerNum + 1;
                                                }
                                            }
                                        });
                                        try {
                                            sleep(15000);
                                            Log.e("Sleep BHayo 15s", "");

                                        } catch (InterruptedException e) {
                                            Log.e("Sleep BHayena", "");
                                        }
                                    }
                                }

                            }


                        }
                    });


                    // String url = null;


                    thread2.start();
////////////////////////////////////////// yo bhanda ek dui step
                /* String url = null;
                url = getMapsApiDirectionsUrl();
                ReadTask downloadTask = new ReadTask();
                downloadTask.execute(url);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude2, longitude2), 8));
                //addMarkers();

                if (location.getAccuracy() <= 5) {
                    if (marker1 == null) {
                        marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));
                    } else {
                        marker1.remove();
                        marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title(String.valueOf(markerNum)));
                        markerNum=markerNum+1;
                    }


                    //mMap.animateCamera(CameraUpdateFactory.newLatLng(latlng));


                }
*/
                } else {
                    Toast.makeText(this, "Please enter a place name", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
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

    //route
    private String getMapsApiDirectionsUrl() throws InterruptedException {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            //String waypoints = "%20waypoints=optimize:true|";
            //include latlng or actual address if u want for the way points
               /* + LOWER_MANHATTAN.latitude + "," + LOWER_MANHATTAN.longitude
                + "|" + "|" + BROOKLYN_BRIDGE.latitude + ","
                + BROOKLYN_BRIDGE.longitude + "|" + WALL_STREET.latitude + ","
                + WALL_STREET.longitude; */

            String sensor = "sensor=false";


            String params = "origin=" + latitude1 + "," + longitude1 + "&destination=" + latitude2 + "," + longitude2;// +waypoints + "&" + sensor;
            //String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
            String output = "json";
            String url = "https://maps.googleapis.com/maps/api/directions/"
                    + output + "?" + params;
            Log.e("url", url);
            while (true) {


                return url;
                //wait(5000);

            }
        }else{
            Toast.makeText(this, "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }return null;

    }

    private void addMarkers() {
       /* if (googleMap != null) {
            googleMap.addMarker(new MarkerOptions().position(BROOKLYN_BRIDGE)
                    .title("First Point"));
            googleMap.addMarker(new MarkerOptions().position(LOWER_MANHATTAN)
                    .title("Second Point"));
            googleMap.addMarker(new MarkerOptions().position(WALL_STREET)
                    .title("Third Point"));
        } */
    }

    private class ReadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("data", "dataaaa "+ data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                //since GOOGLE MAPS API@ supports only 23 middle points we can keep 24 here

                jObject = new JSONObject(jsonData[0]);
                PathJSONParser parser = new PathJSONParser();
                Log.e("json", jsonData[0]);
                routes = parser.parse(jObject);





            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> points = null;
            //PolylineOptions polyLineOptions = null;

            PolylineOptions polylineOptions = new PolylineOptions();


            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {

                Log.e("routes.size() value is ", String.valueOf(routes.size()));

                points = new ArrayList<LatLng>();
                //polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);

                for (int j = 0; j < path.size(); j++) {

                    HashMap<String, String> point = path.get(j);


                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        Log.e("DriverPage","---------------------j=0-------"+distance);
                        // Toast.makeText(DriverPge.this, distance, Toast.LENGTH_SHORT).show();
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        Log.e("DriverPage","---------------------j=1-------"+distance);
                        //Toast.makeText(DriverPge.this, duration, Toast.LENGTH_SHORT).show();
                        continue;
                    }


                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                    polylineOptions.add(position);
                }
            }
            polylineOptions.width(5);
            polylineOptions.color(Color.RED);
            if(line==null){
                line = mMap.addPolyline(polylineOptions);
            }
            else{


                line.remove();//remove
                Log.e("here","here");
                line = mMap.addPolyline(polylineOptions);


            }


        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // moveTaskToBack(true); This line minimizes the app but this doesnt work in co-ordination with login check so
        //dont use moveTaskToBack() instrad just remove the functionality of the back button in this activity
        return;
    }

    public String getStatus(){

        Cursor cursor = userDb.getUserData();
        while (cursor.moveToNext()){
            statusString = cursor.getString(11);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    status.setText("Status: "+statusString);
                }
            });

        }
        return statusString;

    }


}








