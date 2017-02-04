package com.example.shadowstorm.metrocabv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
 * Created by Dipesh on 02/08/16.
 */
public class Transaction extends AsyncTask<String,Void,String> {
    Context context;
    Transaction(Context ctx) {
        context = ctx;
    }
    User_database userDb;
    ProgressDialog dialog;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url =  GlobalValues.MyAppUrl  +"/metrocab/transaction.php";
        String login_url2 =  GlobalValues.MyAppUrl  +"/metrocab/gcm/pushNoti.php";
        if (type.equals("buy_money")) {
            String amount = params[1];
            String user = params[2];
            String desc = "Balance purchased";
            Log.e("trans1",String.valueOf(amount));
            Log.e("trans2",String.valueOf(user));
            //Log.e("trans1",String.valueOf(amount));
            Log.e("paypal Transaction","------------"+amount+", "+user+","+desc);
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                        + URLEncoder.encode("amount", "UTF-8") + "=" + URLEncoder.encode(amount, "UTF-8") + "&"
                        + URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(user, "UTF-8") + "&"
                        + URLEncoder.encode("desc", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8");
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
        dialog.setMessage("Please wait updating balance ...");
        dialog.setTitle("Balance Low");
        dialog.setIcon(R.drawable.myridec1);
        dialog.show();
        dialog.setCancelable(false);
    }
    @Override
    protected void onPostExecute(String result) {
        dialog.cancel();
        Log.e("balance update","------------------"+result);
        Intent intent = new Intent(context,LogInActivity.class);
        context.startActivity(intent);
    }
}