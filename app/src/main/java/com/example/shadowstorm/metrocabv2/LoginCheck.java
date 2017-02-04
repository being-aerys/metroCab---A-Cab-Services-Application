package com.example.shadowstorm.metrocabv2;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by Dipesh on 11/05/16.
 */
public class LoginCheck extends AsyncTask<String,Void,String> {
    Context context;
    LoginCheck(Context ctx) {
        context = ctx;
    }
    User_database userDb;
    ProgressDialog dialog;
    ProgressDialog dialogb;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];

                String login_url = GlobalValues.MyAppUrl+"/metrocab/test.php";
                Log.e("Login URL","--------------------"+login_url);
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");//post this to php file
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.e("Login result","--------------------"+result);
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(type.equals("changeStatus")) {
            try {
                String phone = params[1];
                String status = params[2];

                String login_url =  GlobalValues.MyAppUrl+"/metrocab/changeStatus.php";
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");//post this to php file
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"
                        +URLEncoder.encode("status","UTF-8")+"="+URLEncoder.encode(status,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result="";
                String line="";
                while((line = bufferedReader.readLine())!= null) {
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
        // Toast.makeText(context,"Data Inserted",Toast.LENGTH_LONG).show();
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Login Status");
        userDb= new User_database(context);
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait ...");
        dialog.setTitle("Metrocab");
        dialog.setIcon(R.drawable.myridec1);
        dialog.setCancelable(true);
        dialog.show();

    }

    @Override
    protected void onPostExecute(String result) {
        dialog.cancel();

        //====================


        //=====================
        Log.e("login response  = ", result);
        // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        String[] items = result.split(":");
        String status = items[0];
        if (status.equals("success")) {

            Log.e("LOGIN","HERE");

            dialogb = new ProgressDialog(context);
            dialogb.setMessage("Loading ...");
            dialogb.setTitle("Welcome");
            dialog.setIcon(R.drawable.myridec1);
            dialogb.setCancelable(false);
            dialogb.show();

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
            String lat = items[12];
            String lon = items[13];
            String balance = items[14];
            String advance = items[15];
            String paypal = items[16];
            String session = items[17];
            String sessionWith = items[18];
            String sessionArrived = items[19];
            String sessionCompleted = items[20];
            String paymentAmount = items[21];
            String paymentStatus = items[22];
            //Toast.makeText(context, phone, Toast.LENGTH_LONG).show();
            Cursor res = userDb.getUserData();
            if (res.getCount() == 0) {

                Log.e("LOGIN","HERE");

                // Toast.makeText(context, "==0", Toast.LENGTH_LONG).show();
                boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus);
                if (isInserted == true) {
                    dialogb.cancel();
                    //SHARED PREFERENCE---------------------------------------------

                    SaveSharedPreference.setUserName(context,username);
                    Log.e("LOGIN","HERE");

                    if(actype.equals("Driver")) {
                       /* if(session.equals("1")){
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, CheckNotification.class);
                            //intent.putExtra("session",session);
                            //intent.putExtra("pessenger",phone);
                            context.startActivity(intent);
                        }else{*/
                        //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context, DriverPge.class);
                        context.startActivity(intent);
                        //}
                    }else{
                        if(session.equals("1")){
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, Message.class);
                            //intent.putExtra("session",session);
                            //intent.putExtra("pessenger",phone);
                            context.startActivity(intent);
                        }else{
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MapsActivity.class);
                            context.startActivity(intent);
                        }
                    }
                } else {
                    dialogb.cancel();
                    //Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage("Server error. Please try again ...")
                            .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("myRide")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                }
            }else{
                //Toast.makeText(context, ">1", Toast.LENGTH_LONG).show();
                while (res.moveToNext()) {
                    String id = res.getString(0);
                    Integer deleteRows = userDb.deleteUserData(id);
                    if (deleteRows > 0) {

                        boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus);
                        if (isInserted == true) {
                            dialogb.cancel();
                            //SHARED PREFERENCE---------------------------------------------

                            Log.e("PREFER",username);
                            Log.e("PREFER","");
                            SaveSharedPreference.setUserName(context,username);
                            Log.e("LOGIN","HERE");
                            if(actype.equals("Driver")) {
                                if(session.equals("1")){
                                    GlobalValues.driverLaiSessionOn=true;
                                    //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, CheckNotification.class);
                                    //intent.putExtra("session",session);
                                    //intent.putExtra("pessenger",phone);
                                    context.startActivity(intent);
                                }else{
                                    GlobalValues.driverLaiSessionOn=false;
                                    //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, DriverPge.class);
                                    context.startActivity(intent);
                                }
                            }else{
                                if(session.equals("1")){
                                    //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, Message.class);
                                    //intent.putExtra("session",session);
                                    //intent.putExtra("pessenger",phone);
                                    context.startActivity(intent);
                                }else{
                                    //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(context, MapsActivity.class);
                                    context.startActivity(intent);
                                }
                            }
                        } else {
                            dialogb.cancel();
                            //Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                            myAlert.setMessage("Server error. Please try again ...")
                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle("myRide")
                                    .setIcon(R.drawable.myridec1)
                                    .create();
                            myAlert.show();
                        }
                    }
                }
            }
        }
        if (status.equals("changed")) {

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
            String lat = items[12];
            String lon = items[13];
            String balance = items[14];
            String advance = items[15];
            String paypal = items[16];
            String session = items[17];
            String sessionWith = items[18];
            String sessionArrived = items[19];
            String sessionCompleted = items[20];
            String paymentAmount = items[21];
            String paymentStatus = items[22];
            //Toast.makeText(context, phone, Toast.LENGTH_LONG).show();
            Cursor res = userDb.getUserData();
            if (res.getCount() == 0) {
                // Toast.makeText(context, "==0", Toast.LENGTH_LONG).show();

                boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus); if (isInserted == true) {
                    Toast.makeText(context, "Status changed to "+statuss, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Oops cant change status", Toast.LENGTH_SHORT).show();
                }
            }else{
                //Toast.makeText(context, ">1", Toast.LENGTH_LONG).show();
                while (res.moveToNext()) {
                    String id = res.getString(0);
                    Integer deleteRows = userDb.deleteUserData(id);
                    if (deleteRows > 0) {

                        boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss,lat,lon,balance,advance,paypal,session,sessionWith,sessionArrived,sessionCompleted,paymentAmount,paymentStatus);
                        if (isInserted == true) {
                            Toast.makeText(context, "Status changed to "+statuss, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Oops cant change status", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
        if (status.equals("fail")) {
            //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("Invalid Login")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    //.setTitle("Welcome")
                    //.setIcon(R.drawable.name)
                    .create();
            myAlert.show();

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}

