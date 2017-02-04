package com.example.shadowstorm.metrocabv2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

            String type = "login";
            loginCheck backgroundWorkers = new loginCheck(this);
            backgroundWorkers.execute(type, userName, password);



        /*if(userName.equals("aerys") && password.equals("aerys")){
            Intent intent = new Intent(this, UserActivity.class);
            startActivity(intent);

        }
        else if(userName.equals("admin") && password.equals("admin")){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        else if(userName.equals("Nabin") && password.equals("Nabin")){
            Intent intent = new Intent(this,MapsActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this,"Mula tero milena ", Toast.LENGTH_SHORT).show();
        }*/
    }






   
    public void signUp(View v){

                Intent intent;
                intent = new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);


                // request your webservice here. Possible use of AsyncTask and ProgressDialog
                // show the result here - dialog or Toast




        //Intent intent = new Intent(this,SignUpActivity.class);
       // startActivity(intent);
    }



}
