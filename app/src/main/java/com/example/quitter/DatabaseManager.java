package com.example.quitter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class DatabaseManager {

    private static final String DATABASE_TABLE = "users";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TablesInfo.User.TABLE_NAME + " (" +
                    TablesInfo.User.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
                    TablesInfo.User.COLUMN_USERNAME + " TEXT UNIQUE, " +
                    TablesInfo.User.COLUMN_PASSWORD + " TEXT, " +
                    TablesInfo.User.COLUMN_AMOUNT + " NUMERIC, " +
                    TablesInfo.User.COLUMN_TIME + " NUMERIC, " +
                    TablesInfo.User.COLUMN_PRICE + " NUMERIC " +
                    ")";

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context context) {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    public ContentValues generateContentValues(String username, String password, double amount, double time, double price) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TablesInfo.User.COLUMN_USERNAME, username);
        contentValues.put(TablesInfo.User.COLUMN_PASSWORD, password);
        contentValues.put(TablesInfo.User.COLUMN_AMOUNT, amount);
        contentValues.put(TablesInfo.User.COLUMN_TIME, time);
        contentValues.put(TablesInfo.User.COLUMN_PRICE, price);
        return contentValues;
    }

    public void addUser(String username, String password, double amount, double time, double price) {
        db.insert(TablesInfo.User.TABLE_NAME, null, generateContentValues(username, password, amount, time, price));
    }

    public boolean login(String username, String password) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username, password});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

    public int getUserId(String username) {
        Cursor mCursor = db.rawQuery("SELECT id FROM Users WHERE username=" + username, null);
        if (mCursor != null && mCursor.moveToFirst()){
            int id = mCursor.getInt(mCursor.getColumnIndex("id"));
            mCursor.close();
            return id;
        } else{
            return 0;
        }
    }

    public String check(String username) throws SQLException {
        Cursor mCursor = db.rawQuery("SELECT * FROM Users WHERE username=? AND password=?", new String[]{username});
        if (mCursor != null) {
            if (mCursor.getCount() > 0) {
                String a = "This user already exists.";
                return a;
            }
        }
        return null;
    }
}


