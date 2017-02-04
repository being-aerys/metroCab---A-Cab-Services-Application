package com.example.shadowstorm.metrocabv2;

import java.util.ArrayList;

/**
 * Created by Shadow Storm on 7/15/2016.
 */

public class GlobalValues {
    public static String destAdder,globalLat1, globalLat2, globalLon1, globalLon2, globalDriverLat, globalDriverLon, globalPassengerLat, globalPassengerLon,globalPassenger,globalDriverFName,globalDriverLName;
    static String globalDriverUseId,driverSidePassengerPhoneForId,driverSidePassenLat,driverSidePassenLon,driverSideDriverPhone;
    static String driverSideStartingLat,driverSideStartingLon,passenSideStartingLat,passenSideStartingLon;
    static String retrievedPassenLatF,retrievedPassenLonF,retrievedDriverLatF,retrievedDriverLonF,globalAcceptedPassengerNumber;
    static ArrayList globalRemoveDriverArrayList;
    public static Boolean isSessionOn = false;
    public static  Boolean driverLaiSessionOn = false;
    public static long time1,time2,time3,durationUntilPassenReport;
    public static String MyAppUrl ="http://samundra.ezyro.com";
    //public static String MyAppUrl ="http://metrocab.6te.net
    public static  String db_id_selected_by_passenger;
    public static  String passengerSelectedByDriverBoolean;



    public static void main(String[] args) {
    }
    public void setGlobal1(String latPassen,String lonPassen){
        this.globalPassengerLat = latPassen;
        this.globalPassengerLon = lonPassen;
    }
    public void setGlobal2(String latDriver,String lonDriver){
        this.globalDriverLat = latDriver;
        this.globalDriverLon = lonDriver;
    }
    public String getGlobal1Lat(){
       return globalPassengerLat;
    }
    public String getGlobal1Lon(){
        return globalPassengerLon;
    }
    public String getGlobal2Lat(){
        return globalDriverLat;
    }
    public String getGlobal2Lon(){
        return globalDriverLon;
    }




    public static int isBargain =0; //------------required to refresh Message.java and CheckNotification.java
    public static int isLoaded =1; //------------required to refresh Message.java and CheckNotification.java
    public static int checkPaymentStatus =0; //------------required to refresh Message.java and CheckNotification.java
    public static int isArrived =0; //------------required to refresh Message.java and CheckNotification.java
    public static int isCompleted =0; //------------required to refresh Message.java and CheckNotification.java
    public static int isAccepted =0; //------------required to refresh Message.java and CheckNotification.java
    public static int isSessionComplete =0; //------------required to refresh Message.java and CheckNotification.java
}