package com.shadowstorm.metrocab;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dipesh on 03/06/16.
 */
public class callActivity extends Activity {

    String db_id;
    DriversLocation_db driverDb;
    user_database myDb;
    private RequestQueue requestQueue;
    String address;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);
        driverDb =new DriversLocation_db(this);
        myDb =new user_database(this);

        db_id = getIntent().getStringExtra("db_id");
        requestQueue = Volley.newRequestQueue(this);


        getUserDatas(db_id);

    }

    private void getUserDatas(String db_id) {
        Cursor cursor = driverDb.getDriverDataById(db_id);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String salutes = cursor.getString(1);
                String fnames = cursor.getString(2);
                String lnames = cursor.getString(3);
                String nation = cursor.getString(4);
                String phone = cursor.getString(5);
                String actype = cursor.getString(6);
                String license = cursor.getString(7);
                String vehicle = cursor.getString(8);
                String status = cursor.getString(9);
                String lat = cursor.getString(10);
                String lon = cursor.getString(11);
                String dist = cursor.getString(12);

               //private RequestQueue requestQueue;

                JsonObjectRequest request = new JsonObjectRequest("https://maps.googleapis.com/maps/api/geocode/json?latlng="+lat+","+lon+"&key=AIzaSyC5D-LPh7YXtLO4lpwObifv-fKMo4A5uNs",
                        new Response.Listener<JSONObject>(){
                            @Override
                            public void onResponse(JSONObject response){
                                try{
                                    String address = response.getJSONArray("results").getJSONObject(0).getString("formatted_address");
                                    TextView callloc = (TextView) findViewById(R.id.callLocation);
                                    callloc.setText(address);
                                    //Toast.makeText(callActivity.this, address, Toast.LENGTH_SHORT).show();
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        },new Response.ErrorListener(){
                            @Override
                            public void onErrorResponse(VolleyError error){

                                Toast.makeText(callActivity.this, "volley error", Toast.LENGTH_SHORT).show();
                        }
                });
                requestQueue.add(request);


                //Toast.makeText(callActivity.this, id, Toast.LENGTH_SHORT).show();

                TextView salute = (TextView) findViewById(R.id.callSalute);
                TextView cabNum = (TextView) findViewById(R.id.callCabNumber);
                TextView dista = (TextView) findViewById(R.id.callDistance);
                TextView callfname = (TextView) findViewById(R.id.callFname);
                TextView calllname = (TextView) findViewById(R.id.callLname);
                TextView callphone = (TextView) findViewById(R.id.callPhone);
                TextView callstatus = (TextView) findViewById(R.id.callStatus);
                salute.setText(salutes);
                cabNum.setText(vehicle);
                dista.setText(dist);
                callfname.setText(fnames);
                calllname.setText(lnames);
                callphone.setText(phone);
                callstatus.setText(status);
            }
        }else{
            Toast.makeText(callActivity.this, "nothing found", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendNotiToDriver(View v){
        String ids = db_id;
        Cursor cursor = driverDb.getDriverDataById(ids);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String salutes = cursor.getString(1);
                String fnames = cursor.getString(2);
                String lnames = cursor.getString(3);
                String nation = cursor.getString(4);
                String phone = cursor.getString(5);
                String actype = cursor.getString(6);
                String license = cursor.getString(7);
                String vehicle = cursor.getString(8);
                String status = cursor.getString(9);
                String lat = cursor.getString(10);
                String lon = cursor.getString(11);
                //-------------------------------receiving my info
                Cursor mydetail = myDb.getUserData();
                if (mydetail.getCount() > 0) {
                    while (mydetail.moveToNext()) {
                        String myPhone = mydetail.getString(5);
                        String type="sendReq";
                        pushNotification backgroundWorkers = new pushNotification(callActivity.this);
                        backgroundWorkers.execute(type,id, phone, myPhone);
                    }
                }
            }
        }else{
            Toast.makeText(callActivity.this, "nothing found", Toast.LENGTH_SHORT).show();
        }
    }
}
