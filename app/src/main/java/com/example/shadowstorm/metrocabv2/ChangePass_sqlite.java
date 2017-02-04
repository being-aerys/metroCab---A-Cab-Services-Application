package com.example.shadowstorm.metrocabv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dipesh on 02/06/16.
 */
public class ChangePass_sqlite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "myRide.db";
    public static final String TABLE_NAME = "change_pass";
    public static final String COL_1 = "_id";
    public static final String COL_2 = "phone";
    public static final String COL_3 = "code";
    public static final String COL_4 = "username";

    public ChangePass_sqlite(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,phone VARCHAR,code VARCHAR,username VARCHAR)");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,phone VARCHAR,code VARCHAR,username VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertCodeData(String phone,String code,String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,phone);
        contentValues.put(COL_3,code);
        contentValues.put(COL_4,username);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){return false;}else{return true;}
    }
    public Cursor getCodeData(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,phone VARCHAR,code VARCHAR,username VARCHAR)");
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer deleteCodeData(String mname){
        SQLiteDatabase db =this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"_id = ?", new String[]{mname});
    }
}
