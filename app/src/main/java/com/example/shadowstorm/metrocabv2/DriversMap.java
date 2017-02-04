package com.example.shadowstorm.metrocabv2; //lala pakh


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by Shadow Storm on 31/05/16.
 */
public class DriversMap extends Activity {

    private ArrayList<String> data = new ArrayList<String>();
    private String actualIdToRemove ;
    ArrayList actualIdsToRemove = new ArrayList();
    private String db_id;
    public static String globalDriverId;
    private static Thread checkThread;
    static Runnable run;
    static Boolean count = false;
    Thread threadLoop = new Thread(run);//static nabhaye loop ma create bhairahanxa ra out of memory hunxa
    DriversLocation_db driverLocDb;
    ArrayList listRemove = new ArrayList( );
    private String myLat,myLon;


    //globally remove garne even durong back press from map frag yo banai hai paxi

    public static String removeGlobalDriverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //xml content dekhauna
        setContentView(R.layout.activity_driver);
        openDB();
        populateListViewFromDB();

        //----------------------------------------------
       /* checkThread = new Thread() {
            @Override
            public void run() {

                try {

                    while (true){
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {*/
                                //invalidateOptionsMenu();
                                Log.e("HERE","JUPI");
                                openDB();
                                populateListViewFromDB();
                                //setContentView(R.layout.driver_noti);
/*
                            }
                        });
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        checkThread.start();*/
        //---------------------------------------------------------------------

    }
    private void openDB()
    {
        driverLocDb =new DriversLocation_db(this);
    }


    //--------------------------------------------
    private void populateListViewFromDB() {
        Cursor cursor = driverLocDb.getDriverData();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(13);
                actualIdToRemove = id;
                Log.e("SEEEEE---ACTUAL DRIVERS",id);
                String salutes = cursor.getString(1);
                final String fnames = cursor.getString(2);
                String lnames = cursor.getString(3);
                String driverLat = cursor.getString(10);
                String driverLon = cursor.getString(11);


                //=======================populoateListView while true ma xa
                //=======================So global values ma assign garde






                // Allow activity to manage lifetime of the cursor.
                // DEPRECATED! Runs on the UI thread, OK for small/short queries.
                startManagingCursor(cursor);


                if(actualIdsToRemove.size()>0){

                    //============================================================================
                    //remove bhanne value bhaye po garnu ta haina


                    for(int i = 0;i<actualIdsToRemove.size();i++){

                      //========================================================
                        //listRemove ko each value sanga compare garna paryo ni
                        if(String.valueOf(actualIdsToRemove.get(i))!= id){

                            //=================================================
                            //display nai nagarde tyo driver lai
                            //so nothing in this else block

                                           }
                        else{

                            Log.e("actualID",String.valueOf(actualIdsToRemove.get(i)));
                            Log.e("actualID",String.valueOf(id));


                            String[] fromFieldNames = new String[]
                                    {"salutation","fname","lname","vehicle","dist"};
                            int[] toViewIDs = new int[]
                                    {R.id.driverSalute,R.id.driverFname,R.id.driverLname,R.id.driverCab,R.id.driverDistance};

                            // Create adapter to map columns of the DB onto elemesnt in the UI.
                            final SimpleCursorAdapter myCursorAdapter =
                                    new SimpleCursorAdapter(
                                            this,        // Context
                                            R.layout.driver_rows,    // Row layout template
                                            cursor,                    // cursor (set of DB records to map)
                                            fromFieldNames,            // DB Column names
                                            toViewIDs                // View IDs to put information in
                                    );

                            // Set the adapter for the list view
                            ListView myList = (ListView) findViewById(R.id.listDriverLoc);
                            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                                    db_id= String.valueOf(id);



                                    Log.e("SEE id", db_id);

                                    view.setSelected(true);
                                    //yeha bata id pathako CallActivity ma
                                    //Toast.makeText(DriversMap.this, "List item was clicked at " + db_id, Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(DriversMap.this);
                                    myAlert.setMessage("Proceed to service or Remove the driver from the list.")
                                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    String type = "SELECT";
                                                    //-------------------------
                                                    globalDriverId = db_id;
                                                    GlobalValues.globalDriverUseId=db_id;

                                                    Log.e("SEE id", "Aashish "+ db_id);

                                                    //--------------------------------------------

                                                    Intent intent = new Intent(DriversMap.this,CallActivity.class);
                                                    intent.putExtra("db_id",db_id);
                                                    startActivity(intent);
                                                    //-------------------------------------------
                                                }
                                            })
                                            .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    view.setVisibility(View.GONE);
                                                    listRemove.add(db_id);
                                                    //
                                                    actualIdsToRemove.add(actualIdToRemove);
                                                    //global




                                                }
                                            })
                                            .setTitle("Driver Selection")
                                            .setIcon(R.drawable.myridec1)
                                            .create();
                                    myAlert.show();
                                }
                            });
                            myList.setAdapter(myCursorAdapter);

                        }
                    }

                    //-------------------------------------------------------------------------------------------------------------
                }
                else{
                    //=======================================================
                    //listRemove ko size nai 0 xa ta sidhai garne ni aba


                    String[] fromFieldNames = new String[]
                            {"salutation","fname","lname","vehicle","dist"};
                    int[] toViewIDs = new int[]
                            {R.id.driverSalute,R.id.driverFname,R.id.driverLname,R.id.driverCab,R.id.driverDistance};

                    // Create adapter to map columns of the DB onto elemesnt in the UI.
                    SimpleCursorAdapter myCursorAdapter =
                            new SimpleCursorAdapter(
                                    this,        // Context
                                    R.layout.driver_rows,    // Row layout template
                                    cursor,                    // cursor (set of DB records to map)
                                    fromFieldNames,            // DB Column names
                                    toViewIDs                // View IDs to put information in
                            ){
                                public String getIdfromPosition(int position){
                                    String id = "";



                                    return id;
                                }
                            };

                    // Set the adapter for the list view
                    ListView myList = (ListView) findViewById(R.id.listDriverLoc);
                    myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                            db_id= String.valueOf(id);



                            Log.e("SEE id", db_id);
                            Log.e("SEE id", "Aashish "+ db_id);
                            GlobalValues.globalDriverUseId = db_id;

                            view.setSelected(true);
                            //yeha bata id pathako CallActivity ma
                            //Toast.makeText(DriversMap.this, "List item was clicked at " + db_id, Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(DriversMap.this);
                            myAlert.setMessage("Proceed to service or Remove the driver from the list.")
                                    .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            String type = "SELECT";

                                            //--------------------------------------------

                                            Intent intent = new Intent(DriversMap.this,CallActivity.class);
                                            intent.putExtra("db_id",db_id);
                                            startActivity(intent);
                                            //-------------------------------------------
                                        }
                                    })
                                    .setNegativeButton("Remove", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            view.setVisibility(View.GONE);
                                            listRemove.add(db_id);

                                            actualIdsToRemove.add(actualIdToRemove);

                                        }
                                    })
                                    .setTitle("Driver Selection")
                                    .setIcon(R.drawable.myridec1)
                                    .create();
                            myAlert.show();
                        }
                    });
                    myList.setAdapter(myCursorAdapter);
                }
            }


            //aba yehin nai server bata loyera refresh gardiu na driver list ,baal
           /* run = new Runnable() {
                @Override
                public void run() {
                    try {
                        //======================================================================

                        while(true){
                            Log.e("LOOP" ," IS ON");
                            sleep(10000);
                            //FindFriends class finds drivers
                            FindFriends backgroundWorkers = new FindFriends(DriversMap.this);
                            //aafno data deko FindFriends lai
                            String type = "search";
                            backgroundWorkers.execute(type,GlobalValues.globalPassenger, GlobalValues.globalPassengerLat, GlobalValues.globalPassengerLon);

                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };*/
            //threadLoop.start();





        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //threadLoop.interrupt();
       // checkThread.interrupt();
        //Intent intent = new Intent(this,MapsActivity.class);
        //startActivity(intent);
    }


    public void goToSessionMap(){

/*

        String idValue = db_id;
        myLat = GlobalValues.globalDriverLat;
        myLon = GlobalValues.globalDriverLon;

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
                    while (mydetail.moveToNext()) {   //yo kina gareko bhanda driver khojeko
                        String driverFName = cursor.getString(2);
                        String driverLName = cursor.getString(3);

                        String driverLat = cursor.getString(10);
                        Log.e("CRUCIAL3",driverLat);
                        //Log.e("LATSQL#",String.valueOf())
                        String driverLon = cursor.getString(11);
                        String passengerUserName = mydetail.getString(9);
                        Log.e("name ho ta",String.valueOf(passengerUserName));
                        String myLat = mydetail.getString(12); //google map ma check gareko yo user nai ho driver haina
                        String myLon =mydetail.getString(13);
                        Log.e("THIS_IS",String.valueOf(myLat));
                        Log.e("THIS_IS",String.valueOf(myLon));


                        Toast.makeText(this,"THIS IS "+String.valueOf(myLat),Toast.LENGTH_SHORT).show();

                        Log.e("name1 ho ta? huh",String.valueOf(passengerUserName));


                        Intent intent = new Intent(CallActivity.this,ShowDriverOnMap.class);

                        Bundle extras = new Bundle();
                        extras.putString("driverFName",driverFName);
                        Log.e("show Driver lai dexa",String.valueOf(driverFName));
                        extras.putString("driverLName",driverLName);
                        extras.putString("driverLat",driverLat);
                        extras.putString("driverLon",driverLon);
                        extras.putString("passengerName",passengerUserName);
                        extras.putString("myLat",myLat);
                        extras.putString("myLon",myLon);
                        intent.putExtras(extras);
                        startActivity(intent);


                    }
                }
            }
        }else{
            Toast.makeText(CallActivity.this, "nothing found", Toast.LENGTH_SHORT).show();
        }





*/




    }


}

