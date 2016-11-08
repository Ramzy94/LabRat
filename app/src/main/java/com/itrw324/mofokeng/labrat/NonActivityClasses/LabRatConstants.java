package com.itrw324.mofokeng.labrat.NonActivityClasses;

/**
 * Created by Mofokeng on 07-Nov-16.
 */

public final class LabRatConstants {

    public static final String PACKAGE_NAME = "com\\.itrw324\\.mofokeng\\.labrat";
    public static final int SUCCESSFUL_REQUEST = 9001;

    public final static class Permissions
    {
        /**
         * App Specific code for Accounts Permission
         */
        public static final int ACCOUNTS_PERMISSION = 0;
    }

    public static final class UserDialogs
    {
        public static final String USER_NOT_REGISTERED = "The user is not yet registered";
        public static final String USER_REGISTERED = "The user is already Registered";
        public static final String REGISTRATION_SUCCESSFUL = "Registration Successfully Completed";
        public static final String CONTACT_PERMISSION = "LabRat Needs to be able to Access Your Google Account To Proceed";
    }
}
