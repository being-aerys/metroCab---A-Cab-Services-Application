package com.shadowstorm.metrocab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dipesh on 03/06/16.
 */
public class GCMPushReceiverService extends GcmListenerService {
    String address;
    noti_db notiDb;
    private RequestQueue requestQueue;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        requestQueue = Volley.newRequestQueue(this);
        String message = data.getString("message");
        sendNotification(message);
    }
    private void sendNotification(String message) {
        notiDb= new noti_db(this);
        String[] items = message.split("]");
        String rec_type = items[0];

        if(rec_type.equals("sendReq")) {
            final String rec_salute = items[1];
            final String rec_fname = items[2];
            final String rec_lanme = items[3];
            final String rec_contact = items[4];
            final String rec_lat = items[5];
            final String rec_lon = items[6];
            final String rec_devId = items[7];
            final String rec_driver = items[9];
            final String rec_msg = "I need your cab.";
            final Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //----converting lat long to place
            JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng="+rec_lat+","+rec_lon+"&key=AIzaSyC5D-LPh7YXtLO4lpwObifv-fKMo4A5uNs",
                    new Response.Listener<JSONObject>(){
                        @Override
                        public void onResponse(JSONObject response){
                            try{
                                address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");





                                //---------save sender data to db
                                boolean isInserted = notiDb.insertNotiData(rec_salute, rec_fname, rec_lanme, rec_contact, rec_devId, rec_msg, rec_lat, rec_lon, rec_driver,address);
                                if (isInserted == true) {
                                    Intent intent = new Intent(GCMPushReceiverService.this, LogInActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    int requestCode = 0;//Your request code
                                    PendingIntent pendingIntent = PendingIntent.getActivity(GCMPushReceiverService.this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
                                    //Setup notification
                                    //Sound
                                    //Build notification
                                    NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(GCMPushReceiverService.this)
                                            .setSmallIcon(R.drawable.myridec)
                                            .setContentTitle("Cab Request")
                                            .setContentText(rec_msg)
                                            .setAutoCancel(true)
                                            .setPriority(Notification.PRIORITY_HIGH)
                                            .setSound(sound)
                                            .setContentIntent(pendingIntent);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
                                }


























                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    Toast.makeText(GCMPushReceiverService.this, "volley error", Toast.LENGTH_SHORT).show();
                }
            });
            requestQueue.add(request);

        }
        if(rec_type.equals("cancelReq")){
            String rec_msg = items[1];

            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
        if(rec_type.equals("acceptReq")){
            String rec_msg = items[1];

            Intent intent = new Intent(this, LogInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;//Your request code
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);
            //Setup notification
            //Sound
            Uri sound = Uri.parse("android.resource://" + this.getPackageName() + "/raw/birdnoti");
            //Build notification
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.myridec)
                    .setContentTitle("Cab Request")
                    .setContentText(rec_msg)
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setSound(sound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, noBuilder.build()); //0 = ID of notification
        }
    }
}

