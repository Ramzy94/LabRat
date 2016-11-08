package com.itrw324.mofokeng.labrat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.NonActivityClasses.UserAccount;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener  {

    private GoogleApiClient googleApiClient;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions).build();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case LabRatConstants.Permissions.ACCOUNTS_PERMISSION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    signInIntent();
                }
                else {
                    toast = Toast.makeText(this,"Nope",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }break;
        }
    }

    public void onClick(View view)
    {
        if(checkSelfPermission(Manifest.permission.GET_ACCOUNTS)== PackageManager.PERMISSION_GRANTED) {
            this.signInIntent();
        }
        else
        {
            if(shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS))
            {
                toast = Toast.makeText(this,"Acounts Are Needed to Login Bra",Toast.LENGTH_SHORT);
                toast.show();
            }
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, LabRatConstants.Permissions.ACCOUNTS_PERMISSION);
        }
    }

    public void signInIntent()
    {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 9001);
    }

    public void onConnectionFailed(ConnectionResult result)
    {
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast toast;
        if (requestCode == 9001) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                toast = Toast.makeText(this,"Signed in as "+acct.getDisplayName(),Toast.LENGTH_LONG);

                DatabaseHandler handler = new DatabaseHandler(this.getApplicationContext());

                UserAccount account = new UserAccount();
                account.setAccount(acct);
                account.setRole(UserAccount.STUDENT);
                account.setUniversity_Number("24604186");

                handler.insertUser(account);

                Cursor c = handler.selectUser(account.getAccount().getEmail());

                c.moveToFirst();

                if(c.getCount()==0)
                {
                    toast = Toast.makeText(this.getApplicationContext(), "Hai No", Toast.LENGTH_LONG);
                    toast.show();
                }
                else
                {
                    {
                        String res = c.getString(0)+"\t"+c.getString(1)+"\t"+c.getString(2)+"\t"+c.getString(3);
                        Log.println(Log.INFO,"database Bra",res);
                    }
                }
            }
            else
            {
                toast = Toast.makeText(this,"Login Unsuccessful",Toast.LENGTH_LONG);
            }
            toast.show();
        }
    }
}