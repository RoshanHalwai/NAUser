package com.kirtanlabs.nammaapartments.home.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.fragments.ApartmentServicesHome;
import com.kirtanlabs.nammaapartments.home.fragments.SocietyServicesHome;
import com.kirtanlabs.nammaapartments.navigationdrawer.UserProfile;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.NammaApartmentsHelp;
import com.kirtanlabs.nammaapartments.navigationdrawer.noticeboard.NoticeBoard;
import com.kirtanlabs.nammaapartments.navigationdrawer.settings.NammaApartmentSettings;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.NammaApartmentsGlobal.userUID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class NammaApartmentsHome extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private SmoothActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private Dialog dialog;
    private DatabaseReference userReference;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_nahome_navigation_drawer;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Namma Apartments Home Screen we wouldn't want the users to go back to OTP screen,
          hence hiding the back button from the Title Bar*/
        hideBackButton();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        toggle = new SmoothActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        /* At this point We came to know that user has Successfully Logged In and now its no need to show Splash Screen */
        sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);

        Boolean isLoggedIn = sharedPreferences.getBoolean(Constants.LOGGED_IN, false);

        /*If User is Logged In then take User Uid from Shared Preference*/
        if (isLoggedIn) {
            String userUid = sharedPreferences.getString(Constants.USER_UID, null);
            userReference = Constants.PRIVATE_USERS_REFERENCE.child(userUid);
        } else {
            userReference = Constants.PRIVATE_USERS_REFERENCE
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());

            editor = sharedPreferences.edit();
            editor.putBoolean(Constants.FIRST_TIME, false);
            editor.putBoolean(Constants.LOGGED_IN, true);
            editor.putString(Constants.USER_UID, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            editor.apply();
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /*At this point new user and existing user would have their records in firebase and hence we store
         * the values to NammaApartmentsGlobal*/
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                ((NammaApartmentsGlobal) getApplicationContext()).setNammaApartmentUser(nammaApartmentUser);

                /*Storing user token_id in firebase so Guard can send notification*/
                String token_id = FirebaseInstanceId.getInstance().getToken();
                PRIVATE_USERS_REFERENCE.child(userUID).child("tokenId").setValue(token_id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_myProfile: {
                toggle.runWhenIdle(() -> {
                    Intent intent = new Intent(NammaApartmentsHome.this, UserProfile.class);
                    startActivity(intent);
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_myFamilyMembers: {
                toggle.runWhenIdle(() -> {
                    Intent mySweetHomeIntent = new Intent(NammaApartmentsHome.this, MySweetHome.class);
                    mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mySweetHomeIntent);
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_myNoticeBoard: {
                toggle.runWhenIdle(() -> {
                    Intent noticeBoardIntent = new Intent(NammaApartmentsHome.this, NoticeBoard.class);
                    startActivity(noticeBoardIntent);
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_appSettings: {
                toggle.runWhenIdle(() -> {
                    Intent settingsIntent = new Intent(NammaApartmentsHome.this, NammaApartmentSettings.class);
                    startActivity(settingsIntent);
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_help: {
                toggle.runWhenIdle(() -> {
                    Intent helpIntent = new Intent(NammaApartmentsHome.this, NammaApartmentsHelp.class);
                    startActivity(helpIntent);
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_rateUs: {
                toggle.runWhenIdle(() -> {
                    showRateUsDialog();
                    dialog.show();
                });
                drawer.closeDrawers();
                break;
            }
            case R.id.nav_logout: {
                sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putBoolean(Constants.LOGGED_IN, false);
                editor.putString(Constants.USER_UID, null);
                editor.apply();

                toggle.runWhenIdle(() -> {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(NammaApartmentsHome.this, SignIn.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                });
                drawer.closeDrawers();
                break;
            }
        }
        return true;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This dialog gets invoked when user clicks on rateUs button.
     */
    private void showRateUsDialog() {

        /*Creating and Inflating the layout for rate us dialog */
        dialog = new Dialog(NammaApartmentsHome.this);
        dialog.setContentView(R.layout.layout_rate_us_dialog);

        /*Getting Id's for all the views*/
        TextView textRateDialog = dialog.findViewById(R.id.textRateDialog);
        RatingBar ratingBar = dialog.findViewById(R.id.ratingBar);
        Button buttonRateNow = dialog.findViewById(R.id.buttonRateNow);
        Button buttonRemindLater = dialog.findViewById(R.id.buttonRemindLater);

        /*Setting Fonts for all the views*/
        textRateDialog.setTypeface(setLatoRegularFont(this));
        buttonRateNow.setTypeface(setLatoLightFont(this));
        buttonRemindLater.setTypeface(setLatoLightFont(this));

        /*Setting OnClick Listeners to the views*/
        //TODO:TO Implement on click of Rate Now users will be redirected to PlayStore.
        buttonRateNow.setOnClickListener(v -> dialog.cancel());
        buttonRemindLater.setOnClickListener(v -> dialog.cancel());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /* ------------------------------------------------------------- *
     * SectionPagerAdapter Class
     * ------------------------------------------------------------- */

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /* ------------------------------------------------------------- *
         * Overriding FragmentPagerAdapter Objects
         * ------------------------------------------------------------- */

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SocietyServicesHome();
                case 1:
                    return new ApartmentServicesHome();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

}
