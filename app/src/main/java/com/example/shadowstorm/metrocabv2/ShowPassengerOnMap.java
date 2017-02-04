package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;


public class ShowPassengerOnMap extends Activity {
    public static String latitude1; //passenger ho
    String latitude2; //driverho
    String longitude1;
    String longitude2;
    String duration="";
    String distance="";
    Polyline line = null;
    GoogleMap mMap1;
    Marker marker1,marker2;
    int updateValue ;
    int updateValue2 ;
    public static Context c;

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
        setContentView(R.layout.show_passenger_on_map);
        FragmentManager FM = getFragmentManager();
        FragmentTransaction FT = FM.beginTransaction();
        c = this;
        MapFragmentNew MF = new MapFragmentNew();
        FT.add(R.id.mapLayout1,MF);
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







