package com.itrw324.mofokeng.labrat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.itrw324.mofokeng.labrat.NonActivityClasses.DatabaseHandler;
import com.itrw324.mofokeng.labrat.NonActivityClasses.LabRatConstants;
import com.itrw324.mofokeng.labrat.UIFragments.ClassFragment;
import com.itrw324.mofokeng.labrat.UIFragments.DevBlogFragment;
import com.itrw324.mofokeng.labrat.UIFragments.LabFragment;
import com.itrw324.mofokeng.labrat.UIFragments.ScheduleFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ScheduleFragment.OnFragmentInteractionListener,
        DevBlogFragment.OnFragmentInteractionListener,
        LabFragment.OnFragmentInteractionListener,
        ClassFragment.OnFragmentInteractionListener{

    private FragmentManager fragManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DatabaseHandler handler = new DatabaseHandler(this.getApplicationContext());
        handler.getModuleList();
        handler.getVenueList();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragManager = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragManager.beginTransaction();
        fragTransaction.add(R.id.fragContainer,new ScheduleFragment()).commit();

        setTitle(R.string.title_schdule);
        context = this;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        setView(id);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setView(int id)
    {
        ScheduleFragment scheduleFragment;
        LabFragment labFragment;
        DevBlogFragment devBlogFragment;
        ClassFragment classFragment;

        FragmentTransaction fragTransaction = fragManager.beginTransaction();

        switch (id)
        {
            case R.id.nav_schedule:{
                scheduleFragment = new ScheduleFragment();
                fragTransaction.replace(R.id.fragContainer,scheduleFragment);
                fragTransaction.addToBackStack(null);
                setTitle(R.string.title_schdule);
            }break;
            case R.id.nav_venues:{
                labFragment = new LabFragment();
                fragTransaction.replace(R.id.fragContainer,labFragment);
                fragTransaction.addToBackStack(null);
                setTitle(R.string.title_venues);
            }break;
            case R.id.nav_devBlog:{
                devBlogFragment = new DevBlogFragment();
                fragTransaction.replace(R.id.fragContainer,devBlogFragment);
                fragTransaction.addToBackStack(null);
                setTitle(R.string.title_devblog);
            }break;
            case R.id.nav_signout:{
                signOut();
            }break;
            case R.id.nav_classes:{
                classFragment = new ClassFragment();
                fragTransaction.replace(R.id.fragContainer,classFragment);
                fragTransaction.addToBackStack(null);
                setTitle(R.string.title_classes);
            }break;
        }
        fragTransaction.commit();
    }

    private GoogleApiClient apiClient;
    private void signOut() {


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        apiClient = new GoogleApiClient.Builder(MainActivity.this).enableAutoManage(MainActivity.this,new Something()).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        Intent i = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(i, LabRatConstants.SUCCESSFUL_REQUEST);
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(apiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(context,"Successfully Signed Out",Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==LabRatConstants.SUCCESSFUL_REQUEST)
            Auth.GoogleSignInApi.signOut(apiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            revokeAccess();
                        }
                    });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void signOut(MenuItem item) {

    }

    class Something implements GoogleApiClient.OnConnectionFailedListener
    {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {



        }
    }
}