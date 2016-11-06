package com.itrw324.mofokeng.labrat.NonActivityClasses;

import android.provider.BaseColumns;

/**
 * Created by Mofokeng on 05-Nov-16.
 */

public final class Database
{
    public static final String SQL_CREATE_USER = "CREATE TABLE "+TableUser.TABLE_NAME+"( "+TableUser.COLOUMN_USER_EMAIL+" TEXT PRIMARY KEY NOT NULL, "+TableUser.COLOUMN_DISPLAY_NAME+" TEXT NULL, "+TableUser.COLOUMN_UNIVERSITY_NUMBER+" TEXT NULL, "+TableUser.COLOUMN_ROLE+" TEXT NULL );";
    public static final String SQL_CREATE_MODULE = "CREATE TABLE "+TableModule.TABLE_NAME+" ( "+TableModule.COLOUMN_MODULE_CODE+" TEXT PRIMARY KEY NOT NULL, "+TableModule.COLOUMN_MODULE_DESCR+" TEXT NULL, "+TableModule.COLOUMN_MODULE_LECTURER+" TEXT NULL );";
    public static final String SQL_CREATE_VENUE = "CREATE TABLE "+TableVenue.TABLE_NAME+" ( "+TableVenue.COLOUMN_VENUEID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableVenue.COLOUMN_VENU_NAME+" TEXT NULL );";
    public static final String SQL_CREATE_CLASS = "CREATE TABLE "+TableClass.TABLE_NAME+" ( "+TableClass.COLOUMN_CLASS_ID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableClass.COLOUMN_CLASS_PERIOD+" INTEGER NULL, "+TableClass.COLOUMN_VENUEID+" INTEGER NOT NULL, "+TableClass.COLOUMN_MODULE_CODE+" TEXT NOT NULL, FOREIGN KEY ("+TableClass.COLOUMN_VENUEID+") REFERENCES venue ("+TableVenue.COLOUMN_VENUEID+"), FOREIGN KEY ("+TableClass.COLOUMN_MODULE_CODE+") REFERENCES module ("+TableModule.COLOUMN_MODULE_CODE+") );";
    public static final String SQL_CREATE_SCHEDULE = "CREATE TABLE "+TableSchedule.TABLE_NAME+" ( "+TableSchedule.COLOUMN_SCHEDULEID+" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+TableSchedule.COLOUMN_USER_EMAIL+" TEXT NOT NULL, "+TableSchedule.COLOUMN_CLASS_ID+" INTEGER NOT NULL, FOREIGN KEY ("+TableSchedule.COLOUMN_USER_EMAIL+") REFERENCES user ("+TableUser.COLOUMN_USER_EMAIL+"), FOREIGN KEY ("+TableSchedule.COLOUMN_CLASS_ID+") REFERENCES class ("+TableClass.COLOUMN_CLASS_ID+") );";

    public static abstract class TableUser implements BaseColumns
    {
        public static final String TABLE_NAME = "user";

        public static final String COLOUMN_USER_EMAIL = "user_Email";
        public static final String COLOUMN_DISPLAY_NAME = "display_Name";
        public static final String COLOUMN_UNIVERSITY_NUMBER = "university_Number";
        public static final String COLOUMN_ROLE = "role";
    }

    public static abstract class TableModule implements BaseColumns
    {
        public static final String TABLE_NAME = "module";

        public static final String COLOUMN_MODULE_CODE = "module_Code";
        public static final String COLOUMN_MODULE_DESCR = "module_Description";
        public static final String COLOUMN_MODULE_LECTURER = "module_Lecturer";
    }

    public static abstract class TableVenue implements BaseColumns
    {
        public static final String TABLE_NAME = "venue";

        public static final String COLOUMN_VENUEID = "venueID";
        public static final String COLOUMN_VENU_NAME = "venue_Name";
    }

    public static abstract class TableClass implements BaseColumns
    {
        public static final String TABLE_NAME = "class";

        public static final String COLOUMN_CLASS_ID = "classID";
        public static final String COLOUMN_CLASS_PERIOD = "class_Period";
        public static final String COLOUMN_VENUEID = "venueID";
        public static final String COLOUMN_MODULE_CODE = "module_Code";
    }
    public static abstract class TableSchedule implements BaseColumns
    {
        public static final String TABLE_NAME = "schedule";

        public static final String COLOUMN_SCHEDULEID = "scheduleID";
        public static final String COLOUMN_USER_EMAIL = "user_Email";
        public static final String COLOUMN_CLASS_ID = "classID";

    }
}

