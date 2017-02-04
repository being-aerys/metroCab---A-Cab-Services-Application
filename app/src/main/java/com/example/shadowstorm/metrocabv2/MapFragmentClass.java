package com.example.shadowstorm.metrocabv2;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
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
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import static com.example.shadowstorm.metrocabv2.GCMRegistrationIntentService.TAG;
import static com.google.android.gms.internal.zzip.runOnUiThread;
import static java.lang.Thread.sleep;

/**
 * Created by Shadow Storm on 7/6/2016.
 */

public class MapFragmentClass extends Fragment {

    private GoogleMap  _map;
    String lat1,lat2,lon1,lon2,passengerName,driverFName,driverLName,distance,duration,distanceN,durationN,durationN2,distanceN2;
    Polyline line = null;
    private Context globalContext = null;
    //ShowDriverOnMap s = new ShowDriverOnMap();
    Marker myLocMarker,driverLocMarker;
    int x;

    Boolean y =true,z=true;
    Runnable run1;
    ProgressDialog dialog;
    String mode,rescueNumber,user ;

    DriversLocation_db driversLocation_db;
    Button b,bCall;
    String latToUseForCurrent,lonToUseForCurrent;
    long t1,t2,t3;
    String distanceNinMeter,durationNinSecs,s;
    int toneBoolToStop = 0;
    User_database userDb;
    Noti_db notiDb;
    String passPhone,driverPhone;
    Thread threadLoop = new Thread(run1);//static nabhaye loop ma create bhairahanxa ra out of memory hunxa




    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mode = "mode=driving";



        globalContext = this.getActivity();
        userDb = new User_database(globalContext);
        notiDb = new Noti_db(globalContext);
//=======================kept to get values early
        RetrieveStartValues bgWorker = new RetrieveStartValues(getActivity());
        bgWorker.execute("passengerReport",GlobalValues.globalPassenger);
//'''''''''''''''''''''''''''''''''''

        View v = inflater.inflate(R.layout.map_fragment_driver, container, false);
        b =(Button)v.findViewById(R.id.reportButton);//SEE VERY IMP
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report(v);
            }
        });
        bCall = (Button)v.findViewById(R.id.rescueButton);
        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDriver();
            }
        });

        _map = getMapFragment().getMap();

        //route ko kaam yeha ONLY AFTER getting the fragment natra crash hunxa



        userDb = new User_database(getActivity());
        Cursor cur = userDb.getUserData();
        while (cur.moveToNext()){
            user = cur.getString(9);
        }
//==================================geofence============================
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createGeofence(Double.parseDouble(GlobalValues.retrievedPassenLatF),Double.parseDouble(GlobalValues.retrievedPassenLonF), 30, "CIRCLE", "Feofence for passenger starting location");

            }
        });
        t1.start();
//===============================================

        //LOOP LAGEKO XAENA HOLA  ROUTE UPDATE HUNA

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (0==0) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(globalContext,"LOOP IN ",Toast.LENGTH_SHORT);
                            //=============================================
                            Thread threadBeep = new Thread(new Runnable() {


                                @Override
                                public void run() {
                                    while(true){
                                        try{


                                       /*     Float a = Float.valueOf(GlobalValues.globalDriverLat);
                                            Float b = Float.valueOf(GlobalValues.globalDriverLon);
                                            Float c = Float.valueOf(GlobalValues.retrievedPassenLonF);
                                            Float d = Float.valueOf(GlobalValues.retrievedPassenLonF);
                                            Float e = (a-c)*(a-c);
                                            Float f = (b-d)*(b-d);
                                            Float g = e+f;
                                            double h = Math.sqrt(g);
                                            */
                                            //====================
                                            LatLng l1 = new LatLng(Double.valueOf(GlobalValues.globalDriverLat),Double.valueOf(GlobalValues.globalDriverLon));
                                            LatLng l2 = new LatLng(Double.valueOf(GlobalValues.retrievedPassenLatF),Double.valueOf(GlobalValues.retrievedPassenLonF));
                                            String dist = getDistance(l1,l2);
                                            //=======================

                                         /*   Log.e("uncle", String.valueOf(a));
                                            Log.e("uncle2", String.valueOf(b));
                                            Log.e("uncle", String.valueOf(c));
                                            Log.e("uncle4", String.valueOf(d));
                                            Log.e("uncle", String.valueOf(e));
                                            Log.e("uncle6", String.valueOf(f));
                                            Log.e("uncle", String.valueOf(g));
                                            Log.e("uncle8", String.valueOf(h));
                                            */



                                            Log.e("BADRI3",String.valueOf(dist));
                                            if (Double.valueOf(dist)<=50.000000){

                                                if(MapsActivity.i<=12){
                                                    Log.e("TABLE",String.valueOf(dist));
                                                    ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                                                    toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
                                                    MapsActivity.i++;

                                                }

                                            }


                                        }catch (Exception E){

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(),"Google Server failed to reply. But the route is fine.",Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        }
                                        try {
                                            sleep(3000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
                            });
                            threadBeep.start();

                            //==============================================

                        }
                    });

                    //===========================================================
                    //====================LATLNG UPDATE OF PASSEN


                    lat1=GlobalValues.globalPassengerLat;
                    lon1=GlobalValues.globalPassengerLon;
                    lat2=GlobalValues.globalDriverLat;
                    lon2=GlobalValues.globalDriverLon;




                    String url = null;

                    url = getMapsApiDirectionsUrl();

                    ReadTask downloadTask = new ReadTask();
                    downloadTask.execute(url);

                    Log.e("XXXXX","1");
///===============================================================



                    Log.e("XXXXX","2");

                    String type="driverLatUpdate";
                    String id2="driverLatLng";
                    String phone="12344567";
                    String driverIdReq=GlobalValues.globalDriverUseId;
                    Log.e("STR",String.valueOf(driverIdReq));

                    Log.e("DAMN",String.valueOf(GlobalValues.globalDriverLat));

                    PushNotification backgroundWorkers = new PushNotification(globalContext);

                    backgroundWorkers.execute(type,id2, phone, driverIdReq,"aaaa");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(globalContext,"LOOP IN ",Toast.LENGTH_SHORT);

                        }
                    });

                    //============================================================

                    // threadLoop.resume();















                    //===================================================

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //ANimate camera
                            if(y == true){
                                LatLng latlng2 = new LatLng(Double.valueOf(lat2), Double.valueOf(lon2));
                                Log.e("IS THIS ho",String.valueOf(lon2));
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (driverLocMarker == null) {
                                    try{
                                        driverLocMarker = _map.addMarker(new MarkerOptions()
                                                .position(new LatLng(Double.valueOf(lat2), Double.valueOf(lon2)))
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.car, ""))));
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
                                                .icon(BitmapDescriptorFactory.fromBitmap(writeTextOnDrawable(R.drawable.car, ""))));
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

                }
            }
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
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("value","ho");
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
                Log.e("json1", jsonData[0]);
                routes = parser.parse(jObject);
//
                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes");
                int routesLength = ja.length();
                Log.e(String.valueOf(routesLength),"SEE ROUTE");



                //if google responds with no route length
                //ie route length is 0 if no route if route exists it is 1
                if(routesLength==0) {

                    boolean x = false;
                    if (x == false){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {





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
//======================================NEW WALA FOR RETRIEVED VALUES






    private String getMapsApiDirectionsUrlNext() {
        // Travelling Mode

        Log.e("babu-getMapsApi","dd");


        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        String waypoints = "%20waypoints=optimize:true|";
        Log.e("SHREE",String.valueOf("11111"));

        Log.e("SHREE",String.valueOf(GlobalValues.retrievedPassenLatF));
        //===============================================


        String sensor = "sensor=false";
        Log.e("nabinULine625",String.valueOf(GlobalValues.retrievedPassenLatF));
        String params = "origin="+GlobalValues.retrievedPassenLatF+","+GlobalValues.retrievedPassenLonF+"&destination="+GlobalValues.retrievedDriverLatF+","+GlobalValues.retrievedDriverLonF+/*waypoints + */"&" + sensor+"&"+mode;
        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        Log.e("babu-URL",String.valueOf(url));

        return url;


    }

    private class ReadTaskNext extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... url) {
            Log.e("babu-", "test");
            String data = "";
            try {
                Log.e("nabin url", Arrays.toString(url));
                HttpConnection http = new HttpConnection();
                data = http.readUrl(url[0]);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            Log.e("babu-ACTUALdataReturn", data+"sf");



            try {
                //json response from google
                //    Log.e(String.valueOf(routesLength),"SEE ROUTE");

                //=======================================================================
                //json response from google
                JSONObject jObject = new JSONObject(data); //1

                PathJSONParser parser = new PathJSONParser();

                Log.e("babu-","dd");
                List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
                routes = parser.parse(jObject);
//

                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes"); //2


                int routesLength = ja.length(); //3




                for(int i = 0;i<3;i++){
                    JSONArray LEGS = ((JSONObject)ja.get(i)).getJSONArray("legs");

                    for(int j = 0;j<20;j++){
                        JSONObject DIST = ((JSONObject) LEGS.get(j)).getJSONObject("distance");
                        JSONObject TIM = ((JSONObject) LEGS.get(j)).getJSONObject("duration");
                        Log.e("babu-JSONPARSETIM",String.valueOf( TIM.getString("value")));
                        durationNinSecs = TIM.getString("value");


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




            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("nabin+onPostExe","ho"+result);
            Log.e("JUPITERRRRRRR2","dd");
            //new ParserTaskNext().execute(result);
        }
    }


    private class ParserTaskNext extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            Log.e("nabin_parser", "parser bhitra xiryo");
            JSONObject jObject;
            //List<List<HashMap<String, String>>> routes = null;
            //paxi
            List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
            JSONArray jRoutes = null;
            JSONArray jLegs = null;
            JSONArray jSteps = null;

            try {
                //json response from google
                //    Log.e(String.valueOf(routesLength),"SEE ROUTE");

                //=======================================================================
                //json response from google
                jObject = new JSONObject(jsonData[0]); //1

                PathJSONParser parser = new PathJSONParser();

                Log.e("JUPITERRRRRRR1","dd");
                routes = parser.parse(jObject);
//

                // location patta lagyo but route xaena bhane yeha zero_RESULTS laii handle gar cant go through road bhanera

                JSONArray ja = jObject.getJSONArray("routes"); //2


                int routesLength = ja.length(); //3




                for(int i = 0;i<3;i++){
                    JSONArray LEGS = ((JSONObject)ja.get(i)).getJSONArray("legs");

                    for(int j = 0;j<3;j++){
                        JSONObject DIST = ((JSONObject) LEGS.get(j)).getJSONObject("distance");
                        JSONObject TIM = ((JSONObject) LEGS.get(j)).getJSONObject("duration");
                        Log.e("JUPITER4",String.valueOf( TIM.getString("value")));
                        durationNinSecs = TIM.getString("value");


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


                    //  double latZ = Double.parseDouble(point.get("lat"));
                    // double lngZ = Double.parseDouble(point.get("lng"));
                    //   LatLng position = new LatLng(latZ, lngZ);
                    //  pointsN.add(position);
                    //  polylineOptionsNext.add(position);
                }
            }

        }
    }



    //===========================================================================3rd ho

    private String getMapsApiDirectionsUrlNext2() {
        // Travelling Mode
        String mode ;
        mode = "mode=driving";

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });



        String waypoints = "%20waypoints=optimize:true|";

        String sensor = "sensor=false";
        String params = "origin="+latToUseForCurrent+","+lonToUseForCurrent+"&destination="+GlobalValues.globalDriverLat+","+GlobalValues.globalDriverLon+/*waypoints + */"&" + sensor+"&"+mode;
        // String params = "origin=51.5,-0.1&destination=40.7,-74.0&"+waypoints + "&" + sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + params;
        return url;
    }

    private class ReadTaskNext2 extends AsyncTask<String, Void, String> {
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
            Log.e("value","ho");
            new ParserTaskNext().execute(result);
        }
    }


    private class ParserTaskNext2 extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

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














                //if google responds with no route length
                //ie route length is 0 if no route if route exists it is 1
                if(routesLength==0) {

                    boolean xN2 = false;
                    if (xN2 == false){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {





                            }
                        });
                        xN2 = true;
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
            ArrayList<LatLng> pointsN2 = null;
            //PolylineOptions polyLineOptions = null;

            PolylineOptions polylineOptionsNext2 = new PolylineOptions();


//=======================================================================================================



            // traversing through routes
            for (int i = 0; i < routes.size(); i++) {
                pointsN2 = new ArrayList<LatLng>();
                List<HashMap<String, String>> path = routes.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    if(j==0){    // Get distance from the list
                        distanceN2 = (String)point.get("distance");

                    }else if(j==1){ // Get duration from the list
                        durationN2 = (String)point.get("duration");

                        continue;
                    }


                    //double latZ = Double.parseDouble(point.get("lat"));
                    // double lngZ = Double.parseDouble(point.get("lng"));
                    //LatLng position = new LatLng(latZ, lngZ);
                    // pointsN2.add(position);
                    // polylineOptionsNext2.add(position);
                }
            }

        }
    }





















    //===================================================




    private MapFragment getMapFragment() {
        FragmentManager fm = null;

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


    public void report(View v){

        //=================================take 4 starting values and generate estimaed time and distance


        RetrieveStartValues bgWorker = new RetrieveStartValues(getActivity());
        bgWorker.execute("passengerReport",GlobalValues.globalPassenger);
        Log.e("BABU-",String.valueOf(GlobalValues.globalPassenger));
        //==============geofence already created so comment here=============
/*
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                createGeofence(Double.parseDouble(GlobalValues.retrievedPassenLatF),Double.parseDouble(GlobalValues.retrievedPassenLonF), 30, "CIRCLE", "Feofence for passenger starting location");

            }
        });
        t1.start();
        */

        Log.e("nabin1", "a"+GlobalValues.retrievedPassenLatF);
        final AlertDialog alertDialog1 = new AlertDialog.Builder(globalContext).create();
//        while(GlobalValues.retrievedPassenLatF == null);
//==================================================================================================

      /*  alertDialog1.setTitle("Processing your Report");
        alertDialog1.setMessage("Please wait while the report is being processed!");
        alertDialog1.show();   //

        new CountDownTimer(8000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                alertDialog1.setMessage("Please wait while the report is being processed!");
            }

            @Override
            public void onFinish() {
                alertDialog1.dismiss();
            }
        }.start();
        */


        Log.e("nabin2", "a"+GlobalValues.retrievedPassenLatF);


        // GlobalValues.time2 = System.currentTimeMillis();

        //=====================================================GET SYSTEM TIME 2
        //==========================get system time
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:45"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:45"));

        String localTime = date.format(currentLocalTime);

        int currentHour = cal.get(Calendar.HOUR);
        int currentMinutes = cal.get(Calendar.MINUTE);
        int currentSeconds = cal.get(Calendar.SECOND);

        Log.e("babu-",String.valueOf(currentSeconds));





        //calculating time between acceptance and report such that duration is found
        //if this duration is greater than the duration the we obtain from starting lat lng values duration do sth
        GlobalValues.time2 = currentHour*60*60+currentMinutes*60+currentSeconds;


        //=======================================================================
        //calculation time taken between driver acceptance and the time of report
        Cursor c3 = userDb.getUserData();
        while(c3.moveToNext()){
            String time1 =c3.getString(23);
            GlobalValues.time1= Long.parseLong(time1);
        }

        GlobalValues.durationUntilPassenReport = GlobalValues.time2-GlobalValues.time1;
        Log.e("babu-suruntilreport",String.valueOf(GlobalValues.durationUntilPassenReport));
        //=================================
        String url = null;
        url = getMapsApiDirectionsUrlNext();
        ReadTaskNext downloadTask = new ReadTaskNext();
        downloadTask.execute(url);
        //==========durationN ra distanceN aayo
        //===yeslai yehi compare gar aba duitai ko current lat lng liyera
        //server ma halnai pardaina

        //===============take current lat lng of passen using code,not refer to other vals coz passen move pani huna sakxa ni ta


        final long uptime = GlobalValues.durationUntilPassenReport;
        Log.e("babu-uptime",String.valueOf(uptime));


//==============================================================================


        while(durationNinSecs == null);


        final AlertDialog.Builder myAlert = new AlertDialog.Builder(globalContext);
        Log.e("nabinUDuratinSecOrig",String.valueOf(durationNinSecs));

        Log.e("nabinUdurationNinSecs",String.valueOf(Float.parseFloat(durationNinSecs)));
//durationNinSecs bhaneko driver aaipugna lagne time
        //uptime bhaner=ko passenger le kureko time
        if( uptime > ( Float.valueOf(durationNinSecs)) ){

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int f = Integer.valueOf(durationNinSecs);

                    Log.e("BADRI",String.valueOf(f));
                    int  f1 = Integer.valueOf(Integer.valueOf(String.valueOf(f))/60);
                    Log.e("BADRIf2",String.valueOf(f1));
                    int f2 =  Integer.valueOf(String.valueOf(f))%60;
                    int f3 = Integer.valueOf(String.valueOf(uptime))/60;
                    int f4 = Integer.valueOf(String.valueOf(uptime))%60;
                    //=======================
                    Cursor c1 = userDb.getUserData();
                    while(c1.moveToNext()){
                        driverPhone =c1.getString(18);
                    }
                    Cursor c2 = userDb.getUserData();
                    while(c2.moveToNext()){
                        passPhone =c2.getString(5);
                    }
                    //===========================

                    myAlert.setMessage("Driver had to arrive in "+String.valueOf(f1)+"mins "+String.valueOf(f2)+" secs.You waited for "+String.valueOf(f3)+"mins "+String.valueOf(f4)+" secs..Your account has been creditted with 20% of the fare agreed as a compensation.Proceed with another driver.")
                            .setPositiveButton("Okay",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    //PushNotification pn = new PushNotification(getActivity());
                                    //pn.execute("cancelReq","setDriverToFree",passPhone,driverPhone);//==========
                                    //do

                                    //================================
                                    Intent intent = new Intent(getActivity(), MapsActivity.class);
                                    getActivity().startActivity(intent);
                                }
                            })

                            .setTitle("Report Response From Server")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();

                }
            });



            //================================send noti to drive rthat he didnt arrive on time so balance
            //has been deducted from his acc
            // deduct $3 from driver acc and pass to passenger
            // and make driver free

            Cursor c1 = userDb.getUserData();
            while(c1.moveToNext()){
                driverPhone =c1.getString(18);
            }
            Cursor c2 = userDb.getUserData();
            while(c2.moveToNext()){
                passPhone =c2.getString(5);
            }
            PushNotification backgroundWorkersThis = new PushNotification(getActivity());
            backgroundWorkersThis.execute("driverIsReporting",passPhone,driverPhone,"passenger");




            //============================================================
            Log.e("nabin greater than", String.valueOf(Float.parseFloat(durationNinSecs)));
            //deduct balance from driver acc and pass it to passen
        }

        else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myAlert.setMessage("Estimated time hasn't passed yet.Please be patient.")
                            .setPositiveButton("Okay",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                }
                            })

                            .setTitle("Report Response From Server")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();


                }
            });
        }

        try{


            LocationManager locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            latToUseForCurrent = String.valueOf(location.getLatitude());
            lonToUseForCurrent = String.valueOf(location.getLongitude());

        }
        catch (Exception e){
            Log.e("1113","222");
            GPSTracker gpsTracker = new GPSTracker(getActivity());
            latToUseForCurrent = String.valueOf(gpsTracker.getLatitude());
            lonToUseForCurrent = String.valueOf(gpsTracker.getLongitude());
        }


        //====================aba driver ko current lat lng ta global values ma nai xa katai bata linai parena

//aile use bhayena tara coz system time difference ra original time batai kaam bhayao



        String url2 = null;
        url2 = getMapsApiDirectionsUrlNext2();
        ReadTaskNext2 downloadTask2 = new ReadTaskNext2();
        downloadTask2.execute(url);


        //==================================================duitai aayo duration ra time
        Calendar c = Calendar.getInstance();
        int seconds = c.get(Calendar.SECOND);





    }

    public void onDestroy() {

        super.onDestroy();
        myLocMarker=null;
        driverLocMarker=null;

    }

    public  void callDriver(){
        // driversLocation_db = new DriversLocation_db(getActivity());

        userDb.getWritableDatabase();
        Cursor c = userDb.getUserData();
        String driverNum ="00000";
        while (c.moveToNext()){
            driverNum = c.getString(18);
        }
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:"+ driverNum));
        startActivity(intent);
    }

    private void createGeofence(final double latitude, final double longitude, final int radius,
                                String geofenceType, String title) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Marker stopMarker;

                stopMarker = _map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Geofence for passenger starting point"));
                _map.addCircle(new CircleOptions()
                        .center(new LatLng(latitude, longitude)).radius(50)
                        .fillColor(Color.parseColor("#0033A9F6")));
            }
        });



    }
    public String getDistance(LatLng my_latlong, LatLng frnd_latlong) {
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
}


