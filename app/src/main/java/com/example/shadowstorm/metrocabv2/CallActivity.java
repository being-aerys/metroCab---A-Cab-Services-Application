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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

import static java.lang.Thread.sleep;
/**
 * Created by Dipesh on 03/06/16.
 */
public class CallActivity extends Activity {

    String db_id,paypal_id,destToPass,paymentAmt,currencyCode,shortDescription,user_name,codeb;
    int advance;
    String balance;
    DriversLocation_db driverDb;
    User_database myDb;
    private RequestQueue requestQueue;
    String destPass;
    EditText ed ;
    Thread thread;
    Button resp2;
    String userNameN;
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_activity);
        ed = (EditText)findViewById(R.id.desti);
      //  resp2 = (Button)findViewById(R.id.responseStatus);
        //-----------------------------------------FOR AUTHORIZING THE DEVICE TO TOP UP

        Intent intent = new Intent(this, PayPalService.class);
        // live: don't put any environment extra
        // sandbox: use PaymentActivity.ENVIRONMENT_SANDBOX
        intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, PaymentActivity.ENVIRONMENT_SANDBOX);
        intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, "ASLsrydUpUh3G8N2IobYjZjg7Nqty5t681ckV0cXKE9pD-uEImj79LcHOpUhrGSS8YQhBYwMIoLVJ1yq");
        startService(intent);
        //===============================================================
        driverDb = new DriversLocation_db(this);
        myDb =new User_database(this);
//================================
        db_id = getIntent().getStringExtra("db_id");
        GlobalValues.db_id_selected_by_passenger = db_id;
        //=================

        requestQueue = Volley.newRequestQueue(this);


        getUserDatas(db_id);
        //changeResponseStatus(resp2);


        thread = new Thread(new Runnable() {

            @Override
            public void run() {

                while(true){
                    Log.e("CCCC","CCC");


                    //============this setStatusAtCallActivity can be used to show driver response at call activity itself but since it
                    //hasnt been used at the final UI lets just comment it out

                 //   setStatusAtCallActivity();
                    Log.e("SEE","HERE");

                    try {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("callActivity thread", "-------------------------started");

                                int isAccepted = GlobalValues.isAccepted;
                                int isBargain = GlobalValues.isBargain;
                                if (isAccepted > 0) {
                                    GlobalValues.isAccepted=0;
                                    //thread.interrupt();
                                    Intent intent = new Intent(CallActivity.this, Message.class);
                                    startActivity(intent);
                                }
                                if (isBargain > 0) {
                                    GlobalValues.isBargain=0;
                                    //thread.interrupt();
                                    Intent intent = new Intent(CallActivity.this, Message.class);
                                    startActivity(intent);
                                }
                               /* Cursor ress = myDb.getUserData();
                                if (ress.getCount() > 0) {
                                    while (ress.moveToNext()) {
                                        String driverPhone = ress.getString(18);
                                        if (!driverPhone.equals(null)) {
                                            Intent intent = new Intent(callActivity.this, Message.class);
                                            startActivity(intent);
                                        }
                                    }
                                }*/
                            }

                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.e("thread", "interrupted");
                        break;
                    }


                }

            }
        });
        thread.start();



    }

    private void getUserDatas(String db_id) {
        Cursor cursor = driverDb.getDriverDataById(db_id);
        Log.e("CCCC","CCC");


        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(0);
                String salutes = cursor.getString(1);
                Log.e("salute",String.valueOf(salutes));
                String fnames = cursor.getString(2);
                String lnames = cursor.getString(3);
                String nation = cursor.getString(4);
                String phone = cursor.getString(5);
                String actype = cursor.getString(6);
                String license = cursor.getString(7);
                String vehicle = cursor.getString(8);
                String status = cursor.getString(9);
                String lat = cursor.getString(10);
                Log.e("LATSQL2",String.valueOf(lat));
                String lon = cursor.getString(11);
                String dist = cursor.getString(12);

                //================================================================
                //================================= Assign global Values

                GlobalValues.globalDriverFName=fnames;
                GlobalValues.globalDriverLName=lnames;
                GlobalValues.globalDriverLat=lat;
                GlobalValues.globalDriverLon=lon;


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

                        Toast.makeText(CallActivity.this, "Android Volley Error on System", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(CallActivity.this, "Found no data. Reset App.", Toast.LENGTH_SHORT).show();
        }


        try {
            sleep((1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    public void sendNotiToDriver(View v){

        //=====================================
        Cursor ress = myDb.getUserData();

        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                user_name = ress.getString(9);

                balance = ress.getString(14);//----------------------changed here
                paypal_id = ress.getString(16);//----------------------changed here

                if (Float.valueOf(balance) < 35) {
                    AlertDialog.Builder alertDialogb = new AlertDialog.Builder(CallActivity.this);
                    alertDialogb.setTitle("Balance Low");
                    alertDialogb.setMessage("Your balance is getting low\nEnter amount to add your balance");

                    final EditText inputb = new EditText(CallActivity.this);
                    LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    inputb.setLayoutParams(lpb);
                    alertDialogb.setView(inputb);
                    alertDialogb.setIcon(R.drawable.myridec1);
                    alertDialogb.setPositiveButton("Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    codeb = inputb.getText().toString();
                                    Log.e("entered amount","---------------------"+codeb);
                                    if (!codeb.equals("")) {
                                        doPaypalReserve(Integer.parseInt(codeb));
                                    } else {
                                        AlertDialog.Builder myAlert = new AlertDialog.Builder(CallActivity.this);
                                        myAlert.setMessage("Amount cannot be empty")
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setTitle("Balance Low")
                                                .setIcon(R.drawable.myridec1)
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
                    acceptRequest();
                }
            }
        }

        //==================================IN CASE THE PASSENGER DOESNT RECEIVE NOTIbecause of reasons
        //we gotta set the starting point anyway even before he receives the confirming noti

        User_database userDb = new User_database(this);
        //for SQLite db we need cursor and its adapter
        Cursor res = userDb.getUserData();
        Log.e(String.valueOf(res),"cursor");
        if (res.getCount() > 0) {
            while (res.moveToNext()) {
                userNameN = res.getString(9);
                //-------------------
            }
        }

        try{
            Log.e("1112","222");


            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            GlobalValues.passenSideStartingLat = String.valueOf(location.getLatitude());
            GlobalValues.passenSideStartingLon = String.valueOf(location.getLongitude());




            //=============================SETTING STARTING VALS
            SetStartingValues backgroundWorkers = new SetStartingValues(this);
            backgroundWorkers.execute("setPassengerStart",  GlobalValues.passenSideStartingLat,  GlobalValues.passenSideStartingLon,userNameN);



        }
        catch (Exception e){
            Log.e("1113","222");
            GPSTracker gpsTracker = new GPSTracker(this);
            GlobalValues.passenSideStartingLat = String.valueOf(gpsTracker.getLatitude());
            GlobalValues.passenSideStartingLon = String.valueOf(gpsTracker.getLongitude());
            SetStartingValues backgroundWorkers = new SetStartingValues(this);
            backgroundWorkers.execute("setPassengerStart",  GlobalValues.passenSideStartingLat,  GlobalValues.passenSideStartingLon,userNameN);


        }



    }

    public void showDriverOnMapMethod(View v){
        String ids = db_id;

        //--------------------------------------------------------------------------------------
        Cursor cursor = driverDb.getDriverDataById(ids);  //RETURNS ONLY 1 DRIVER INFO coz cursor.getCount is 1 always
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String SQLite_driver_id = cursor.getString(0); //driverId
                String salutes = cursor.getString(1);
                String driverFname = cursor.getString(2);
                String driverLname = cursor.getString(3);
                String driverIdOfServer = cursor.getString(13);

                GlobalValues.globalDriverUseId = driverIdOfServer;

                Toast.makeText(this,"Is this the driver? "+String.valueOf(driverFname),Toast.LENGTH_SHORT).show();

                String nation = cursor.getString(4);
                String phone = cursor.getString(5);
                String actype = cursor.getString(6);
                String license = cursor.getString(7);
                String vehicle = cursor.getString(8);
                String status = cursor.getString(9);
                String driverLati = cursor.getString(10);
                String driverLoni = cursor.getString(11);
                //-------------------------------receiving my info
                Cursor mydetail = myDb.getUserData();
                if (mydetail.getCount() > 0) {
                    while (mydetail.moveToNext()) {   //yo kina gareko bhanda afno leko
                        String userFName = cursor.getString(2);
                        String userLName = cursor.getString(3);
                        String passengerUserName = mydetail.getString(9);
                        Log.e("name ho ta",String.valueOf(passengerUserName));
                        String userLati = mydetail.getString(12); //google map ma check gareko yo user nai ho driver haina
                        String userLoni =mydetail.getString(13);
                        Log.e("THIS_IS",String.valueOf(userLati));
                        Log.e("THIS_IS",String.valueOf(userLoni));
                        //Toast.makeText(this,"THIS IS "+String.valueOf(userLati),Toast.LENGTH_SHORT).show();
                        Log.e("Is this you? ",String.valueOf(passengerUserName));
                        Intent intent = new Intent(CallActivity.this,ShowDriverOnMap.class);


                        //=========================================================================
                        //Bundle user nagari baru global variable bata value lini map fragment ma
                        //====================Yeha bata pani Global variables assign nagareko coz while loop xaena
                        //DriverMap bata gara
                              /*
                        GlobalValues.globalPassenger= passengerUserName;
                        GlobalValues.globalPassengerLat= String.valueOf(userLati);
                        GlobalValues.globalPassengerLon= String.valueOf(userLoni);
                        GlobalValues.globalDriverFName=driverFname;
                        GlobalValues.globalDriverLName=driverLname;
                        GlobalValues.globalDriverLat=driverLati;
                        GlobalValues.globalDriverLon=driverLoni;

                        //LOG CHECK
                        Log.e("LOG CHECK",passengerUserName);
                        Log.e("LOG CHECK",String.valueOf(userLati));

                        Log.e("LOG CHECK",String.valueOf(userLoni);

                        Log.e("LOG CHECK",driverFname);
                        Log.e("LOG CHECK",driverLati);
                        Log.e("LOG CHECK",driverLoni);
                        */














                        /*
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
                        */
                        startActivity(intent);


                    }
                }
            }
        }else{
            Toast.makeText(CallActivity.this, "Found nothing", Toast.LENGTH_SHORT).show();
        }

    }


    public  void setStatusAtCallActivity(){
        ;//gcm.driverResponse
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String jupi = GCMPushReceiverService.driverResponse;

                //gcm.getResp();
                if (jupi != null){
                    Log.e("HERE k payo ",jupi);
                    //=====we get the response and set text for the status but now we comment coz we dont need it
                    //===but u know it took me more than 24 hours to build and its not even being used now shit

                    //resp2.setText(jupi);
                }
                else {
                    A.t(CallActivity.this,"else ma gayo");
                }

                //return  String.valueOf(gcm.driverResponse);


            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        thread.interrupt();
    }

    private void acceptRequest(){
        String ids = db_id;
        destPass = String.valueOf(ed.getText());

        Cursor cursor = driverDb.getDriverDataById(ids);
        Log.e("HERE","HERE");
        if (cursor.getCount() > 0) {
            Log.e("HERE","HER1E");

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

                    Log.e("HERE","HER3E");

                    while (mydetail.moveToNext()) {
                        String myPhone = mydetail.getString(5);
                        String latQ = mydetail.getString(12);
                        Log.e("NOTICE",fnames);
                        Log.e("NOTICE   my phone",myPhone);

                        Log.e("NOTICE  not my phone",phone);

                        String type="sendReq";


                        destToPass = destPass;
                        Log.e("DEST",String.valueOf(destToPass));
                        if(!destToPass.equals("")) {
                            PushNotification backgroundWorkers = new PushNotification(CallActivity.this);
                            backgroundWorkers.execute(type, id, phone, myPhone, destToPass);
                            //changeResponseStatus(resp2);
                        }else{
                            AlertDialog.Builder myAlert = new AlertDialog.Builder(CallActivity.this);
                            myAlert.setMessage("Enter the destination ")
                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialog, int which){
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle("Call Cab")
                                    .setIcon(R.drawable.myridec1)
                                    .create();
                            myAlert.show();
                        }
                    }
                }
            }
        }else{
            Toast.makeText(CallActivity.this, "nothing found", Toast.LENGTH_SHORT).show();
        }
    }

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

     /*   User_database userDb = new User_database(this);
        userDb.getWritableDatabase();
        Cursor c = userDb.getUserData();
        String paypalId = "zzzzz";
        while (c.moveToNext()){
            paypalId = c.getString(16);
        }
        */
        intent.putExtra(PaymentActivity.EXTRA_PAYER_ID,"buyer@metrocab.com");


        intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL, "greenview@hotel.com");
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, 0);

    }
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            acceptRequest();
            Log.e("glenmark1",String.valueOf(requestCode));
            Log.e("glenmark2",String.valueOf(resultCode));
            Log.e("glenmark3",String.valueOf(data));
            Toast.makeText(CallActivity.this, "payment Success", Toast.LENGTH_SHORT).show();
            PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if (confirm != null) {
                try {
                    Log.i("paymentExample", confirm.toJSONObject().toString(4));

                    // //=============================PARSE THE JSON RESULT
                    try{
                        JSONObject j = new JSONObject(confirm.toJSONObject().toString(4));
                        JSONArray ja = j.getJSONArray("payment");
                        int count = 0;
                        while(count<ja.length()){
                            JSONObject jo= ja.getJSONObject(count);
                            paymentAmt = jo.getString("amount");
                            currencyCode = jo.getString("currency_code");
                            shortDescription = jo.getString("short_description");

                        }
                    }
                    catch(Exception E){

                    }
                    Log.e("paypal value","------------"+codeb+", "+user_name);
                    String type="buy_money";

                    Log.e("glenmark4",String.valueOf(codeb));
                    Transaction background = new Transaction(this);
                    background.execute(type, codeb, user_name, shortDescription);


                    //====================================================

                    // TODO: send 'confirm' to your server for verification.
                    // see https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                    // for more details.

                } catch (JSONException e) {
                    Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                }
            }
        }
        else if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("paymentExample", "The user canceled.");
        }
        else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
            Log.i("paymentExample", "An invalid payment was submitted. Please see the docs.");
        }
    }


    private void bargain(){
        GlobalValues.isBargain=0;
        Cursor cursor = myDb.getUserData();
        if (cursor.getCount() > 0) {
            //-------------------------------passenger  info
            while (cursor.moveToNext()) {
                final String passPhone = cursor.getString(5);
                final String driverPhone = cursor.getString(18);
                final String amount = cursor.getString(21);
                final String status = cursor.getString(22);

                final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setMessage("The driver is requesting you to pay $"+amount+" as cab charge.\nPress ACCEPT to accept the request")
                        .setPositiveButton("ACCEPT",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                String type="bargainByPass";
                                Bargain backgroundWorkers = new Bargain(CallActivity.this);
                                backgroundWorkers.execute(type, passPhone, driverPhone,amount,"accept");
                            }
                        })
                        .setNegativeButton("REJECT",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                AlertDialog.Builder myAlerta = new AlertDialog.Builder(CallActivity.this);
                                final EditText inputb = new EditText(CallActivity.this);
                                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                inputb.setLayoutParams(lpb);
                                myAlerta.setView(inputb);
                                myAlerta.setMessage("Either you can suggest how much you want to pay\nPress REJECT to cancel the request with driver")
                                        .setPositiveButton("CONTINUE",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                                codeb = inputb.getText().toString();
                                                Log.e("entered amount","---------------------"+codeb);
                                                if (!codeb.equals("")) {
                                                    Log.e("callActivity bargain","-----passPhone :"+passPhone+"--driver phone:"+driverPhone+"---amount and status"+amount+"/"+status);
                                                    String type="bargainByPass";
                                                    Bargain backgroundWorkers = new Bargain(CallActivity.this);
                                                    backgroundWorkers.execute(type, passPhone, driverPhone, codeb, "reject");
                                                } else {
                                                    AlertDialog.Builder myAlertb = new AlertDialog.Builder(CallActivity.this);
                                                    myAlertb.setMessage("Amount cannot be empty")
                                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    GlobalValues.isBargain=1;
                                                                }
                                                            })
                                                            .setTitle("Cab payment request")
                                                            .setIcon(R.drawable.myridec1)
                                                            .setCancelable(false)
                                                            .create();
                                                    myAlertb.show();
                                                }
                                            }
                                        })
                                        .setNegativeButton("REJECT",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                                String type="cancelPengingRequestByPassenger";
                                                PushNotification backgroundWorkers = new PushNotification(CallActivity.this);
                                                backgroundWorkers.execute(type, passPhone, driverPhone,"");
                                            }
                                        })
                                        .setNeutralButton("CLOSE",new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                dialog.dismiss();
                                                GlobalValues.isBargain=1;
                                            }
                                        })
                                        .setTitle("Cab payment request")
                                        .setIcon(R.drawable.myridec1)
                                        .setCancelable(false)
                                        .create();
                                myAlerta.show();
                            }
                        })
                        .setNeutralButton("CLOSE",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                GlobalValues.isBargain=1;
                            }
                        })
                        .setTitle("Cab payment request")
                        .setIcon(R.drawable.myridec1)
                        .setCancelable(false)
                        .create();
                myAlert.show();
                //changeResponseStatus(resp2);
            }
        }
    }



    @Override
    public void onDestroy(){
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
        thread.interrupt();
    }
}