package com.example.shadowstorm.metrocabv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.shadowstorm.metrocabv2.GCMRegistrationIntentService.TAG;
import static com.google.android.gms.internal.zzip.runOnUiThread;
import static java.lang.Thread.sleep;

/**
 * Created by Shadow Storm on 7/6/2016.
 */

public class MapFragmentNew extends Fragment {

    private GoogleMap  _map;
    String lat1,lat2,lon1,lon2,passengerName,driverFName,driverLName,distance,duration;
    Polyline line,line1 = null;
    private Context globalContext = null;
    //ShowDriverOnMap s = new ShowDriverOnMap();
    Marker myLocMarker,driverLocMarker;
    int x;
    Boolean y =true,z=true;
    Runnable run1;
    Thread threadLoop = new Thread(run1);//static nabhaye loop ma create bhairahanxa ra out of memory hunxa
    String type,urlz;
    String id2;
    //Noti_db notiDb = new Noti_db(getActivity());
    User_database userDb;
    Noti_db notiDb;

    String phone, passPhone,driverPhone;
    String passengerIdByPhone;
    Button b1,b2;
    String durationN,distanceN,durationNinSecs,distanceNinMeter,referenceDistance;




    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.e("show", "11111");

        globalContext = this.getActivity();
        View v = inflater.inflate(R.layout.map_fragment_new, container, false);
        _map = getMapFragment().getMap();
        b1 =(Button)v.findViewById(R.id.idReportButton1);//SEE VERY IMP
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportByDriver();
            }
        });
        //================call client
        b2 =(Button)v.findViewById(R.id.button5);//SEE VERY IMP
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callClient();
            }
        });
        //=============================



        //route ko kaam yeha ONLY AFTER getting the fragment natra crash hunxa




        userDb = new User_database(getActivity());
        notiDb = new Noti_db(getActivity());

        //LOOP LAGEKO XAENA HOLA  ROUTE UPDATE HUNA

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (0==0) {
                    Log.e("DHADDUMAN2","zzz");



                  try{

                        Log.e("baini1","zzz");

                        LocationManager locationManager = (LocationManager)
                                getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Log.e("baini2","zzz");
                        Criteria criteria = new Criteria();
                        Log.e("baini3","zzz");
                        Location location = locationManager.getLastKnownLocation(locationManager
                                .getBestProvider(criteria, false));
                        Log.e("baini4","zzz");
                        //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
                        //works fine with location as well now
                        Log.e("baini5",String.valueOf(location.getLatitude()));
                        GlobalValues.globalDriverLat = String.valueOf(location.getLatitude());
                        Log.e("YDRI","YYY");
                        Log.e("YDRIVERLAT",String.valueOf(location.getLatitude()));
                        GlobalValues.globalDriverLon = String.valueOf(location.getLongitude());

                        Log.e("XA-RA",GlobalValues.globalDriverLat);
                        //insert in sqlite
                    }
                    catch (Exception e){
                        GPSTracker gpsTracker = new GPSTracker(getActivity());
                        GlobalValues.globalDriverLat = String.valueOf(gpsTracker.getLatitude());
                        Log.e("CATCHMA",String.valueOf(gpsTracker.getLatitude()));
                        GlobalValues.globalDriverLon = String.valueOf(gpsTracker.getLongitude());
                    }


                      //===================================================



                    //===================================================
                    Log.e("DHADDUMAN","zzz");

                    //while(GlobalValues.driversidePassenLat == null);

                    GlobalValues.driverSidePassenLat=GlobalValues.globalPassengerLat;


                    GlobalValues.driverSidePassenLon=GlobalValues.globalPassengerLon;
                    // Log.e("MMMMMMAPFRAGNEW1",GlobalValues.driversidePassenLat);

                    // Log.e("MMMMMMAPFRAGNEW2",GlobalValues.driversidePassenLat);

                    //=======================UPDATE PASSEN LAT ON RUN TIME

                    Log.e("DHADDUMAN6","zzz");

                    Log.e("DHADDUMAN8","zzz");

                    PushNotification pn = new PushNotification(getActivity());
                    Cursor c = notiDb.getNotiData();
                    while (c.moveToNext()){
                        GlobalValues.globalAcceptedPassengerNumber = c.getString(4);
                    }
                    Log.e("BIKASHDAKA",GlobalValues.globalAcceptedPassengerNumber);
                    Log.e("POS2",String.valueOf("qqq"));

                    Log.e("BIKASHDAKA123",GlobalValues.driverSideDriverPhone);
                    pn.execute("driverSideAcceptedPassengerLat",   GlobalValues.globalAcceptedPassengerNumber,GlobalValues.driverSideDriverPhone);
                    //=====================================================


                    while(GlobalValues.driverSidePassenLat==null);

                    Log.e("KUN","2");

                    lat1=GlobalValues.driverSidePassenLat;

                    lon1=GlobalValues.driverSidePassenLon;

                    lat2=GlobalValues.globalDriverLat;
                    lon2=GlobalValues.globalDriverLon;


                    Log.e("got?",String.valueOf(lat1));
                    Log.e("got?",String.valueOf(lat2));

//==========this block calculates the route between passen and driver in real time
                    String url = null;

                    Log.e("DHADDUMAN4","zzz");

                    url = getMapsApiDirectionsUrl();
                    Log.e("DHADDUMAN5","zzz");


                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url);
                    Log.e("XXXXX","1");
///===============================================================



                    Log.e("XXXXX","2");

                    type="passengerLatUpdate";
                    id2="driverLatLng";
                    phone="12344567";
                    passengerIdByPhone =GlobalValues.driverSidePassengerPhoneForId;

                    Log.e("DAMN",String.valueOf(GlobalValues.globalDriverLat));

                    PushNotification backgroundWorkers = new PushNotification(globalContext);

                    //backgroundWorkers.execute(type,id2, phone, passengerIdByPhone,"");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(globalContext,"LOOP IN ",Toast.LENGTH_SHORT).show();

                        }
                    });

                    //============================================================

                    // threadLoop.resume();


                    lat2 = GlobalValues.globalDriverLat;
                    lon2 = GlobalValues.globalDriverLon;


                    //===================================================

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ANimate camera
                            if(y == true){



                                LatLng latlng2 = new LatLng(Double.valueOf(lat2), Double.valueOf(lon2));
                                Log.e("MMMMM",String.valueOf(lat2));

                                Log.e("MMMMM",String.valueOf(lon2));
                                CameraPosition cameraPosition2 = new CameraPosition.Builder()
                                        .target(latlng2)
                                        .zoom(19)
                                        .bearing(0)
                                        .tilt(0)
                                        .build();


                                _map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition2),
                                        5000, null);
                                y=false;
                            }

                        }
                    });

                    try{
                        Log.e("MMMMMPASSEN",lat1);
                        Log.e("MMMMMpassen",lon1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (driverLocMarker == null) {
                                    try{
                                        driverLocMarker = _map.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(lat2), Double.valueOf(lon2)))
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.car, "aaaa"))));
                                        Log.e("value","ho 444");
                                    }catch (NullPointerException n){
                                        n.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(globalContext,"BUNDLE AAYENA2",Toast.LENGTH_SHORT);

                                            }
                                        });
                                    }



                                } else {
                                    driverLocMarker.remove();
                                    try{
                                        driverLocMarker = _map.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(lat2), Double.valueOf(lon2)))
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.car, "bbbb"))));
                                        Log.e("value","ho 333");
                                    }catch (NullPointerException n){
                                        n.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(globalContext,"BUNDLE AAYENA3",Toast.LENGTH_SHORT);

                                            }
                                        });
                                    }

                                }

                                if (myLocMarker == null) {
                                    try{

                                        myLocMarker = _map.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(lat1), Double.valueOf(lon1)))
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.waitt, ""))));

                                    }
                                    catch (NullPointerException n){
                                        n.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(globalContext,"BUNDLE AAYENA 4",Toast.LENGTH_SHORT);

                                            }
                                        });
                                    }

                                } else {
                                    myLocMarker.remove();
                                    try{
                                        myLocMarker = _map.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(lat1), Double.valueOf(lon1)))
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.waitt, ""))));

                                    } catch (NullPointerException n){
                                        n.printStackTrace();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(globalContext,"BUNDLE AAYENA5",Toast.LENGTH_SHORT);

                                            }
                                        });
                                    }
                                }
                            }
                        });


                    }catch (NullPointerException n){
                        n.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(globalContext,"BUNDLE AAYENA6",Toast.LENGTH_SHORT);

                            }
                        });
                    }






                    //yo muni ko nafaal
                    //Marker marker1 = _map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(lat1), Double.valueOf(lon1))).title("Update "));
                    // Marker marker2 = _map.addMarker(new MarkerOptions().position(new LatLng(Double.valueOf(lat2), Double.valueOf(lon2))).title("Update "));

                    Log.e("value","next loop");
                    try {
                        sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }            }
        });
        thread.start();

        return v;
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        //to prevent from crashing coz fragment not attached to activity
        Bitmap bm2 = null;
        if(isAdded()){
            Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                    .copy(Bitmap.Config.ARGB_8888, true);
            Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            paint.setTypeface(tf);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(convertToPixels(globalContext, 11));

            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);

            Canvas canvas = new Canvas(bm);

            //If the text is bigger than the canvas , reduce the font size
            if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
                paint.setTextSize(convertToPixels(globalContext, 7));        //Scaling needs to be used for different dpi's

            //Calculate the positions
            int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

            //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
            int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

            canvas.drawText(text, xPos, yPos, paint);
            bm2=bm;

        }
        return  bm2;


    }



    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    private String getMapsApiDirectionsUrl() {
        // Travelling Mode
        String mode = "mode=driving";

        Log.e("BHAYO ?",String.valueOf(lat1));
        mode = "mode=driving";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });



        String waypoints = "%20waypoints=optimize:true|";

        String sensor = "sensor=false";
        String params = "origin="+lat1+","+lon1+"&destination="+lat2+","+lon2+/*waypoints + */"&" + sensor+"&"+mode;
        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.e("url", url);


        return url;
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
            Log.e("data",String.valueOf(data));
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("onPo",String.valueOf(result));
            new ParserTask().execute(result);
        }
    }


    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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

                PathJSONParser parser = new PathJSONParser();
                Log.e("Parser", jsonData[0]);
                routes = parser.parse(jObject);
//
                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes");
                int routesLength = ja.length();
                Log.e("SEE",String.valueOf(routesLength));



                //if google responds with no route length
                //ie route length is 0 if no route if route exists it is 1
                if(routesLength==0) {

                    boolean x = false;
                    if (x == false){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                              /*  AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                                myAlert.setMessage("Sorry1!\nNo route exists for this place!\nOnly the location is shown.")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setTitle("myRide")
                                        .setIcon(R.drawable.myridec)
                                        .create();
                                myAlert.show();
                                Log.e("route","aayena");
                                */


                            }
                        });
                        x = true;
                    }





                }
                else {


                    Log.e("route","aayo");


                    //do nothing here map ta tala post execute ma xa ni



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

            Log.e("route","aayo hola");

            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                points = new ArrayList<LatLng>();
                //polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");

                        //textView1.setText(" " + "Distance = " + distance + " ");



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
                        //textView2.setText(" "+"Time = "+duration);
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
            if (z==true){
                polylineOptions.color(Color.BLUE);
                z=false;
            }
            else if (z==false){
                polylineOptions.color(Color.BLACK);
                z=true;
            }
            else{
                polylineOptions.color(Color.GREEN);
            }


            if(line==null){


                line = _map.addPolyline(polylineOptions);
                x=0;
                Log.e("x value ",String.valueOf(x));

            }
            else{
                Log.e("value","ho next wala");
                Log.e("x value ",String.valueOf(x));
                line.remove();//remove
                line = _map.addPolyline(polylineOptions);

                x=x+1;


            }


        }
    }





    private MapFragment getMapFragment() {
        FragmentManager fm = null;
        Log.e("ASSESSMENT","ASS");

        Log.d(TAG, "sdk: " + Build.VERSION.SDK_INT);
        Log.d(TAG, "release: " + Build.VERSION.RELEASE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Log.d(TAG, "using getFragmentManager");
            fm = getFragmentManager();
        } else {
            Log.d(TAG, "using getChildFragmentManager");
            fm = getChildFragmentManager();
        }

        return (MapFragment) fm.findFragmentById(R.id.mapId);
    }


    public void onDestroy() {

        super.onDestroy();
        myLocMarker=null;
        driverLocMarker=null;

    }

    public void reportByDriver(){

        Thread thre = new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor c = userDb.getUserData();
                while (c.moveToNext()){
                    Log.e("GAURIKAUSEID",String.valueOf(c.getString(9)));
                    GlobalValues.globalDriverUseId = c.getString(9);

                }
                        Log.e("GAURIKA","111");
                RetrieveStartValues bgWorker = new RetrieveStartValues(getActivity());
                Log.e("GAURIKAUSEID",String.valueOf(GlobalValues.globalDriverUseId));

                bgWorker.execute("DriverReport",GlobalValues.globalDriverUseId);
                Log.e("GAURIKA","444");
                //====================================NOW TO CALCULATE 1st lat DISTANCE BETWEEN

                //======================wait for a while
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final AlertDialog alertDialog1 = new AlertDialog.Builder(globalContext).create();


                        alertDialog1.setTitle("Processing your Report");
                        alertDialog1.setMessage("Please wait while the report is being processed!");
                        alertDialog1.show();   //

                        new CountDownTimer(3000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                alertDialog1.setMessage("Please wait while the report is being processed!");
                            }

                            @Override
                            public void onFinish() {
                                alertDialog1.dismiss();
                            }
                        }.start();

                    }
                });

                while(GlobalValues.retrievedPassenLatF == null);

                String url = null;
                url = getMapsApiDirectionsUrlNext();
                ReadTaskNext downloadTask = new ReadTaskNext();
                downloadTask.execute(url);







                //===============================check distances now


//arkai thread ho so sleep gardiye ni hunxa


                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//=================CHECK THE ROAD DISTANCE BETWEEN STARTING LAT OF CLIENT AND THE DRIVER

                try{
                    if(Integer.parseInt(distanceNinMeter)<100){
                        //===passenger ra driver ko current dist nikaal

                        //====================
                        LatLng l1 = new LatLng(Double.valueOf(GlobalValues.driverSidePassenLat),Double.valueOf(GlobalValues.driverSidePassenLon));
                        LatLng l2 = new LatLng(Double.valueOf(GlobalValues.globalDriverLat),Double.valueOf(GlobalValues.globalDriverLon));


                        Float dist = Float.valueOf(getDistance1(l1,l2));















                  /*

                    Log.e("BADD",String.valueOf(GlobalValues.globalPassengerLat));
                    Log.e("BADD",String.valueOf(GlobalValues.globalDriverLat));

                    float diff1=Float.parseFloat(GlobalValues.driverSidePassenLat)-Float.parseFloat(GlobalValues.globalDriverLat);
                    Log.e("BADdiff0",String.valueOf(diff1));
                    float diff1Sqr = diff1 * diff1;
                    Log.e("BADdiff1",String.valueOf(diff1Sqr));
                    float diff2 = Float.parseFloat(GlobalValues.driverSidePassenLon)-Float.parseFloat(GlobalValues.globalDriverLon);
                   Log.e("BADdiff2",String.valueOf(diff2));
                    float diff2Sqr = diff2 * diff2;
                    Log.e("BADdiff2Sqr",String.valueOf(diff1Sqr));
                    float sumOfDiff = diff1Sqr+diff2Sqr;
                    Log.e("BADsunOf",String.valueOf(sumOfDiff));
                    float actualDifference = (float) Math.sqrt(sumOfDiff);
                    Log.e("referenceDist",String.valueOf(actualDifference));
                    */
                        //==================IF THE DISTANCE IS LESS THAN 100meters,there can be 2 possible ways  to go
                        //=======either passenger is at the site or is not
                        if(dist<100){
                            //if passenger is at site the the reference distace will br less than 100
                            //so driver can wait for passen

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Toast.makeText(getActivity(),"You are at the location.Please wait for the passenger",Toast.LENGTH_SHORT).show();



                                    final AlertDialog alertDialog1 = new AlertDialog.Builder(globalContext).create();


                                    alertDialog1.setTitle("Report Response");
                                    alertDialog1.setMessage("Client is around.Please wait for the client with patience!");
                                    alertDialog1.show();   //

                                    new CountDownTimer(3000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            alertDialog1.setMessage("You are "+distanceNinMeter+" meters away from the client.Please wait for the passenger!");
                                        }

                                        @Override
                                        public void onFinish() {
                                            alertDialog1.dismiss();
                                        }
                                    }.start();

                                }
                            });

                        }
                        else{
                            //=========if passsenger has moved from the starting location,distance will be >100
                            //====deduct balance from the client account and transfer to the driver


                            Cursor c1 = notiDb.getNotiData();
                            while(c1.moveToNext()){
                                passPhone =c1.getString(4);
                            }
                            Cursor c2 = userDb.getUserData();
                            while(c2.moveToNext()){
                                driverPhone =c2.getString(5);
                            }
                            PushNotification backgroundWorkersThis = new PushNotification(getActivity());
                            backgroundWorkersThis.execute("driverIsReporting",passPhone,driverPhone,"driver");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    final AlertDialog alertDialog1 = new AlertDialog.Builder(globalContext).create();


                                    alertDialog1.setTitle("Report Response");
                                    alertDialog1.setMessage("Balance deducted from the passenger account and transferred to you!");
                                    alertDialog1.show();   //

                                    new CountDownTimer(3000, 1000) {
                                        @Override
                                        public void onTick(long millisUntilFinished) {
                                            alertDialog1.setMessage("Balance deducted from the passenger account and transferred to you!");
                                        }

                                        @Override
                                        public void onFinish() {
                                            alertDialog1.dismiss();
                                        }
                                    }.start();



                                }
                            });
                        }





                    }
                    //====if the distance betn driver and starting passen latlng is >100,display that the driver hasnt reached the location
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(getActivity(),"You haven't reached the destination yet!",Toast.LENGTH_SHORT).show();
                                final AlertDialog alertDialog1 = new AlertDialog.Builder(globalContext).create();


                                alertDialog1.setTitle("Report Response");
                                alertDialog1.setMessage("Yout haven't reached the destination yet!");
                                alertDialog1.show();   //

                                new CountDownTimer(3000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        alertDialog1.setMessage("Yout haven't reached the destination yet!");
                                    }

                                    @Override
                                    public void onFinish() {
                                        alertDialog1.dismiss();
                                    }
                                }.start();


                            }
                        });

                    }

                }
                catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(),"Connection Timed Out.Try again.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        thre.start();


    }
    private String getMapsApiDirectionsUrlNext() {
        // String urlz;

                        // your code here
                        Log.e("DAI","2");

                        String mode ;
                        mode = "mode=driving";
                        String waypoints = "%20waypoints=optimize:true|";

                        String sensor = "sensor=false";
                        String params = "origin="+GlobalValues.retrievedPassenLatF+","+GlobalValues.retrievedPassenLonF+"&destination="+GlobalValues.globalDriverLat+","+GlobalValues.globalDriverLon+/*waypoints + */"&" + sensor+"&"+mode;
                        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/"
                                + output + "?" + params;
                        Log.e("GAURIKAURLGET",url);
                        urlz = url;




        return urlz;



    }

    private String getMapsApiDirectionsUrlNext2() {
        // Travelling Mode
        String mode ;
        mode = "mode=driving";
        Log.e("bday","zzz");




        String waypoints = "%20waypoints=optimize:true|";

        String sensor = "sensor=false";
        String params = "origin="+GlobalValues.globalDriverLat+","+GlobalValues.globalDriverLon+"&destination="+GlobalValues.globalPassengerLat+","+GlobalValues.globalPassengerLon+/*waypoints + */"&" + sensor+"&"+mode;
        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.e("URL2",String.valueOf(url));
        return url;
    }


    private class ReadTaskNext extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            String data = "";
            Log.e("GAURIKANEXTurl",String.valueOf(url));
            try {
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);
                Log.e("GAURIKA",data);
                Log.e("GAURIKA",data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("GAURIKA2",data);
            Log.e("GAURIKA3",data);

            //======================pathjsonparser ko part
            JSONObject jObject;
            //List<List<HashMap<String, String>>> routes = null;
            //paxi
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;
            try {


                //json response from google
                jObject = new JSONObject(data);

                PathJSONParser parser = new PathJSONParser();
                Log.e("json1", data);
                routes = parser.parse(jObject);
//
                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes");
                int routesLength = ja.length();
                Log.e(String.valueOf(routesLength), "SEE ROUTE");

                //==================================DISTANCE CALCULATE
                for (int i = 0; i < 3; i++) {
                    JSONArray LEGS = ((JSONObject) ja.get(i)).getJSONArray("legs");

                    for (int j = 0; j < 3; j++) {
                        JSONObject DIST = ((JSONObject) LEGS.get(j)).getJSONObject("distance");
                        JSONObject TIM = ((JSONObject) LEGS.get(j)).getJSONObject("duration");
                        Log.e("biralo1", String.valueOf(DIST.getString("value")));

                        distanceNinMeter = DIST.getString("value");

                        Log.e("BHENA", String.valueOf(distanceNinMeter));


                    }
                }
            }catch (Exception e){

            }


                return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("value","ho");
            Log.e("GAURIKA3",result);
            new ParserTaskNext().execute(result);
        }
    }





    private class ParserTaskNext extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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

                PathJSONParser parser = new PathJSONParser();
                Log.e("json1", jsonData[0]);
                routes = parser.parse(jObject);
//
                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes");
                int routesLength = ja.length();
                Log.e(String.valueOf(routesLength),"SEE ROUTE");

                //==================================DISTANCE CALCULATE
                for(int i = 0;i<3;i++) {
                    JSONArray LEGS = ((JSONObject) ja.get(i)).getJSONArray("legs");

                    for (int j = 0; j < 3; j++) {
                        JSONObject DIST = ((JSONObject) LEGS.get(j)).getJSONObject("distance");
                        JSONObject TIM = ((JSONObject) LEGS.get(j)).getJSONObject("duration");
                        Log.e("BIRALO1", String.valueOf(DIST.getString("value")));
                        distanceNinMeter = DIST.getString("value");
                        Log.e("BHENANEXT", String.valueOf(distanceNinMeter));


                    }
                }


                        //if google responds with no route length
                //ie route length is 0 if no route if route exists it is 1
                if(routesLength==0) {

                    boolean xN = false;
                    if (xN == false){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                AlertDialog.Builder myAlert = new AlertDialog.Builder(getActivity());
                                myAlert.setMessage("Sorry1!\nNo route exists for this place!\nOnly the location is shown.")
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
                                Log.e("route","aayena");


                            }
                        });
                        xN = true;
                    }





                }
                else {


                    Log.e("route","aayo");


                    //do nothing here map ta tala post execute ma xa ni



                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
            ArrayList<LatLng> pointsN = null;
            //PolylineOptions polyLineOptions = null;

            PolylineOptions polylineOptionsNext = new PolylineOptions();



            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                pointsN = new ArrayList<LatLng>();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distanceN = (String)point.get("distance");
                        distanceNinMeter = (String)point.get("distanceValue");


                    }else if(j==1){ // Get duration from the list
                        durationN = (String)point.get("duration");
                        durationNinSecs = (String)point.get("durationValue");


                        continue;
                    }





                }
            }

        }



    }


    public String getDistance1(LatLng my_latlong, LatLng frnd_latlong) {
        Location l1 = new Location("One");
        l1.setLatitude(my_latlong.latitude);
        l1.setLongitude(my_latlong.longitude);

        Location l2 = new Location("Two");
        l2.setLatitude(frnd_latlong.latitude);
        l2.setLongitude(frnd_latlong.longitude);

        float distance = l1.distanceTo(l2);
        String dist = String.valueOf(distance);


        return dist;
    }



    public  void callClient(){
        Noti_db notiDb;
        notiDb = new Noti_db(getActivity());

        notiDb.getWritableDatabase();
        Cursor c = notiDb.getNotiData();
        String clientNum ="00000";
        while (c.moveToNext()){
            clientNum = c.getString(4);
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+ clientNum));
        startActivity(intent);
    }


}


