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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Thread.sleep;

public class MapsActivity extends FragmentActivity {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    GPSTracker gps;
    double longitude1, latitude1,latitude2,longitude2,latChangeCheck,longChangeCheck,latitude3,longitude3;


    Thread thread;
    User_database userDb;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    //this publicLatLng is for universal use which has been initialized in the section below at location manager
    public LatLng publicLatLng;
    Marker marker = null;
    Marker marker1=null;
    //String input = "";
    int updateNumber=1;
    int markerNum=2;
    PolylineOptions polylineOptions;
    Polyline line = null;
    String distance="";
    Boolean inputSaveEdit = true;
    Boolean inputSaveEdit2 = true;

    String duration="";
    public Boolean camUpdateSet,lat1Check,isSessionOnRefer;
    List<Integer> list = new ArrayList<Integer>();
    List<Marker> markerArray = new ArrayList<Marker>();
    List<Marker> markerArray1 = new ArrayList<Marker>();
    RadioButton rbDriving=null;
    RadioButton rbBiCycling=null;
    RadioButton rbWalking=null;
    RadioGroup rgModes=null;
    TextView textView1,textView2;
    Button nextRoute;
    ArrayList<LatLng> markerPoints;
    int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_BICYCLING=1;
    final int MODE_WALKING=2;
    int buttonWidth;
    EditText searchBar;
    ImageButton imgBtn;
    String input;
    LinearLayout frameLayout,frameLayout1;
    String inputSave="$%^*!*@%$$";
    String inputSave2="&*^&@*$^&@(*";
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    public static int i=0;

    //Add line to map



    private void setUpMap() {

        userDb= new User_database(this);
        frameLayout=(LinearLayout)findViewById(R.id.idFrameLayout);
        frameLayout1=(LinearLayout)findViewById(R.id.idFrameLayout1);

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int isAccepted = GlobalValues.isAccepted;
                            if(isAccepted > 0){
                                Intent intent = new Intent(MapsActivity.this,Message.class);
                                startActivity(intent);
                            }


                            Cursor res = userDb.getUserData();
                            if (res.getCount() > 0) {
                                while (res.moveToNext()) {
                                    //take the 10th item from the SQLite db
                                    String user = res.getString(9);
                                    //ALL THE MAP LOCARION UPDATES COME FROM HERE
                                    try{LocationManager locationManager = (LocationManager)
                                            getSystemService(Context.LOCATION_SERVICE);
                                        Criteria criteria = new Criteria();


                                        Location location = locationManager.getLastKnownLocation(locationManager
                                                .getBestProvider(criteria, false));
                                        //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                                        //works fine with location as well now
                                        latitude1 = location.getLatitude();


                                        Log.e("CHECK",String.valueOf(latitude1));

                                        longitude1 = location.getLongitude();



                                        //========================================
                                       // Toast.makeText(MapsActivity.this,"UpdatedLatupda",Toast.LENGTH_LONG).show();

                                        GlobalValues.globalPassengerLat = String.valueOf(latitude1);
                                        GlobalValues.globalPassengerLon = String.valueOf(longitude1);

                                        //----------------------------
                                        //Toast.makeText(MapsActivity.this,"UpdatedLat",Toast.LENGTH_LONG).show();
                                        Log.e("GLOBAL",String.valueOf(GlobalValues.globalPassengerLat));
                                        Log.e("CRUCIAL1","loc manager bhaxa"+String.valueOf(latitude1));

                                       // Toast.makeText(MapsActivity.this,"LOCATION Manager On Work "+latitude1,Toast.LENGTH_SHORT).show();

                                        //insert in sqlite

                                        userDb.insertLatLon(String.valueOf(latitude1), String.valueOf(longitude1));

                                        //Toast.makeText(MapsActivity.this,"INSEERT HUNA PARYO "+latitude1,Toast.LENGTH_SHORT).show();


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

                                    }
                                    catch (Exception e){

                                        GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
                                        latitude1 = gpsTracker.getLatitude();
                                        longitude1 = gpsTracker.getLongitude();

                                        //========================================
                                        GlobalValues.globalPassengerLat = String.valueOf(latitude1);
                                        GlobalValues.globalPassengerLon = String.valueOf(longitude1);
                                        Log.e("GLOBAL",String.valueOf(GlobalValues.globalPassengerLat));

                                        //----------------------------
                                        //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                                        //Toast.makeText(MapsActivity.this, "Lat: " + latitude1 + "Lon: " + longitude1, Toast.LENGTH_SHORT).show();
                                        publicLatLng = new LatLng(latitude1, longitude1);
                                        Log.e("LOC update","GPSTRACKER bhaxa");
                                        Log.e("CHECK",String.valueOf(latitude1));

                                        userDb.insertLatLon(String.valueOf(latitude1), String.valueOf(longitude1));




                                    }

                                    if (latitude1 * 2 == 0){

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(MapsActivity.this,"Turn ON the GPS",Toast.LENGTH_SHORT).show();

                                                }
                                            });


                                    }
                                    else{
                                        Utils.updateLocationInDatabase(MapsActivity.this, user, latitude1 + "", longitude1 + "");
                                        Log.e("update bhaxa sqlite zz",String.valueOf(latitude1));

                                    }


                                    if (marker == null) {
                                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("foo position"));
                                        Log.e("ERROR_HO4?",String.valueOf(latitude1));

                                    } else {
                                        marker.remove();

                                        // lol google ko blue marker le wrong  dekhaxa mero marker le accurate dekhaxa lol
                                        marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("updateValue "+String.valueOf(updateNumber)));
                                        updateNumber = updateNumber+1;
                                        Log.e("ERROR_HO3?",String.valueOf(latitude1));

                                        Log.e("CRUCIAL7",String.valueOf(latitude1));
                                        Log.e("CRUCIAL7",String.valueOf(longitude1));
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //======================make the driver radio btns invisible to place driver and menu icon at the bottom=========

        frameLayout=(LinearLayout)findViewById(R.id.idFrameLayout);
       // frameLayout1=(LinearLayout)findViewById(R.id.idFrameLayout1);
        frameLayout.setVisibility(View.GONE);
       // frameLayout1.setVisibility(View.INVISIBLE);

        //===================================
        GlobalValues.isSessionComplete=0;

        userDb= new User_database(this);
        Cursor res = userDb.getUserData();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                //take the 10th item from the SQLite db
                String user = res.getString(9);
                Long  sessionWith = Long.valueOf(res.getString(18));
                Log.e("sessionWith", "-------------------" + sessionWith);
                if (sessionWith > 0) {
                    GlobalValues.isSessionOn = true;
                } else {
                    GlobalValues.isSessionOn = false;
                }
            }
        }

        if(SaveSharedPreference.getUserName(MapsActivity.this).length() == 0)
        {
            Intent intent = new Intent(MapsActivity.this,LogInActivity.class);
            startActivity(intent);
        }
        else
        {
            imgBtn=(ImageButton)findViewById(R.id.idDriverIcon);
 //Polyline line = null;
            setUpMapIfNeeded();
            //-for place name
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    //Check type of intent filter
                    if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                        //Registration success
                        String token = intent.getStringExtra("token");
                        //Toast.makeText(getApplicationContext(), "GCM token:" + token, Toast.LENGTH_LONG).show();
                    } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){
                        //Registration error
                        Toast.makeText(getApplicationContext(), "GCM registration error!!!", Toast.LENGTH_LONG).show();
                    } else {
                        //Tobe define
                    }
                }
            };

            //Check status of Google play service in device
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            if(ConnectionResult.SUCCESS != resultCode) {
                //Check type of error
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
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
            //THE FIRST MAP LOCATION IS FETCHED BY THE FOLLWING

            try{
                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                //works fine with location as well now
                latitude1 = location.getLatitude();
                //Toast.makeText(MapsActivity.this,"UpdatedLat",Toast.LENGTH_LONG).show();
                longitude1 = location.getLongitude();
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("location.position"));
                publicLatLng = new LatLng(latitude1, longitude1);

                mMap.setMyLocationEnabled(true);
               // Toast.makeText(this,"LOCATION Manager",Toast.LENGTH_LONG).show();
                //zoomlevel
                CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(latitude1, longitude1));
                CameraUpdate zoom =CameraUpdateFactory.zoomTo(16);
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

            }
            catch (Exception e){
                Log.e("1113","222");

                GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
                latitude1 = gpsTracker.getLatitude();
                longitude1 = gpsTracker.getLongitude();
                //marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("current position"));
                publicLatLng = new LatLng(latitude1, longitude1);
                //Toast.makeText(MapsActivity.this, "yei ta ho ni", Toast.LENGTH_SHORT);
                Log.e("GPSTRACKER","bhaxa");


                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).title("location.position"));

                Log.e("ERROR_HO2?",String.valueOf(latitude1));

                publicLatLng = new LatLng(latitude1, longitude1);
                //zoomlevel
                CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(latitude1, longitude1));
                CameraUpdate zoom =CameraUpdateFactory.zoomTo(16);
                mMap.moveCamera(center);
                mMap.animateCamera(zoom);


            }
        }


    }


    //
    public void getMyLocationAddress(View v){

        isSessionOnRefer = GlobalValues.isSessionOn;
        Cursor c = userDb.getUserData();
        String sessionVal = "garbageHalexa";
        while(c.moveToNext()){
             sessionVal = c.getString(22);
            Log.e("qqqq",String.valueOf(sessionVal));
        }
       // if (isSessionOnRefer == false){
        if (sessionVal.equals("0")){
            Toast.makeText(this,"SESSION IS OFF",Toast.LENGTH_SHORT).show();
            if(v.getId()==R.id.idDriverIcon){
                String lat= Double.toString(publicLatLng.latitude);
                String lon= Double.toString(publicLatLng.longitude);
                String type = "search";
                User_database userDb = new User_database(this);
                //for SQLite db we need cursor and its adapter
                Cursor res = userDb.getUserData();
                Log.e(String.valueOf(res),"cursor");
                if (res.getCount() > 0) {
                    while (res.moveToNext()) {
                        for (int i=0;i<10;i++){
                            Log.e("cursor",String.valueOf(res.getString(i)));
                        }

                        String userName = res.getString(9);
                        //-------------------
                        GlobalValues.globalPassenger = userName;
                        Log.e("CUR"+res.getString(9),"cursor");
                        //---------------------
                        // call a method that contains FindFriends object
                        //findTheDriversMethod();
                        //FindFriends class finds drivers
                        FindFriends backgroundWorkers = new FindFriends(MapsActivity.this);
                        //aafno data deko FindFriends lai
                        backgroundWorkers.execute(type, userName, String.valueOf(GlobalValues.globalPassengerLat),String.valueOf(GlobalValues.globalPassengerLon));
                    }
                }
            }
        }
        else if(sessionVal.equals("")){
            Toast.makeText(this,"SESSION IS OFF",Toast.LENGTH_SHORT).show();
            if(v.getId()==R.id.idDriverIcon){
                String lat= Double.toString(publicLatLng.latitude);
                String lon= Double.toString(publicLatLng.longitude);
                String type = "search";
                User_database userDb = new User_database(this);
                //for SQLite db we need cursor and its adapter
                Cursor res = userDb.getUserData();
                Log.e(String.valueOf(res),"cursor");
                if (res.getCount() > 0) {
                    while (res.moveToNext()) {
                        for (int i=0;i<10;i++){
                            Log.e("cursor",String.valueOf(res.getString(i)));
                        }

                        String userName = res.getString(9);
                        //-------------------
                        GlobalValues.globalPassenger = userName;
                        Log.e("CUR"+res.getString(9),"cursor");
                        //---------------------
                        // call a method that contains FindFriends object
                        //findTheDriversMethod();
                        //FindFriends class finds drivers
                        FindFriends backgroundWorkers = new FindFriends(MapsActivity.this);
                        //aafno data deko FindFriends lai
                        backgroundWorkers.execute(type, userName, String.valueOf(GlobalValues.globalPassengerLat),String.valueOf(GlobalValues.globalPassengerLon));
                    }
                }
            }
        }
        else{
            Toast.makeText(this,"SESSION IS ON",Toast.LENGTH_SHORT).show();
            //==========================SIDHAI CALL ACTIVITY MA
            Intent intent = new Intent(this,Message.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText( this, R.string.click_the_button, Toast.LENGTH_LONG).show();

        Log.e("ccccc","BBBBB");

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));
        setUpMapIfNeeded();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //thread.interrupt();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }



    public void onZoomIn(View v){

        if (v.getId() == R.id.idZoomInButton){
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }

    }
//public methods such that we can get the values at CallActivity.java by calling these funcs

    public  double getMyLatitude(){
        return latitude1;
    }
    public double getMyLongitude(){
        return longitude1;
    }
    public void myLocationZoom(View v){
        if(v.getId()==R.id.imageView){
            //Location location = null;
            //location.setLatitude(latitude1);
            //location.setLongitude(longitude1);
            LatLng latlng1 = new LatLng(latitude1,longitude1);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latlng1)
                    .zoom(16)
                    .bearing(0)
                    .tilt(0)
                    .build();



            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition ),
                    5000, null);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //frameLayout.setVisibility(View.INVISIBLE);

                }
            });

           /* CameraUpdate center= CameraUpdateFactory.newLatLng(new LatLng(latitude1, longitude1));
            CameraUpdate zoom =CameraUpdateFactory.zoomTo(16);
            mMap.moveCamera(center);
            mMap.animateCamera(zoom);
            */
        }
    }

    public void onZoomOut(View v){
        if (v.getId() == R.id.idZoomOutButton) {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());

        }
    }
    public void changeType(View v){
        if(mMap.getMapType()== GoogleMap.MAP_TYPE_NORMAL) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        else{
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        }
    }
    public void onSearcha(View v) {

        //animation haleko
        Button button = (Button)findViewById(R.id.idSearchButton);

        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        v.startAnimation(animRotate);
        button.setAnimation(animRotate);
        //yo chahi fading out animation ho hai
        // Animation animation = new AlphaAnimation(1.0f,0.0f);
        // animation.setDuration(1000);
        // button.setAnimation(animation);

        //xxxxxxxxxxxxxxxxxx
        // input = searchBar.getText().toString();


        searchBar = (EditText) findViewById(R.id.idSearchBar);

        /*searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.clearComposingText();
            }
        }); */



        new Thread(new Runnable() {
            @Override
            public void run() {
                input = searchBar.getText().toString();
                List<Address> addressList = null;
                //first run ma camupdateset ra latcheck true
                //latcheck chai lat value same xa ki xaena update garnu bhanda paile check garna

                camUpdateSet = true;
                lat1Check = true;
                //to display the modes after the button is clicked,access the sub Layout
                //eh aama yo talako duita code le 1 hour khayo access garna namilera paila layout
                // This code will always run on the UI thread, therefore is safe to modify UI elements.
                //hamro UI ko dist ra time ma search  garda suru ma 3 patak update bhako dekhauxa its bcoz of Lat1check log herr



                //half width
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int width = displaymetrics.widthPixels;
                buttonWidth = width/2;





                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //textvieews of distance duration
                        textView1 = (TextView)findViewById(R.id.id_distance);
                        textView2 = (TextView)findViewById(R.id.id_duration);
                        textView1.setWidth(buttonWidth);
                        textView2.setWidth(buttonWidth);
                        /*textView1.setText("");
                        textView2.setText("");*/
                    }
                });








                // Getting reference to rb_driving
                rbDriving = (RadioButton) findViewById(R.id.rb_driving);

                // Getting reference to rb_bicylcing
                //rbBiCycling = (RadioButton) findViewById(R.id.rb_cycling);

                // Getting reference to rb_walking
                rbWalking = (RadioButton) findViewById(R.id.rb_walking);

                // Getting Reference to rg_modes
                rgModes = (RadioGroup) findViewById(R.id.rg_modes);

                rgModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        // Checks, whether start and end locations are captured

                        //LatLng origin = markerPoints.get(0);
                        //LatLng dest = markerPoints.get(1);

                        // Getting URL to the Google Directions API
                        String url = null;

                        url = getMapsApiDirectionsUrl();

                        ReadTask downloadTask = new ReadTask();
                        downloadTask.execute(url);

                /*
                String url = getMapsApiDirectionsUrl();

                    //DownloadTask downloadTask = new DownloadTask();
                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url);
                    */

                    }
                });





       /* final Button button = (Button) findViewById(R.id.idNextRoute);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
            */



                Thread thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setUpMap();
                    }
                });
                thread3.start();
                try {
                    //
                    // if (searchBar.getText().toString().equals("")||searchBar.getText().toString().equals(null) )
                    if ((searchBar.getText().length())== 0)
                    {



                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //input nabhayesi aafnai lat lng dekhaidine

                                longitude2 = longitude1;
                                latitude2 = latitude1;
                                //ANimate camera


                                LatLng latlng2 = new LatLng(latitude2, longitude2);
                                CameraPosition cameraPosition2 = new CameraPosition.Builder()
                                        .target(latlng2)
                                        .zoom(16)
                                        .bearing(0)
                                        .tilt(0)
                                        .build();


                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),
                                        5000, null);


                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MapsActivity.this);
                                myAlert.setMessage("Insert a valid input11111!\nCurrent Location directed!")
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("myRide")
                                        .setIcon(R.drawable.car)
                                        .create();
                                myAlert.show();


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                        //tala ko duration haru visible banayeko

                                        if(frameLayout1.getVisibility()==View.VISIBLE){
                                            frameLayout.setVisibility(View.GONE);
                                            //frameLayout1.setVisibility(View.GONE);
                                        }



                                    }
                                });


                            }
                        });


                    }

                    else

                    {


                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        try {
                            addressList = geocoder.getFromLocationName(input, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Address address ;

                        //input ta diyo ta google le result farkayena bhane

                        if(addressList.isEmpty()==true){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(MapsActivity.this);
                                    Log.e("Google le thau chinena","HERE");
                                    myAlert.setMessage("Location was not specified properly!\nPrevious destination directed instead!")
                                            .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                                @Override
                                                public void onClick(DialogInterface dialog, int which){
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setTitle("myRide")
                                            .setIcon(R.drawable.myridec1)
                                            .create();
                                    myAlert.show();

                                    //ANimate camera

                                    LatLng latlng2 = new LatLng(latitude1, longitude1);
                                    CameraPosition cameraPosition2 = new CameraPosition.Builder()
                                            .target(latlng2)
                                            .zoom(16)
                                            .bearing(0)
                                            .tilt(0)
                                            .build();


                                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),
                                            5000, null);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            // This code will always run on the UI thread, therefore is safe to modify UI elements.
                                            //tala ko duration haru visible banayeko



                                           // MATHI NAI DEFINE GAREXA SO AILE COMMENT  HANDIYE

                                            frameLayout=(LinearLayout)findViewById(R.id.idFrameLayout);
                                            frameLayout1=(LinearLayout)findViewById(R.id.idFrameLayout1);
                                            frameLayout.setVisibility(View.VISIBLE);
                                            frameLayout1.setVisibility(View.VISIBLE);


                                        }
                                    });
                                }
                            });
                        }

                        //result farkayo bhane
                        else{

                            address= addressList.get(0);
                            latitude2 = address.getLatitude();
                            longitude2 = address.getLongitude();

                            //naya variable ma assign gareko
                            latitude3 = latitude2;
                            longitude3 = longitude2;

                            LatLng latlng = new LatLng(latitude2, longitude2);
                            Location location = new Location(String.valueOf(latlng));
                            //for route


                            Thread thread2 = new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    while (true) {
                                        if (lat1Check == true) {

                                            //suru ma ta jasai map ma route banauna paryo ni so lat1check tru gareko
                                            //aba else ma chai latitude value change nabhaye kei update nagarne route
Log.e("1st lat1check","yeha setText vary");

                                            String url = null;

                                            url = getMapsApiDirectionsUrl();

                                            ReadTask downloadTask = new ReadTask();
                                            downloadTask.execute(url);
                                            lat1Check = false;

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (camUpdateSet.equals(true)) {
                                                        Log.e("CAMUPDATESET tru","here");

                                                        //ANimate camera

                                                        LatLng latlng2 = new LatLng(latitude2, longitude2);
                                                        CameraPosition cameraPosition2 = new CameraPosition.Builder()
                                                                .target(latlng2)
                                                                .zoom(16)
                                                                .bearing(0)
                                                                .tilt(0)
                                                                .build();


                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),
                                                                5000, null);
                                                        camUpdateSet = false;



                                                        frameLayout.setVisibility(View.VISIBLE);
                                                        frameLayout1.setVisibility(View.VISIBLE);

                                                        //flush haan aba lat 2 lon 2 ko value so that gibberish input ma ni past value ma camera animate nahos
                                                        //yo bhanda aghi ko thik xa matra flush hunxa searchbar

                                                        //searchBar.setText("");

                                                        //here
                                                        // input=null;
                                                    /*latitude2=0.0;
                                                    longitude2=0.0;
                                                     */
                                                        //mMap.clear();

                                                        if (marker1 == null) {
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));

                                                        } else {
                                                            marker1.remove();
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("LocUpdate" + String.valueOf(markerNum)));
                                                            markerNum = markerNum + 1;
                                                        }


                                                    }
                                                    //camUpdateSet false hudaa
                                                    else {

                                                        Log.e("CAMUPDATESET false","here");

                                                        if (marker1 == null) {
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));

                                                        } else {
                                                            marker1.remove();
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("LocUpdate" + String.valueOf(markerNum)));
                                                            markerNum = markerNum + 1;
                                                        }

                                                    }



                                                }
                                            });
                                            try {
                                                sleep(15000);

                                            } catch (InterruptedException e) {
                                            }
                                        } else {


                                            Log.e("LATCHECK ma po","test");
                                            //route update garna paryo ni ta aafno latitude long change bhaye
                                            //latitude aafno change nabhaye do nothing

                                            if (latitude1 == latChangeCheck && longitude1 == longChangeCheck) {
                                                //to be sure the lat values equal we divide to get 1
                                                //Double d = (latitude1 / latChangeCheck);


                                            } else {
                                                //Double d = (latitude1 / latChangeCheck);
                                                Log.e("2st lat1check","yeha setText vary");

                                                String url = null;
                                                url = getMapsApiDirectionsUrl();
                                                ReadTask downloadTask = new ReadTask();
                                                downloadTask.execute(url);
                                                /*
                                                //hya pani define garna paryo talako duita line
                                                //natra view heirarchy ,mandaina
                                                frameLayout=(LinearLayout)findViewById(R.id.idFrameLayout);
                                                frameLayout1=(LinearLayout)findViewById(R.id.idFrameLayout1);

                                                frameLayout.setVisibility(View.VISIBLE);
                                                frameLayout1.setVisibility(View.VISIBLE);
                                                */



                                                //lat1Check = false;

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (camUpdateSet.equals(true)) {
                                                            Log.e("test","zxcvb");
                                                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude2, longitude2), 8));
                                                            camUpdateSet = false;
                                                            Log.e("ERROR_HO6?",String.valueOf(latitude1));

                                                        } else {
                                                        }
                                                        if (marker1 == null) {
                                                            //first marker for destination
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("foo2 position"));
                                                            Log.e("ERROR_HO5?",String.valueOf(latitude1));

                                                        } else {
                                                            //2nd and other markers for destination
                                                            Log.e("ERROR_HO5?",String.valueOf(latitude1));

                                                            marker1.remove();
                                                            marker1 = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude2, longitude2)).title("LocUpdate" + String.valueOf(markerNum)));
                                                            markerNum = markerNum + 1;
                                                        }
                                                    }
                                                });
                                                try {
                                                    sleep(15000);

                                                } catch (InterruptedException e) {
                                                }
                                            }
                                        }

                                    }


                                }
                            });


                            // String url = null;


                            thread2.start();


                        }

                    }






                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();



    }

    public void streetSearchBar(Location location) {
        if (!location.hasAccuracy()) {
            return;
        }
        if (location.getAccuracy() > 5) {
            return;
        }
        // do something with location accurate to 5 meters here.
    }


    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
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

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */

//





    //route
    private String getMapsApiDirectionsUrl() {
        // Travelling Mode
        String mode = "mode=driving";

        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView1.setText(" Updating distance");
                    textView2.setText(" Updating time");
                }
            });


        }else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView1.setText(" Updating distance");
                    textView2.setText(" Updating time");
                }
            });
        }
        String waypoints = "%20waypoints=optimize:true|";

        String sensor = "sensor=false";
        String params = "origin="+latitude1+","+longitude1+"&destination="+latitude2+","+longitude2+/*waypoints + */"&" + sensor+"&"+mode;
        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.e("url", url);


        return url;
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
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            new ParserTask().execute(result);
        }
    }

    private class ParserTask
            extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject;
            //List<List<HashMap<String, String>>> routes = null;
            //paxi
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {
                //json response from google
                jObject = new JSONObject(jsonData[0]);
                Log.e("SHIFT",String.valueOf(jsonData[0]));

                PathJSONParser parser = new PathJSONParser();
                Log.e("json1", jsonData[0]);
                routes = parser.parse(jObject);
//
                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes");
                int routesLength = ja.length();
                Log.e(String.valueOf(routesLength),"SEE ROUTE");



                //if google responds with no route length
                //ie route length is 0 if no route if route exists it is 1
                if(routesLength==0){





                    if(input != inputSave && inputSaveEdit==true ){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MapsActivity.this);
                                myAlert.setMessage("Sorry 1!\nNo route exists for this place!\nOnly the location is shown.")
                                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("myRide")
                                        .setIcon(R.drawable.myridec1)
                                        .create();
                                myAlert.show();


                                if(frameLayout.getVisibility()==View.VISIBLE){
                                    frameLayout.setVisibility(View.GONE);
                                    frameLayout1.setVisibility(View.VISIBLE);
                                }


                                inputSave =input;
                                Log.e("CHANGED INPUTSAVE", "to+ " + input);
                                inputSaveEdit = false;
                                Log.e("1111","2222");

                            }
                        });
                    }
                    else if (input== inputSave && inputSaveEdit==false){
                        //do nothing coz alert dialog haldiyo bhane ta hamro while true loop chaliraako xa ani infinite loop auxa pheri
                        //tei bhayera ta boolean ko concept thapeko yeha
                        //automatic update bhairnxa log ma herr 333
                        Log.e("NO SORRY","input==inputSave");
                        Log.e("1111", "333");
                    }

                    else if ((inputSave!= input) && inputSaveEdit==false){




                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder myAlert = new AlertDialog.Builder(MapsActivity.this);
                                myAlert.setMessage("Sorry 2!\nNo route exists for this place!\nThe location is shown. ")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("myRide")
                                        .setIcon(R.drawable.myridec1)
                                        .create();
                                myAlert.show();
                                if(frameLayout.getVisibility()==View.VISIBLE){
                                    frameLayout.setVisibility(View.GONE);

                                }
                                inputSave = input;
                                Log.e("INPUTSAVE VALUE", inputSave);
                                Log.e("1111", "4444");
                            }
                        });


                        //inputSaveEdit = false;

                    }


                }                          //yeha
                else {





                    if(input != inputSave2 && inputSaveEdit2==true ){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                                inputSave2 =input;
                                Log.e("CHANGED INPUTSAVE2", "to+ " + input);
                                inputSaveEdit2 = false;
                                Log.e("1111","2222");

                            }
                        });
                    }
                    else if (input== inputSave2 && inputSaveEdit2==false){
                        //do nothing coz alert dialog haldiyo bhane ta hamro while true loop chaliraako xa ani infinite loop auxa pheri
                        //tei bhayera ta boolean ko concept thapeko yeha
                        //automatic update bhairnxa log ma herr 333
                        Log.e("NO SORRY","input2==inputSave2");
                        Log.e("1111", "333");
                    }

                    else if ((inputSave2!= input) && inputSaveEdit2==false){





                        //inputSaveEdit = false;

                    }

                }























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
                points = new ArrayList<LatLng>();
                //polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");

                        textView1.setText(" " + "Distance = " + distance + " ");



                        /*
                        Toast toast1 = new Toast(MapsActivity.this);
                        toast1.makeText(MapsActivity.this, distance, Toast.LENGTH_SHORT).show();
                        */


                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        /*
                        Toast toast2 = new Toast(MapsActivity.this);
                        toast2.makeText(MapsActivity.this, duration, Toast.LENGTH_SHORT).show();
                        toast2.cancel();
                        */
                        textView2.setText(" "+"Time = "+duration);
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
            // Changing the color polyline according to the mode
            if(mMode==MODE_DRIVING)
                polylineOptions.color(Color.RED);
            else if(mMode==MODE_BICYCLING)
                polylineOptions.color(Color.GREEN);
            else if(mMode==MODE_WALKING)
                polylineOptions.color(Color.BLUE);
            // polylineOptions.color(Color.RED);
            if(line==null){
                line = mMap.addPolyline(polylineOptions);
            }
            else{


                line.remove();//remove
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



    //========================================DRAWER ITEMS===========================================
    private void addDrawerItems() {
        String[] osArray = { "Android", "iOS", "Windows", "OS X", "Linux" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
    }
//======================================================================================



    public void updateOnSessionRefer(Boolean b){
        b = GlobalValues.isSessionOn;

    }

}














//
//aba route update garne with a loop and i guess route update hunxa on the map because our location is automatically getting updates even in onSearcha()









