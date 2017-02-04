package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dipesh on 04/06/16.
 */
public class CheckNotification extends Activity {
    Noti_db notiDb;
    User_database userDb;
    TextView to;
    //private Handler mHandler = new Handler();
    Thread checkThread;
    ArrayList listDelete = new ArrayList( );
    String myPhone ; //driver
    String phone,cancel_phone,accept_phone,accept_id,accept_name;//caller
    LinearLayout layout1;
    String db_id,paypal_id,codeb,user_name;
    String bargainStatus,paymentPassPhone,paymentDriverPhone;
    int balance,advance,bargainAmount;
    String driverVal,selectedNoti;
    String bargainedAmount,normalIdContinuousFlow;
    public static String passPhoneLocalGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.driver_noti);
        //===========================================paypal
        Intent intent = new Intent(this, PayPalService.class);

        // live: don't put any environment extra
        // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);

        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "AdlH4iLsCPoWEkIvPVz6enziso8Qrktk1-uD5GcHyHDDSd_Hw0W3FWVROijThh3V7W1jbQ6xkMg6Gwfe");

        startService(intent);
        //======================================================================================
        // YEAH KO LAYOUT SET VISIBILITY MA BUG AAYERA COMMENT HANDEKO EK PATAK HERR K KAAM HO ESKO
        //layout1.setVisibility(View.GONE);
        openNotiDB();

        //============================
        /*to = (TextView)findViewById(R.id.notiDesti);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String jupi = GCMPushReceiverService.destAddr;

                //gcm.getResp();
                if (jupi != null) {
                    Log.e("HERE k payo ", jupi);
                    to.setText(jupi);
                } else {
                    A.t(CheckNotification.this, "else ma gayo");
                }
            }
        });*/
        //==================================================
        //The following code took me more than 12 hours to develop...10 pm last night to 12:38pm this noon
        //but finally did it
        //u see it looks so easy but the actual documentation of Android and all help from internet did no magic
        //so devised my own method to do the updates
        //this thing depressses me
        //if u look the code it looks normal
        //but the effort applied  by the developer isnt seen
        //u see it takes hours  to remove a single bug even with the logcat
        checkThread = new Thread() {
            @Override
            public void run() {
                Log.e("checkThread","BADRI");

                try {

                    while (true){
                        sleep(5000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //invalidateOptionsMenu();
                                Log.e("HERE","JUPI");
                                openNotiDB();
                                int checkPayment = GlobalValues.checkPaymentStatus;
                                if(checkPayment > 0){
                                    //checkBargain();
                                }
                                //setContentView(R.layout.driver_noti);

                            }
                        });
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        checkThread.start();
    }
    public void checkBargain(final String db_id) {
        Log.e("checkBargain", "-----------------------received");
        Cursor res = userDb.getUserData();
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                paymentDriverPhone = res.getString(5);
            }
        }
        Cursor ress = notiDb.getNotiData();
        //=================yeti dukha paunai naparne getNotiCancelData() use gare only the selected onre authyo ani tesailai sidhai
        //accept or reject gare bhaihalthyo ni :D thukka masala demo ra preentation ko dui din agadi aaja esai time khayo yesle

        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {

                Log.e("database id", ress.getString(0));
                Log.e("id from listview", db_id);

                //paymentPassPhone = ress.getString(4);
                bargainAmount = Integer.parseInt(ress.getString(14));
                bargainStatus = ress.getString(15);
                normalIdContinuousFlow = ress.getString(0);
                Log.e("checkbarga status","---------------------------"+bargainStatus);

                if (bargainStatus.equals("NEW")) {
                    //==============================
                    try{
                        Log.e("1112","222");

                        LocationManager locationManager = (LocationManager)
                                getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        Location location = locationManager.getLastKnownLocation(locationManager
                                .getBestProvider(criteria, false));
                        GlobalValues.driverSideStartingLat = String.valueOf(location.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(location.getLongitude());
                        Log.e("HYYYY","ll");
                        SetStartingValues bg = new SetStartingValues(this);
                        Log.e("1111122222",String.valueOf(GlobalValues.driverSideStartingLat));
                        Log.e("1111122222",user_name);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }
                    catch (Exception e){
                        Log.e("1113","222");
                        GPSTracker gpsTracker = new GPSTracker(CheckNotification.this);
                        GlobalValues.driverSideStartingLat = String.valueOf(gpsTracker.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(gpsTracker.getLongitude());
                        Log.e("HYYYY","22");
                        SetStartingValues bg = new SetStartingValues(this);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }


                    //===============================
                    GlobalValues.checkPaymentStatus=0;
                    AlertDialog.Builder alertDialogb = new AlertDialog.Builder(CheckNotification.this);
                    alertDialogb.setTitle("Payment Request");
                    alertDialogb.setMessage("Your payment amount is rejected by passenger\nPassenger wants to pay $" + bargainAmount + " as cab charge.\nPress ACCEPT to accept his request");
                    alertDialogb.setIcon(R.drawable.car);
                    alertDialogb.setCancelable(false);
                    alertDialogb.setPositiveButton("REJECT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    //------------------------------------
                                    AlertDialog.Builder alertDialogc = new AlertDialog.Builder(CheckNotification.this);
                                    alertDialogc.setTitle("Payment Request");
                                    alertDialogc.setMessage("If you are not satisfied with passenger' request,\nEnter the amount you wanted to receive");

                                    final EditText inputb = new EditText(CheckNotification.this);
                                    LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    inputb.setLayoutParams(lpb);
                                    alertDialogc.setView(inputb);
                                    alertDialogc.setIcon(R.drawable.car);
                                    alertDialogc.setCancelable(false);
                                    alertDialogc.setPositiveButton("CONTINUE",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    codeb = inputb.getText().toString();
                                                    if (!codeb.equals("")) {
                                                        String passPhoneLocal = "1000000000";
                                                        Bargain backgroundWorkers = new Bargain(CheckNotification.this);

                                                        Log.e("passPhoneGlobal-urBarg",passPhoneLocalGlobal);
                                                        //pass the phone number that u actually entered the amount for passPhoneLocalGlob
                                                        backgroundWorkers.execute("bargainByDriver", passPhoneLocalGlobal,paymentDriverPhone,String.valueOf(codeb),"reject");
                                                    } else {
                                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(CheckNotification.this);
                                                        myAlert.setMessage("Amount cannot be empty")
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.dismiss();
                                                                        Intent intent = new Intent(CheckNotification.this, CheckNotification.class);
                                                                        startActivity(intent);
                                                                    }
                                                                })
                                                                .setTitle("Payment Request")
                                                                .setIcon(R.drawable.car)
                                                                .create();
                                                        myAlert.show();
                                                    }
                                                }
                                            });

                                    alertDialogc.setNegativeButton("REJECT",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    Integer deleteRows = notiDb.deleteNotiData(db_id);
                                                    Log.e("checkNoti", String.valueOf(notiDb.getNotiData()));
                                                    if (deleteRows > 0) {
                                                        Log.e("Noti_db deleted","---------------------"+db_id);
                                                        Log.e("passPhoneGlobal-Rej",passPhoneLocalGlobal);

                                                        PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                                                        backgroundWorkers.execute("cancelReq", "0", passPhoneLocalGlobal,paymentDriverPhone);

                                                    }else{
                                                        Log.e("Noti_db not deleted","---------------------"+db_id);
                                                    }

                                                }
                                            });

                                    alertDialogc.setNeutralButton("CLOSE",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();

                                                }
                                            });

                                    alertDialogc.show();
                                }
                            });

                    alertDialogb.setNegativeButton("ACCEPT",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    Bargain backgroundWorkers = new Bargain(CheckNotification.this);


                                    Log.e("passPhoneGlobal-Accept",passPhoneLocalGlobal);

                                    backgroundWorkers.execute("bargainByDriver", passPhoneLocalGlobal,paymentDriverPhone, String.valueOf(bargainAmount),"accept");
                                    acceptRequest(db_id);
                                }
                            });
                    alertDialogb.setNeutralButton("CLOSE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialogb.show();
                }
                if(bargainStatus.equals("accept")){
                    //======================
                    try{
                        Log.e("1112","222");

                        LocationManager locationManager = (LocationManager)
                                getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        Location location = locationManager.getLastKnownLocation(locationManager
                                .getBestProvider(criteria, false));
                        GlobalValues.driverSideStartingLat = String.valueOf(location.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(location.getLongitude());
                        Log.e("HYYYY","ll");
                        SetStartingValues bg = new SetStartingValues(this);
                        Log.e("1111122222",String.valueOf(GlobalValues.driverSideStartingLat));
                        Log.e("1111122222",user_name);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }
                    catch (Exception e){
                        Log.e("1113","222");
                        GPSTracker gpsTracker = new GPSTracker(CheckNotification.this);
                        GlobalValues.driverSideStartingLat = String.valueOf(gpsTracker.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(gpsTracker.getLongitude());
                        Log.e("HYYYY","22");
                        SetStartingValues bg = new SetStartingValues(this);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }


                    //===========================
                    AlertDialog.Builder myAlertb = new AlertDialog.Builder(CheckNotification.this);
                    myAlertb.setMessage("Passenger agreed to pay $"+bargainAmount)
                            .setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    boolean isUpdated = userDb.updateStatus(paymentDriverPhone,"BUSY");
                                    if(isUpdated == true){
                                        Log.e("driver status : ", "-------------------------- BUSY");
                                        Bargain backgroundWorkers = new Bargain(CheckNotification.this);
                                        Log.e("passPhoneGlo-driverBarg",passPhoneLocalGlobal);

                                        backgroundWorkers.execute("bargainByDriver", passPhoneLocalGlobal,paymentDriverPhone, String.valueOf(bargainAmount),"accept");
                                        acceptRequest(db_id);
                                    }else{
                                        Log.e("driver status : ", "---------------------- CANNOT CHANGE");
                                        Toast.makeText(CheckNotification.this, "Cannot change driver status to BUSY", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss(); Integer deleteRows = notiDb.deleteNotiData(db_id);
                                    Log.e("checkNoti", String.valueOf(notiDb.getNotiData()));
                                    if (deleteRows > 0) {
                                        Log.e("Noti_db deleted","---------------------"+db_id);
                                        Log.e("passPhoneGlobal-Rej",passPhoneLocalGlobal);

                                        PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                                        backgroundWorkers.execute("cancelReq", "0", passPhoneLocalGlobal,paymentDriverPhone);

                                    }else{
                                        Log.e("Noti_db not deleted","---------------------"+db_id);
                                    }
                                }
                            })
                            .setTitle("Cab payment request")
                            .setIcon(R.drawable.car)
                            .setCancelable(false)
                            .create();
                    myAlertb.show();

                }
                if(bargainStatus.equals("OLD")){
                    AlertDialog.Builder myAlertb = new AlertDialog.Builder(CheckNotification.this);
                    myAlertb.setMessage("Wait until passenger approval to your payment request")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    GlobalValues.isBargain=1;
                                }
                            })
                            .setTitle("Cab payment request")
                            .setIcon(R.drawable.car)
                            .setCancelable(false)
                            .create();
                    myAlertb.show();
                }
                if(bargainStatus.equals("0")){
                    //================================
                    try{
                        Log.e("1112","222");

                        LocationManager locationManager = (LocationManager)
                                getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        Location location = locationManager.getLastKnownLocation(locationManager
                                .getBestProvider(criteria, false));
                        GlobalValues.driverSideStartingLat = String.valueOf(location.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(location.getLongitude());
                        Log.e("HYYYY","ll");
                        SetStartingValues bg = new SetStartingValues(this);
                        Log.e("1111122222",String.valueOf(GlobalValues.driverSideStartingLat));
                        Log.e("1111122222",user_name);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }
                    catch (Exception e){
                        Log.e("1113","222");
                        GPSTracker gpsTracker = new GPSTracker(CheckNotification.this);
                        GlobalValues.driverSideStartingLat = String.valueOf(gpsTracker.getLatitude());
                        GlobalValues.driverSideStartingLon = String.valueOf(gpsTracker.getLongitude());
                        Log.e("HYYYY","22");
                        SetStartingValues bg = new SetStartingValues(this);
                        bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

                    }

                    //=========================Selecting only one on the basis of db_id


                    //================================
                    // Log.e("twice-normalid","tala ko"+normalIdContinuousFlow);
                    // Log.e("twice-db_id","-------"+db_id);



                    Log.e("database_id_whileNext",String.valueOf(ress.getString(0)));

                    if(ress.getString(0).equals(db_id)) {
                        Log.e("database_id_selected",String.valueOf(db_id));

                        AlertDialog.Builder myAlerta = new AlertDialog.Builder(this);
                        final EditText inputb = new EditText(this);
                        LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        inputb.setLayoutParams(lpb);
                        myAlerta.setView(inputb);
                        myAlerta.setMessage("Enter the amount you want from this request\nPress REJECT to cancel the request with passenger")
                                .setPositiveButton("CONTINUE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        codeb = inputb.getText().toString();
                                        Log.e("entered amount", "---------------------" + codeb);
                                        if (!codeb.equals("")) {
                                            Log.e("databaseNotitoPassen",String.valueOf(selectedNoti));
                                            //=====================================================
                                            String passPhoneLocal = "100000000";
                                            Cursor c = notiDb.getNotiCancelData(db_id);
                                            while (c.moveToNext()){

                                                passPhoneLocal = c.getString(4);
                                            }

                                            passPhoneLocalGlobal = passPhoneLocal;
                                            //========================================================
                                            Log.e("passPhoneG", "-----passPhone :" + passPhoneLocal + "--driver phone:" + paymentDriverPhone + "---amount and status" + codeb + "/reject");
                                            String type = "bargainByPass";
                                            Bargain backgroundWorkers = new Bargain(CheckNotification.this);
                                            backgroundWorkers.execute("bargainByDriver", passPhoneLocalGlobal, paymentDriverPhone, String.valueOf(codeb), "reject");
                                        } else {
                                            AlertDialog.Builder myAlertb = new AlertDialog.Builder(CheckNotification.this);
                                            myAlertb.setMessage("Amount cannot be empty")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                            GlobalValues.isBargain = 1;
                                                        }
                                                    })
                                                    .setTitle("Cab payment request")
                                                    .setIcon(R.drawable.car)
                                                    .setCancelable(false)
                                                    .create();
                                            myAlertb.show();
                                        }
                                    }
                                })
                                .setNegativeButton("REJECT", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        String passPhoneLocal = "100000000";
                                        Cursor c = notiDb.getNotiCancelData(db_id);
                                        while (c.moveToNext()){

                                            passPhoneLocal = c.getString(4);
                                        }
                                        Log.e("passPhoneLocalRej",passPhoneLocal);

                                        Integer deleteRows = notiDb.deleteNotiData(db_id);
                                        Log.e("checkNoti", String.valueOf(notiDb.getNotiData()));
                                        if (deleteRows > 0) {
                                            Log.e("Noti_db deleted", "---------------------" + db_id);
                                             PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                                            backgroundWorkers.execute("cancelReq", "0", passPhoneLocal, paymentDriverPhone);

                                        } else {
                                            Log.e("Noti_db not deleted", "---------------------" + db_id);
                                        }
                                    }
                                })
                                .setNeutralButton("CLOSE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        GlobalValues.isBargain = 1;
                                    }
                                })
                                .setTitle("Cab payment request")
                                .setIcon(R.drawable.car)
                                .setCancelable(false)
                                .create();
                        myAlerta.show();

                    }
                }
            }
        }
    }



    private void openNotiDB() {
        notiDb =new Noti_db(this);
        userDb =new User_database(this);
        showButtons();
    }
    private void showButtons(){
        Button ArrivedButton = (Button) findViewById(R.id.buttonDriverArrived);
        Button CompleteButton = (Button) findViewById(R.id.buttonDriverCompleted);
        Button RejectButton = (Button) findViewById(R.id.buttonDriverReject);
        Button ReportButton = (Button) findViewById(R.id.buttonDriverReport);
        Button MapButton = (Button) findViewById(R.id.buttonDriverMap);
        Button payment = (Button) findViewById(R.id.paymentButton);
        Cursor res = notiDb.getNotiData();
        if (res.getCount() > 0) {
            Log.e("button", "----------------------------------------------" + res.getCount());
            while (res.moveToNext()) {
                String status = res.getString(11);
                int Arrived = Integer.parseInt(res.getString(12));
                if(status.equals("Accepted")||status.equals("Arrived")){
                    RejectButton.setVisibility(View.VISIBLE);
                    ReportButton.setVisibility(View.VISIBLE);
                    MapButton.setVisibility(View.VISIBLE);
                    CompleteButton.setVisibility(View.VISIBLE);
                    ArrivedButton.setVisibility(View.VISIBLE);
                    payment.setVisibility(View.GONE);
                   /*if(Arrived > 0){
                        CompleteButton.setBackgroundColor(Color.GREEN);
                       // ArrivedButton.setEnabled(false);
                    }else {
                        //CompleteButton.setEnabled(false);
                        ArrivedButton.setBackgroundColor(Color.GREEN);
                    }*/
                }
                if(status.equals("Pending")){
                    ArrivedButton.setVisibility(View.GONE);
                    CompleteButton.setVisibility(View.GONE);
                    RejectButton.setVisibility(View.GONE);
                    ReportButton.setVisibility(View.GONE);
                    MapButton.setVisibility(View.GONE);
                    payment.setVisibility(View.GONE);
                }
                if(status.equals("Completed")){
                    payment.setVisibility(View.VISIBLE);
                }
            }
        }else{
            try{
                checkThread.interrupt();
            }
            catch(Exception e){

            }

            Intent intent = new Intent(this,DriverPge.class);
            startActivity(intent);
        }
        retriveData();
        Log.e("ShowButtons","--------------------received");
    }

    //--------------------------------------getting notification from db
    private void retriveData() {

        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                user_name = ress.getString(9);
                myPhone = ress.getString(5);
                balance = (int)Float.parseFloat(ress.getString(14));
                paypal_id = ress.getString(16);
                Log.e("paypal Balance and id", "----------------------------------" + balance + " " + paypal_id);
                Cursor res = notiDb.getNotiData();
                if (res.getCount() > 0) {
                    Log.e("total noti on db", "----------------------------------------------" + res.getCount());

                    //Toast.makeText(CheckNotification.this, "found", Toast.LENGTH_SHORT).show();
                    while (res.moveToNext()) {
                        String id = res.getString(0);
                        Log.e("VVVV", String.valueOf(id));
                        String salutation = res.getString(1);
                        String fname = res.getString(2);
                        String lname = res.getString(3);
                        phone = res.getString(4);
                        GlobalValues.driverSidePassengerPhoneForId = phone;
                        String deviceid = res.getString(5);
                        String msg = res.getString(6);
                        String lat = res.getString(7);

                        String lon = res.getString(8);
                        String driver = res.getString(9);
                        driverVal = driver;
                        String address = res.getString(10);
                        String status = res.getString(11);
                        bargainedAmount = res.getString(14);


                        Log.e("checknoti", "----------------------------------------------" + driver + " " + myPhone);
                        if (driver.equals(myPhone)) {

                            // if (status.equals("Pending")) {
                            startManagingCursor(res);

                            String[] fromFieldNames = new String[]
                                    {"salutation", "fname", "lname", "phone", "place", "destToPass", "msg", "status"};
                            int[] toViewIDs = new int[]
                                    {R.id.notiSalute, R.id.notiFname, R.id.notiLname, R.id.notiContact, R.id.notiLocation, R.id.notiDesti, R.id.notiMessage, R.id.notiStatus};

                            // Create adapter to may columns of the DB onto elemesnt in the UI.
                            SimpleCursorAdapter myCursorAdapters =
                                    new SimpleCursorAdapter(
                                            CheckNotification.this,        // Context
                                            R.layout.driver_noti_rows,    // Row layout template
                                            res,                    // cursor (set of DB records to map)
                                            fromFieldNames,            // DB Column names
                                            toViewIDs                // View IDs to put information in
                                    );
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String jupi = GCMPushReceiverService.destAddr;

                                    //gcm.getResp();
                                    if (jupi != null) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                // to.setText(GCMPushReceiverService.destAddr);
                                            }
                                        });
                                    } else {
                                        // A.t(CheckNotification.this, "else ma gayo");
                                    }
                                }
                            });
                            // Set the adapter for the list view
                            final ListView myList = (ListView) findViewById(R.id.listDrivernoti);
                            myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, final View view, int position, final long id) {

                                    //========================USE THIS ID TO REFERENCE AN ITEM
                                    db_id = String.valueOf(id);
                                    selectedNoti = String.valueOf(db_id);
                                    //  Log.e("twiceUseTHis Id",String.valueOf(db_id));
                                    view.setSelected(true);
                                    //Toast.makeText(CheckNotification.this, "List item was clicked at " + id, Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(CheckNotification.this);
                                    myAlert.setMessage("Reply to this request")
                                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    if (balance < 35) {
                                                        AlertDialog.Builder alertDialogb = new AlertDialog.Builder(CheckNotification.this);
                                                        alertDialogb.setTitle("Balance Low");
                                                        alertDialogb.setMessage("Your balance is getting low\nEnter amount to add your balance");

                                                        final EditText inputb = new EditText(CheckNotification.this);
                                                        LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                                        inputb.setLayoutParams(lpb);
                                                        alertDialogb.setView(inputb);
                                                        alertDialogb.setIcon(R.drawable.car);
                                                        alertDialogb.setPositiveButton("Continue",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        codeb = inputb.getText().toString();
                                                                        if (!codeb.equals("")) {
                                                                            doPaypalReserve(Integer.parseInt(codeb));
                                                                        } else {
                                                                            AlertDialog.Builder myAlert = new AlertDialog.Builder(CheckNotification.this);
                                                                            myAlert.setMessage("Amount cannot be empty")
                                                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    })
                                                                                    .setTitle("Balance Low")
                                                                                    .setIcon(R.drawable.car)
                                                                                    .create();
                                                                            myAlert.show();
                                                                        }
                                                                    }
                                                                });

                                                        alertDialogb.setNegativeButton("CANCEL",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                        alertDialogb.show();
                                                    } else {
                                                        checkBargain(db_id);
                                                    }


                                                }
                                            })
                                            .setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Cursor resaa = notiDb.getNotiCancelData(db_id);
                                                    if (resaa.getCount() > 0) {
                                                        //Toast.makeText(CheckNotification.this, "found", Toast.LENGTH_SHORT).show();
                                                        while (resaa.moveToNext()) {
                                                            cancel_phone = resaa.getString(4);
                                                        }
                                                    }
                                                    checkThread.interrupt();
                                                    String type = "cancelReq";
                                                    PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                                                    backgroundWorkers.execute(type, "0", cancel_phone, myPhone);
                                                }
                                            })
                                            .setTitle("Cab Request")
                                            .setIcon(R.drawable.car)
                                            .create();
                                    myAlert.show();
                                }
                            });
                            myList.setAdapter(myCursorAdapters);
                            //}
                        } else {
                            /*String type = "checkNoti";
                            //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                            PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                            backgroundWorkers.execute(type, "null", myPhone, "null", "");*/

                            //}
                        }
                    }
                } else {
                   /* String type = "checkNoti";
                    //Toast.makeText(CheckNotification.this, phone, Toast.LENGTH_SHORT).show();
                    PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                    backgroundWorkers.execute(type, "null", myPhone, "null", "");*/

                }

            }
        }
    }

    //--------------------------------------driver is arrived to passenger
    public void iAmArrived(View v ) {
        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                String user_name = ress.getString(9);
                String driver_phone = ress.getString(5);
                String paypal_id = ress.getString(16);

                Cursor res = notiDb.getNotiData();
                if (res.getCount() > 0) {
                    Log.e("i am arrived", "----------------------------------------------" + res.getCount());
                    while (res.moveToNext()) {
                        String id = res.getString(0);
                        String salutation = res.getString(1);
                        String fname = res.getString(2);
                        String lname = res.getString(3);
                        String pass_phone = res.getString(4);
                        GlobalValues.driverSidePassengerPhoneForId = phone;
                        String deviceid = res.getString(5);
                        String msg = res.getString(6);
                        String lat = res.getString(7);
                        String lon = res.getString(8);
                        String driver = res.getString(9);
                        driverVal = driver;
                        String address = res.getString(10);
                        String status = res.getString(11);
                        int Completed = Integer.parseInt(res.getString(12));

                        if(Completed > 0){
                            iAmArrivedDisable();
                        }else{
                            PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                            backgroundWorkers.execute("iAmArrived",driver_phone, pass_phone);
                        }
                    }
                }else{
                    Toast.makeText(CheckNotification.this, "You do not have any request", Toast.LENGTH_SHORT).show();
                    showButtons();
                }
            }
        }
    }//------------------------------------driver arrived disabled
    public void iAmArrivedDisable(){
        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("Sorry this button is disabled. \nYou were already arrived to passenger")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setTitle("Cab Arrived")
                .setIcon(R.drawable.car)
                .create();
        myAlert.show();
    }

    //----------------------------driver reached to passenger destination with him
    public void reachedDestination(View v ) {
        Log.e("reachedDestination","-------------------------- clicked");
        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                String user_name = ress.getString(9);
                String driver_phone = ress.getString(5);
                String paypal_id = ress.getString(16);

                Cursor res = notiDb.getNotiData();
                if (res.getCount() > 0) {
                    Log.e("reachedDestination", "----------------------------------------------" + res.getCount());
                    while (res.moveToNext()) {
                        String id = res.getString(0);
                        String salutation = res.getString(1);
                        String fname = res.getString(2);
                        String lname = res.getString(3);
                        String pass_phone = res.getString(4);
                        GlobalValues.driverSidePassengerPhoneForId = phone;
                        String deviceid = res.getString(5);
                        String msg = res.getString(6);
                        String lat = res.getString(7);
                        String lon = res.getString(8);
                        String driver = res.getString(9);
                        driverVal = driver;
                        String address = res.getString(10);
                        String status = res.getString(11);
                        int Arrived = Integer.parseInt(res.getString(12));
                        Log.e("arrived status", "----------------------------------------------"+status);
                        Log.e("arrived arrived", "----------------------------------------------"+Arrived);

                        if(Arrived > 0 && Arrived < 2){
                            Log.e("reachedDestination", "----------------------------------------------arrived");

                            PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                            backgroundWorkers.execute("reachedDestination",driver_phone, pass_phone);
                        }
                        if(Arrived < 1){
                            reachedDestinationDisable("notComplete");
                        }
                        if(Arrived > 1){
                            reachedDestinationDisable("Complete");
                        }

                    }
                }else{
                    Toast.makeText(CheckNotification.this, "You do not have any request", Toast.LENGTH_SHORT).show();
                    showButtons();
                }
            }
        }

    }
    //------------------------------------driver reached to passenger destination with him disable
    public void reachedDestinationDisable(String complete){
        if(complete.equals("notComplete")) {
            Log.e("arrived arrived", "----------------------------complete------------------"+complete);
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Sorry this button is LOCKED. \nTo unlock this button, click on arrived button and request passenger to click on YES button")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Trip Completed")
                    .setIcon(R.drawable.car)
                    .create();
            myAlert.show();
        }else{
            Log.e("arrived arrived", "----------------------------not complete------------------"+complete);
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Sorry this button is LOCKED. \nYou have already reached to destination.\nRequest for payment.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Button Locked")
                    .setIcon(R.drawable.car)
                    .create();
            myAlert.show();
        }
    }

    //----------------------------driver rejected request after it was accepted
    public void onCancelSessionClick(View v ) {
        String cancel_phone1;
        Cursor c = notiDb.getNotiData();
        if(c.getCount()>0){
            while (c.moveToNext()){
                cancel_phone1 = c.getString(4);
                checkThread.interrupt();
                String type = "cancelReq";
                Log.e("passPhoneGlobal-cancelg",passPhoneLocalGlobal);

                Log.e("omsir","---cancel_accept 0,passPhone "+cancel_phone1+"driverPhone "+myPhone );

                PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
                backgroundWorkers.execute(type, "0", cancel_phone1, myPhone);

            }
        }
       /* Cursor resaa = notiDb.getNotiCancelData(db_id);
        if (resaa.getCount() > 0) {
            Toast.makeText(CheckNotification.this, "Found to cancel", Toast.LENGTH_SHORT).show();
            while (resaa.moveToNext()) {
                cancel_phone1 = resaa.getString(4);
            }
        } */
        // openNotiDB();
        // retriveData();
    }
    //------------------------make payment
    public void makePayment(View v){
        Log.e("payment button","--------------received");

       /* AlertDialog.Builder alertDialogb = new AlertDialog.Builder(CheckNotification.this);
        alertDialogb.setTitle("Payment Request");
        alertDialogb.setMessage("Enter amount for payment");

        final EditText inputb = new EditText(CheckNotification.this);
        LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        inputb.setLayoutParams(lpb);
        alertDialogb.setView(inputb);
        alertDialogb.setIcon(R.drawable.myridec);
        alertDialogb.setPositiveButton("Continue",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        codeb = inputb.getText().toString();
                        if (!codeb.equals("")) {
                            doPayment(Integer.parseInt(codeb));
                        } else {
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(CheckNotification.this);
                            myAlert.setMessage("Amount cannot be empty")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle("Request Payment")
                                    .setIcon(R.drawable.myridec)
                                    .create();
                            myAlert.show();
                        }
                    }
                });

        alertDialogb.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialogb.show();*/
        doPayment(bargainAmount);
    }

    //----------------------------finding driver and passenger location on map
    public void onSessionMapClick(View v){
        String ids = db_id;
        Log.e("BIKASHDAKA1","sds");

        //---notification table bata accepted wala request refer garera data li-----------------------------------------------------------------------------------
        Cursor cursor = notiDb.getNotiData();
        //RETURNS ONLY 1 DRIVER INFO coz cursor.getCount is 1 always
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Log.e("BIKASHDAKA1st",cursor.getString(9));

                String SQLite_driver_id = cursor.getString(0); //driverId
                Log.e("SQLite id kun aayo",String.valueOf(SQLite_driver_id));
                String salutes = cursor.getString(1);
                Log.e("salutes k aayo",String.valueOf(salutes));
                //===============================take only accepted one
                String statusOfAcceptance = cursor.getString(11);
                Log.e("BIKASHDAKASTAtUS",String.valueOf(statusOfAcceptance));
                if(statusOfAcceptance.equals("Accepted")){
                    GlobalValues.globalAcceptedPassengerNumber =  cursor.getString(4);
                    Log.e("BIKASHDAKA3st",cursor.getString(4));


                }
                Log.e("BIKASHDAKAaccepted xa?",String.valueOf(statusOfAcceptance));

                //-------------------------------receiving my info
            }
        }

        Cursor mydetail = userDb.getUserData();
        if (mydetail.getCount() > 0) {
            while (mydetail.moveToNext()) {   //yo kina gareko bhanda afno leko
                // String userFName = cursor.getString(2);
                // String userLName = cursor.getString(3);
                String driverUserName = mydetail.getString(9);
                String driver_phone = mydetail.getString(5);
                Log.e("POS1",String.valueOf(driver_phone));
                GlobalValues.driverSideDriverPhone = driver_phone;

                Log.e("name ho ta",String.valueOf(driverUserName));
                String userLati = mydetail.getString(12); //google map ma check gareko yo user nai ho driver haina
                String userLoni =mydetail.getString(13);

                Log.e("THIS_IS",String.valueOf(userLati));
                Log.e("THIS_IS",String.valueOf(userLoni));
                Log.e("Is this you? ",String.valueOf(driverUserName));
                Intent intent = new Intent(CheckNotification.this,ShowPassengerOnMap.class);
                startActivity(intent);


            }

        }else{
            Toast.makeText(CheckNotification.this, "nothing found", Toast.LENGTH_SHORT).show();
        }

    }

    //--------------------------------driver accepts request
    private void acceptRequest(String db_id){
        Cursor resaa = notiDb.getNotiCancelData(db_id);
        if (resaa.getCount() > 0) {
            //Toast.makeText(CheckNotification.this, "found", Toast.LENGTH_SHORT).show();
            //==================================SET VALUES on server
            try{
                Log.e("1112","222");

                LocationManager locationManager = (LocationManager)
                        getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                Location location = locationManager.getLastKnownLocation(locationManager
                        .getBestProvider(criteria, false));
                GlobalValues.driverSideStartingLat = String.valueOf(location.getLatitude());
                GlobalValues.driverSideStartingLon = String.valueOf(location.getLongitude());
                Log.e("HYYYY","ll");
                SetStartingValues bg = new SetStartingValues(this);
                Log.e("1111122222",String.valueOf(GlobalValues.driverSideStartingLat));
                Log.e("1111122222",user_name);
                bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

            }
            catch (Exception e){
                Log.e("1113","222");
                GPSTracker gpsTracker = new GPSTracker(CheckNotification.this);
                GlobalValues.driverSideStartingLat = String.valueOf(gpsTracker.getLatitude());
                GlobalValues.driverSideStartingLon = String.valueOf(gpsTracker.getLongitude());
                Log.e("HYYYY","22");
                SetStartingValues bg = new SetStartingValues(this);
                bg.execute("setDriverStart",GlobalValues.driverSideStartingLat,GlobalValues.driverSideStartingLon,user_name);

            }


            //==============================================
            while (resaa.moveToNext()) {
                accept_phone = resaa.getString(4);
                accept_name = resaa.getString(2);
                accept_id = resaa.getString(0);

            }
        }
        Log.e("myPhone", myPhone);
        String type = "acceptReq";
        //==================get driver id
        Cursor c = userDb.getUserData();
        while (c.moveToNext()){
            Log.e("GAURIKAUSEID",String.valueOf(c.getString(9)));
            GlobalValues.globalDriverUseId = c.getString(9);

        }
        //=================
        notiDb.updateNotiData(accept_id,"Accepted");

        //layout1.setVisibility(View.VISIBLE);
        //==================================
        Log.e("ANIL",String.valueOf(accept_phone));
        Log.e("ANIL",String.valueOf(myPhone));
        PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
        backgroundWorkers.execute(type, accept_id, accept_phone, myPhone,"ACCEPTED");
        Log.e("accepted request of","---------------------------"+accept_name);
        //=============================================================================================================

        //DO HERE--------HAHAHAHHAHAHAHHAHAHHA just now realized that hamle hya leko id ta noti db ma xa
        //tei lai reference gare bhaigo ni
        //last ris uthyo aile ta
        //yo maile 2 days aghi nai gareklo aile code nai xaena katai ni
        Cursor c1 = notiDb.getNotiData();
        while(c1.moveToNext()){
            listDelete.add(c1.getString(4));
        }

        for(int i = 0;i<listDelete.size();i++){
            if(!accept_phone.equals(listDelete.get(i))){

                //===================================
                //delete from SQLIte
                int z = notiDb.deleteNotiDataq(String.valueOf(listDelete.get(i)));
                //====================================
                //delete from server now
                String type1 = "cancelReq";
                PushNotification backgroundWorkersThis = new PushNotification(CheckNotification.this);
                backgroundWorkersThis.execute(type1, "0", String.valueOf(listDelete.get(i)), myPhone);

            }
        }
        //=========================================================starting address record garne aba
        //=================================================
    }

    //--------------------------------if driver balance is low than 35
    private void doPaypalReserve(int amount) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(amount), "USD", "Balance Purchase for Metrocabv2");

        Intent intent = new Intent(this, PaymentActivity.class);

        // comment this line out for live or set to PaymentActivity.ENVIRONMENT_SANDBOX for sandbox
        //intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_NO_NETWORK);// for live
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);

        // it's important to repeat the clientId here so that the SDK has it if Android restarts your
        // app midway through the payment UI flow.
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "credential-from-developer.paypal.com");

        // Provide a payerId that uniquely identifies a user within the scope of your system,
        // such as an email address or user ID.
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID, "dipesh@metrocab.com");

        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "main@metrocab.com");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Toast.makeText(CheckNotification.this, "payment Success", Toast.LENGTH_SHORT).show();
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
                Log.e("paypal value","------------"+codeb+", "+user_name);
                String type="buy_money";
                Transaction background = new Transaction(this);
                background.execute(type, codeb, user_name,"");
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
        }
    }


    private void doPayment(int balance) {
        Log.e("doPayment","-------------------pass phone: "+phone+"---driver phone: "+myPhone);
        PushNotification backgroundWorkers = new PushNotification(CheckNotification.this);
        backgroundWorkers.execute("makePayment", String.valueOf(balance), phone, myPhone,"driver","accept");
    }

    @Override
    public void onBackPressed() {

        checkThread.interrupt();
        Boolean xxx = GlobalValues.driverLaiSessionOn;
        if(xxx == true) {
            Log.e("SESSIONMANENA", "hai");
            return;
        }
        else{
            super.onBackPressed();

        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


}
