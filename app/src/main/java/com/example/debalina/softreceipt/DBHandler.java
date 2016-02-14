package com.example.debalina.softreceipt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Debalina on 2/13/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ReceiptDB.db";
    private static final String TABLE_ADMIN = "ADMIN";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_EMAIL = "EMAIL";

    public DBHandler(Context context, String name,
                     SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);

        String CREATE_ADMIN_TABLE = "CREATE TABLE " +
                TABLE_ADMIN + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_PASSWORD + " TEXT, " +
                COLUMN_EMAIL + " TEXT" + ")";

        db.execSQL(CREATE_ADMIN_TABLE);

    }

    //Open database with FK on
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADMIN);
        onCreate(db);
    }

    //insert admin
    public String saveAdmin (AccountProfile accountProfile) {

        String operation_flag = "add";

        SQLiteDatabase db = this.getWritableDatabase();

        // check if exists
        Boolean ifExists = false;
        String password = accountProfile.getpassword();

        ifExists = checkIfPasswordExists(db, password);
        if (ifExists) {
            updateAdmin(db, accountProfile);
            operation_flag = "update";
        } else {
            addAdmin(db, accountProfile);
        }
        return operation_flag;
    }

    public void addAdmin(SQLiteDatabase db, AccountProfile ap) {

        ContentValues values = new ContentValues();

        values.put(COLUMN_PASSWORD, ap.getpassword());
        values.put(COLUMN_EMAIL, ap.getemail());

        db.insert(TABLE_ADMIN, null, values);

        db.close();
    }

    //update admin
    public void updateAdmin (SQLiteDatabase db, AccountProfile ap) {


        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, ap.getpassword());
        values.put(COLUMN_EMAIL, ap.getemail());

        db.update(TABLE_ADMIN, values, COLUMN_PASSWORD + " = ?", new String[]{String.valueOf(ap.getpassword())});

        db.close();

    }

    public Boolean checkIfPasswordExists (SQLiteDatabase db, String member) {

        String Query = "Select * from " + TABLE_ADMIN + " where " + COLUMN_PASSWORD + " =  \"" + member + "\"" ;

        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }

    }

//get profile
    public AccountProfile getAccountProfile() {

        String password = "";
        String email = "";

        String query = "Select " + COLUMN_PASSWORD + ", " + COLUMN_EMAIL +
                " FROM " + TABLE_ADMIN;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            password  = cursor.getString(cursor.getColumnIndex("PASSWORD"));
            email = cursor.getString(cursor.getColumnIndex("EMAIL"));
        }

        AccountProfile acProf = new AccountProfile(password, email);

        return acProf;
    }
}
