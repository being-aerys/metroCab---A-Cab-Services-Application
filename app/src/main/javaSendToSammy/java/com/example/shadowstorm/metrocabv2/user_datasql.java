package com.example.shadowstorm.metrocabv2;


        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.net.Uri;
        import android.os.AsyncTask;
        import android.support.v4.app.NotificationCompat;
        import android.support.v4.app.TaskStackBuilder;
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
public class user_datasql extends AsyncTask<String,Void,String> {
    Context context;
    user_datasql(Context ctx) {

        context = ctx;
    }
    user_database userDb; //SQLite database
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
        String login_url = "http://192.168.43.24/metrocab/signup.php";
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
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
        return "";
    }

    @Override
    protected void onPreExecute() {

        userDb= new user_database(context);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e("info", result);
        String[] items = result.split(":");
        String status = items[0];
       // Toast.makeText(context, result, Toast.LENGTH_LONG).show();




        //IF USER SIGNUP IS SUCCESSFUL ONLY THEN IT IS NOW COPIED TO SQLite DATABASE AS BELOW



        if (status.equals("success")) {
        //server ma post success bhaye balla SQLite ma pani hunxa
            //herr mula items[1] bata suru xa not 0 because hamile 0 chai id ko lagi rakhya xau tara server ko id ra SQLite ko id
            //synchronized xaena both are independent but equal
            //nice logic implemented here
            //instead of comparing two ids we increment  both independently
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
            Cursor res = userDb.getUserData();//getting from SQLite
            if (res.getCount() == 0) {//from User_database ,select all users from SQLite db
                boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss);
                if (isInserted == true) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    context.startActivity(intent);
                }
            }else{
                while (res.moveToNext()) { //1st row batai suru hunxa
                    //IMP
                    String id = res.getString(0);
                    //delete if there is an already existing account in the APP's local SQLite db
                    Integer deleteRows = userDb.deleteUserData(id);
                    if (deleteRows > 0) {
                        boolean isInserted = userDb.insertUserData(salute, fname, lname, country, phone, actype, license, vehicle, username, password, statuss);

                        if (isInserted == true) {
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(context, MapsActivity.class);
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unable to post to SQLite DB even though successfully posted to server ", Toast.LENGTH_LONG).show();
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
