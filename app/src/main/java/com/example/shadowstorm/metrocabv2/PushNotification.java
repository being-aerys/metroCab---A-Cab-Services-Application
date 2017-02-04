package com.example.shadowstorm.metrocabv2;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Created by Dipesh on 03/06/16.
 */
public class PushNotification extends AsyncTask<String,Void,String> {
    Context context;
    PushNotification(Context ctx) {
        context = ctx;
    }
    ProgressDialog dialog;
    Request_database reqDb; //dipesh
    Noti_db notiDb;
    User_database userDb;


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        // if(params[4] != null){
        // destToPass = params[4];
        //}
        // else {
        //    destToPass = "  ";
        /// }

        String globalDriverId,passengerIdByPhone;
        Log.e("XXXX","###");

        //Log.w("myPhonePushnotification", myphone);


        String login_url =  GlobalValues.MyAppUrl+"/metrocab/gcm/pushNoti.php";

        // ======================< CHECK NOTI >===================================================================================
        // ======================< CHECK NOTI >===================================================================================
        // ======================< CHECK NOTI >===================================================================================
        // ======================< CHECK NOTI >===================================================================================







        
        if (type.equals("checkNoti")) {
            String phone = params[1];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8");

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
                Log.e("CheckNoti",String.valueOf(result));
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //==============================================get passen corrent lat from driver side
        if (type.equals("getPassengerCurrentLat")) {
            String passengerPhone = params[1];
            String myPhone = params[2];//driver phone

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("myPhone", "UTF-8") + "=" + URLEncoder.encode(myPhone, "UTF-8");

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

        // ======================< CANCEL REQUEST >===================================================================================
        // ======================< CANCEL REQUEST >===================================================================================
        // ======================< CANCEL REQUEST >===================================================================================
        // ======================< CANCEL REQUEST >===================================================================================

        if (type.equals("cancelReq")) {
            String cancel_accept = params[1];//--special request to delete accepted request
            String phone = params[2];//--PASSENGER
            String myphone = params[3];//---DRIVER
            Log.e("omsir","---cancel_accept "+cancel_accept+" passPhone "+phone+"driverPhone "+myphone  );
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"
                        + URLEncoder.encode("cancel_accept", "UTF-8") + "=" + URLEncoder.encode(cancel_accept, "UTF-8");

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


        // ======================< SEND REQUEST >===================================================================================
        // ======================< SEND REQUEST >===================================================================================
        // ======================< SEND REQUEST >===================================================================================
        // ======================< SEND REQUEST >===================================================================================

        if (type.equals("sendReq")) {
            String id = params[1];
            String phone = params[2];
            String myphone = params[3];
            String destToPass = params[4] ;
            String isSession = params[4];
            Log.e("myPhone","-----------------"+myphone);
            try {

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8") + "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"
                        + URLEncoder.encode("destToPass", "UTF-8") + "=" + URLEncoder.encode(destToPass, "UTF-8");

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
                Log.e("CHECK","HERE");
                Log.e("NOTICE 1",String.valueOf(result));
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // ======================< ACCEPT REQUEST >===================================================================================
        // ======================< ACCEPT REQUEST >===================================================================================
        // ======================< ACCEPT REQUEST >===================================================================================
        // ======================< ACCEPT REQUEST >===================================================================================

        if (type.equals("acceptReq")) {
            String id = params[1];
            String phone = params[2];
            String myphone = params[3];
            String destToPass = params[4] ;
            String isSession = params[4];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"

                        + URLEncoder.encode("isSession", "UTF-8") + "=" + URLEncoder.encode(isSession, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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
        // ======================< DRIVER LAT UPDATE >===================================================================================
        // ======================< DRIVER LAT UPDATE >===================================================================================
        // ======================< DRIVER LAT UPDATE >===================================================================================
        // ======================< DRIVER LAT UPDATE >===================================================================================

        if (type.equals("driverLatUpdate")) {
            String id = params[1];
            String phone = params[2];
            String myphone = params[3];
            String destToPass = params[4] ;
            String isSession = params[4];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                myphone="12344567";
                globalDriverId=params[3];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(globalDriverId, "UTF-8");
//------------------------------------------------<>------------------------
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
        // ======================< PASSENGER LAT UPDATE >===================================================================================
        // ======================< PASSENGER LAT UPDATE >===================================================================================
        // ======================< PASSENGER LAT UPDATE >===================================================================================
        // ======================< PASSENGER LAT UPDATE >===================================================================================

        if (type.equals("passengerLatUpdate")) {
            String id = params[1];
            String phone = params[2];
            String myphone = params[3];
            String destToPass = params[4] ;
            String isSession = params[4];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                myphone="12344567";
                passengerIdByPhone=params[3];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"
                        + URLEncoder.encode("passengerIdByPhone", "UTF-8") + "=" + URLEncoder.encode(passengerIdByPhone, "UTF-8");
//------------------------------------------------<>------------------------
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

        //==========================================DRIVER SIDE accepted PASSENGER LAT UPDATE
        if (type.equals("driverSideAcceptedPassengerLat")) {

            String phone = params[1];
            Log.e("MMMMPassennumPush",phone);
            String myphone2 = params[2];
            Log.e("MMMMDriverNumPush",myphone2);


            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                //passengerIdByPhone=params[3];
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone2, "UTF-8");//+ "&"
                      //  + URLEncoder.encode("passengerIdByPhone", "UTF-8") + "=" + URLEncoder.encode(passengerIdByPhone, "UTF-8");
//------------------------------------------------<>------------------------
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
                Log.e("baini7",String.valueOf(result));
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        // ======================< SHOW PASSENGER >===================================================================================
        // ======================< SHOW PASSENGER >===================================================================================
        // ======================< SHOW PASSENGER >===================================================================================
        // ======================< SHOW PASSENGER >===================================================================================

        if (type.equals("showPassenger")) {
            String id = params[1];
            String phone = params[2];
            String myphone = params[3];
            String destToPass = params[4] ;
            String isSession = params[4];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phone, "UTF-8")+ "&"
                        + URLEncoder.encode("myphone", "UTF-8") + "=" + URLEncoder.encode(myphone, "UTF-8")+ "&"
                        + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");

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

        //======================< CANCEL PENDING REQUEST BY PASSENGER >===================================================================================
        //======================< CANCEL PENDING REQUEST BY PASSENGER >===================================================================================
        //======================< CANCEL PENDING REQUEST BY PASSENGER >===================================================================================
        //======================< CANCEL PENDING REQUEST BY PASSENGER >===================================================================================

        if (type.equals("cancelPengingRequestByPassenger")) {
            String driverPhone = params[1];
            String passengerPhone = params[2];
            String temp_type = params[3];//------------if condition arko thapna naparos vanera banako
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(temp_type, "UTF-8")+ "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8");

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
        //=============================< I AM ARRIVED >===================================================================================
        //=============================< I AM ARRIVED >===================================================================================
        //=============================< I AM ARRIVED >===================================================================================
        //=============================< I AM ARRIVED >===================================================================================

        if (type.equals("iAmArrived")) {
            String driverPhone = params[1];
            String passengerPhone = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8");

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
        //=============================< CHECK SESSION >===================================================================================
        //=============================< CHECK SESSION >===================================================================================
        //=============================< CHECK SESSION >===================================================================================
        //=============================< CHECK SESSION >===================================================================================

        if (type.equals("checkSession")) {
            String msg_to = params[1];
            String passengerPhone = params[2];
            String driverPhone = params[3];
            String yesno = params[4];
            Log.e("checkSession pushNoti","-------------------------------msgto"+msg_to);
            Log.e("checkSession pushNoti","-------------------------------passenger"+passengerPhone);
            Log.e("checkSession pushNoti","-------------------------------driver"+driverPhone);
            Log.e("checkSession pushNoti","-------------------------------yesno"+yesno);
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("msg_to", "UTF-8") + "=" + URLEncoder.encode(msg_to, "UTF-8")+ "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("YesNo", "UTF-8") + "=" + URLEncoder.encode(yesno, "UTF-8");

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

        //=============================< REACHED DESTINATION>===================================================================================
        //=============================< REACHED DESTINATION>===================================================================================
        //=============================< REACHED DESTINATION>===================================================================================
        //=============================< REACHED DESTINATION>===================================================================================

        if (type.equals("reachedDestination")) {
            String driverPhone = params[1];
            String passengerPhone = params[2];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8");

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
        //=============================< makePayment >===================================================================================
        //=============================< makePayment >===================================================================================
        //=============================< makePayment >===================================================================================
        //=============================< makePayment >===================================================================================

        if (type.equals("makePayment")) {
            String balance = params[1];
            String passengerPhone = params[2];
            String driverPhone = params[3];
            String actype = params[4];
            String status = params[5];
            Log.e("PushNotification","-------driver :"+driverPhone+" , pass phone : "+passengerPhone);
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8")+ "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passengerPhone, "UTF-8")+ "&"
                        + URLEncoder.encode("actype", "UTF-8") + "=" + URLEncoder.encode(actype, "UTF-8")+ "&"
                        + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8")+ "&"
                        + URLEncoder.encode("balance", "UTF-8") + "=" + URLEncoder.encode(balance, "UTF-8");

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
        if (type.equals("driverIsReporting")) {
            String passPhone = params[1];
            String driverPhone = params[2];
            String acType = params[3];
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passPhone, "UTF-8") + "&"
                        + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8") + "&"
                        + URLEncoder.encode("actype", "UTF-8") + "=" + URLEncoder.encode(acType, "UTF-8");
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
        if( ((Activity) context) == null)
            return;
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
                userDb = new User_database(context);
            }
        });


        // reqDb= new Request_database(context);
    }

    @Override
    protected void onPostExecute(String result) {
        try{
            dialog.cancel();

        }
        catch(Exception e){

        }
        Log.w("LogInActivity", "onResume");
        Log.e("NOTICE","HERE");

        Log.e("PushNotification", "-------------------------"+result);
        String[] itemsa = result.split("]/");
        String statusa = itemsa[0];


        // ======================< SUCCESS >===================================================================================
        // ======================< SUCCESS >===================================================================================
        // ======================< SUCCESS >===================================================================================
        // ======================< SUCCESS >===================================================================================

        if (statusa.equals("success")) {//---checkNoti
            String status = itemsa[1];
            String[] items = status.split("]");

            String[] salute = new String[10];
            String[] fname = new String[10];
            String[] lname = new String[10];
            String[] contact = new String[10];
            String[] statuss = new String[10];
            String[] lat = new String[10];
            String[] lon = new String[10];
            String[] deviceid = new String[10];
            Log.e("NOTICE",String.valueOf(deviceid));

            String[] driverid = new String[10];
            String[] msg = new String[10];
            String[] desti = new String[10];
            String[] isCompleted = new String[10];
            String[] paymentAmount = new String[10];
            String[] paymentStatus = new String[10];
            //yo loop banauna sarai garho bhayo
            int c = 0;
            try {
                for (int i = 0; i < 10; i++) {
                    salute[i] = items[c];
                    //seriously dont know why but the code gives an error if we use c++ instead of c+1 in the second line
                    fname[i] = items[c + 1];
                    Log.e("CheckNoti",String.valueOf(fname[i]));
                    lname[i] = items[c + 2];
                    contact[i] = items[c + 3];
                    deviceid[i] = items[c + 4];
                    msg[i] = items[c + 5];
                    lat[i] = items[c + 6];
                    lon[i] = items[c + 7];
                    driverid[i] = items[c + 8];//---it means phone
                    statuss[i] = items[c + 10];
                    desti[i] = items[c + 11];
                    isCompleted[i] = items[c + 12];
                    paymentAmount[i] = items[c + 13];
                    paymentStatus[i] = items[c + 14];
                    c = c + 15;
                    Log.e("CHECK ",String.valueOf(fname));

                    boolean isInserted = notiDb.insertNotiData(salute[i], fname[i], lname[i], contact[i], deviceid[i], msg[i], lat[i], lon[i], driverid[i],"n/a", statuss[i],isCompleted[i],desti[i],paymentAmount[i],paymentStatus[i]);
                    if (isInserted == true) {
                        Log.e("PushNotification", "----------------------------------checkNoti insert vayo "+i+" ota notification");
                        if(i>9) {
                            Toast.makeText(context, "Notification Updated.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, CheckNotification.class);
                            context.startActivity(intent);
                        }
                    } else {
                        Log.e("PushNotification","----------------------------------checkNoti insert vayena "+i+" ota notification");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        // ======================< CAB REQUEST SENT >===================================================================================
        // ======================< CAB REQUEST SENT >===================================================================================
        // ======================< CAB REQUEST SENT >===================================================================================
        // ======================< CAB REQUEST SENT >===================================================================================

        if (statusa.equals("CabRequestSent")) {
            String msg = itemsa[1];
            String driiverPhone = itemsa[2];
            String passPhone = itemsa[5];
            boolean isUpdated = userDb.updateUserSessonOnOff(passPhone,"0",driiverPhone);
            if (isUpdated == true) {
                Log.e("pushNoti CabRequestSent","--------------------------- user_db updated with sessionWith");
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Cab Request")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlert.show();
            }else{
                Log.e("pushNoti CabRequestSent","--------------------------- user_db not updated with sessionWith");

            }
        }
        // ======================< CANCEL PENDING REQUEST BY PASSENGER >==============================================================
        // ======================< CANCEL PENDING REQUEST BY PASSENGER >==============================================================
        // ======================< CANCEL PENDING REQUEST BY PASSENGER >==============================================================
        // ======================< CANCEL PENDING REQUEST BY PASSENGER >==============================================================

        if (statusa.equals("cancelPengingRequestByPassenger")) {
            //String msg = itemsa[1];
            Log.e("CBRBP pushnoti","--------------received");
            String status = itemsa[2];
            String passPhone = itemsa[3];
            Log.e("pending/accepted","----------------------------"+status);
            GlobalValues.isSessionOn=false;
            //int i= 3;
            String msg= String.valueOf(itemsa[1]);
            if(status.equals("0")) {
                boolean isUpdated = userDb.updateUserSessonComplete(passPhone);
                if (isUpdated == true) {
                    Log.e("cancelByPassenger", "----------------------------if pending ma gayo and update vayo");
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(context, MapsActivity.class);
                                    context.startActivity(intent);
                                }
                            })
                            .setTitle("Request")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                }else{
                    Log.e("cancelByPassenger", "----------------------------if pending ma gayo and update vayena");
                }
            }else if(status.equals("1")){
                Log.e("cancelByPassenger","----------------------------if accepted ma gayo");
                final String previous_driver_id = itemsa[3];
                String cancel_button_text = itemsa[4];
                final String previous_driver_name = itemsa[5];
                final String pass_phone = itemsa[6];
                AlertDialog.Builder myAlerta = new AlertDialog.Builder(context);
                myAlerta.setMessage("You'll be charged with 20% of the fare if cancelled at this instance.")
                        .setPositiveButton(cancel_button_text,new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                Log.e("driver cancel accept","-------------------"+previous_driver_id+' '+previous_driver_name);
                                //cancelPendingRequestByPassenger
                                //driverphone
                                //passengerphone
                                //temmp_type
                                PushNotification backgroundWorkers = new PushNotification(context);
                                backgroundWorkers.execute("cancelPengingRequestByPassenger", previous_driver_id, pass_phone,"cancelAcceptedRequestByPassenger");
                            }
                        })
                        .setNegativeButton("EXIT",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                Log.e("driver cancel cancel","-------------------"+previous_driver_id+' '+previous_driver_name);
                            }
                        })
                        .setNeutralButton("POLICY",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                Log.e("Policy","-------------------"+previous_driver_id+' '+previous_driver_name);
                            }
                        })
                        .setTitle("Cab Request Cancel")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlerta.show();

            }else{
                Log.e("cancelByPassenger", "----------------------------if pending ma gayo and update vayo");
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, MapsActivity.class);
                                context.startActivity(intent);
                            }
                        })
                        .setTitle("Request")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlert.show();
            }
        }
        // ======================< DOUBLE REQUEST >===================================================================================
        // ======================< DOUBLE REQUEST >===================================================================================
        // ======================< DOUBLE REQUEST >===================================================================================
        // ======================< DOUBLE REQUEST >===================================================================================

        if (statusa.equals("DoubleRequest")) {
            String msg = itemsa[1];
            final String previous_driver_id = itemsa[2];
            String cancel_button_text = itemsa[3];
            final String previous_driver_name = itemsa[4];
            final String passenger_phone = itemsa[5];
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage(msg)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(cancel_button_text,new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            Log.e("previous driver phone","-------------------"+previous_driver_id);
                            AlertDialog.Builder myAlerta = new AlertDialog.Builder(context);
                            myAlerta.setMessage("Are you sure you want to cancel request to "+previous_driver_name+" ?")
                                    .setPositiveButton("CONTINUE",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                            Log.e("driver cancel pending","-------------------"+previous_driver_id+' '+previous_driver_name);

                                            PushNotification backgroundWorkers = new PushNotification(context);
                                            backgroundWorkers.execute("cancelPengingRequestByPassenger", previous_driver_id, passenger_phone,"cancelPengingRequestByPassenger");
                                        }
                                    })
                                    .setNegativeButton("EXIT",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                            Log.e("driver cancel cancel","-------------------"+previous_driver_id+' '+previous_driver_name);
                                        }
                                    })
                                    .setTitle("Cab Request Cancel")
                                    .setIcon(R.drawable.myridec1)
                                    .create();
                            myAlerta.show();
                        }
                    })
                    .setTitle("Cab Request")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // ======================< DRIVER LAT LANG >===================================================================================
        // ======================< DRIVER LAT LANG >===================================================================================
        // ======================< DRIVER LAT LANG >===================================================================================
        // ======================< DRIVER LAT LANG >===================================================================================


        if (statusa.equals("driverLatLng")) {

            try{
                String status = itemsa[1];

                String[] items = status.split("]");
                Log.e("AAAAAAAAAAAAAA",String.valueOf(items));


                String driverLat = items[0];
                Log.e("WHAT IS",driverLat);
                String driverLon = items[1];
                //============================================================
                GlobalValues.globalDriverLat=driverLat;
                Log.e("GLOBAL LAT SET",driverLat);
                GlobalValues.globalDriverLon=driverLon;


            }catch (Exception E){

            }

        }
        // ======================< PASSENGER LAT LANG >===================================================================================
        // ======================< PASSENGER LAT LANG >===================================================================================
        // ======================< PASSENGER LAT LANG >===================================================================================
        // ======================< PASSENGER LAT LANG >===================================================================================

        if (statusa.equals("passengerLatLng")) {

            String status = itemsa[1];

            String[] items = status.split("]");
            Log.e("AAAAAAAAAAAAAA",String.valueOf(items));


            String passengerLat = items[0];

            String passengerLon = items[1];
            //============================================================
            GlobalValues.driverSidePassenLat=passengerLat;

            GlobalValues.driverSidePassenLon=passengerLon;


        }
        //=======================
        if (statusa.equals("acceptedPassengerLatLng")) {

            String status = itemsa[1];

            String[] items = status.split("]");
            Log.e("BBBB",String.valueOf(items));


            String passengerLat1 = items[0];

            String passengerLon1 = items[1];
            String driverLat1 = items[2];
            String driverLon1 = items[3];
            //============================================================
            Log.e("KUN","1");

            GlobalValues.driverSidePassenLat=passengerLat1;
            GlobalValues.driverSidePassenLon=passengerLon1;
            Log.e("MMMMMMpushNotiLat",GlobalValues.driverSidePassenLat);
            Log.e("MMMMMMpushNotiLon",GlobalValues.driverSidePassenLon);

            Log.e("AcceptedPassen",GlobalValues.driverSidePassenLat);


            GlobalValues.driverSidePassenLon=passengerLon1;
            GlobalValues.globalDriverLat = driverLat1;
            Log.e("bainiDriver",driverLat1);
            GlobalValues.globalDriverLon = driverLon1;
            Log.e("bainiDriverLon",driverLon1);



        }



        // ======================< ERROR >===================================================================================
        // ======================< ERROR >===================================================================================
        // ======================< ERROR >===================================================================================
        // ======================< ERROR >===================================================================================
        // ======================< ERROR >===================================================================================

        if (statusa.equals("error")) {
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog myAlert = new AlertDialog.Builder(context).create();
            myAlert.setMessage("You do not have any request received yet.");
                    /*.setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();}
                    })
                    .setTitle("Request")
                    .setIcon(R.drawable.myridec)
                    .create();*/
            myAlert.setTitle("Request");
            //myAlert.dismiss();
            //myAlert.show();
        }

        // ======================< ACCEPTED >===================================================================================
        // ======================< ACCEPTED >===================================================================================
        // ======================< ACCEPTED >===================================================================================
        // ======================< ACCEPTED >===================================================================================


        if(statusa.equals("accepted")){  //although returned string "accepted" it is actually cancelled and thus sets status busy
            String status = itemsa[1];
            String driverPhone = itemsa[2];
            String passPhone = itemsa[3];
            Log.e("ZZZZ",status);

            boolean update = notiDb.updateNotiData(passPhone,"Accepted");
            Log.e("HERE ",String .valueOf(update));
            if(update == true){
                GlobalValues.driverLaiSessionOn=true;//================session on gareko for backPress disabling on CheckNoti activity
                Log.e("pushNoti","------------status changed to ACCEPTED");
            }else{
                Log.e("pushNoti","------------status not changed");
            }

        }
        // ======================< ERRORS >===================================================================================
        // ======================< ERRORS >===================================================================================
        // ======================< ERRORS >===================================================================================
        // ======================< ERRORS >===================================================================================

        if (statusa.equals("errors")) {
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("Database Uploaded. Try again")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();}
                    })
                    .setTitle("Request")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // ======================< DRIVER REQUEST CANCEL BEFORE ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL BEFORE ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL BEFORE ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL BEFORE ACCEPT >===================================================================================

        if (statusa.equals("driverRequestCancelBeforeAccept")) {
            String msg = itemsa[1];
            String cancel_status=itemsa[2];
            String pass_phone = itemsa[3];

            if(cancel_status.equals("success")) {
                String driver_phone = itemsa[4];
                Integer deleteRows = notiDb.deleteNotiDataq(pass_phone);
                Log.e("checkNoti", String.valueOf(notiDb.getNotiData()));
                if (deleteRows > 0) {
                    GlobalValues.driverLaiSessionOn=false;

                    Log.e("ZZZZZ", String.valueOf(deleteRows));
                    boolean isUpdated = userDb.updateStatus(driver_phone,"FREE");
                    if(isUpdated == true){
                        Log.e("driver status : ", "-------------------------- FREE");
                        // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                        myAlert.setMessage(msg)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(context, DriverPge.class);
                                        context.startActivity(intent);
                                    }
                                })
                                .setTitle("Cancel Cab Request")
                                .setIcon(R.drawable.myridec1)
                                .create();
                        myAlert.show();
                        GlobalValues.isSessionOn = false;
                    }else{
                        Log.e("driver status : ", "---------------------- CANNOT CHANGE");
                    }
                } else {
                    Toast.makeText(context, "SQLite already refreshed. Only UI refresh incomplete.Continue with your task.", Toast.LENGTH_SHORT).show();
                }
            }else{
                // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage(msg)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent intent = new Intent(context, DriverPge.class);
                                context.startActivity(intent);
                            }
                        })
                        .setTitle("Cancel Cab Request")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlert.show();
            }
        }
        // ======================< DRIVER REQUEST CANCEL AFTER ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL AFTER ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL AFTER ACCEPT >===================================================================================
        // ======================< DRIVER REQUEST CANCEL AFTER ACCEPT >===================================================================================

        if (statusa.equals("driverRequestCancelAfterAccept")) {
            String msg = itemsa[1];
            final String driver_phone = itemsa[2];
            final String pass_phone = itemsa[3];


             Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("If you cancel th session now,20% of the fare will be deducted from your account and tranferred to the passenger's account.")
                    .setPositiveButton("CONTINUE",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            Log.e("driver cancel accepted","-------------------"+driver_phone+' '+pass_phone);

                            //==========================
                                PushNotification backgroundWorkers = new PushNotification(context);
                                backgroundWorkers.execute("cancelReq", "1", pass_phone,driver_phone);



                        }
                    })
                    .setNegativeButton("EXIT",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();}
                    })
                    .setNeutralButton("POLICY",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            Log.e("Driver calcel","---------------------policy");
                        }
                    })
                    .setTitle("Request")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
            //GlobalValues.isSessionOn = false;

        }
        // =============================< I AM ARRIVED >===================================================================================
        // =============================< I AM ARRIVED >===================================================================================
        // =============================< I AM ARRIVED >===================================================================================
        // =============================< I AM ARRIVED >===================================================================================

        if (statusa.equals("iAmArrived")) {
            String msg = itemsa[1];
            String driver_phone = itemsa[2];
            if(!driver_phone.equals("error")) {
                Log.e("iAmArrived", "----------------------------" + msg);
                boolean isUpdated = userDb.updateUserSessonArrived(driver_phone, "1");
                if (isUpdated == true) {
                    Log.e("DBUpdated arrived : ", "1");
                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            }
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage(msg)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();}
                    })
                    .setTitle("Cab Request")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // =============================< Reached Destination >===================================================================================
        // =============================< Reached Destination >===================================================================================
        // =============================< Reached Destination >===================================================================================
        // =============================< Reached Destination >===================================================================================

        if (statusa.equals("reachedDestination")) {
            String msg = itemsa[1];
            String msga = itemsa[2];
            String driver_phone = itemsa[3];
            Log.e("iAmArrived","----------------------------"+msg);
            boolean isUpdated = userDb.updateUserSessonComplete(driver_phone, "1");
            if (isUpdated == true) {
                Log.e("DBUpdated arrived : ", "1");
            } else {
                Log.e("DBUpdated with msg : ", "----------------------------not updated");
            }
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage(msg+"\n"+msga)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();}
                    })
                    .setTitle("Metrocab")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // =============================< MESSAGE >===================================================================================
        // =============================< MESSAGE >===================================================================================
        // =============================< MESSAGE >===================================================================================
        // =============================< MESSAGE >===================================================================================

        if (statusa.equals("checkSession")) {
            final String driver_phone = itemsa[1];
            final String pass_phone = itemsa[2];
            String msg = itemsa[3];
            String msga = itemsa[4];
            Log.e("checkSesson","----------------------------"+msg+" "+msga);
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage(msg+"\n"+msga)
                    .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            PushNotification backgroundWorkers = new PushNotification(context);
                            backgroundWorkers.execute("checkSession","Driver", pass_phone,driver_phone,"Yes");
                        }
                    })
                    .setNegativeButton("NO",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                            PushNotification backgroundWorkers = new PushNotification(context);
                            backgroundWorkers.execute("checkSession","Driver", pass_phone,driver_phone,"Yes");

                        }
                    })
                    .setTitle("Cab has arrived")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // =============================<
        if (statusa.equals("testMsg")) {
            String msg = itemsa[1];
            Log.e("testMsg","----------------------------"+msg);

        }
        // =============================< tripCompleted >===================================================================================
        // =============================< tripCompleted >===================================================================================
        // =============================< tripCompleted >===================================================================================
        // =============================< tripCompleted >===================================================================================

        if (statusa.equals("tripCompleted")) {
            String msg = itemsa[1];
            String msga = itemsa[2];
            Log.e("checkSesson","----------------------------"+msg+" "+msga);
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage(msg+"\n"+msga)
                    .setPositiveButton("CLOSE",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Reached Destination")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
        // =============================< paymentSuccess >===================================================================================
        // =============================< paymentSuccess >===================================================================================
        // =============================< paymentSuccess >===================================================================================
        // =============================< paymentSuccess >===================================================================================

        if (statusa.equals("paymentSuccess")) {
            String acType = itemsa[1];
            String msg = itemsa[2];
            String amt = itemsa[3];
            String passPhone = itemsa[4];
            String status = itemsa[5];
            if(acType.equals("driver")){
                boolean isUpdated =notiDb.updateUserPayment(passPhone,amt,status);
                if (isUpdated == true) {
                    Log.e("DBUpdated arrived : ", "1");
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg)
                            .setPositiveButton("CLOSE",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Payment Request")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();

                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            }else{
                boolean isUpdated = userDb.updateUserPayment(passPhone,amt,status);
                if (isUpdated == true) {
                    Log.e("DBUpdated arrived : ", "1");
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg)
                            .setPositiveButton("CLOSE",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which){
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Payment Request")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            }

            Log.e("paymentSuccess","-------------------------------------------------------received with msg: "+msg+" amount: "+amt);


        }
        // =============================< driverIsReporting >===================================================================================
        // =============================< driverIsReporting >===================================================================================
        // =============================< driverIsReporting >===================================================================================
        // =============================< driverIsReporting >===================================================================================

        if (statusa.equals("reportBalanceAdded")) {
            String msg = itemsa[1];
            String msga = itemsa[2];
            String passPhone = itemsa[3];
            String driverPhone = itemsa[4];
            String acType = itemsa[5];

            if (acType.equals("driver")) {
                Log.e("reportBalanceAdded", "----------------------- hahahahaha vayo" + passPhone);
                Integer deleteRows = notiDb.deleteNotiDataq(passPhone);
                if (deleteRows > 0) {
                    GlobalValues.driverLaiSessionOn=false;
                    boolean isUpdated = userDb.updateStatus(driverPhone,"FREE");
                    if(isUpdated == true){
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                        myAlert.setMessage(msg + "\n" + msga)
                                .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent intent = new Intent(context, DriverPge.class);
                                        context.startActivity(intent);
                                    }
                                })
                                .setTitle("Metrocab")
                                .setIcon(R.drawable.myridec1)
                                .create();
                        myAlert.show();
                    }else{
                        Toast.makeText(context, "Cant update status", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Cant delete notification", Toast.LENGTH_SHORT).show();
                }
            }else{
                boolean isUpdated = userDb.updateUserSessonComplete(passPhone);
                if (isUpdated == true) {
                    GlobalValues.isCompleted = 1;
                    GlobalValues.isSessionOn = false;
                    Log.e("reportBlanace", "----------------------------------user_db update vayo");

                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg + "\n" + msga)
                            .setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            })
                            .setTitle("Metrocab")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                } else {
                    Log.e("reportBlanace", "----------------------------------user_db update vayo");
                }
            }

        }


    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

