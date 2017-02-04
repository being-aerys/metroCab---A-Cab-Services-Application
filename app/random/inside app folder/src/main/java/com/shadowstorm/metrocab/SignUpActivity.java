package com.shadowstorm.metrocab;


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.telephony.SmsManager;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.Spinner;
        import android.widget.Toast;

        import java.util.Random;

/**
 * Created by Dipesh on 13/05/16.
 */
public class SignUpActivity extends Activity {
    user_database userDb;
    EditText fname,lname,email,nation,phone,license,vehicle,username,password;
    Spinner salute,actype;
    Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        userDb= new user_database(this);

        fname = (EditText)findViewById(R.id.fname);
        lname = (EditText)findViewById(R.id.lname);
        nation = (EditText)findViewById(R.id.country);
        phone = (EditText)findViewById(R.id.phone);
        license = (EditText)findViewById(R.id.license);
        vehicle = (EditText)findViewById(R.id.vehicle);
        salute = (Spinner)findViewById(R.id.salute);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        actype = (Spinner)findViewById(R.id.actype);
        btnAddUser = (Button)findViewById(R.id.buttonCreateAccount);
        AddUser();
    }
    public void AddUser(){
        btnAddUser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String rfname = fname.getText().toString();
                        final String rlname = lname.getText().toString();
                        final String rcountry = nation.getText().toString();
                        final String rphone = phone.getText().toString();
                        final String rlicense = license.getText().toString();
                        final String rvehicle = vehicle.getText().toString();
                        final String rusername = username.getText().toString();
                        final String rpassword = password.getText().toString();
                        final String rsal = salute.getSelectedItem().toString();
                        final String ractype = actype.getSelectedItem().toString();
                        if (!rfname.equals("")) {
                            if (!rlname.equals("")) {
                                if (!rcountry.equals("")) {
                                    if (!rusername.equals("")) {
                                        if (!rpassword.equals("")) {
                                            if (!rsal.equals("")) {
                                                if(!rphone.equals("")) {
                                                    if (ractype.equals("Driver")) {
                                                        if (!rlicense.equals("")) {
                                                            if (!rvehicle.equals("")) {
                                                                int max = 9865;
                                                                int min = 1598;
                                                                final Random rand = new Random();
                                                                final String diceRoll = String.valueOf(rand.nextInt(max-min) + 1);


                                                                SmsManager manager = SmsManager.getDefault();
                                                                manager.sendTextMessage(rphone,null,diceRoll,null,null);

                                                                //-------------------------------------------------------------for confirm password field
                                                                AlertDialog.Builder alertDialogb = new AlertDialog.Builder(SignUpActivity.this);
                                                                alertDialogb.setTitle("Confirm phone");
                                                                alertDialogb.setMessage("Enter the code you received in sms");

                                                                final EditText inputb = new EditText(SignUpActivity.this);
                                                                LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                                                        LinearLayout.LayoutParams.MATCH_PARENT);
                                                                inputb.setLayoutParams(lpb);
                                                                alertDialogb.setView(inputb);
                                                                alertDialogb.setIcon(R.drawable.myridec);
                                                                alertDialogb.setPositiveButton("Continue",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                               String codeb = inputb.getText().toString();
                                                                                if (codeb.equals(diceRoll)) {
                                                                                    String type = "addAppUser";
                                                                                    user_datasql backgroundWorkers = new user_datasql(SignUpActivity.this);
                                                                                    backgroundWorkers.execute(type, rsal, rfname, rlname, rcountry,rphone,ractype,rlicense,rvehicle,rusername,rpassword);

                                                                                } else {
                                                                                    AlertDialog.Builder myAlert = new AlertDialog.Builder(SignUpActivity.this);
                                                                                    myAlert.setMessage("Code not matched")
                                                                                            .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which){
                                                                                                    dialog.dismiss();
                                                                                                }
                                                                                            })
                                                                                            .setTitle("myRide Admin")
                                                                                            .setIcon(R.drawable.myridec)
                                                                                            .create();
                                                                                    myAlert.show();
                                                                                }
                                                                            }
                                                                        });

                                                                alertDialogb.setNegativeButton("NO",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });

                                                                alertDialogb.show();

                                                           } else {Toast.makeText(SignUpActivity.this, "Vehicle Number is required for Drivers", Toast.LENGTH_LONG).show();                                                        }
                                                        } else {Toast.makeText(SignUpActivity.this, "License Number is required for Drivers", Toast.LENGTH_LONG).show();                                                    }
                                                    } else {

                                                        int max = 9865;
                                                        int min = 1598;
                                                        final Random rand = new Random();
                                                        final String diceRoll = String.valueOf(rand.nextInt(max-min) + 1);


                                                        SmsManager manager = SmsManager.getDefault();
                                                        manager.sendTextMessage(rphone,null,diceRoll,null,null);

                                                        //-------------------------------------------------------------for confirm password field
                                                        AlertDialog.Builder alertDialogb = new AlertDialog.Builder(SignUpActivity.this);
                                                        alertDialogb.setTitle("Confirm phone");
                                                        alertDialogb.setMessage("Enter the code you received in sms");

                                                        final EditText inputb = new EditText(SignUpActivity.this);
                                                        LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                                LinearLayout.LayoutParams.MATCH_PARENT);
                                                        inputb.setLayoutParams(lpb);
                                                        alertDialogb.setView(inputb);
                                                        alertDialogb.setIcon(R.drawable.myridec);
                                                        alertDialogb.setPositiveButton("Continue",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        String codeb = inputb.getText().toString();
                                                                        if (codeb.equals(diceRoll)) {
                                                                            String type = "addAppUser";
                                                                            user_datasql backgroundWorkers = new user_datasql(SignUpActivity.this);
                                                                            backgroundWorkers.execute(type, rsal, rfname, rlname, rcountry,rphone,ractype,rlicense,rvehicle,rusername,rpassword);

                                                                        } else { AlertDialog.Builder myAlert = new AlertDialog.Builder(SignUpActivity.this);
                                                                            myAlert.setMessage("Code not matched")
                                                                                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which){
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    })
                                                                                    .setTitle("myRide Admin")
                                                                                    .setIcon(R.drawable.myridec)
                                                                                    .create();
                                                                            myAlert.show();
                                                                        }
                                                                    }
                                                                });

                                                        alertDialogb.setNegativeButton("NO",
                                                                new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        dialog.cancel();
                                                                    }
                                                                });

                                                        alertDialogb.show();  }
                                                } else {Toast.makeText(SignUpActivity.this, "Phone number is required", Toast.LENGTH_LONG).show();}
                                            } else {Toast.makeText(SignUpActivity.this, "Salutation field is empty", Toast.LENGTH_LONG).show();}
                                        } else {Toast.makeText(SignUpActivity.this, "Password field is empty", Toast.LENGTH_LONG).show();}
                                    } else {Toast.makeText(SignUpActivity.this, "Username field is empty", Toast.LENGTH_LONG).show();}
                                } else {Toast.makeText(SignUpActivity.this, "Nationality field is empty", Toast.LENGTH_LONG).show();}
                            } else {Toast.makeText(SignUpActivity.this, "Last Name field is empty", Toast.LENGTH_LONG).show();}
                        } else {Toast.makeText(SignUpActivity.this, "First Name field is empty", Toast.LENGTH_LONG).show();}
                    }
                }
        );
    }
}

