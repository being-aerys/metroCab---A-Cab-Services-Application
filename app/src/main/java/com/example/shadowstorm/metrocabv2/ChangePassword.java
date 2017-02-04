package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Random;

/**
 * Created by Dipesh on 02/06/16.
 */
public class ChangePassword extends Activity{
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
            String type = "changePassword";
            ChangePass_db backgroundWorkers = new ChangePass_db(this);
            backgroundWorkers.execute(type, rphoneNum, diceRoll);

            /*Intent intent;
            intent = new Intent(this,ConfirmPhone.class);
            startActivity(intent);*/
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(this);
            myAlert.setMessage("Enter your phone number,myan!!!")
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
