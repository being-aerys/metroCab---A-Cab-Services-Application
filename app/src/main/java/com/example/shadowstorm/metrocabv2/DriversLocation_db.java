package com.example.shadowstorm.metrocabv2;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Shadow Storm on 5/27/2016.
 */
public class DriversLocation_db extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myRide.db";
    public static final String TABLE_NAME = "driver_loc";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "salutation";
    public static final String COL_3 = "fname";
    public static final String COL_4 = "lname";
    public static final String COL_5 = "nationality";
    public static final String COL_6 = "phone";
    public static final String COL_7 = "actype";
    public static final String COL_8 = "license";
    public static final String COL_9 = "vehicle";
    public static final String COL_10 = "status";
    public static final String COL_11 = "lat";
    public static final String COL_12 = "lon";
    public static final String COL_13 = "dist";
    public static final String COL_14 = "driverIdFromServer";



    /////////////////////////////////////////////
    // For logging:
    private static final String TAG = "DBAdapter";

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
//constructor to do tasks right during object creation
        public DriversLocation_db(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, dist VARCHAR, driverIdFromServer INTEGER)");
           /*ContentValues contentValues = new ContentValues();
        contentValues.put("salutation","Mr.");
        contentValues.put("fname","Dipesh");
        contentValues.put("lname","Adhikari");
        contentValues.put("country","Nepal");
        contentValues.put("email","adhikari@gmail.com");
        db.insert("user",null,contentValues);*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, dist VARCHAR, driverIdFromServer INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertDriverData(String salute,String fname,String lname,String country,String phone,String actype,String license,String vehicle,String statuss,String lat,String lon,String dist,String driverIdFromServer) {
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
        contentValues.put(COL_10,statuss);
        contentValues.put(COL_11,lat);
        contentValues.put(COL_12,lon);
        contentValues.put(COL_13,dist);
        contentValues.put(COL_14,driverIdFromServer);
        long result = db.insert(TABLE_NAME,null,contentValues);

        //===========================================Global Value Assign Gareko==================








        //============================================================

        if(result == -1){return false;}else{return true;}
    }
    public Cursor getDriverData(){
        //to get all drivers ,not only 1,from the sqlite db if exists
        SQLiteDatabase db =this.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, dist VARCHAR, driverIdFromServer INTEGER)");
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        Log.e("GET Driver Data",String.valueOf(res));
        return res;
    }
    public Cursor getDriverDataById(String db_id){

        SQLiteDatabase db =this.getWritableDatabase();
        //get only that particular driver data
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR, dist VARCHAR, driverIdFromServer INTEGER)");




        Cursor ress =db.rawQuery("select * from "+TABLE_NAME+" where _id = ?", new String[]{db_id});
        Log.e("GET_Driver Data by id",String.valueOf(ress));

        return ress;
    }
  /*  public boolean updateData(String id,String name,String valid,String mname){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,id);
        contentValues.put(COL_3,name);
        contentValues.put(COL_4,valid);
        contentValues.put(COL_5,mname);
        db.update(TABLE_NAME, contentValues,"id = ?",new String[] {id});
        return true;
    }*/

    public Integer deleteDriverData(){
        SQLiteDatabase db =this.getWritableDatabase();
        return  db.delete(TABLE_NAME,null, null);
    }


}

