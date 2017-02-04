package com.example.shadowstorm.metrocabv2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Shadow Storm on 5/27/2016.
 */
public class User_database extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myRide.db";
    public static final String TABLE_NAME = "user";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "salutation";
    public static final String COL_3 = "fname";

    public static final String COL_4 = "lname";
    public static final String COL_5 = "nationality";
    public static final String COL_6 = "phone";
    public static final String COL_7 = "actype";
    public static final String COL_8 = "license";
    public static final String COL_9 = "vehicle";
    public static final String COL_10 = "username";
    public static final String COL_11 = "password";
    public static final String COL_12 = "status";
    public static final String COL_13 = "lat";
    public static final String COL_14 = "lon";
    public static final String COL_15 = "balance";
    public static final String COL_16 = "advance";
    public static final String COL_17 = "paypal_id";

    public static final String COL_18 = "sessonOn";
    public static final String COL_19 = "sessonWith";//------driver phone
    public static final String COL_20 = "sessonArrived";
    public static final String COL_21 = "sessonComplete";
    public static final String COL_22 = "paymentAmount";
    public static final String COL_23 = "paymentStatus";

    public static final String COL_24 = "acceptedTime1";

    public User_database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, balance VARCHAR, advance VARCHAR, paypal_id VARCHAR,  "+COL_18+" VARCHAR,  "+COL_19+" VARCHAR,  "+COL_20+" VARCHAR, "+COL_21+" VARCHAR, "+COL_22+" VARCHAR, "+COL_23+" VARCHAR, "+COL_24+" TEXT)");
        Log.e("lat ho ta 5",String.valueOf(db));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, balance VARCHAR, advance VARCHAR, paypal_id VARCHAR,  "+COL_18+" VARCHAR,  "+COL_19+" VARCHAR,  "+COL_20+" VARCHAR, "+COL_21+" VARCHAR, "+COL_22+" VARCHAR, "+COL_23+" VARCHAR, "+COL_24+" TEXT)");
        Log.e("lat ho ta 4",String.valueOf(db));
    }


    public void insertLatLon(String lati,String loni){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE "+ TABLE_NAME + " SET " + "lat" + " = " + lati );
        db.execSQL("UPDATE "+ TABLE_NAME + " SET " + "lon" + " = " + loni );
        //check if inserted or not
        //Log.e(String.valueOf(db.execSQL("SELECT * FROM "+TABLE_NAME);
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        Log.e("CHECK",String.valueOf(res));

        try{
            while (res.moveToNext()){
                String a = res.getString(12);
                Double c = res.getDouble(12);
                Double d = res.getDouble(13);
                Log.e("CHECK LATI",lati); //yo thik xa
                Log.e("CHECK LONI",loni);
                Log.i(TAG, "CHECK DOUBLE lat" +c);//yesma katiyo
                Log.i(TAG, "CHECK DOUBLE lon" +d);
                String b = res.getString(13);

                Log.e("CHECK",String.valueOf(a)); //yesma pani
                //Log.i(TAG, "value =" + res.);

                Log.e("CHECK",String.valueOf(b));
                if(lati==a){
                    Log.e("CHECK _EQUAL","getString ko jhyau haina");

                }
                else if(c.equals(lati)){
                    Log.e("CHECK _EQUAL_NOT","getString le jhayu ho");
                }
                else{
                    Log.e("CHECK _EQUAL_bhayena","bujh");
                }

            }

        }
        catch (Exception E){

        }

        // res.getString(12);
        // res.getString(13);


        //Log.e("CRUCIAL2",String.valueOf(lati));

        //i AM SURE THAT THE DATA HAS BEEN FED TO SQLITE CORRECTLY COZ yei db bata input leko xa call activity ma and the values match


        Log.e("LATSQL",String.valueOf(lati));


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
        Log.e("lat ho ta 6hy",String.valueOf(db));
    }

    public boolean insertUserData(String salute,String fname,String lname,String country,String phone,String actype,String license,String vehicle,String username, String password,String statuss,String lat,String lon,String balance,String advance,String paypal_id,String sessonOn, String sessonWith,String sessonArrived, String sessonCompleted,String paymentAmount,String paymentStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,salute);
        contentValues.put(COL_3,fname);
        contentValues.put(COL_4,lname);
        contentValues.put(COL_5,country);
        contentValues.put(COL_6,phone);
        contentValues.put(COL_7,actype);
        contentValues.put(COL_8,license);
        contentValues.put(COL_9,vehicle);
        contentValues.put(COL_10,username);
        contentValues.put(COL_11,password);
        contentValues.put(COL_12,statuss);
        contentValues.put(COL_13,lat);
        contentValues.put(COL_14,lon);
        contentValues.put(COL_15,balance);
        contentValues.put(COL_16,advance);
        contentValues.put(COL_17,paypal_id);

        contentValues.put(COL_18,sessonOn);
        contentValues.put(COL_19,sessonWith);
        contentValues.put(COL_20,sessonArrived);
        contentValues.put(COL_21,sessonCompleted);
        contentValues.put(COL_22,paymentAmount);
        contentValues.put(COL_23,paymentStatus);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){return false;}else{return true;}
    }

    public Cursor getUserData(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, balance VARCHAR, advance VARCHAR, paypal_id VARCHAR,  "+COL_18+" VARCHAR,  "+COL_19+" VARCHAR,  "+COL_20+" VARCHAR, "+COL_21+" VARCHAR, "+COL_22+" VARCHAR, "+COL_23+" VARCHAR, "+COL_24+" TEXT)");
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer deleteUserData(String mname){
        SQLiteDatabase db =this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"_id = ?", new String[]{mname});
    }
    public boolean updateUserData(String phone,String status,String sessonON,String sessonWith){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,status);
        contentValues.put(COL_18,sessonON);
        contentValues.put(COL_19,sessonWith);
        contentValues.put(COL_20,"0");
        contentValues.put(COL_21,"0");
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---sesson="+sessonON+", sessonWith="+sessonWith+"status="+status+" = 0 "+" WHERE phone="+phone);
        return true;
    }
    public boolean updateUserBalance(String phone, String amount,String status){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_22,amount);
        contentValues.put(COL_23,status);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        return true;
    }
    public boolean updateUserSessonComplete(String phone){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_18,"0");
        contentValues.put(COL_19,"0");
        contentValues.put(COL_20,"0");
        contentValues.put(COL_21,"0");
        contentValues.put(COL_22,"0");
        contentValues.put(COL_23,"0");
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_22+"= "+" 0 "+" WHERE phone="+phone);
        return true;
    }
    public boolean updateUserSessonArrived(String phone,String sessonArrived){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_20,sessonArrived);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_20+"= "+sessonArrived+" WHERE phone="+phone);
        return true;
    }
    public boolean updateUserSessonOnOff(String phone,String sessonOnOff,String sessionWith){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_18,sessonOnOff);
        contentValues.put(COL_19,sessionWith);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_18+"= "+sessonOnOff+" WHERE phone="+phone);
        return true;
    }
    public boolean updateUserSessonComplete(String phone,String sessonComplete){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_21,sessonComplete);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_20+"= "+sessonComplete+" WHERE phone="+phone);
        return true;
    }

    public boolean updateUserPayment(String phone,String paymentAmount,String paymentStatus){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_22,paymentAmount);
        contentValues.put(COL_23,paymentStatus);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_22+"= "+paymentAmount+" WHERE phone="+phone);
        return true;
    }
    public boolean updateStatus(String phone, String status){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,status);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_12+"= "+status+" WHERE phone="+phone);
        return true;
    }
    public boolean updateStartingTime(String phone, String time){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_24,time);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("user_db updated","---"+COL_24+"= "+time+" WHERE phone="+phone);
        return true;
    }
    public boolean updateSessionCompleteStatus(String phone){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,"FREE");
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("DRIVER STATUS UPDATED","---"+COL_12+"= FREE WHERE phone="+phone);
        return true;
    }


}

