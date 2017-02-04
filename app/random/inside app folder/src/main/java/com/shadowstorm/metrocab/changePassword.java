package com.shadowstorm.metrocab;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Dipesh on 02/06/16.
 */
public class changePassword extends Activity{
    String diceRoll;
    int max;
    int min;
    EditText Pnumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        Pnumber=(EditText)findViewById(R.id.changePassPhone);
    }

    public void changePasswordExit(View v) {
        Intent intent;
        intent = new Intent(this,LogInActivity.class);
        startActivity(intent);
    }
    public void changePhoneContinue(View v) {
        String rphoneNum = Pnumber.getText().toString();
        if(!rphoneNum.equals("")){
            max = 9865;
            min = 1598;
            final Random rand = new Random();
            diceRoll = String.valueOf(rand.nextInt(max-min) + 1);

            //Toast.makeText(this, diceRoll+" Sent "+rphoneNum, Toast.LENGTH_LONG).show();
            String type = "ChangePassword";
            changePass_db backgroundWorkers = new changePass_db(this);
            backgroundWorkers.execute(type, rphoneNum, diceRoll);

            /*Intent intent;
            intent = new Intent(this,ConfirmPhone.class);
            startActivity(intent);*/
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Where is your number? HUH!!!")
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
