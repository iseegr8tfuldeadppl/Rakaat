package com.krimzon.scuffedbots.raka3at.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL extends SQLiteOpenHelper {

    //variables
    private static final String DATABASE_NAME = "rakaat.db"; //not case sensitive
    private static final String COL_1 = "_ID";
    private static final String COL_2 = "_SETTING";
    private static final String DAY = "_DAY";
    private static final String FAJR = "_FAJR";
    private static final String RISE = "_RISE";
    private static final String DHUHR = "_DHUHR";
    private static final String ASR = "_ASR";
    private static final String MAGHRIB = "_MAGHRIB";
    private static final String ISHA = "_ISHA";
    private static final String PRAYED = "_PRAYED";
    private static final String VERIFIED = "_VERIFIED";
    private static final String ATHOME = "_ATHOME";
    private static final String LONGITUDE = "_LONGITUDE";
    private static final String LATITUDE = "_LATITUDE";

    //constructor functions (selected 1st one)
    //Database creator function
    public SQL(Context context) {
        super(context, DATABASE_NAME, null, 1);

        //line just for checking
        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    //impelment functions
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "slat" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT" + ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "force3" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAY + " TEXT, " + PRAYED + " TEXT, " + VERIFIED + " TEXT, " + ATHOME + " TEXT" + ");");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "force" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LONGITUDE + " TEXT, " + LATITUDE + " TEXT" + ");");
        //create a table whenever oncreate is called
    }

    public void delete(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLSharing.TABLE_NAME_INPUTER);
        onCreate(sqLiteDatabase);
    }

    private static SQL mInstance = null;


    public static SQL getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new SQL(ctx.getApplicationContext());
        }
        return mInstance;
    }

    //inputting data
    public boolean insertData(String _SETTING){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, _SETTING);
        long result = sqLiteDatabase.insert("slat", null, contentValues); //returns -1 if failed to add
        if(result == -1) return false;
        else return true;
    }

    //inputting data
    public boolean insertMawa9it(String _LONGITUDE, String _LATITUDE){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LONGITUDE, _LONGITUDE);
        contentValues.put(LATITUDE, _LATITUDE);
        long result = sqLiteDatabase.insert("force", null, contentValues); //returns -1 if failed to add
        if(result == -1) return false;
        else return true;
    }

    public boolean updateMawa9it(String _ID, String _LONGITUDE, String _LATITUDE){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(LONGITUDE, _LONGITUDE);
        contentValues.put(LATITUDE, _LATITUDE);
        sqLiteDatabase.update("force", contentValues, COL_1 + "=?", new String[] { _ID });
        return true;
    }

    //outputting data
    public Cursor getAllDateslat() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "slat" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_2 + " TEXT" + ");");
        return sqLiteDatabase.rawQuery("select * from " + "slat" + ";", null);
    }

    public Cursor getAllDateforce3() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "force3" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAY + " TEXT, " + PRAYED + " TEXT, " + VERIFIED + " TEXT" + ");");
        return sqLiteDatabase.rawQuery("select * from " + "force3" + ";", null);
    }

    public Cursor getAllDateforce() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + "force" + "(" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DAY + " TEXT, " + FAJR + " TEXT, " + RISE + " TEXT, " + DHUHR + " TEXT, " + ASR + " TEXT, " + MAGHRIB + " TEXT, " + ISHA + " TEXT" + ");");
        return sqLiteDatabase.rawQuery("select * from " + "force" + ";", null);
    }

    //inputting data
    public boolean insertPrayed(String _DAY, String _PRAYED, String _VERIFIED, String _ATHOME){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DAY, _DAY);
        contentValues.put(PRAYED, _PRAYED);
        contentValues.put(VERIFIED, _VERIFIED);
        contentValues.put(ATHOME, _ATHOME);
        long result = sqLiteDatabase.insert("force3", null, contentValues); //returns -1 if failed to add
        if(result == -1) return false;
        else return true;
    }

    //modify data
    public boolean updatePrayed(String _DAY, String _PRAYED, String _VERIFIED, String _ATHOME){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DAY, _DAY);
        contentValues.put(PRAYED, _PRAYED);
        contentValues.put(VERIFIED, _VERIFIED);
        contentValues.put(ATHOME, _ATHOME);
        sqLiteDatabase.update("force3", contentValues, DAY + "=?", new String[] { _DAY });
        return true;
    }

    //modify data
    public boolean updateData(String _FIREBASE_ID, String _ID){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, _ID);
        contentValues.put(COL_2, _FIREBASE_ID);
        sqLiteDatabase.update("slat", contentValues, COL_1 + "=?", new String[] { _ID });
        return true;
    }
}
