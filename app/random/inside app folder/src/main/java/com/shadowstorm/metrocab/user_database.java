package com.shadowstorm.metrocab;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shadow Storm on 5/27/2016.
 */
public class user_database extends SQLiteOpenHelper {
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

    public user_database(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR)");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUserData(String salute,String fname,String lname,String country,String phone,String actype,String license,String vehicle,String username, String password,String statuss,String lat,String lon) {
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
        long result = db.insert(TABLE_NAME,null,contentValues);
        if(result == -1){return false;}else{return true;}
    }
    public Cursor getUserData(){
        SQLiteDatabase db =this.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT,salutation TEXT, fname TEXT, lname TEXT, nationality TEXT, phone VARCHAR,actype VARCHAR, license VARCHAR, vehicle VARCHAR, username VARCHAR, password VARCHAR, status VARCHAR, lat VARCHAR, lon VARCHAR)");
        Cursor res =db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer deleteUserData(String mname){
        SQLiteDatabase db =this.getWritableDatabase();
        return  db.delete(TABLE_NAME,"_id = ?", new String[]{mname});
    }
}

