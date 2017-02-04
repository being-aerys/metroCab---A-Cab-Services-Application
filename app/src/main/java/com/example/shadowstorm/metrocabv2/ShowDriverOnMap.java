package com.example.shadowstorm.metrocabv2;

import android.app.Activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONObject;

import static java.lang.Thread.sleep;


public class ShowDriverOnMap extends Activity {
    public static String latitude1;
    String latitude2;
    String longitude1;
    String longitude2;
    String duration="";
    String distance="";
    Polyline line = null;
    GoogleMap mMap1;
    Marker marker1,marker2;
    int updateValue ;
    int updateValue2 ;
    String user;
    public static Context c;
    JSONArray LegsJ;

    JSONObject DistanceJ,DurationJ;
    String latToUseForCurrent,lonToUseForCurrent,durationNinSecs,distanceN,distanceNinMeter,durationN,distanceN2,durationN2;

    //public ShowDriverOnMap(Context c){
    //   this.c = c;
    // }

    public Location getLocation() {

        //context pass garna chainxa manager


        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        return location;



    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_driver_on_map);
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        c = this;
        User_database userDb = new User_database(this);
        MapFragmentClass MF = new MapFragmentClass();
        FT.add(R.id.mapLayout,MF);
        //=================
        Cursor cur = userDb.getUserData();
        while (cur.moveToNext()){
            user = cur.getString(9);
        }
        try{LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();


            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            //i changed it from location.getLatitude() to gps.getLatitude cause the earlier one returned null and crashed but
            //works fine with location as well now
            GlobalValues.globalPassengerLat = String.valueOf(location.getLatitude());



            GlobalValues.globalPassengerLon = String.valueOf(location.getLongitude());

//insert in sqlite
            userDb.insertLatLon(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));





        }
        catch (Exception e){

            GPSTracker gpsTracker = new GPSTracker(this);

            //========================================
            GlobalValues.globalPassengerLat = String.valueOf(gpsTracker.getLatitude());
            GlobalValues.globalPassengerLon = String.valueOf(gpsTracker.getLongitude());
            userDb.insertLatLon(String.valueOf(gpsTracker.getLatitude()), String.valueOf(gpsTracker.getLongitude()));




        }

        Utils.updateLocationInDatabase(this, user,GlobalValues.globalPassengerLat + "", GlobalValues.globalPassengerLon + "");

        //==================================================================

        FT.commit();





    }

    public void getValues(){
        String lat1,lat2,lon1,lon2;
        lat1=latitude1;
        lat2=latitude2;
        lon1=longitude1;
        lon2=longitude2;
    }
    public void setUpMap1(){






    }


}







