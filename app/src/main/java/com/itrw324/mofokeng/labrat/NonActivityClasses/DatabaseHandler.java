package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


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

    public void insertClass(Class universityClass)
    {
        ContentValues values = new ContentValues();
        values.put(Database.TableClass.COLOUMN_CLASS_PERIOD,universityClass.getClass_Period());
        values.put(Database.TableClass.COLOUMN_CLASS_DAY,universityClass.getDay());
        values.put(Database.TableClass.COLOUMN_MODULE_CODE,universityClass.getModule_Code());
        values.put(Database.TableClass.COLOUMN_VENUEID,this.getVenueID(universityClass));

        database = getWritableDatabase();
        database.insert(Database.TableClass.TABLE_NAME,null,values);
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

    public String getVenueID(Class universityClass)
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + " WHERE "+Database.TableVenue.COLOUMN_VENU_NAME+" = ?";

        database = getReadableDatabase();

        String whereClause[] = {universityClass.getVenueID()};

        Cursor c = database.rawQuery(sql,whereClause);
        c.moveToFirst();

        return c.getString(0);
    }



    public String getVenueName(int venueID)
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + " WHERE "+Database.TableVenue.COLOUMN_VENUEID+" = ?";

        database = getReadableDatabase();

        String whereClause[] = {String.valueOf(venueID)};

        Cursor c = database.rawQuery(sql,whereClause);
        c.moveToFirst();

        Log.println(Log.DEBUG,"Venue","There are "+c.getCount()+" Venues in this List");
        return c.getString(1);
    }

    public String[] getVenueList()
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        String results[] = new String[c.getCount()];

        Log.println(Log.DEBUG,"Venues","There are "+c.getCount()+" Venues in this Table");

        for (int i=0;i<c.getCount();i++)
        {
            Log.println(Log.DEBUG,"Yeah",c.getString(0)+"\t"+c.getString(1)+"\t");
            results[i] = c.getString(1);
            c.moveToNext();
        }
        return results;
    }

    public String getClassID(Class campusClass)
    {
        String sql = "SELECT "+Database.TableClass.COLOUMN_CLASS_ID+" FROM "+ Database.TableClass.TABLE_NAME + " WHERE "+Database.TableClass.COLOUMN_CLASS_PERIOD+" = ? AND "+Database.TableClass.COLOUMN_CLASS_DAY+" = ? AND "+Database.TableClass.COLOUMN_MODULE_CODE+" = ?;";

        database = getReadableDatabase();

        String whereClause[] = {String.valueOf(campusClass.getClass_Period()),campusClass.getDay(),campusClass.getModule_Code()};

        Cursor c = database.rawQuery(sql,whereClause);
        c.moveToFirst();

        Log.println(Log.DEBUG,"ClasID","There is "+c.getCount()+" ClassID in this List");
        return c.getString(0);
    }

    public void addToSchedule(Class universityClass,GoogleSignInAccount user)
    {
        ContentValues values = new ContentValues();
        values.put(Database.TableSchedule.COLOUMN_CLASS_ID,universityClass.getClassID());
        values.put(Database.TableSchedule.COLOUMN_USER_EMAIL,user.getEmail());

        database = this.getWritableDatabase();

        database.insert(Database.TableSchedule.TABLE_NAME,null,values);
    }

    public void deleteSchedule(Schedule schedule)
    {
        String sql = "DELETE FROM "+ Database.TableSchedule.TABLE_NAME + " WHERE "+Database.TableSchedule.COLOUMN_SCHEDULEID+" = "+schedule.getClassID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void deleteSchedule(Class campusClass)
    {
        String sql = "DELETE FROM "+ Database.TableSchedule.TABLE_NAME + " WHERE "+Database.TableSchedule.COLOUMN_CLASS_ID+" = "+campusClass.getClassID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void deleteClass(Class campusClas)
    {
        deleteSchedule(campusClas);
        String sql = "DELETE FROM "+ Database.TableClass.TABLE_NAME + " WHERE "+Database.TableClass.COLOUMN_CLASS_ID+" = "+campusClas.getClassID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void updateClass(Class campusClass)
    {
        String venueID = getVenueID(campusClass);

        String sql = "UPDATE "+Database.TableClass.TABLE_NAME + " SET "+Database.TableClass.COLOUMN_CLASS_DAY+"=\""+campusClass.getDay()+"\", "+Database.TableClass.COLOUMN_CLASS_PERIOD+"="+campusClass.getClass_Period()+", "+Database.TableClass.COLOUMN_VENUEID+"="+venueID+" WHERE "+Database.TableClass.COLOUMN_CLASS_ID+" = "+campusClass.getClassID()+";";
        database = getWritableDatabase();

        database.execSQL(sql);
    }

    public Schedule[] getMySchedule(GoogleSignInAccount account)
    {
        String sql = "SELECT * FROM "+ Database.TableSchedule.TABLE_NAME + " WHERE "+ Database.TableSchedule.COLOUMN_USER_EMAIL+" = ?;";

        database = getReadableDatabase();
        String results[] = {account.getEmail()};
        Cursor c = database.rawQuery(sql,results);
        c.moveToFirst();

        Schedule schedule[] = new Schedule[c.getCount()];

        Log.println(Log.DEBUG,"Venues","There are "+c.getCount()+" Classes in my schedule");

        for (int i=0;i<c.getCount();i++)
        {
            Log.println(Log.DEBUG,"Schedule",c.getString(0)+"\t"+c.getString(1)+"\t"+c.getString(2));
            int clID = Integer.parseInt(c.getString(2));
            int schId = Integer.parseInt(c.getString(0));
            schedule[i] = new Schedule(schId,c.getString(1),clID);

            c.moveToNext();
        }
        return schedule;
    }

    public Class getOneClass(int classID) {
        String sql = "SELECT * FROM " + Database.TableClass.TABLE_NAME + " WHERE " + Database.TableClass.COLOUMN_CLASS_ID + " = ?";

        database = getReadableDatabase();
        String whereClause[] = {String.valueOf(classID)};
        Cursor c = database.rawQuery(sql, whereClause);
        c.moveToFirst();


        Log.println(Log.DEBUG, "Classes", "There are " + c.getCount() + " Classes in this Table");


        Log.println(Log.DEBUG, "Yeah", c.getString(0) + "\t" + c.getString(1) + "\t");

        String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
        int classP = Integer.parseInt(c.getString(1));
        Class results = new Class(classP, venue, c.getString(4), c.getString(2));
        results.setClassID(c.getString(0));
        c.moveToNext();

        return results;
    }

    public Class[] getClassList()
    {
        String sql = "SELECT * FROM "+ Database.TableClass.TABLE_NAME+ ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        Class results[] = new Class[c.getCount()];

        Log.println(Log.DEBUG,"Classes","There are "+c.getCount()+" Classes in this Table");

        for (int i=0;i<c.getCount();i++)
        {
            Log.println(Log.DEBUG,"Yeah",c.getString(0)+"\t"+c.getString(1)+"\t");
            String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
            int classP = Integer.parseInt(c.getString(1));
            results[i] = new Class(classP,venue,c.getString(4),c.getString(2));
            results[i].setClassID(c.getString(0));
            c.moveToNext();
        }
        return results;
    }

    public void insertVenues()
    {
        if(getVenueList().length==0) {
            database = this.getWritableDatabase();
            String venues[] = {"9A-102", "9A-103", "9A-104", "9A-106", "9A-107", "3-103"};
            String vNames[] = {"Buffel", "Luiperd", "Leeu", "Renoster", "Tavern", "Walvis"};

            ContentValues values = new ContentValues();

            for (int i = 0; i < venues.length; i++) {
                //values.put(Database.TableVenue.COLOUMN_VENUEID,venues[i]);
                values.put(Database.TableVenue.COLOUMN_VENU_NAME, venues[i]);

                database.insert(Database.TableVenue.TABLE_NAME, null, values);
            }
        }
    }

    public void insertModules()
    {
        if(getModuleList().length==0) {
            database = this.getWritableDatabase();

            String[] modules = {"ITRW321", "ITRW322", "ITRW324", "ITRW325", "ITRW124", "ITRW123", "ITRW225", "ITRW222", "STTF121", "ITSP121"};
            String[] description = {
                    "Databases II", "Computer Networks", "IT Developments", "Decision Support Systems II", "Programming I", "Graphic Interface Programming I",
                    "Systems Analysis and Design II", "Data Structures and Algorithms", "Foundation Statistics II", "Introductory Programming Principles"};

            ContentValues values = new ContentValues();

            for (int i = 0; i < modules.length; i++) {
                values.put(Database.TableModule.COLOUMN_MODULE_CODE, modules[i]);
                values.put(Database.TableModule.COLOUMN_MODULE_DESCR, description[i]);
                values.put(Database.TableModule.COLOUMN_MODULE_LECTURER, (byte[]) null);
                database.insert(Database.TableModule.TABLE_NAME, null, values);
            }
        }
    }

    public String[] getModuleList()
    {
        String sql = "SELECT * FROM "+ Database.TableModule.TABLE_NAME + ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        String results[] = new String[c.getCount()];

        Log.println(Log.DEBUG,"Yeah","There are "+c.getCount()+" Modules in this Table");

        for (int i=0;i<c.getCount();i++)
        {
            Log.println(Log.DEBUG,"Yeah",c.getString(0)+"\t"+c.getString(1)+"\t"+c.getString(2));
            results[i] = c.getString(0);
            c.moveToNext();
        }
        return results;
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
