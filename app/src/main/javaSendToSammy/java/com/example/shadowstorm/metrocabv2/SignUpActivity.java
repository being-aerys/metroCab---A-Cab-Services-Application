package com.example.shadowstorm.metrocabv2;


        import android.app.Activity;
        import android.content.Intent;
        import android.database.Cursor;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Spinner;
        import android.widget.Toast;

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
                        String rfname = fname.getText().toString();
                        String rlname = lname.getText().toString();
                        String rcountry = nation.getText().toString();
                        String rphone = phone.getText().toString();
                        String rlicense = license.getText().toString();
                        String rvehicle = vehicle.getText().toString();
                        String rusername = username.getText().toString();
                        String rpassword = password.getText().toString();
                        String rsal = salute.getSelectedItem().toString();
                        String ractype = actype.getSelectedItem().toString();
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
                                                                String type = "addAppUser";
                                                                user_datasql backgroundWorkers = new user_datasql(SignUpActivity.this);
                                                                backgroundWorkers.execute(type, rsal, rfname, rlname, rcountry,rphone,ractype,rlicense,rvehicle,rusername,rpassword);
                                                            } else {Toast.makeText(SignUpActivity.this, "Vehicle Number is required for Drivers", Toast.LENGTH_LONG).show();                                                        }
                                                        } else {Toast.makeText(SignUpActivity.this, "License Number is required for Drivers", Toast.LENGTH_LONG).show();                                                    }
                                                    } else {
                                                        String type = "addAppUser";
                                                        user_datasql backgroundWorkers = new user_datasql(SignUpActivity.this);
                                                        backgroundWorkers.execute(type, rsal, rfname, rlname, rcountry,rphone,ractype,rlicense,rvehicle,rusername,rpassword);
                                                    }
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

