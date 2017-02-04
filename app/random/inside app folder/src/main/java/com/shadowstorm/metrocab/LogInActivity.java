package com.shadowstorm.metrocab;


        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;


public class LogInActivity extends Activity {


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

    }

    public void login(View v){
        String userName = userNameField.getText().toString();
        String password = passwordField.getText().toString();

        String type = "login";
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(LogInActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            loginCheck backgroundWorkers = new loginCheck(this);
            backgroundWorkers.execute(type, userName, password);
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(LogInActivity.this);
            myAlert.setMessage("Please check your internet connection.")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Internet error")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();
        }

    }
    public void forgetPassword(View v){
        ConnectivityManager cManager = (ConnectivityManager) getSystemService(LogInActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if(nInfo !=null && nInfo.isConnected()) {
            Intent intent= new Intent(this,changePassword.class);
            startActivity(intent);
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(LogInActivity.this);
            myAlert.setMessage("Please check your internet connection.")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Internet error")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();
        }
    }

    public void signUp(View v) {

        ConnectivityManager cManager = (ConnectivityManager) getSystemService(LogInActivity.this.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = cManager.getActiveNetworkInfo();
        if (nInfo != null && nInfo.isConnected()) {
            Intent intent;
            intent = new Intent(LogInActivity.this, SignUpActivity.class);
            startActivity(intent);
        }else{
            AlertDialog.Builder myAlert = new AlertDialog.Builder(LogInActivity.this);
            myAlert.setMessage("Please check your internet connection.")
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            dialog.dismiss();
                        }
                    })
                    .setTitle("Internet error")
                    .setIcon(R.drawable.myridec)
                    .create();
            myAlert.show();
        }
    }
}

