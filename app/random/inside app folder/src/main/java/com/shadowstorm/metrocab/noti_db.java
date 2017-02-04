package com.shadowstorm.metrocab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dipesh on 04/06/16.
 */
public class noti_db extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myRide.db";
    public static final String TABLE_NAME = "notification";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "salutation";
    public static final String COL_3 = "fname";
    public static final String COL_4 = "lname";
    public static final String COL_5 = "phone";
    public static final String COL_6 = "deviceid";
    public static final String COL_7 = "msg";
    public static final String COL_8 = "lat";
    public static final String COL_9 = "long";
    public static final String COL_10 = "driverid";
    public static final String COL_11 = "place";

    public noti_db(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR)");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNotiData(String salute, String fname, String lname, String phone, String deviceid, String msg, String lat, String lon, String driver, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, salute);
        contentValues.put(COL_3, fname);
        contentValues.put(COL_4, lname);
        contentValues.put(COL_5, phone);
        contentValues.put(COL_6, deviceid);
        contentValues.put(COL_7, msg);
        contentValues.put(COL_8, lat);
        contentValues.put(COL_9, lon);
        contentValues.put(COL_10, driver);
        contentValues.put(COL_11, address);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getNotiData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR)");
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteNotiData(String mname) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id = ?", new String[]{mname});
    }
}