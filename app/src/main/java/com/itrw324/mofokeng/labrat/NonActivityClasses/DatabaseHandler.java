package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Mofokeng on 06-Nov-16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LabRat.db";
    private SQLiteDatabase database;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void insertUser(UserAccount account) {
        ContentValues values = new ContentValues();
        values.put(Database.TableUser.COLOUMN_USER_EMAIL, account.getAccount().getEmail());
        values.put(Database.TableUser.COLOUMN_DISPLAY_NAME, account.getAccount().getDisplayName());
        values.put(Database.TableUser.COLOUMN_UNIVERSITY_NUMBER, account.getUniversity_Number());
        values.put(Database.TableUser.COLOUMN_ROLE, account.getRole());

        database = this.getWritableDatabase();

        database.insert(Database.TableUser.TABLE_NAME, null, values);
    }

    public boolean alreadySignedUp(String email) {
        database = this.getReadableDatabase();

        String[] args = {email};

        String sql = "SELECT " + Database.TableUser.COLOUMN_USER_EMAIL + " FROM " + Database.TableUser.TABLE_NAME + " WHERE " + Database.TableUser.COLOUMN_USER_EMAIL + " = ?;";
        Cursor c = database.rawQuery(sql, args);
        c.moveToFirst();

        Log.println(Log.DEBUG, "Yeah", "ABCDEFGHIJKLMNOPQRSTUVWXYX" + c.getCount());

        if (c.getCount() == 0) {
            return false;
        }

        if (c.getString(0).equalsIgnoreCase(email)) {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Database.SQL_CREATE_USER);
        database.execSQL(Database.SQL_CREATE_MODULE);
        database.execSQL(Database.SQL_CREATE_VENUE);
        database.execSQL(Database.SQL_CREATE_CLASS);
        database.execSQL(Database.SQL_CREATE_SCHEDULE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        database.execSQL("DROP TABLE IF EXISTS"+Database.TableSchedule.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS"+Database.TableClass.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS"+Database.TableVenue.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS"+Database.TableModule.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS"+Database.TableUser.TABLE_NAME);

        onCreate(database);
    }
}
