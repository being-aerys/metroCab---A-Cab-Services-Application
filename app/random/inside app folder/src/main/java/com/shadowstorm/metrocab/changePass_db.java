package com.shadowstorm.metrocab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.telephony.SmsManager;
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
 * Created by Dipesh on 02/06/16.
 */
public class changePass_db extends AsyncTask<String,Void,String> {
    Context context;
    changePass_db(Context ctx) {
        context = ctx;
    }
    changePass_sqlite userDb;
    ProgressDialog dialog;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String phoneNum = params[1];
        String code = params[2];
        String login_url = "http://omkishan.com.np/metrocab/ChangePassword.php";
        if (!type.equals("")) {
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phoneNum, "UTF-8") + "&"
                        + URLEncoder.encode("code", "UTF-8") + "=" + URLEncoder.encode(code, "UTF-8");

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
        dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait ...");
        dialog.setTitle("Change Password");
        dialog.setIcon(R.drawable.myridec);
        dialog.show();
        dialog.setCancelable(false);
        userDb= new changePass_sqlite(context);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.cancel();
        String[] items = result.split(":");
        String status = items[0];
        // Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        if (status.equals("success")) {
            final String username= items[1];
            String salute = items[2];
            String fname = items[3];
            String lname = items[4];
            String country = items[5];
            final String phone = items[6];
            final String code = items[7];

            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("Name : "+salute+" "+fname+" "+lname+"\nCountry : "+country+"\nPhone : "+phone)
                    .setPositiveButton("This is me",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();

                            Cursor res = userDb.getCodeData();
                            if (res.getCount() == 0) {
                                // Toast.makeText(context, "==0", Toast.LENGTH_LONG).show();
                                boolean isInserted = userDb.insertCodeData(phone, code, username);
                                if (isInserted == true) {
                                    SmsManager manager = SmsManager.getDefault();
                                    manager.sendTextMessage(phone,null,code,null,null);

                                    Intent intent;
                                    intent = new Intent(context,confirmPhone.class);
                                    context.startActivity(intent);
                                }
                            }else{
                                //Toast.makeText(context, ">1", Toast.LENGTH_LONG).show();
                                while (res.moveToNext()) {
                                    String id = res.getString(0);
                                    Integer deleteRows = userDb.deleteCodeData(id);
                                    if (deleteRows > 0) {
                                        boolean isInserted = userDb.insertCodeData(phone, code, username);
                                        if (isInserted == true) {
                                            SmsManager manager = SmsManager.getDefault();
                                            manager.sendTextMessage(phone,null,code,null,null);

                                            Intent intent;
                                            intent = new Intent(context,confirmPhone.class);
                                            context.startActivity(intent);
                                        } else {
                                            Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            SmsManager manager = SmsManager.getDefault();
                            manager.sendTextMessage(phone,null,code,null,null);

                            Intent intent;
                            intent = new Intent(context,confirmPhone.class);
                            context.startActivity(intent);
                        }
                    })
                    .setNegativeButton("This is Not me",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Confirm account")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();

        }
        if (status.equals("fail")) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("This number is not registered. Try again")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("myRide Amdin")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();
        }
        if (status.equals("error")) {
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("User is not registered.")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("myRide Amdin")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();
        }
        if (status.equals("changed")) {
            Intent intent = new Intent(context,LogInActivity.class);
            context.startActivity(intent);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
