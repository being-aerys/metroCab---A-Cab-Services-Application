package com.example.shadowstorm.metrocabv2;



import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Dipesh on 17/05/16.
 */
public class User_datasql extends AsyncTask<String,Void,String> {
    Context context;
    User_datasql(Context ctx) {
        context = ctx;
    }
    User_database userDb;
    ProgressDialog dialog;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String salute = params[1];
        String fname = params[2];
        String lname = params[3];
        String country = params[4];
        String phone = params[5];
        String actype = params[6];
        String license = params[7];
        String vehicle = params[8];
        String username = params[9];
        String password = params[10];
        String paypal = params[11];
        Log.e("signup","----------------------type "+type);


        String login_url =  GlobalValues.MyAppUrl+"/metrocab/signup.php";
        if (type.equals("addAppUser")) {
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("fname", "UTF-8") + "=" + URLEncoder.encode(fname, "UTF-8") + "&"
                        + URLEncoder.encode("lname", "UTF-8") + "=" + URLEncoder.encode(lname, "UTF-8") + "&"
                        + URLEncoder.encode("country", "UTF-8") + "=" + URLEncoder.encode(country, "UTF-8")+ "&"
                        + URLEncoder.encode("salute", "UTF-8") + "=" + URLEncoder.encode(salute, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("actype", "UTF-8") + "=" + URLEncoder.encode(actype, "UTF-8") + "&"
                        + URLEncoder.encode("license", "UTF-8") + "=" + URLEncoder.encode(license, "UTF-8") + "&"
                        + URLEncoder.encode("vehicle", "UTF-8") + "=" + URLEncoder.encode(vehicle, "UTF-8") + "&"
                        + URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") + "&"
                        + URLEncoder.encode("paypal", "UTF-8") + "=" + URLEncoder.encode(paypal, "UTF-8");

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
        Log.e("signup","----------------------recived");
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait ...");
        dialog.setTitle("Sign up");
        dialog.setIcon(R.drawable.myridec1);
        dialog.show();
        dialog.setCancelable(false);
        userDb= new User_database(context);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.cancel();
        Log.e("signup","-------------result---------"+result);

        String[] items = result.split(":");
        String status = items[0];
        // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (status.equals("success")) {
            String username= items[1];
            String password = items[2];
            String salute = items[3];
            String fname = items[4];
            String lname = items[5];
            String country = items[6];
            String phone = items[7];
            String actype = items[8];
            String license = items[9];
            String vehicle = items[10];
            String statuss = items[11];
            String lat = "n/a";
            String lon = "n/a";
            String balance = "0";
            String advance = "0";
            String paypal = items[12];
            String session = "0";
            String sessionWith = "0";
            String sessionArrived = "0";
            String sessionCompleted = "0";
            String paymentAmount = "0";
            String paymentStatus = "0";
            // Toast.makeText(context, phone, Toast.LENGTH_LONG).show();
            Cursor res = userDb.getUserData();
            if (res.getCount() == 0) {
                // Toast.makeText(context, "==0", Toast.LENGTH_LONG).show();
                boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus);
                if (isInserted == true) {
                    Log.e("INSERT BHAYO",String.valueOf(lat));
                    //Toast.makeText(context, "Insert success", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, MapsActivity.class);
                    context.startActivity(intent);
                }
            }else{
                //Toast.makeText(context, ">1", Toast.LENGTH_LONG).show();
                while (res.moveToNext()) {
                    String id = res.getString(0);
                    Integer deleteRows = userDb.deleteUserData(id);
                    if (deleteRows > 0) {
                        boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus);
                        if (isInserted == true) {
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MapsActivity.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
        if (status.equals("fail")) {
            Toast.makeText(context, "Oops! Something is wrong in server. Please try again...", Toast.LENGTH_LONG).show();
            return;
        }
        if (status.equals("userError")) {
            Toast.makeText(context, "Username already exists.", Toast.LENGTH_LONG).show();return;
        }
        if (status.equals("phoneError")) {
            Toast.makeText(context, "Phone number already registered.", Toast.LENGTH_LONG).show();return;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}