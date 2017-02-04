package com.example.shadowstorm.metrocabv2;

        import android.content.Context;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.AsyncTask;
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
 * Created by Shadow Storm on 5/26/2016.
 */
public class findFriends extends AsyncTask<String,Void,String> {
    Context context;
    findFriends (Context ctx) {
        context = ctx;
    }//find friends ra MapsActivity ko context milako
    DriversLocation_db userDb;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://192.168.43.24/metrocab/search_friends.php";
        if(type.equals("search")) {
            try {
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

        userDb= new DriversLocation_db(context); //context milayo on preexecute
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(context,result,Toast.LENGTH_LONG).show();
        Cursor res = userDb.getDriverData();
        if (res.getCount() > 0) {
            Integer deleteRows = userDb.deleteDriverData();
            if (deleteRows > 0) {
                String[] parts = result.split("]");
                int totalRows = Integer.parseInt(parts[0]);
                String otherRows = parts[1];

                String[] items = otherRows.split(":");
                //String[] myStringArray = new String[3];
                String[] salute = new String[3];
                String[] fname = new String[3];
                String[] lname = new String[3];
                String[] country = new String[3];
                String[] phone = new String[3];
                String[] actype = new String[3];
                String[] license = new String[3];
                String[] vehicle = new String[3];
                String[] status = new String[3];
                String[] lat = new String[3];
                String[] lon = new String[3];
                //yo loop banauna sarai garho bhayo
                int c = 0;

                try{
                    for(int i=0;i<10;i++){
                        salute[i]=items[c];
                        //seriously dont know why but the code gives an error if we use c++ instead of c+1 in the second line
                        fname[i]=items[c+1];
                        lname[i]=items[c+2];
                        country[i]=items[c+3];
                        phone[i]=items[c+4];
                        actype[i]=items[c+5];
                        license[i]=items[c+6];
                        vehicle[i]=items[c+7];
                        status[i]=items[c+8];
                        lat[i]=items[c+9];
                        lon[i]=items[c+10];
                        c=c+11;
                        //Toast.makeText(context, "salute:"+salute[i]+"fname:"+fname[i]+"-lname: "+lname[i]+"-country: "+country[i]+"-phone: "+phone[i]+"-actype: "+actype[i]+"-license: "+license[i]+"-vehicle: "+vehicle[i]+"-status: "+status[i]+"-lat: "+lat[i]+"-lon: "+lon[i], Toast.LENGTH_LONG).show();
                        //context.startActivity(new Intent(context, MapsActivity.class));

                        boolean isInserted = userDb.insertDriverData(salute[i], fname[i], lname[i], country[i], phone[i], actype[i], license[i], vehicle[i], status[i], lat[i], lon[i]);
                        if (isInserted == true) {
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(context, DriversMap.class);
                            //context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Cursor ress = userDb.getDriverData();
                if (ress.getCount() == totalRows) {
                    Toast.makeText(context, "Total found "+totalRows, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, DriversMap.class);
                    context.startActivity(intent);
                }else{
                    Toast.makeText(context, "Total not found "+totalRows, Toast.LENGTH_LONG).show();
                }
            }
        }else{
            String[] parts = result.split("]");
            int totalRows = Integer.parseInt(parts[0]);
            String otherRows = parts[1];

            String[] items = otherRows.split(":");

            //String[] myStringArray = new String[3];
            String[] salute = new String[3];
            String[] fname = new String[3];
            String[] lname = new String[3];
            String[] country = new String[3];
            String[] phone = new String[3];
            String[] actype = new String[3];
            String[] license = new String[3];
            String[] vehicle = new String[3];
            String[] status = new String[3];
            String[] lat = new String[3];
            String[] lon = new String[3];
            String[] totalItem = new String[3];
            //yo loop banauna sarai garho bhayo
            int c = 0;

            try{
                for(int i=0;i<10;i++){
                    salute[i]=items[c];
                    //seriously dont know why but the code gives an error if we use c++ instead of c+1 in the second line
                    fname[i]=items[c+1];
                    lname[i]=items[c+2];
                    country[i]=items[c+3];
                    phone[i]=items[c+4];
                    actype[i]=items[c+5];
                    license[i]=items[c+6];
                    vehicle[i]=items[c+7];
                    status[i]=items[c+8];
                    lat[i]=items[c+9];
                    lon[i]=items[c+10];
                    c=c+11;
                    //Toast.makeText(context,"salute:"+salute[i]+"fname:"+fname[i]+"-lname: "+lname[i]+"-country: "+country[i]+"-phone: "+phone[i]+"-actype: "+actype[i]+"-license: "+license[i]+"-vehicle: "+vehicle[i]+"-status: "+status[i]+"-lat: "+lat[i]+"-lon: "+lon[i], Toast.LENGTH_LONG).show();
                    //context.startActivity(new Intent(context, MapsActivity.class));

                    boolean isInserted = userDb.insertDriverData(salute[i], fname[i], lname[i], country[i], phone[i], actype[i], license[i], vehicle[i], status[i], lat[i], lon[i]);
                    if (isInserted == true) {
                        //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                       // Intent intent = new Intent(context, DriversMap.class);
                        //context.startActivity(intent);
                    } else {
                        Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Cursor ress = userDb.getDriverData();
            if (ress.getCount() == totalRows) {
                Toast.makeText(context, "Total found "+totalRows, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DriversMap.class);
                context.startActivity(intent);
            }else{
                Toast.makeText(context, "Total not found "+totalRows, Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



}

