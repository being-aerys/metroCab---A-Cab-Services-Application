package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import static java.lang.Thread.sleep;

/**
 * Created by Dipesh on 06/08/16.
 */
public class Message extends Activity{

    Thread checkThread;
    User_database userDb;
    String driverPhone,passPhone;
    String type_b,bargainStatus,codeb;
    int session,sessionArrived,sessionCompleted,bargainAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Message", "-------------------------------------receive");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        userDb =new User_database(this);
        //DriversLocation_db driverDb;

        /*Intent iin= getIntent();
        Bundle dataa = iin.getExtras();
        String session =(String) dataa.get("session");
        String pessenger =(String) dataa.get("pessenger");
        Log.e("message","-------------------------------pessenger"+pessenger);
        Log.e("message","-------------------------------session"+session);*/
        //==========================================

        checkThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true){
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //invalidateOptionsMenu();
                                Log.e("HERE","JUPI");
                                int isBargain = GlobalValues.isBargain;
                                int isLoaded = GlobalValues.isLoaded;
                                int isAccepted = GlobalValues.isAccepted;
                                int checkPayment = GlobalValues.checkPaymentStatus;
                                int isArrived = GlobalValues.isArrived;
                                int isCompleted = GlobalValues.isCompleted;
                                int isSessionComplete = GlobalValues.isSessionComplete;
                                Log.e("isSessionComplete","-------------"+isSessionComplete);
                                Log.e("isbargain","-------------"+isBargain);
                                Log.e("isLoaded","-------------"+isLoaded);
                                Log.e("isAccepted","-------------"+isAccepted);
                                Log.e("checkPayment","-------------"+checkPayment);
                                Log.e("isArrived","-------------"+isArrived);
                                Log.e("isCompleted","-------------"+isCompleted);
                                if(isLoaded > 0){
                                    GlobalValues.isLoaded=0;
                                    loadContext();
                                }
                                if(isBargain > 0){
                                    GlobalValues.isBargain=0;
                                    loadContext();
                                }
                                if(isAccepted > 0){
                                    GlobalValues.isAccepted=0;
                                    loadContext();
                                }
                                if(isArrived > 0){
                                    Log.e("SAMMY","isArr");
                                    detdata();
                                }
                                if(isCompleted > 0){
                                    Log.e("SAMMY","isComp");
                                    detdata();
                                }
                                if(checkPayment > 0){
                                    Log.e("SAMMY","checkPayment");
                                    detdata();
                                }
                                if(isSessionComplete > 0){
                                    GlobalValues.isSessionComplete=0;
                                    checkThread.interrupt();
                                    Intent intent = new Intent(Message.this, MapsActivity.class);
                                    startActivity(intent);
                                }
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

    private class ReadTask extends AsyncTask<String, Void, String> {
        Context context;
        ReadTask(Context ctx) {
            context = ctx;
        }
        @Override
        protected String doInBackground(String... params) {

            String type = params[0];


            String login_url = GlobalValues.MyAppUrl + "/metrocab/gcm/checkSession.php";
            if(type.equals("checkArrived") || type.equals("checkCompleted") || type.equals("retriveSession")) {
                String passPhone = params[1];
                String driverPhone = params[2];
                Log.e("message","--------------------------------------"+passPhone+" "+driverPhone);
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                            + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8") + "&"
                            + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passPhone, "UTF-8");

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
            if(type.equals("confirmArrived") || type.equals("confirmCompleted")) {
                String passPhone = params[1];
                String driverPhone = params[2];
                String YesNo = params[3];
                try {
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type, "UTF-8") + "&"
                            + URLEncoder.encode("driverPhone", "UTF-8") + "=" + URLEncoder.encode(driverPhone, "UTF-8") + "&"
                            + URLEncoder.encode("passPhone", "UTF-8") + "=" + URLEncoder.encode(passPhone, "UTF-8") + "&"
                            + URLEncoder.encode("yesno", "UTF-8") + "=" + URLEncoder.encode(YesNo, "UTF-8");

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

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            Log.e("Message session","**************"+result);
            String[] items = result.split("]/");
            String type = items[0];
            if(type.equals("isArrived")){type_b="confirmArrived";}
            if(type.equals("isCompleted")){type_b="confirmCompleted";}
            if(type.equals("isArrived") || type.equals("isCompleted")){
                final String driverPhone = items[1];
                final String passPhone = items[2];
                String msga = items[3];
                String msgb = items[4];
                // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                AlertDialog.Builder myAlert = new AlertDialog.Builder(context);
                myAlert.setMessage(msga+"\n"+msgb)
                        .setPositiveButton("YES",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                ReadTask backgroundWorkers = new ReadTask(Message.this);
                                backgroundWorkers.execute(type_b, passPhone, driverPhone,"Yes");
                            }
                        })
                        .setNegativeButton("NO",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                ReadTask backgroundWorkers = new ReadTask(Message.this);
                                backgroundWorkers.execute(type_b, passPhone, driverPhone,"No");

                            }
                        })
                        .setTitle("METROCAB")
                        .setIcon(R.drawable.myridec1)
                        .create();
                myAlert.show();

            }
            if(type.equals("sessionResult")){
                try{
                    Log.e("sessionResult","------------------------------------------------------------message.java loaded");
                    String fullname = items[1];
                    String status = items[2];
                    String destination = items[3];

                    Thread thr = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thr.start();

                    TextView name = (TextView) findViewById(R.id.msgSalute);
                    TextView statuss = (TextView) findViewById(R.id.msgStatus);
                    TextView contact = (TextView) findViewById(R.id.msgContact);
                    TextView desti = (TextView) findViewById(R.id.msgDesti);
                    name.setText(fullname);
                    statuss.setText(status);
                    contact.setText(driverPhone);
                    desti.setText(destination);
                }
                catch (Exception E){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context,"data insertion failed but all is fine",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
            if(type.equals("ArrivedSuccess")){
                String passPhone = items[1];
                boolean isUpdated = userDb.updateUserSessonArrived(passPhone, "2");
                if (isUpdated == true) {
                    Log.e("DBUpdated with msg : ", "----------------arrived completed"+passPhone);
                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            }
            if(type.equals("CompletedSuccess")){
                String passPhone = items[1];
                boolean isUpdated = userDb.updateUserSessonComplete(passPhone, "2");
                if (isUpdated == true) {
                    Log.e("DBUpdated with msg : ", "----------------arrived completed"+passPhone);
                } else {
                    Log.e("DBUpdated with msg : ", "----------------------------not updated");
                }
            }
        }
    }

    public void detdata(){
        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                passPhone = ress.getString(5);
                session = Integer.parseInt(ress.getString(17));
                driverPhone = ress.getString(18);
                sessionArrived = Integer.parseInt(ress.getString(19));
                sessionCompleted = Integer.parseInt(ress.getString(20));
                try{
                    bargainAmount = Integer.parseInt(ress.getString(21));

                }
                catch (Exception e){

                }
                bargainStatus = ress.getString(22);
                if(bargainStatus.equals("NEW")) {
                    Log.e("SAMMY","332");
                    checkBargain(String.valueOf(bargainAmount),driverPhone,passPhone);
                    // bargainStatus = "OLD";
                }
                if(sessionArrived > 0 ){
                    if(sessionCompleted > 0 ){
                        if(sessionCompleted>1){
                        }else{
                            GlobalValues.isCompleted=0;
                            ReadTask backgroundWorkers = new ReadTask(Message.this);
                            backgroundWorkers.execute("checkCompleted", passPhone, driverPhone);
                            Log.e("Message.java", "------------------- checkCompleted");
                        }
                    }else{
                        GlobalValues.isArrived=0;
                        ReadTask backgroundWorkers = new ReadTask(Message.this);
                        backgroundWorkers.execute("checkArrived", passPhone, driverPhone);
                        Log.e("Message.java","------------------- checkArrived");

                    }
                }
                ReadTask backgroundWorkers = new ReadTask(Message.this);
                backgroundWorkers.execute("retriveSession", passPhone, driverPhone);
                Log.e("Message.java","------------------- retriveSession");
                Log.e("message status","------------"+bargainStatus);
            }
        }
    }
    public void loadContext(){
        Cursor ress = userDb.getUserData();
        if (ress.getCount() > 0) {
            while (ress.moveToNext()) {
                passPhone = ress.getString(5);
                driverPhone = ress.getString(18);
                try{
                    bargainAmount = Integer.parseInt(ress.getString(21));
                }catch(Exception e){

                }

                bargainStatus = ress.getString(22);
                if(bargainStatus.equals("NEW")) {
                    checkBargain(String.valueOf(bargainAmount),driverPhone,passPhone);
                }

                try {
                    sleep(1000);
                    ReadTask backgroundWorkers = new ReadTask(Message.this);
                    backgroundWorkers.execute("retriveSession", passPhone, driverPhone);
                    Log.e("Message.java","------------------- retriveSession== Diver:"+driverPhone+", Passphone:"+passPhone);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void goHome(View v){
        //Toast.makeText(context, "deleted and Insert success", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
    public void msgReport(View v){

    }
    public void msgReject(View v){
        PushNotification backgroundWorkers = new PushNotification(this);
        backgroundWorkers.execute("cancelPengingRequestByPassenger", driverPhone, passPhone,"cancelPengingRequestByPassenger");
    }
    public void termsCondition(View v){
        Log.e("terms and condition","-----------------------received");
    }
    public void checkBargain(final String amount, String driver_phone, String pass_phone){
        GlobalValues.checkPaymentStatus=0;
        Log.e("checkBargain","-----------------------received");
        Log.e("SAMMY","406");
        final AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
        myAlert.setMessage("The driver is requesting you to pay $"+amount+" as cab charge.\nPress ACCEPT to accept the request")
                .setPositiveButton("ACCEPT",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        String type="bargainByPass";
                        Bargain backgroundWorkers = new Bargain(Message.this);
                        backgroundWorkers.execute(type, passPhone, driverPhone,amount,"accept");
                    }
                })
                .setNegativeButton("REJECT",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                        AlertDialog.Builder myAlerta = new AlertDialog.Builder(Message.this);
                        final EditText inputb = new EditText(Message.this);
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
                                            Log.e("CallActivity bargain","-----passPhone :"+passPhone+"--driver phone:"+driverPhone+"---amount and status"+amount+"/"+bargainStatus);
                                            String type="bargainByPass";
                                            Bargain backgroundWorkers = new Bargain(Message.this);
                                            backgroundWorkers.execute(type, passPhone, driverPhone, codeb, "reject");
                                        } else {
                                            AlertDialog.Builder myAlertb = new AlertDialog.Builder(Message.this);
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
                                        Log.e("CBRBP","--------------received");
                                        PushNotification backgroundWorkers = new PushNotification(Message.this);
                                        backgroundWorkers.execute("cancelPengingRequestByPassenger", driverPhone, passPhone,"cancelPengingRequestByPassenger");

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
    }
    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // moveTaskToBack(true); This line minimizes the app but this doesnt work in co-ordination with login check so
        //dont use moveTaskToBack() instrad just remove the functionality of the back button in this activity
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalValues.isLoaded=1;
    }


    @Override
    protected void onPause() {
        super.onPause();
        GlobalValues.isLoaded=1;
        checkThread.interrupt();
    }
    public void showDriverOnMapMethod(View v){
        try{
            String ids = GlobalValues.db_id_selected_by_passenger;
            Log.e("MESSAGE1",String.valueOf(ids));

            //--------------------------------------------------------------------------------------
            DriversLocation_db driverDb;
            driverDb =new DriversLocation_db(this);

            Cursor cursor = driverDb.getDriverDataById(ids);  //RETURNS ONLY 1 DRIVER INFO coz cursor.getCount is 1 always
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String SQLite_driver_id = cursor.getString(0); //driverId
                    String salutes = cursor.getString(1);
                    String driverFname = cursor.getString(2);
                    String driverLname = cursor.getString(3);
                    String driverIdOfServer = cursor.getString(13);
                    Log.e("MESSAGE2",String.valueOf(driverIdOfServer));

                    GlobalValues.globalDriverUseId = driverIdOfServer;

                    Toast.makeText(this,"Is this the driver? "+String.valueOf(driverFname),Toast.LENGTH_SHORT).show();

                    Log.e("MESSAGE3",String.valueOf(driverFname));
                    String nation = cursor.getString(4);
                    String phone = cursor.getString(5);
                    String actype = cursor.getString(6);
                    String license = cursor.getString(7);
                    String vehicle = cursor.getString(8);
                    String status = cursor.getString(9);
                    String driverLati = cursor.getString(10);
                    String driverLoni = cursor.getString(11);
                    //-------------------------------receiving my info
                    Cursor mydetail = userDb.getUserData();
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
                            Toast.makeText(this,"THIS IS "+String.valueOf(userLati),Toast.LENGTH_SHORT).show();
                            Log.e("Is this you? ",String.valueOf(passengerUserName));
                            Intent intent = new Intent(this,ShowDriverOnMap.class);


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
                Toast.makeText(this, "nothing found", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),"Local database refreshed due to high memory usage. Driver data flushed. Cannot execute the action.",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

}

