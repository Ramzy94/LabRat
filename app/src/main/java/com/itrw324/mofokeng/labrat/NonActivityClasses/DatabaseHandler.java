package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;
import java.util.Calendar;


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

    private void checkClassExists(Class universityClass)
    {
        Class[]classes = getClassList();

        for(Class theClass:classes)
        {
            if((theClass.getDay().equalsIgnoreCase(universityClass.getDay())&&(theClass.getVenueID().equalsIgnoreCase(universityClass.getVenueID()))&&(theClass.getClass_Period()==universityClass.getClass_Period())))
                throw new IllegalArgumentException(theClass.getModule_Code()+" is already Booked for that slot");
        }
    }

    public void insertClass(Class universityClass)
    {
        this.checkClassExists(universityClass);

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

    private String getVenueName(int venueID)
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + " WHERE "+Database.TableVenue.COLOUMN_VENUEID+" = ?";

        database = getReadableDatabase();

        String whereClause[] = {String.valueOf(venueID)};

        Cursor c = database.rawQuery(sql,whereClause);
        c.moveToFirst();

        return c.getString(1);
    }

    private Venue[] getVenueArray()
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        Venue results[] = new Venue[c.getCount()];

        for (int i=0;i<c.getCount();i++)
        {
            results[i] = new Venue(c.getString(0),c.getString(1));
            c.moveToNext();
        }
        c.close();
        return results;
    }

    public String[] getVenueList()
    {
        String sql = "SELECT * FROM "+ Database.TableVenue.TABLE_NAME + ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        String results[] = new String[c.getCount()];

        for (int i=0;i<c.getCount();i++)
        {
            results[i] = c.getString(1);
            c.moveToNext();
        }
        c.close();
        return results;
    }

    public ArrayList<Class> getClassesInVenue(String aVenue)
    {
        Venue[] venues = getVenueArray();

        int index = 0;
        while (index<venues.length)
        {
            if(venues[index].getVenueName().equalsIgnoreCase(aVenue))
                break;
            index++;
        }

        String sql = "SELECT * FROM "+ Database.TableClass.TABLE_NAME+ " WHERE "+Database.TableClass.COLOUMN_VENUEID+" = ?;";

        String []args = {venues[index].getVenueID()};

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,args);
        c.moveToFirst();

        ArrayList<Class> classes = new ArrayList<>();


        Calendar calendar = Calendar.getInstance();

        for (int i=0;i<c.getCount();i++)
        {
            String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
            int classP = Integer.parseInt(c.getString(1));
            Class aClass = new Class(classP,venue,c.getString(4),c.getString(2));
            aClass.setClassID(c.getString(0));

            if(aClass.getCalenderDay()==calendar.get(Calendar.DAY_OF_WEEK))
            {
                classes.add(aClass);
            }

            c.moveToNext();
        }
        c.close();
        return classes;
    }

    public void addToSchedule(Class universityClass,GoogleSignInAccount user)
    {
        Schedule[] schedules = getMySchedule(user);

        for(Schedule theSchedule:schedules)
            if(theSchedule.getClassID()==Integer.parseInt(universityClass.getClassID())&&theSchedule.getUserEmail().equalsIgnoreCase(user.getEmail()))
                throw new IllegalArgumentException(universityClass.getModule_Code()+" is already in your Schedule for that time and venue");

        ContentValues values = new ContentValues();
        values.put(Database.TableSchedule.COLOUMN_CLASS_ID,universityClass.getClassID());
        values.put(Database.TableSchedule.COLOUMN_USER_EMAIL,user.getEmail());

        database = this.getWritableDatabase();
        database.insert(Database.TableSchedule.TABLE_NAME,null,values);
    }

    public void deleteSchedule(Schedule schedule)
    {
        String sql = "DELETE FROM "+ Database.TableSchedule.TABLE_NAME + " WHERE "+Database.TableSchedule.COLOUMN_SCHEDULEID+" = "+schedule.getScheduleID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    private void deleteSchedule(Class campusClass)
    {
        String sql = "DELETE FROM "+ Database.TableSchedule.TABLE_NAME + " WHERE "+Database.TableSchedule.COLOUMN_CLASS_ID+" = "+campusClass.getClassID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void deleteClass(Class campusClass)
    {
        deleteSchedule(campusClass);
        String sql = "DELETE FROM "+ Database.TableClass.TABLE_NAME + " WHERE "+Database.TableClass.COLOUMN_CLASS_ID+" = "+campusClass.getClassID();
        database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void updateClass(Class campusClass)
    {
        this.checkClassExists(campusClass);
        String venueID = getVenueID(campusClass);

        String sql = "UPDATE "+Database.TableClass.TABLE_NAME + " SET "+Database.TableClass.COLOUMN_CLASS_DAY+"=\""+campusClass.getDay()+"\", "+Database.TableClass.COLOUMN_CLASS_PERIOD+" = "+campusClass.getClass_Period()+", "+Database.TableClass.COLOUMN_VENUEID+"="+venueID+" WHERE "+Database.TableClass.COLOUMN_CLASS_ID+" = "+campusClass.getClassID()+";";
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


        for (int i=0;i<c.getCount();i++)
        {
            int clID = Integer.parseInt(c.getString(2));
            int schId = Integer.parseInt(c.getString(0));
            schedule[i] = new Schedule(schId,c.getString(1),clID);

            c.moveToNext();
        }
        c.close();
        return schedule;
    }

    public Class getOneClass(int classID) {
        String sql = "SELECT * FROM " + Database.TableClass.TABLE_NAME + " WHERE " + Database.TableClass.COLOUMN_CLASS_ID + " = ?";

        database = getReadableDatabase();
        String whereClause[] = {String.valueOf(classID)};
        Cursor c = database.rawQuery(sql, whereClause);
        c.moveToFirst();


        String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
        int classP = Integer.parseInt(c.getString(1));
        Class results = new Class(classP, venue, c.getString(4), c.getString(2));
        results.setClassID(c.getString(0));
        c.moveToNext();

        c.close();
        return results;
    }

    public Class[] searchClasses(String moduleCode)
    {
        String sql = "SELECT * FROM "+ Database.TableClass.TABLE_NAME+ " WHERE "+Database.TableClass.COLOUMN_MODULE_CODE+ " = ?";
        String args[] = {moduleCode};

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,args);
        c.moveToFirst();

        Class results[] = new Class[c.getCount()];

        for (int i=0;i<c.getCount();i++)
        {
            String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
            int classP = Integer.parseInt(c.getString(1));
            results[i] = new Class(classP,venue,c.getString(4),c.getString(2));
            results[i].setClassID(c.getString(0));
            c.moveToNext();
        }
        c.close();
        return results;
    }


    public Class[] getClassList()
    {
        String sql = "SELECT * FROM "+ Database.TableClass.TABLE_NAME+ ";";

        database = getReadableDatabase();
        Cursor c = database.rawQuery(sql,null);
        c.moveToFirst();

        Class results[] = new Class[c.getCount()];


        for (int i=0;i<c.getCount();i++)
        {
            String venue = this.getVenueName(Integer.parseInt(c.getString(3)));
            int classP = Integer.parseInt(c.getString(1));
            results[i] = new Class(classP,venue,c.getString(4),c.getString(2));
            results[i].setClassID(c.getString(0));
            c.moveToNext();
        }
        c.close();
        return results;
    }

    public void insertVenues()
    {
        if(getVenueList().length==0) {
            database = this.getWritableDatabase();
            String venues[] = {"9A-102", "9A-103", "9A-104", "9A-106", "9A-107", "3-103"};

            ContentValues values = new ContentValues();

            for (String venue:venues) {
                values.put(Database.TableVenue.COLOUMN_VENU_NAME, venue);
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

        for (int i=0;i<c.getCount();i++)
        {
            results[i] = c.getString(0);
            c.moveToNext();
        }
        c.close();
        return results;
    }

    public boolean venueHasClass(String venue)
    {
        ArrayList<Class> classes = getClassesInVenue(venue);

        Calendar cal = Calendar.getInstance();

        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);

        for(Class aClass:classes) {
            if((aClass.getStartHourMins()[0]<=hours) && (aClass.getStartHourMins()[1]<=minutes) ){
                return ((aClass.getEndHourMins()[0]>hours) && (aClass.getEndHourMins()[1]>minutes));
            }
        }
        return false;
    }

    public UserAccount getUser(GoogleSignInAccount account)
    {
        database = this.getReadableDatabase();

        String[] args = {account.getEmail()};

        String sql = "SELECT * FROM " + Database.TableUser.TABLE_NAME + " WHERE " + Database.TableUser.COLOUMN_USER_EMAIL + " = ?;";
        Cursor c = database.rawQuery(sql, args);
        c.moveToFirst();

        String uniNum = c.getString(2);
        String role = c.getString(3);

        UserAccount acc = new UserAccount(account,uniNum);

        acc.setRole(role);
        return acc;
    }

    public boolean alreadySignedUp(String email) {

        database = this.getReadableDatabase();

        String[] args = {email};

        String sql = "SELECT " + Database.TableUser.COLOUMN_USER_EMAIL + " FROM " + Database.TableUser.TABLE_NAME + " WHERE " + Database.TableUser.COLOUMN_USER_EMAIL + " = ?;";
        Cursor c = database.rawQuery(sql, args);
        c.moveToFirst();

        if (c.getCount() == 0) {
            return false;
        }

        return c.getString(0).equalsIgnoreCase(email);
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

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Database.TableSchedule.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Database.TableClass.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Database.TableVenue.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Database.TableModule.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Database.TableUser.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
