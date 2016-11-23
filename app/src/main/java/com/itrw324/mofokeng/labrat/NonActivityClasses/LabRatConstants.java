package com.itrw324.mofokeng.labrat.NonActivityClasses;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Mofokeng on 07-Nov-16.
 */

public class LabRatConstants {

    public static final String PACKAGE_NAME = "com\\.itrw324\\.mofokeng\\.labrat";
    public static final int SUCCESSFUL_REQUEST = 9001;
    public static GoogleSignInAccount LOGGED_IN = null;
    public static GoogleApiClient API_CLIENT;

    public final static class Permissions
    {
        /**
         * App Specific code for Accounts Permission
         */
        public static final int ACCOUNTS_PERMISSION = 0;
    }

    public static class Alerts
    {
        public static final int CONTACTS_PERMISSION_DIALOG = 0;
        public static final int USER_INFORMATION_DIALOG = 1;

    }
}
