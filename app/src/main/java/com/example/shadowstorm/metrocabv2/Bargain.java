package com.example.shadowstorm.metrocabv2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
 * Created by Shadow Storm on 7/14/2016.
 */

public class Bargain  extends AsyncTask<String,Void,String> {
    Context context;

    Bargain(Context ctx) {
        context = ctx;
    }

    ProgressDialog dialog;
    Request_database reqDb;
    Noti_db notiDb;
    User_database userDb;


    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = GlobalValues.MyAppUrl + "/metrocab/bargain.php";

        // ======================< BARGAIN By PASSENGER>===================================================================================

        if (type.equals("bargainByPass")) {
            String passPhone = params[1];
            String driverPhone = params[2];
            String amount = params[3];
            String status = params[4];
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
                        + URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&"
                        + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");

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

        // ======================< BARGAIN By Driver>===================================================================================

        if (type.equals("bargainByDriver")) {
            String passPhone = params[1];
            String driverPhone = params[2];
            String amount = params[3];
            String status = params[4];
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
                        + URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&"
                        + URLEncoder.encode("status", "UTF-8") + "=" + URLEncoder.encode(status, "UTF-8");

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
        //Code for the UiThread
        notiDb= new Noti_db(context);
        userDb = new User_database(context);

        /*dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait ...");
        dialog.setTitle("Metrocab");
        dialog.setIcon(R.drawable.myridec);
        dialog.setCancelable(true);
        dialog.show();*/

        // reqDb= new Request_database(context);
    }

    @Override
    protected void onPostExecute(String result) {
        //dialog.cancel();
        Log.e("Bargin result", "--------" + result);

        String[] itemsa = result.split("]/");
        String statusa = itemsa[0];

        if(statusa.equals("bargainByPass")){
            String msg = itemsa[1];
            String amount = itemsa[2];
            String passPhone = itemsa[3];
            String status = itemsa[4];
            boolean isUpdated = userDb.updateUserBalance(passPhone,amount,status);
            if (isUpdated == true) {
                Log.e("Bargaain", "----------------------------------user_db update vayo");
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage(msg)
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Request")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlert.show();

            } else {
                Log.e("Bargaain", "----------------------------------user_db update vayena");
            }
        }
        if(statusa.equals("bargainByDriver")){
            String msg = itemsa[1];
            String amount = itemsa[2];
            String passPhone = itemsa[3];
            String status = itemsa[4];
            String status_b = itemsa[5];
            if(status_b.equals("accept")){
                boolean isUpdated = notiDb.updateNotiData(passPhone,"Accepted");
                if (isUpdated == true) {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayo");
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Request")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                }else {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayena");
                }
            }else{
                boolean isUpdated = notiDb.updateUserPayment(passPhone,amount,status);
                if (isUpdated == true) {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayo");
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage(msg)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Bargain")
                            .setIcon(R.drawable.myridec1)
                            .create();
                    myAlert.show();
                }else {
                    Log.e("Bargaain", "----------------------------------Noti_db update vayena");
                }
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}