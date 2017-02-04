package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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

public class RetrieveStartValues extends AsyncTask<String,Void,String> {
    Context context;

    RetrieveStartValues(Context ctx) {
        context = ctx;
    }

    ProgressDialog dialog;
    Request_database reqDb;
    Noti_db notiDb;



    @Override
    protected String doInBackground(String... params) {
        String type = params[0];


        String userNameN = params[1];

        Log.e("ZZZZZ","1");


        String login_url =  GlobalValues.MyAppUrl  +"/metrocab/gcm/RetrieveStartingValues.php";

        if (type.equals("passengerReport")) {
            try {
               // Log.e("YYYYYYYYYYYYY2","UUUUUUU");
                //===========================

                User_database myDb = new User_database(context);
                Cursor ress = myDb.getUserData();
                if (ress.getCount() > 0) {
                    while (ress.moveToNext()) {
                        String user = ress.getString(9);
                        userNameN = user;
                    }
                }

                        //================================

                Log.e("ZZZZZ","2");

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();

                Log.e("nabin usernameN", "ads "+userNameN );
                Log.e("nabin type", "ads "+type );

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
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
               Log.e("nabin",String.valueOf(result));




                String[] itemsa = result.split("]/");
                String statusa = itemsa[0];

                if (statusa.equals("passengerReportRetrieval")) {
                    Log.e("ZZZZZ","3");

                    String status = itemsa[1];

                    String[] items = status.split("]");
                    Log.e("babu-",items[0]);
                    Log.e("babu-",items[1]);
                    Log.e("babu-",items[2]);
                    Log.e("babu-",items[3]);

                    String passenLatFirst = items[0];


                    String passenLonFirst = items[1];
                    Log.e("babu-",String.valueOf(items));
                    String driverLatFirst = items[2];
                    String driverLonFirst = items[3];
                    Log.e("babu-",String.valueOf(passenLatFirst));



                    GlobalValues.retrievedPassenLatF = passenLatFirst;
                    GlobalValues.retrievedPassenLonF = passenLonFirst;
                    GlobalValues.retrievedDriverLatF = driverLatFirst;
                    GlobalValues.retrievedDriverLonF = driverLonFirst;
                   //==================================aba check garne report garya eligible xa ki nai




                }


                Log.e("babu-","end of doInBack");

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("ZZZZZ","4");


        if (type.equals("DriverReport")) {
            Log.e("GAURIKA","222");
            Log.e("ZZZZZ","5");

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("userNameN", "UTF-8") + "=" + URLEncoder.encode(userNameN, "UTF-8");

                Log.e("ZZZZZ","6");
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
                Log.e("ZZZZZ","7");
                //============================before return
                Log.e("ZZZZZ",result);


                String[] itemsa = result.split("]/");
                String statusa = itemsa[0];
                if (statusa.equals("DriverReportRetrieval")) {



                    Log.e("ZZZZZ","6");

                    String status = itemsa[1];

                    String[] items = status.split("]");
                    String passenLatFirst = items[0];
                    String passenLonFirst = items[1];
                    String driverLatFirst = items[2];
                    String driverLonFirst = items[3];

                    Log.e("SHIFT",String.valueOf(items[1]));

                    Log.e("DAI","1");

                    GlobalValues.retrievedPassenLatF = passenLatFirst;
                    GlobalValues.retrievedPassenLonF = passenLonFirst;
                    GlobalValues.retrievedDriverLatF = driverLatFirst;
                    GlobalValues.retrievedDriverLonF = driverLonFirst;
                    Log.e("SHIFTY1",GlobalValues.retrievedPassenLatF);

                    Log.e("GAURIKA",passenLatFirst);

                }

                //===============================================
                Log.e("ZZZZZ7",result);

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }















        }

        Log.e("nabin", "null bhitra");

        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Code for the UiThread
                dialog = new ProgressDialog(context);
                dialog.setMessage("Proccessing your request ...");
                dialog.setTitle("myRide");
                dialog.setIcon(R.drawable.myridec1);
                //dialog.show();
                dialog.setCancelable(false);
                notiDb= new Noti_db(context);
            }
        });


        // reqDb= new Request_database(context);
    }

    @Override
    protected void onPostExecute(String result) {

        super.onPostExecute(result);

        Log.e("nabin", "hait");


    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

