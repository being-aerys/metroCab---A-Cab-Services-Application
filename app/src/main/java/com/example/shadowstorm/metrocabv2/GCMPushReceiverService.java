package com.example.shadowstorm.metrocabv2;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static com.example.shadowstorm.metrocabv2.GlobalValues.isSessionOn;
import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * Created by Dipesh on 03/06/16.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String address,userNameN;
    Noti_db notiDb;
    User_database userDb;
    private RequestQueue requestQueue;
    private String responseMsg;
    Button button;
    String message;
    public static String  driverResponse,destAddr;
    String type = "setPassengerStart";

    @Override
    public void onMessageReceived(String from, Bundle data) {
        requestQueue = Volley.newRequestQueue(this);
         message = data.getString("message");
        checkIfLogged();



    }
    public void checkIfLogged(){
        if (SaveSharedPreference.getUserName(this)!= null){
            sendNotification(message);
            Log.e("GCM","message aayo hai tala");
            Log.e("GCM",message);
        }
    }

    private void sendNotification(String message) {
        notiDb = new Noti_db(this);
        userDb = new User_database(this);
        Log.e("CheckNotificationResult", message);
        String[] items = message.split("]");
        String rec_type = items[0];

        User_database userDb = new User_database(this);
        //for SQLite db we need cursor and its adapter
        Cursor res = userDb.getUserData();
        Log.e(String.valueOf(res), "cursor");
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                userNameN = res.getString(9);
                //-------------------
            }
        }

        if (rec_type.equals("DoubleRequest")) {
            String rec_msg = items[1];
            //-----------------------------
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        if (rec_type.equals("PassengerCancelledPending")) {
            String rec_pass_phone = items[1];
            String rec_msg = items[2];
            //====================================================================

            //==========================================================================
            Integer deleteRows = notiDb.deleteNotiDataq(rec_pass_phone);
            if (deleteRows > 0) {
                Log.e("dbdeleted with msg : ", rec_msg);
            } else {
                Log.e("dbdeleted with msg : ", rec_msg);
            }
            //-----------------------------
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request Cancelled")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        if (rec_type.equals("PassengerCancelledAccepted")) {
            String rec_pass_phone = items[1];
            String rec_driver_phone = items[3];
            String rec_msg = items[2];

            boolean isUpdated = userDb.updateStatus(rec_driver_phone,"FREE");
            if(isUpdated == true){
                Log.e("driver status : ", "-------------------------- FREE");
            }else{
                Log.e("driver status : ", "---------------------- CANNOT CHANGE");
            }


            Integer deleteRows = notiDb.deleteNotiDataq(rec_pass_phone);
            if (deleteRows > 0) {
                GlobalValues.driverLaiSessionOn=false;
                Log.e("dbdeleted with msg : ", rec_msg);
            } else {
                Log.e("dbdeleted with msg : ", rec_msg);
            }
            GlobalValues.isSessionOn = false;
            //-----------------------------
            Intent intent = new Intent(this, MapsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request Cancelled")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        if (rec_type.equals("sendReq")) {
            final String rec_salute = items[1];
            final String rec_fname = items[2];
            final String rec_lanme = items[3];
            final String rec_contact = items[4];
            final String rec_lat = items[5];
            final String rec_lon = items[6];
            final String rec_devId = items[7];
            final String rec_msg = items[8];
            final String rec_driver = items[9];
            final String destToPass = items[11];
            final String paymentAmount = items[12];
            final String paymentStatus = items[13];
            //===============
            destAddr = destToPass;
            Log.e("IIII", String.valueOf(destToPass));
            Log.e("III", String.valueOf(rec_driver));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(GCMPushReceiverService.this, "DESTI" + String.valueOf(destToPass), Toast.LENGTH_SHORT).show();

                }
            });
            //responseMsg = rec_msg;

            //CallActivity callActivityObj = new CallActivity();


            final Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //----converting lat long to place
            JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + rec_lat + "," + rec_lon + "&key=AIzaSyC5D-LPh7YXtLO4lpwObifv-fKMo4A5uNs",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("GCM", rec_fname + " KO request AAYO");
                                address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");

                                //---------save passenf=ger to db


                                //------------------NUMBER SAME NABHYE MATRA INSERT BANAI

                                boolean isInserted = notiDb.insertNotiData(rec_salute, rec_fname, rec_lanme, rec_contact, rec_devId, rec_msg, rec_lat, rec_lon, rec_driver, address, "Pending", "0", destToPass, paymentAmount, paymentStatus);
                                if (isInserted == true) {
                                    Intent intent = new Intent(GCMPushReceiverService.this, CheckNotification.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    int requestCode = 0;//Your request code
                                    PendingIntent pendingIntent = PendingIntent.getActivity(GCMPushReceiverService.this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                                    //Setup notification
                                    //Sound
                                    //Build notification
                                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(GCMPushReceiverService.this)
                                            .setSmallIcon(R.drawable.myridec1)
                                            .setContentTitle("Cab Request")
                                            .setContentText(rec_msg)
                                            .setAutoCancel(true)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setSound(sound)
                                            .setContentIntent(pendingIntent);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                                    Toast.makeText(getApplicationContext(), "Notification saved", Toast.LENGTH_SHORT);

                                } else {
                                    Toast.makeText(getApplicationContext(), "Notification not saved", Toast.LENGTH_SHORT);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(GCMPushReceiverService.this, "volley error", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);

        }
        if (rec_type.equals("cancelReq")) {
            String rec_msg = items[1];
            String rec_phone = items[2];
            boolean isUpdated = userDb.updateUserSessonComplete(rec_phone);
            if (isUpdated == true) {
                Log.w("dbdeleted with msg : ", rec_msg);
            } else {
                Log.w("dbdeleted with msg : ", rec_msg);
            }
            GlobalValues.isSessionOn = false;

            //-----------------------------
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
            GlobalValues.isSessionComplete = 1;
        }


        if (rec_type.equals("buy_money")) {
            String rec_msg = items[1];
            String rec_amt = items[2];
            String rec_id = "1";
            Log.e("paypal Transaction noti", "------------" + rec_amt + ", " + rec_msg + "," + rec_type);


            /*GCMPushReceiverService gcm = new GCMPushReceiverService();
            gcm.setResp("On my way.");
            Log.e("HERE", "ON MY WAY");
            //driverResponse = "On my way.";*/

            Intent intent = new Intent(this, ShowDriverOnMap.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Balance Updated")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        if (rec_type.equals("acceptReq")) {
            String rec_msg = items[1];
            String rec_id = items[2];
            String rec_pass_phone = items[3];
            String rec_driver_phone = items[4];

            boolean isUpdated = userDb.updateUserData(rec_pass_phone, "Accepted", "1", rec_driver_phone);
            if (isUpdated == true) {
                Log.e("DBUpdated with msg : ", rec_msg);
                GlobalValues.isAccepted = 1;
            } else {
                Log.e("DBUpdated with msg : ", rec_msg);
            }

            GCMPushReceiverService gcm = new GCMPushReceiverService();
            gcm.setResp("On my way.");
            //----------------------------------------------------------------------------------------------
            isSessionOn = true;


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

            Log.e("WWW", String.valueOf(currentSeconds));
            int time1= currentHour * 60 * 60 + currentMinutes * 60 + currentSeconds;
           // GlobalValues.time1 =time1;
            Log.e("nabinUpdateGCMtime1", String.valueOf(GlobalValues.time1));

            boolean isupdated = userDb.updateStartingTime(rec_pass_phone, String.valueOf(time1));
            if(isupdated == true){
                Log.e("acceptedTime", "-------------updated with time "+time1);
            }else{
                Log.e("acceptedTime","-------------not updated with time "+time1);
            }

            try {
                Log.e("1112", "222");


                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                GlobalValues.passenSideStartingLat = String.valueOf(location.getLatitude());
                GlobalValues.passenSideStartingLon = String.valueOf(location.getLongitude());


                Log.e("LOCATIONV", String.valueOf(location.getLatitude()));
                Log.e("SETTING1", GlobalValues.passenSideStartingLat);
                Log.e("SETTING1", GlobalValues.passenSideStartingLon);


                //=============================SETTING STARTING VALS
                SetStartingValues backgroundWorkers = new SetStartingValues(GCMPushReceiverService.this);
                // Log.e(String)
                backgroundWorkers.execute(type, GlobalValues.passenSideStartingLat, GlobalValues.passenSideStartingLon, userNameN);


            } catch (Exception e) {
                Log.e("1113", "222");
                GPSTracker gpsTracker = new GPSTracker(GCMPushReceiverService.this);
                //kept in comment cause it has 0.0 as value

                ////GlobalValues.passenSideStartingLat = String.valueOf(gpsTracker.getLatitude());
                // GlobalValues.passenSideStartingLon = String.valueOf(gpsTracker.getLongitude());

                //=======================Setting the starting  location again in case the passenger moved
                //kept in comment cause it has 0.0 as value

                /* SetStartingValues backgroundWorkers = new SetStartingValues(GCMPushReceiverService.this);
                backgroundWorkers.execute(type,  GlobalValues.passenSideStartingLat,  GlobalValues.passenSideStartingLon,userNameN);

                Log.e("SETTING",GlobalValues.passenSideStartingLat);
                Log.e("SETTING",GlobalValues.passenSideStartingLon);
                */


            }


            //=============================================================================================
            Log.e("HERE", "ON MY WAY");
            //driverResponse = "On my way.";

            Intent intent = new Intent(this, ShowDriverOnMap.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }


//---------------------------------  i am arrived----this noti is for passenger
        if (rec_type.equals("iAmArrived")) {
            String rec_msg = items[1];
            String rec_msga = items[2];
            String driver_phone = items[3];
            String pass_phone = items[4];
            Log.e("iAmArrived Push Noti", "----------------------------" + rec_msg);

            boolean isUpdated = userDb.updateUserSessonArrived(pass_phone, "1");
            if (isUpdated == true) {
                Log.e("DBUpdated with msg : ", rec_msg);
            } else {
                Log.e("DBUpdated with msg : ", "----------------------------not updated");
            }
            GlobalValues.isArrived = 1;

            Intent intent = new Intent(this, Message.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification


        }
        //-----------------------------------this noti will receive by driver---------------- arrivedMessage
        //----this type is retrived from checkSession.php after passenger click Yes on driver arrival
        if (rec_type.equals("arrivedMessage")) {
            String rec_msg = items[1];
            String rec_phone = items[2];//--passenger phone
            String rec_YesNo = items[3];
            Log.e("check yesNo ", "-------------------------received  " + rec_YesNo);
            if (rec_YesNo.equals("Yes")) {
                Log.e("check update ", "-------------------------received  ");
                boolean isUpdated = notiDb.updateNotiDataComplete(rec_phone, "1", "Arrived");
                if (isUpdated == true) {
                    Log.e("DBUpdated with msg : ", rec_msg);
                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            } else {
                Log.e("check update ", "-------------------------not received  ");
            }
            Log.e("arrivedMessage Noti", "----------------------------" + rec_msg);
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Metrocab")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        //------------------------noti for passenger---------  i am arrived
        if (rec_type.equals("reachedDestination")) {
            String rec_msg = items[1];
            String rec_msga = items[2];
            String driver_phone = items[3];
            String pass_phone = items[4];
            Log.e("reachedDestination Noti", "----------------------------" + rec_msg);

            boolean isUpdated = userDb.updateUserSessonComplete(pass_phone, "1");
            if (isUpdated == true) {
                Log.e("DBUpdated with msg : ", rec_msg);
            } else {
                Log.e("DBUpdated with msg : ", "----------------------------not updated");
            }
            GlobalValues.isCompleted = 1;

            Intent intent = new Intent(this, Message.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Metrocab")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

        }
        //-----------------------------------this noti will receive by driver---------------- arrivedMessage
        //----this type is retrived from checkSession.php after passenger click Yes on driver arrival
        if (rec_type.equals("completedMessage")) {
            String rec_msg = items[1];
            String rec_phone = items[2];//--passenger phone
            String rec_YesNo = items[3];
            Log.e("check yesNo ", "-------------------------received  " + rec_YesNo);
            if (rec_YesNo.equals("Yes")) {
                Log.e("check update ", "-------------------------received  ");
                boolean isUpdated = notiDb.updateNotiDataComplete(rec_phone, "2", "Completed");
                if (isUpdated == true) {
                    Log.e("DBUpdated with msg : ", rec_msg);
                    //======================================CHANGE STATUS TO FREE ON SQLITE ALREADY===============
                    //doesnrt matter whether u change status to free here or not because on servere it is still busy until payment is made
                    String st = "FREE";
                    //==============================SESSION COMPLETE HUDA STATUS UPDATE=============
                    final Cursor c = userDb.getUserData();

                    while ((c.moveToNext())) {

                        st = c.getString(5);
                    }
                    boolean b = userDb.updateStatus(st,"FREE");

                    Log.e("==================: ", "----1----");

                    if(b == true){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("==========: ", "------------------------2");

                                Toast.makeText(GCMPushReceiverService.this,"Status Updated To FREE",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("============", "3=====not updated");

                                Toast.makeText(GCMPushReceiverService.this,"Status Update Failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    //===============================================

                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            } else {
                Log.e("check update ", "-------------------------not received  ");
            }
            Log.e("arrivedMessage Noti", "----------------------------" + rec_msg);
            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec1)
                    .setContentTitle("Metrocab")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        //---------------------------------------------------makePayment
        if (rec_type.equals("makePayment")) {
            String acType = items[1];
            String msg = items[2];
            String amt = items[3];
            String passPhone = items[4];
            String status = items[5];
            if (!status.equals("PAID")) {
                if (acType.equals("driver")) {
                    boolean isUpdated = notiDb.updateUserPayment(passPhone, amt, status);
                    if (isUpdated == true) {
                        Log.e("gcm payment : ", "1");
                        Intent intent = new Intent(this, DriverPge.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int requestCode = 0;//Your request code
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.myridec1)
                                .setContentTitle("Metrocab")
                                .setContentText(msg)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(sound)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                        GlobalValues.checkPaymentStatus = 1;


                    } else {
                        Log.e("DBUpdated with msg : ", "----------------------------not updated");
                    }
                } else {
                    boolean isUpdated = userDb.updateUserSessonComplete(passPhone);
                    if (isUpdated == true) {
                        Log.e("gcm payment : ", "1");
                        Intent intent = new Intent(this, MapsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int requestCode = 0;//Your request code
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.myridec1)
                                .setContentTitle("Metrocab")
                                .setContentText(msg)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(sound)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                        GlobalValues.isSessionComplete = 1;
                        // Intent intx = new Intent(this,MapsActivity.class);
                        //startActivity(intx);

                    } else {
                        Log.e("DBUpdated with msg : ", "----------------------------not updated");
                    }
                }
            } else {
                if (acType.equals("driver")) {
                    Integer deleteRows = notiDb.deleteNotiDataq(passPhone);
                    if (deleteRows > 0) {
                        GlobalValues.driverLaiSessionOn=false;
                        Log.e("dbdeleted with msg : ", passPhone);
                        Intent intent = new Intent(this, CheckNotification.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int requestCode = 0;//Your request code
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.myridec1)
                                .setContentTitle("Metrocab")
                                .setContentText(msg)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(sound)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                        GlobalValues.isSessionComplete = 1;
                    } else {
                        Log.e("DBUpdated with msg : ", "----------------------------not updated");
                    }
                } else {
                    boolean isUpdated = userDb.updateUserSessonComplete(passPhone);
                    if (isUpdated == true) {
                        Log.e("gcm payment : ", "1");
                        Intent intent = new Intent(this, Message.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int requestCode = 0;//Your request code
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.myridec1)
                                .setContentTitle("Metrocab")
                                .setContentText(msg)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(sound)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                        GlobalValues.isSessionComplete = 1;
                        GlobalValues.isSessionOn = false;


                    } else {
                        Log.e("====== : ", "---------------------4");
                    }
                }

            }
        }
        if (rec_type.equals("bargainByPass")) {
            String rec_msg = items[1];
            String amount = items[2];
            String passPhone = items[3];
            String status = items[4];
            if (status.equals("reject")) {
                String new_status = "NEW";
                boolean isUpdated = notiDb.updateUserPayment(passPhone, amount, new_status);
                if (isUpdated == true) {
                    Log.e("Bargain", "----------------------------------Noti_db update vayo");
                    Intent intent = new Intent(this, CheckNotification.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int requestCode = 0;//Your request code
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.myridec1)
                            .setContentTitle("Metrocab")
                            .setContentText(rec_msg)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(sound)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                } else {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayena");
                }
            } else {
                String new_status = "accept";
                boolean isUpdated = notiDb.updateUserPayment(passPhone, amount, new_status);
                if (isUpdated == true) {
                    Log.e("Bargain", "----------------------------------Noti_db update vayo");
                    Intent intent = new Intent(this, CheckNotification.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int requestCode = 0;//Your request code
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.myridec1)
                            .setContentTitle("Metrocab")
                            .setContentText(rec_msg)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(sound)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification

                } else {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayena");
                }
            }
        }
        if (rec_type.equals("bargainByDriver")) {
            String rec_msg = items[1];
            String amount = items[2];
            String passPhone = items[3];
            String driverPhone = items[4];
            String status = items[5];
            if (status.equals("reject")) {
                boolean isUpdated = userDb.updateUserBalance(passPhone, amount, "NEW");
                if (isUpdated == true) {
                    GlobalValues.isBargain = 1;
                    GlobalValues.isSessionOn = true;
                    Log.e("Bargain", "----------------------------------user_db update vayo");

                    Intent intent = new Intent(this, Message.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int requestCode = 0;//Your request code
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.myridec1)
                            .setContentTitle("Metrocab")
                            .setContentText(rec_msg)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(sound)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                } else {
                    Log.e("Bargaain", "----------------------------------user_db update vayena");
                }
            } else {
                boolean isUpdated = userDb.updateUserSessonOnOff(passPhone, "1", driverPhone);
                if (isUpdated == true) {
                    GlobalValues.isAccepted = 1;
                    Intent intent = new Intent(this, Message.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int requestCode = 0;//Your request code
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.myridec1)
                            .setContentTitle("Metrocab")
                            .setContentText(rec_msg)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(sound)
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                } else {
                    Log.e("Bargaain", "----------------------------------user_db update vayena");
                }
            }
        }
        if (rec_type.equals("reportBalanceAdded")) {
            String rec_msg = items[1];
            String passPhone = items[2];
            String driverPhone = items[3];
            String actype = items[4];

            if(actype.equals("passenger")) {
                boolean isUpdated = userDb.updateUserSessonComplete(passPhone);
                if (isUpdated == true) {
                    GlobalValues.isCompleted = 1;
                    GlobalValues.isSessionOn = false;
                    Log.e("reportBlanace", "----------------------------------user_db update vayo");
                    Intent intent = new Intent(this, MapsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    int requestCode = 0;//Your request code
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                    Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.myridec1)
                            .setContentTitle("Metrocab")
                            .setContentText(rec_msg)
                            .setAutoCancel(true)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(sound)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                } else {
                    Log.e("reportBlanace", "----------------------------------user_db update vayo");
                }
            }else{
                Integer deleteRows = notiDb.deleteNotiDataq(passPhone);
                if (deleteRows > 0) {
                    GlobalValues.driverLaiSessionOn=false;
                    boolean isUpdated = userDb.updateStatus(driverPhone,"FREE");
                    if(isUpdated == true) {
                        Log.e("reportBlanace", "----------------------------------notiDb update vayo");
                        Intent intent = new Intent(this, DriverPge.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        int requestCode = 0;//Your request code
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                        Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
                        NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.myridec1)
                                .setContentTitle("Metrocab")
                                .setContentText(rec_msg)
                                .setAutoCancel(true)
                                .setPriority(Notification.PRIORITY_HIGH)
                                .setSound(sound)
                                .setContentIntent(pendingIntent);

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                    }else{
                        Log.e("reportBlanace", "----------------------------------notiDb status update  vayena");
                    }
                }else {
                    Log.e("reportBlanace", "----------------------------------notiDb dnoti delete vayena");
                }
            }
        }
    }

    public String getResp() {
        if (driverResponse == null) {
            return "n/a";
        } else {
            return driverResponse;
        }
    }





    public void setResp(String name){
        this.driverResponse = name;
        Log.e("HERE","SET");
        Log.e("HERE",this.driverResponse);

    }




}

