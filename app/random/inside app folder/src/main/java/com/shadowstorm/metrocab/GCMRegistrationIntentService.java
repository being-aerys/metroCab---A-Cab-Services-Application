package com.shadowstorm.metrocab;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Dipesh on 03/06/16.
 */
public class GCMRegistrationIntentService extends IntentService {

    user_database userDb;
    noti_db notiDb;
    String phone;
    String username;
    public static final String REGISTRATION_SUCCESS = "RegistrationSuccess";
    public static final String REGISTRATION_ERROR = "RegistrationError";
    public static final String TAG = "GCMTOKEN";
    public GCMRegistrationIntentService() {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        userDb= new user_database(this);
        notiDb= new noti_db(this);
        registerGCM();
    }

    private void registerGCM() {
        SharedPreferences sharedPreferences = getSharedPreferences("GCM", Context.MODE_PRIVATE);//Define shared reference file name
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Intent registrationComplete = null;
        String token = null;
        try {
            InstanceID instanceID = InstanceID.getInstance(getApplicationContext());
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.w("GCMRegIntentService", "token:" + token);
            //notify to UI that registration complete success
            registrationComplete = new Intent(REGISTRATION_SUCCESS);
            registrationComplete.putExtra("token", token);

            String oldToken = sharedPreferences.getString(TAG, "");//Return "" when error or key not exists
            //Only request to save token when token is new
            if(!"".equals(token)){// && !oldToken.equals(token)) {
                saveTokenToServer(token);
                //Save new token to shared reference
                editor.putString(TAG, token);
                editor.commit();
            } else {
                Log.w("GCMRegistrationService", "Old token");
            }
        } catch (Exception e) {
            Log.w("GCMRegIntentService", "Registration error");
            registrationComplete = new Intent(REGISTRATION_ERROR);
        }
        //Send broadcast
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void saveTokenToServer(String token){
        Cursor cursor = userDb.getUserData();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                phone = cursor.getString(5);
                username = cursor.getString(9);
            }
        }
        Map paramPost = new HashMap();
        paramPost.put("action","add");
        paramPost.put("phone",phone);
        paramPost.put("username",username);
        paramPost.put("tokenid", token);
        try {
            String msgResult = getStringResultFromService_POST("http://omkishan.com.np/metrocab/gcm/gcm.php", paramPost);
            Log.w("ServiceResponseMsg", msgResult);
        }catch (Exception e){
            //e.printStackTrace();
            Log.w("ServiceResponseMsg", "unable to connect to server");
        }
    }
    public String getStringResultFromService_POST(String serviceURL, Map<String, String> params) {
        HttpURLConnection cnn = null;
        String line = null;
        URL url;
        try{
            url = new URL(serviceURL);
        } catch (MalformedURLException e){
            throw  new IllegalArgumentException("URL invalid:"+serviceURL);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        //Construct the post body using the parameter
        while (iterator.hasNext()){
            Map.Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
            if(iterator.hasNext()){
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString(); //format same to arg1=val1&arg2=val2
        Log.w("AccessService", "param:" + body);
        byte[]bytes = body.getBytes();
        try{
            cnn = (HttpURLConnection)url.openConnection();
            cnn.setDoOutput(true);
            cnn.setUseCaches(false);
            cnn.setFixedLengthStreamingMode(bytes.length);
            cnn.setRequestMethod("POST");
            cnn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            //Post the request
            OutputStream outputStream = cnn.getOutputStream();
            outputStream.write(bytes);
            outputStream.close();

            //Handle the response
            int status = cnn.getResponseCode();
            if(status!=200){
                throw  new IOException("Post fail with error code:" + status);
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(cnn.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line+'\n');
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}

