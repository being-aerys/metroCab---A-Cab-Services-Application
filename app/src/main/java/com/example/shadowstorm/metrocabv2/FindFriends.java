package com.example.shadowstorm.metrocabv2;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
 * Created by Shadow Storm on 5/26/2016.
 */
public class FindFriends extends AsyncTask<String,Void,String> {
    Context context;
    FindFriends(Context ctx) {
        context = ctx;
    }
    DriversLocation_db userDb;
    ProgressDialog dialog;



    @Override
    protected String doInBackground(String... params) {

        String type = params[0];

        String login_url =  GlobalValues.MyAppUrl+"/metrocab/search_friends.php";
        String highlight = params[0];
        Log.e("HIGH",String.valueOf(params[0]));
        if(type.equals("search")) {
            try {
                Log.e("CUR","HERE");
                String user_name = params[1];
                String lat = params[2];
                String lon = params[3];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");//post this to php file
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("user_name", "UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"
                        +URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8")+"&"
                        +URLEncoder.encode("lon","UTF-8")+"="+URLEncoder.encode(lon,"UTF-8");
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
                Log.e("CUR0",String.valueOf("cur0"));
                Log.e("CUR1",result);
                Log.e("CUR2",String.valueOf("cur2"));

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if(highlight.equals("highlight")){
            Intent intent = new Intent(context,CheckNotification.class);
            intent.putExtra("KEY1",highlight);
            return null;





        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        // Toast.makeText(context,"Data Inserted",Toast.LENGTH_LONG).show();
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Login Status");
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Locating the drivers...");
        dialog.setTitle("Cabs Search");
        dialog.setIcon(R.drawable.myridec1);
        dialog.setCancelable(true);
        dialog.show();
        userDb= new DriversLocation_db(context);//new sqlite db banayo to give drivers rows values returned from web server db
    }

    @Override
    protected void onPostExecute(String result) {

        Log.e("findFriend onPost","---------------------------------Got driver from server");

        /*Log.e("see",result);
        Toast.makeText(context,result,Toast.LENGTH_LONG).show();*/

        Cursor res = userDb.getDriverData(); //server bata aayeko reply leko cursor ma aba tala
        if (res.getCount() > 0) {
            Integer deleteRows = userDb.deleteDriverData();
            if (deleteRows > 0) {
                Log.e("findFriend onPost","---------------------------------old driver list deleted");
                Log.e("DIPESH1.1",String.valueOf(result));
                record_driver_data(result);
            }
        }else{
            Log.e("DIPESH1",String.valueOf(result));
            record_driver_data(result);
        }
    }

    private void record_driver_data (String result){
        Log.e("findFriend record_data","---------------------------------"+result);
        Log.e("findFriend record_data","---------------------------------recording driver list");

        Log.e("NABIN",String.valueOf(result));
        String[] parts = result.split("]");
        int totalRows = Integer.parseInt(parts[0]);
        if (totalRows != 0) {
            String otherRows = parts[1];

            String[] items = otherRows.split(":");

            //String[] myStringArray = new String[3];
            String[] salute = new String[10];
            String[] fname = new String[10];
            String[] lname = new String[10];
            String[] country = new String[10];
            String[] phone = new String[10];
            String[] actype = new String[10];
            String[] license = new String[10];
            String[] vehicle = new String[10];
            String[] status = new String[10];
            String[] lat = new String[10];
            String[] lon = new String[10];
            String[] dist = new String[10];
            String[] driverIdFromServer = new String[10];
            //yo loop banauna sarai garho bhayo
            int c = 0;

            try {    //10 ota driver k
                for (int i = 0; i < 10; i++) {
                    salute[i] = items[c];
                    //seriously dont know why but the code gives an error if we use c++ instead of c+1 in the second line
                    fname[i] = items[c + 1];
                    lname[i] = items[c + 2];
                    country[i] = items[c + 3];
                    phone[i] = items[c + 4];
                    actype[i] = items[c + 5];
                    license[i] = items[c + 6];
                    vehicle[i] = items[c + 7];
                    status[i] = items[c + 8];
                    lat[i] = items[c + 9];
                    lon[i] = items[c + 10];
                    dist[i] = items[c + 11];
                    driverIdFromServer[i]= items[c+12];
                    c = c + 13;

                    boolean isInserted = userDb.insertDriverData(salute[i], fname[i], lname[i], country[i], phone[i], actype[i], license[i], vehicle[i], status[i], lat[i], lon[i], dist[i],driverIdFromServer[i]);
                    if (isInserted == true) {
                        Log.e("findFriend record_data","---------------------------------recorded driver"+i);
                    } else {
                        //Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                        Log.e("findFriend record_data","---------------------------------error recording driver"+i);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //aba for precaution check gareko sab driver  server bata retrieve bhayo ki bhayena bhanera
            Cursor ress = userDb.getDriverData();
            if (ress.getCount() == totalRows) {
                dialog.cancel();
                Log.e("findFriend record_data","----------------------server data vs sqlite data ="+ress.getCount()+"/"+totalRows);
                Intent intent = new Intent(context, DriversMap.class);
                context.startActivity(intent);
            } else {
                dialog.dismiss();
                Log.e("findFriend record_data","----------------------server data vs sqlite data ="+ress.getCount()+"/"+totalRows);
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage("Cant load all cabs")
                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        //.setTitle("Welcome")
                        //.setIcon(R.drawable.name)
                        .create();
                myAlert.show();
            }
        }else{
            dialog.cancel();
            Log.e("findFriend record_data","----------------------total driver found in server is "+totalRows);
            AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
            myAlert.setMessage("Drivers not available in your area")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Driver search")
                    .setIcon(R.drawable.myridec1)
                    .create();
            myAlert.show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



}

