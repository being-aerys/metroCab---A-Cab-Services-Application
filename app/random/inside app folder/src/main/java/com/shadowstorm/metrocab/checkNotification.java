package com.shadowstorm.metrocab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dipesh on 04/06/16.
 */
public class checkNotification extends Activity {
    String myPhone;
    noti_db notiDb;
    user_database userDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_noti);
        openNotiDB();
        retriveData();
    }
    private void openNotiDB() {
        notiDb =new noti_db(this);
        userDb =new user_database(this);
    }

    private void retriveData() {

        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                myPhone = ress.getString(5);
            }
        }
        Cursor res = notiDb.getNotiData();
        if (res.getCount() > 0) {
            //Toast.makeText(CheckNotification.this, "found", Toast.LENGTH_SHORT).show();
           while (res.moveToNext()) {
                String id = res.getString(0);
                String salutation= res.getString(1);
                String fname= res.getString(2);
                String lname= res.getString(3);
                final String phone= res.getString(4);
                String deviceid= res.getString(5);
                String msg= res.getString(6);
                String lat= res.getString(7);
               String lon= res.getString(8);
               String driver= res.getString(9);
               String address= res.getString(10);

               if(driver.equals(myPhone)) {
                   startManagingCursor(res);

                   String[] fromFieldNames = new String[]
                           {"salutation", "fname", "lname", "phone","place", "msg"};
                   int[] toViewIDs = new int[]
                           {R.id.notiSalute, R.id.notiFname, R.id.notiLname, R.id.notiContact,R.id.notiLocation, R.id.notiMessage};

                   // Create adapter to may columns of the DB onto elemesnt in the UI.
                   SimpleCursorAdapter myCursorAdapters =
                           new SimpleCursorAdapter(
                                   checkNotification.this,        // Context
                                   R.layout.driver_noti_rows,    // Row layout template
                                   res,                    // cursor (set of DB records to map)
                                   fromFieldNames,            // DB Column names
                                   toViewIDs                // View IDs to put information in
                           );

                   // Set the adapter for the list view
                   ListView myList = (ListView) findViewById(R.id.listDrivernoti);
                   myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                           String db_id = String.valueOf(id);
                           //Toast.makeText(CheckNotification.this, "List item was clicked at " + id, Toast.LENGTH_SHORT).show();
                           AlertDialog.Builder myAlert = new AlertDialog.Builder(checkNotification.this);
                           myAlert.setMessage("Reply to this request")
                                   .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           dialog.dismiss();
                                           String type = "acceptReq";
                                           //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                                           pushNotification backgroundWorkers = new pushNotification(checkNotification.this);
                                           backgroundWorkers.execute(type, "null", phone, "null");
                                       }
                                   })
                                   .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                           String type = "cancelReq";
                                           //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                                           pushNotification backgroundWorkers = new pushNotification(checkNotification.this);
                                           backgroundWorkers.execute(type, "null", phone, "null");
                                       }
                                   })
                                   .setTitle("Cab Request")
                                   .setIcon(R.drawable.myridec)
                                   .create();
                           myAlert.show();
                       }
                   });
                   myList.setAdapter(myCursorAdapters);
               }else{
                   String type = "checkNoti";
                   //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                   pushNotification backgroundWorkers = new pushNotification(checkNotification.this);
                   backgroundWorkers.execute(type, "null", myPhone, "null");
               }
            }
        }else{
            String type = "checkNoti";
            //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
            pushNotification backgroundWorkers = new pushNotification(checkNotification.this);
            backgroundWorkers.execute(type, "null", myPhone, "null");
        }
    }
}
