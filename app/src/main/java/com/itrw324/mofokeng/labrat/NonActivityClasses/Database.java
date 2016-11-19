package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.provider.BaseColumns;

/**
 * Created by Mofokeng on 05-Nov-16.
 */

public final class Database
{
    static final String SQL_CREATE_USER = "CREATE TABLE "+TableUser.TABLE_NAME+"( "+TableUser.COLOUMN_USER_EMAIL+" TEXT PRIMARY KEY NOT NULL, "+TableUser.COLOUMN_DISPLAY_NAME+" TEXT NULL, "+TableUser.COLOUMN_UNIVERSITY_NUMBER+" TEXT NULL, "+TableUser.COLOUMN_ROLE+" TEXT NULL );";
    static final String SQL_CREATE_MODULE = "CREATE TABLE "+TableModule.TABLE_NAME+" ( "+TableModule.COLOUMN_MODULE_CODE+" TEXT PRIMARY KEY NOT NULL, "+TableModule.COLOUMN_MODULE_DESCR+" TEXT NULL, "+TableModule.COLOUMN_MODULE_LECTURER+" TEXT NULL );";
    static final String SQL_CREATE_VENUE = "CREATE TABLE "+TableVenue.TABLE_NAME+" ( "+TableVenue.COLOUMN_VENUEID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableVenue.COLOUMN_VENU_NAME+" TEXT NULL );";
    static final String SQL_CREATE_CLASS = "CREATE TABLE "+TableClass.TABLE_NAME+" ( "+TableClass.COLOUMN_CLASS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableClass.COLOUMN_CLASS_PERIOD+" INTEGER NULL, "+TableClass.COLOUMN_CLASS_DAY+" TEXT NULL, " +TableClass.COLOUMN_VENUEID+" INTEGER NOT NULL, "+TableClass.COLOUMN_MODULE_CODE+" TEXT NOT NULL, FOREIGN KEY ("+TableClass.COLOUMN_VENUEID+") REFERENCES venue ("+TableVenue.COLOUMN_VENUEID+"), FOREIGN KEY ("+TableClass.COLOUMN_MODULE_CODE+") REFERENCES module ("+TableModule.COLOUMN_MODULE_CODE+") );";
    static final String SQL_CREATE_SCHEDULE = "CREATE TABLE "+TableSchedule.TABLE_NAME+" ( "+TableSchedule.COLOUMN_SCHEDULEID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableSchedule.COLOUMN_USER_EMAIL+" TEXT NOT NULL, "+TableSchedule.COLOUMN_CLASS_ID+" INTEGER NOT NULL, FOREIGN KEY ("+TableSchedule.COLOUMN_USER_EMAIL+") REFERENCES user ("+TableUser.COLOUMN_USER_EMAIL+"), FOREIGN KEY ("+TableSchedule.COLOUMN_CLASS_ID+") REFERENCES class ("+TableClass.COLOUMN_CLASS_ID+") );";

    static final String DROP_DATABASE = "DROP TABLE schedule; DROP TABLE class; DROP TABLE venue; DROP TABLE user; DROP TABLE module;";

    static abstract class TableUser implements BaseColumns
    {
        static final String TABLE_NAME = "user";

        static final String COLOUMN_USER_EMAIL = "user_Email";
        static final String COLOUMN_DISPLAY_NAME = "display_Name";
        static final String COLOUMN_UNIVERSITY_NUMBER = "university_Number";
        static final String COLOUMN_ROLE = "role";
    }

    static abstract class TableModule implements BaseColumns
    {
        static final String TABLE_NAME = "module";

        static final String COLOUMN_MODULE_CODE = "module_Code";
        static final String COLOUMN_MODULE_DESCR = "module_Description";
        static final String COLOUMN_MODULE_LECTURER = "module_Lecturer";
    }

     static abstract class TableVenue implements BaseColumns
    {
        static final String TABLE_NAME = "venue";

        static final String COLOUMN_VENUEID = "venueID";
        static final String COLOUMN_VENU_NAME = "venue_Name";
    }

    static abstract class TableClass implements BaseColumns
    {
        static final String TABLE_NAME = "class";

        static final String COLOUMN_CLASS_ID = "classID";
        static final String COLOUMN_CLASS_PERIOD = "class_Period";
        static final String COLOUMN_CLASS_DAY = "class_day";
        static final String COLOUMN_VENUEID = "venueID";
        static final String COLOUMN_MODULE_CODE = "module_Code";
    }
    static abstract class TableSchedule implements BaseColumns
    {
        static final String TABLE_NAME = "schedule";

        static final String COLOUMN_SCHEDULEID = "scheduleID";
        static final String COLOUMN_USER_EMAIL = "user_Email";
        static final String COLOUMN_CLASS_ID = "classID";

    }
}

