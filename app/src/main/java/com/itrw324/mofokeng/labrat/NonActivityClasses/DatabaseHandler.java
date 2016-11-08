package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mofokeng on 06-Nov-16.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LabRat.db";

    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    public void insertUser(UserAccount account)
    {
        ContentValues values = new ContentValues();
        values.put(Database.TableUser.COLOUMN_USER_EMAIL,account.getAccount().getEmail());
        values.put(Database.TableUser.COLOUMN_DISPLAY_NAME,account.getAccount().getDisplayName());
        values.put(Database.TableUser.COLOUMN_UNIVERSITY_NUMBER,account.getUniversity_Number());
        values.put(Database.TableUser.COLOUMN_ROLE,account.getRole());

        SQLiteDatabase database = this.getWritableDatabase();

        database.insert(Database.TableUser.TABLE_NAME,null,values);
    }

    public Cursor selectUser(String email)
    {
        SQLiteDatabase database = this.getReadableDatabase();

        String columns[] = {Database.TableUser.COLOUMN_USER_EMAIL,Database.TableUser.COLOUMN_DISPLAY_NAME,Database.TableUser.COLOUMN_UNIVERSITY_NUMBER,Database.TableUser.COLOUMN_ROLE};

        String []args = {email};

        String sql = "SELECT * FROM "+Database.TableUser.TABLE_NAME+" WHERE "+Database.TableUser.COLOUMN_USER_EMAIL+" = ?;";

        //database.rawQuery()
        return database.rawQuery(sql,args);
        //return database.query(Database.TableUser.TABLE_NAME,columns,Database.TableUser.COLOUMN_USER_EMAIL,args,null,null,null);
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

    }
}
