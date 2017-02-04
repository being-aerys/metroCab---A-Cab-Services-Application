package com.shadowstorm.metrocab;

        import android.app.AlertDialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.DialogInterface;
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
    }
    DriversLocation_db userDb;
    ProgressDialog dialog;
    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://omkishan.com.np/metrocab/search_friends.php";
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
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Searching, please wait ...");
        dialog.setTitle("Find Cabs");
        dialog.setIcon(R.drawable.myridec);
        dialog.setCancelable(true);
        dialog.show();

        userDb= new DriversLocation_db(context);
    }

    @Override
    protected void onPostExecute(String result) {
        dialog.dismiss();
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
                        dist[i]=items[c+11];
                        c=c+12;
                        //Toast.makeText(context, "salute:"+salute[i]+"fname:"+fname[i]+"-lname: "+lname[i]+"-country: "+country[i]+"-phone: "+phone[i]+"-actype: "+actype[i]+"-license: "+license[i]+"-vehicle: "+vehicle[i]+"-status: "+status[i]+"-lat: "+lat[i]+"-lon: "+lon[i], Toast.LENGTH_LONG).show();
                        //context.startActivity(new Intent(context, MapsActivity.class));

                        boolean isInserted = userDb.insertDriverData(salute[i], fname[i], lname[i], country[i], phone[i], actype[i], license[i], vehicle[i], status[i], lat[i], lon[i],dist[i]);
                        if (isInserted == true) {
                            //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(context, DriversMap.class);
                            //context.startActivity(intent);
                        } else {
                            //Toast.makeText(context, otherRows, Toast.LENGTH_LONG).show();
                            Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                Cursor ress = userDb.getDriverData();
                if (ress.getCount() == totalRows) {
                    dialog.cancel();
                    //Toast.makeText(context, "Total found "+totalRows, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, DriversMap.class);
                    context.startActivity(intent);
                }else{
                    dialog.cancel();
                    //Toast.makeText(context, "Total not found "+totalRows, Toast.LENGTH_LONG).show();
                    AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                    myAlert.setMessage("Cant load all cabs")
                            .setPositiveButton("Try Again",new DialogInterface.OnClickListener(){
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
        }else{
            String[] parts = result.split("]");
            int totalRows = Integer.parseInt(parts[0]);
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
                    dist[i]=items[c+11];
                    c=c+12;
                    //Toast.makeText(context,"salute:"+salute[i]+"fname:"+fname[i]+"-lname: "+lname[i]+"-country: "+country[i]+"-phone: "+phone[i]+"-actype: "+actype[i]+"-license: "+license[i]+"-vehicle: "+vehicle[i]+"-status: "+status[i]+"-lat: "+lat[i]+"-lon: "+lon[i], Toast.LENGTH_LONG).show();
                    //context.startActivity(new Intent(context, MapsActivity.class));

                    boolean isInserted = userDb.insertDriverData(salute[i], fname[i], lname[i], country[i], phone[i], actype[i], license[i], vehicle[i], status[i], lat[i], lon[i], dist[i]);
                    if (isInserted == true) {
                        //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
                       // Intent intent = new Intent(context, DriversMap.class);
                        //context.startActivity(intent);
                    } else {
                        //Toast.makeText(context, otherRows, Toast.LENGTH_LONG).show();
                        Toast.makeText(context, "Unable to retrieve user data from server ", Toast.LENGTH_LONG).show();
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
            Cursor ress = userDb.getDriverData();
            if (ress.getCount() == totalRows) {
                dialog.cancel();
                //Toast.makeText(context, "Total found "+totalRows, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, DriversMap.class);
                context.startActivity(intent);
            }else{
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage("Cant load all cabs")
                        .setPositiveButton("Try Again",new DialogInterface.OnClickListener(){
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
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



}

