package com.shadowstorm.metrocab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Dipesh on 02/06/16.
 */
public class confirmPhone extends Activity {
    changePass_sqlite userDb;
    EditText Pcode;
    String password,passwordb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirm_phone);
        userDb= new changePass_sqlite(this);
        Pcode=(EditText)findViewById(R.id.confirmPassCode);
    }
    public void confirmCodeContinue(View v) {
        String rCodeNum = Pcode.getText().toString();
        //Toast.makeText(getApplicationContext(), rCodeNum, Toast.LENGTH_SHORT).show();
        if(!rCodeNum.equals("")){
            Cursor res = userDb.getCodeData();
            if (res.getCount() == 0) {
                AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                myAlert.setMessage("Request code again")
                        .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                                Intent intent;
                                intent = new Intent(confirmPhone.this,changePassword.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Try Again",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                dialog.dismiss();
                            }
                        })
                        .setTitle("myRide Admin")
                        .setIcon(R.drawable.myridec)
                        .create();
                myAlert.show();
            }else{
                while (res.moveToNext()) {
                    String id = res.getString(0);
                    String phone = res.getString(1);
                    String code = res.getString(2);
                    final String username = res.getString(3);
                    if(!code.equals(rCodeNum)){
                        AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
                        myAlert.setMessage("Code not matched")
                                .setPositiveButton("Try Again",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which){
                                        dialog.dismiss();
                                    }
                                })
                                .setTitle("myRide Admin")
                                .setIcon(R.drawable.myridec)
                                .create();
                        myAlert.show();
                    }else{
                        //Toast.makeText(getApplicationContext(), username, Toast.LENGTH_SHORT).show();
                        //---------------for password field
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                        alertDialog.setTitle("PASSWORD");
                        alertDialog.setMessage("Enter New Password");
                        final EditText input = new EditText(this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        input.setLayoutParams(lp);
                        alertDialog.setView(input);
                        alertDialog.setIcon(R.drawable.myridec);
                        alertDialog.setPositiveButton("Continue",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        password = input.getText().toString();
                                        if (!password.equals("")) {
                                            //-------------------------------------------------------------for confirm password field
                                            AlertDialog.Builder alertDialogb = new AlertDialog.Builder(confirmPhone.this);
                                            alertDialogb.setTitle("PASSWORD");
                                            alertDialogb.setMessage("Confirm Password");

                                            final EditText inputb = new EditText(confirmPhone.this);
                                            LinearLayout.LayoutParams lpb = new LinearLayout.LayoutParams(
                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                    LinearLayout.LayoutParams.MATCH_PARENT);
                                            inputb.setLayoutParams(lpb);
                                            alertDialogb.setView(inputb);
                                            alertDialogb.setIcon(R.drawable.myridec);
                                            alertDialogb.setPositiveButton("Continue",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            passwordb = inputb.getText().toString();
                                                            if (passwordb.equals(password)) {
                                                                String type = "changePasswordConfirm";
                                                                changePass_db backgroundWorkers = new changePass_db(confirmPhone.this);
                                                                backgroundWorkers.execute(type, password,username);
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_SHORT).show();
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

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                        alertDialog.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                        alertDialog.show();

                    }
                }
            }
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Please enter the code from sms")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
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
