package com.example.shadowstorm.metrocabv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Shadow Storm on 8/2/2016.
 */

public class SetStartingValues extends AsyncTask<String,Void,String> {
    Context context;

    SetStartingValues(Context ctx) {
        context = ctx;
    }

    ProgressDialog dialog;
    Request_database reqDb;
    Noti_db notiDb;


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String startingLat = params[1];
        String startingLon = params[2];
        String userNameN = params[3];
        Log.e("RRR",startingLat);
        //String myphone = params[3];
        //String destToPass = params[4];
        //String isSession = params[4];


        String login_url =  GlobalValues.MyAppUrl  +"/metrocab/gcm/setStartingValues.php";

        if (type.equals("setPassengerStart")) {
            try {
                Log.e("22222","22222");
                Log.e("9992",String.valueOf(userNameN));

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.e("22222",String.valueOf(startingLat));

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("startingLat", "UTF-8") + "=" + URLEncoder.encode(startingLat, "UTF-8")+ "&"
                        + URLEncoder.encode("startingLon", "UTF-8") + "=" + URLEncoder.encode(startingLon, "UTF-8")+ "&"
                        + URLEncoder.encode("userNameN", "UTF-8") + "=" + URLEncoder.encode(userNameN, "UTF-8");



                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.e("22222",String.valueOf(result));

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (type.equals("setDriverStart")) {
            try {
                Log.e("9991",String.valueOf(userNameN));
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("startingLat", "UTF-8") + "=" + URLEncoder.encode(startingLat, "UTF-8")+ "&"
                        + URLEncoder.encode("startingLon", "UTF-8") + "=" + URLEncoder.encode(startingLon, "UTF-8")+ "&"
                        + URLEncoder.encode("userNameN", "UTF-8") + "=" + URLEncoder.encode(userNameN, "UTF-8");


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                Log.e("11111","33333");

                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        // reqDb= new Request_database(context);
    }

    @Override
    protected void onPostExecute(String result) {
        //dialog.cancel();
        String[] itemsa = result.split("]/");
        Log.e("RRRR",String.valueOf(itemsa));
        String statusa = itemsa[0];
        Log.e("RRRR",String.valueOf(statusa));
        if (statusa.equals("driverStartSet")) {





        }
        if (statusa.equals("passengerStartSet")) {


            Log.e("RRR","PASSENGER START VALUE SET ");

        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

