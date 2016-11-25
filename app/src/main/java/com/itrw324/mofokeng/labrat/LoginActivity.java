package com.itrw324.mofokeng.labrat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.NonActivityClasses.UserAccount;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    private GoogleApiClient googleApiClient;
    private DatabaseHandler handler;
    private Context context;
    private GoogleSignInAccount acct;
    private SignInButton button;
    private AlertDialog dialog;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        setTitle(R.string.title_login);

        context = this.getApplicationContext();
        button = (SignInButton) findViewById(R.id.btnGoogle);
        button.setOnClickListener(this);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();


        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        handler = new DatabaseHandler(this);
        handler.insertModules();
        handler.insertVenues();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LabRatConstants.Permissions.ACCOUNTS_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signInIntent();
                } else {
                    toast = Toast.makeText(this, "Nope", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            break;
        }
    }


    public void onClick(View view) {
        if (checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            this.signInIntent();
        } else {
            if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {

            }
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, LabRatConstants.Permissions.ACCOUNTS_PERMISSION);
        }
    }

    public void signInIntent() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, LabRatConstants.SUCCESSFUL_REQUEST);
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.println(Log.ERROR,"Yoh","No Connection bra");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        handler = new DatabaseHandler(context);

        if (requestCode == LabRatConstants.SUCCESSFUL_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                acct = result.getSignInAccount();

                if (handler.alreadySignedUp(acct.getEmail())) {
                    //LabRatConstants.LOGGED_IN = acct;
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                } else {
                    dialog = getDialog(0);
                    dialog.show();
                }
            } else {
                toast = Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_LONG);
            }
        }
    }

    public void success()
    {
        toast = Toast.makeText(context,"Yess Bitch",Toast.LENGTH_LONG);
        toast.show();
    }

    public void res()
    {
        toast = Toast.makeText(context,"Yess djdsj",Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        if(intent.getBooleanExtra("SIGN_OUT",false))
        {
           // googleApiClient = LabRatConstants.API_CLIENT;
            //signOut();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private AlertDialog getDialog(int Dialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch (Dialog) {
            case 0: {
                builder.setView(R.layout.content_dialog);
                builder.setTitle(R.string.moreDetails);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.btnProceed, new DetailsDialog());
            }
            break;
            case 1: {
                builder.setTitle(R.string.permission_Accounts_Header);
                builder.setMessage(R.string.permission_Accounts);
                builder.setCancelable(true);
            }
            break;
        }

        return builder.create();
    }



    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        toast = Toast.makeText(context,"Successfully Signed Out",Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
    }

    public void signOut(MenuItem item) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        revokeAccess();
                    }
                });
    }


    class DetailsDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            EditText text = (EditText) dialog.findViewById(R.id.txt_UNum);
            CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.chkLecturer);

            UserAccount account = new UserAccount(acct, text.getText().toString());

            if (checkBox.isChecked())
                account.setRole(UserAccount.LECTURER);
            else
                account.setRole(UserAccount.STUDENT);

            LabRatConstants.LOGGED_IN = account;
            LabRatConstants.API_CLIENT = googleApiClient;

            //handler.insertUser(account);

            Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
        }
    }

}