package com.example.shadowstorm.metrocabv2;


        import android.app.AlertDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.AsyncTask;
        import android.util.Log;
        import android.widget.Toast;

        import com.example.shadowstorm.metrocabv2.user_database;

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
public class loginCheck extends AsyncTask<String,Void,String> {
    Context context;
    loginCheck (Context ctx) {
        context = ctx;
    }
    user_database userDb;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://192.168.43.24/metrocab/login.php";
        if(type.equals("login")) {
            try {
                String user_name = params[1];
                String password = params[2];
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
        // Toast.makeText(context,"Data Inserted",Toast.LENGTH_LONG).show();
        //alertDialog = new AlertDialog.Builder(context).create();
        //alertDialog.setTitle("Login Status");
        userDb= new user_database(context);

    }

    @Override
    protected void onPostExecute(String result) {
        String[] items = result.split(":");
        String status = items[0];
        Log.e("check", result);
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
            // Toast.makeText(context, phone, Toast.LENGTH_LONG).show();
            Cursor res = userDb.getUserData();
            if (res.getCount() == 0) {
                // Toast.makeText(context, "==0", Toast.LENGTH_LONG).show();
                boolean isInserted = userDb.insertUserData(salute, fname, lname, country,phone,actype,license,vehicle,username,password,statuss);
                if (isInserted == true) {
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
                        boolean isInserted = userDb.insertUserData(salute, fname, lname, country, phone, actype, license, vehicle, username, password, statuss);

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

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


}

