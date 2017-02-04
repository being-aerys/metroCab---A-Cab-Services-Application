package com.example.shadowstorm.metrocabv2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dipesh on 04/06/16.
 */
public class Noti_db extends SQLiteOpenHelper {
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
    public static final String COL_12 = "status";
    public static final String COL_13 = "isCompleted";
    public static final String COL_14 = "destToPass";
    public static final String COL_15 = "paymentAmt";
    public static final String COL_16 = "paymentStatus";

    public Noti_db(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR," + COL_12 + " VARCHAR,"+COL_13 + " VARCHAR,"+COL_14 + " VARCHAR,"+COL_15 + " VARCHAR,"+COL_16 + " VARCHAR)");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR," + COL_12 + " VARCHAR,"+COL_13 + " VARCHAR,"+COL_14 + " VARCHAR,"+COL_15 + " VARCHAR,"+COL_16 + " VARCHAR)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNotiData(String salute, String fname, String lname, String phone, String deviceid, String msg, String lat, String lon, String driver, String address,String statuss,String isCompleted,String destToPass,String paymentAmount,String paymentStatus) {
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
        contentValues.put(COL_12, statuss);
        contentValues.put(COL_13, isCompleted);
        contentValues.put(COL_14, destToPass);
        contentValues.put(COL_15, paymentAmount);
        contentValues.put(COL_16, paymentStatus);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getNotiData() {
        Log.e("yeha","aayo");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR," + COL_12 + " VARCHAR,"+COL_13 + " VARCHAR,"+COL_14 + " VARCHAR,"+COL_15 + " VARCHAR,"+COL_16 + " VARCHAR)");

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        Log.e("AAYO HERR 2",String.valueOf(res));
        return res;
    }

    public Cursor getNotiCancelData(String rows) {
        Log.e("cancel maa","aayo");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " VARCHAR, " + COL_3 + " VARCHAR, " + COL_4 + " VARCHAR, " + COL_5 + " VARCHAR, " + COL_6 + " VARCHAR," + COL_7 + " VARCHAR, " + COL_8 + " VARCHAR, " + COL_9 + " VARCHAR," + COL_10 + " VARCHAR," + COL_11 + " VARCHAR," + COL_12 + " VARCHAR,"+COL_13 + " VARCHAR,"+COL_14 + " VARCHAR,"+COL_15 + " VARCHAR,"+COL_16 + " VARCHAR)");
        Cursor res = db.rawQuery("select * from " + TABLE_NAME+" where _id='"+rows+"'",null);
        Log.e("AAYO HERR 2",String.valueOf(res));
        return res;
    }


    public boolean updateNotiData(String phone,String msg){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,msg);
        Log.e("CHECK MSG",msg);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        return true;
    }
    public boolean updateNotiDataComplete(String phone,String msg,String status){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_12,status);
        contentValues.put(COL_13,msg);
        Log.e("noti db updated","-----------------------completed"+msg+"----"+status);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        return true;
    }

    public Integer deleteNotiData(String mname) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "_id = ?", new String[]{mname});
    }
    public Integer deleteNotiDataq(String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "phone = ?", new String[]{phone});
    }
    public boolean updateUserPayment(String phone,String paymentAmount,String paymentStatus){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_15,paymentAmount);
        contentValues.put(COL_16,paymentStatus);
        db.update(TABLE_NAME, contentValues,"phone = ?",new String[] {phone});
        Log.e("Noti_db updated","---"+COL_15+"= "+paymentAmount+" WHERE phone="+phone);
        return true;
    }
}