package com.example.ilkut.iskibris;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainArea extends AppCompatActivity implements FragSearchJob.FragListener{


    ListView drawerListView;
    ActionBarDrawerToggle theToggle;        //Careful! This is v7
    DrawerLayout drawerLayout;
    android.support.v7.widget.Toolbar mainToolbar;
    Typeface DINPro;
    MainDrawerCustomAdapter drawerCustomAdapter;
    ArrayList<MainDrawerItem> drawerItems;
    TypedArray drawerTitles;
    TypedArray drawerDrawebles;
    FragmentManager mManager;
    FragmentTransaction mTransaction;
    AlertDialog infoDialog;


    //TODO: Check role, well, the whole role thingie, you know!
    String userName;
    String userRole;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_area);

        SharedPreferences mPrefs = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        Boolean isLogged = mPrefs.getBoolean("logged", false);

        //TODO: Check whether this works, somehow
        //Set username and role from shared prefs (the values were saved in shared preferences in the splash screen)
        if (isLogged) {
            userName = mPrefs.getString("uname", null);
            userRole = mPrefs.getString("role", null);

            if (userName == null || userRole == null) {
                redirectLogin();
                //TODO: Check this
            }

        } else {
            redirectLogin();
        }

        mManager = getSupportFragmentManager();

        //region Navigation Drawer & Toolbar

        drawerListView = (ListView) findViewById(R.id.drawerListView);

        //Inflate the correct drawer based on the role of the account
        if (userRole != null) {
            if (userRole.equalsIgnoreCase("candidate")) {


                mTransaction = mManager.beginTransaction();
                mTransaction.add(R.id.fragment_container, new FragHomeEmployee());
                mTransaction.commit();

                drawerTitles = getResources().obtainTypedArray(R.array.drawer_titles_candidate);
                drawerDrawebles = getResources().obtainTypedArray(R.array.drawer_icons_candidate);
                drawerItems = new ArrayList<>();

                //CAREFUL! Number changes according to the number of items on the list!  ->  TODO: use array.count so that it's automatic!
                for (int i = 0; i < 6; i++) {
                    drawerItems.add(new MainDrawerItem(drawerDrawebles.getDrawable(i), drawerTitles.getString(i)));
                }

                drawerCustomAdapter = new MainDrawerCustomAdapter(this, drawerItems);
                drawerListView.setAdapter(drawerCustomAdapter);
                //Candidate
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                setFragmentLayout(SwitchFragment.HOMEPAGE_CANDIDATE);
                                break;
                            case 1:
                                setFragmentLayout(SwitchFragment.SEARCH_CANDIDATE);
                                break;
                            case 2:
                                setFragmentLayout(SwitchFragment.MANAGE_RESUME);
                                break;
                            case 3:
                                setFragmentLayout(SwitchFragment.BLOG);
                                break;
                            case 4:
                                setFragmentLayout(SwitchFragment.SETTINGS);
                                break;
                            case 5:
                                logOut();
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });
            } else if (userRole.equalsIgnoreCase("employer")) {

                mTransaction = mManager.beginTransaction();
                mTransaction.add(R.id.fragment_container, new FragHomeEmployer());
                mTransaction.commit();

                drawerTitles = getResources().obtainTypedArray(R.array.drawer_titles_employer);
                drawerDrawebles = getResources().obtainTypedArray(R.array.drawer_icons_employer);
                drawerItems = new ArrayList<>();

                //CAREFUL! Number changes according to the number of items on the list!
                for (int i = 0; i < 7; i++) {
                    drawerItems.add(new MainDrawerItem(drawerDrawebles.getDrawable(i), drawerTitles.getString(i)));
                }

                drawerCustomAdapter = new MainDrawerCustomAdapter(this, drawerItems);
                drawerListView.setAdapter(drawerCustomAdapter);
                //Employer
                drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                setFragmentLayout(SwitchFragment.HOMEPAGE_EMPLOYER);
                                //Homepage
                                break;
                            case 1:
                                setFragmentLayout(SwitchFragment.SEARCH_EMPLOYER);
                                //Search
                                break;
                            case 2:
                                setFragmentLayout(SwitchFragment.JOB_DASHBOARD);
                                break;
                            case 3:
                                setFragmentLayout(SwitchFragment.ALL_CV);
                                //All CV
                                break;
                            case 4:
                                setFragmentLayout(SwitchFragment.BLOG);
                                break;
                            case 5:
                                setFragmentLayout(SwitchFragment.SETTINGS);
                                break;
                            case 6:
                                logOut();
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });

            }
        }

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.mainToolbar);
        mainToolbar.setTitle(mPrefs.getString("uname", getResources().getString(R.string.app_name_formatted)));
        setSupportActionBar(mainToolbar);

        theToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                mainToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        drawerLayout.setDrawerListener(theToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //endregion


    }

    public void redirectLogin() {
        infoDialog = new AlertDialog.Builder(this).create();
        infoDialog.setMessage(getResources().getString(R.string.main_not_logged_in));

        infoDialog.setButton(DialogInterface.BUTTON_NEUTRAL, getResources().getText(R.string.main_ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                infoDialog.cancel();
                Intent mainActivityIntent;
                mainActivityIntent = new Intent(MainArea.this, MainActivity.class);
                startActivity(mainActivityIntent);
                finish();
            }
        });
        infoDialog.show();
    }

    public void logOut() {
        SharedPreferences mPrefs = getSharedPreferences("PREFS", Activity.MODE_PRIVATE);
        SharedPreferences.Editor mPrefsEditor = mPrefs.edit();
        mPrefsEditor.remove("logged");
        mPrefsEditor.remove("uname");
        mPrefsEditor.remove("paword");
        mPrefsEditor.remove("role");
        mPrefsEditor.apply();

        SingletonCache.getInstance().clearJobListingsCache();
        SingletonCache.getInstance().clearUserResumesCache();
        SingletonCache.getInstance().clearBlogPostsCache();

        Intent mainActivityIntent;
        mainActivityIntent = new Intent(MainArea.this, MainActivity.class);
        startActivity(mainActivityIntent);
        finish();
    }

    enum SwitchFragment {
        HOMEPAGE_EMPLOYER,
        HOMEPAGE_CANDIDATE,
        SEARCH_EMPLOYER,
        SEARCH_CANDIDATE,
        MANAGE_RESUME,
        JOB_DASHBOARD,
        ALL_CV,
        BLOG,
        SETTINGS
    }

    public void setFragmentLayout(SwitchFragment frag) {
        mTransaction = mManager.beginTransaction();

        switch (frag) {
            case HOMEPAGE_CANDIDATE:
                mTransaction.replace(R.id.fragment_container, new FragHomeEmployee());
                mTransaction.commit();
                break;
            case HOMEPAGE_EMPLOYER:
                mTransaction.replace(R.id.fragment_container, new FragHomeEmployer());
                mTransaction.commit();
                break;
            case SEARCH_EMPLOYER:

                break;
            case SEARCH_CANDIDATE:
                mTransaction.replace(R.id.fragment_container, new FragSearchJob());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                break;
            case MANAGE_RESUME:
                mTransaction.replace(R.id.fragment_container, new FragUserResumes());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                break;
            case JOB_DASHBOARD:
                mTransaction.replace(R.id.fragment_container, new FragJobListings());
                mTransaction.commit();
                break;
            case ALL_CV:

                break;
            case BLOG:
                mTransaction.replace(R.id.fragment_container, new FragBlog());
                mTransaction.commit();
                break;
            case SETTINGS:
                mTransaction.replace(R.id.fragment_container, new FragSettings());
                mTransaction.commit();
                break;
            default:
                //Homepage
                if (userRole.equals("employer")) {
                    mTransaction.replace(R.id.fragment_container, new FragHomeEmployer());
                    mTransaction.commit();
                } else if (userRole.equals("candidate")) {
                    mTransaction.replace(R.id.fragment_container, new FragHomeEmployee());
                    mTransaction.commit();
                }
                break;
        }

    }

    @Override
    public void setExternalFragment(ExternalFragment frag) {
        mTransaction = mManager.beginTransaction();

        switch (frag) {
            case SEARCH_JOBS_RESULTS:
                mTransaction.replace(R.id.fragment_container, new FragSearchJobsResults());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                break;
            case SEARCH_JOBS_SCREEN:
                mTransaction.replace(R.id.fragment_container, new FragSearchJob());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                break;
            case SEARCH_RESUMES_RESULTS:
                mTransaction.replace(R.id.fragment_container, new FragSearchResume());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
            case SEARCH_RESUMES_SCREEN:
                mTransaction.replace(R.id.fragment_container, new FragSearchResumesResults());
                mTransaction.addToBackStack(null);
                mTransaction.commit();
            default:
                mTransaction.replace(R.id.fragment_container, new FragSearchJob());     //TODO: Are you sure?...
                mTransaction.addToBackStack(null);
                mTransaction.commit();
                break;
        }
    }

    public void setTypefaces() {
        DINPro = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/DINPro.otf");

    }

    //region Navigation Drawer & Toolbar

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        theToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        theToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (theToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion

}




