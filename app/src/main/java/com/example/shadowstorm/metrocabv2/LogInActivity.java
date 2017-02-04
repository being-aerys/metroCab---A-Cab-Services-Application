package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LogInActivity extends Activity {
    public static String currentUser="";


    EditText userNameField,passwordField;
    Button logInButton;
    TextView signUp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        userNameField = (EditText)findViewById(R.id.userNameField);
        passwordField = (EditText)findViewById(R.id.passwordField);
        logInButton = (Button)findViewById(R.id.loginButton);
        signUp = (TextView)findViewById(R.id.idSignUpText);








        /// Crouton.makeText(LogInActivity.this,)


    }

    public void login(View v){
        String userName = userNameField.getText().toString();
        currentUser = userName;
        String password = passwordField.getText().toString();
        Toast.makeText(this,"username="+userName+" pass= "+password,Toast.LENGTH_LONG).show();

        String type = "login";

        //============================================================
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            LoginCheck backgroundWorkers = new LoginCheck(this);
            backgroundWorkers.execute(type, userName, password);


        }else{
            Toast.makeText(this, "NO Internet Connection", Toast.LENGTH_SHORT).show();
        }

        //=====================================================



    }







    public void signUp(View v){

        Intent intent;
        intent = new Intent(LogInActivity.this,SignUpActivity.class);
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            startActivity(intent);


        }else{
            Toast.makeText(this, "NO Internet Connection", Toast.LENGTH_SHORT).show();
        }


        // request your webservice here. Possible use of AsyncTask and ProgressDialog
        // show the result here - dialog or Toast




        //Intent intent = new Intent(this,SignUpActivity.class);
        // startActivity(intent);
    }
    public void forgetPassword(View v){
        Intent intent = new Intent(LogInActivity.this,ChangePassword.class);
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            startActivity(intent);


        }else{
            Toast.makeText(this, "NO Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        // moveTaskToBack(true); This line minimizes the app but this doesnt work in co-ordination with login check so
        //dont use moveTaskToBack() instrad just remove the functionality of the back button in this activity
        return;


    }




}
