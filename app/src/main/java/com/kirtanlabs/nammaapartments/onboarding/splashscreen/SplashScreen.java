package com.kirtanlabs.nammaapartments.onboarding.splashscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.onboarding.ActivationRequired;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVILEGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIRST_TIME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.LOGGED_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VERIFIED;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        startCorrespondingActivity();
    }

    /**
     * Start corresponding activity based on shared preference data
     */
    //TODO: Logic seems to be correct but can be written in a efficient way and startActivity API can be reused
    private void startCorrespondingActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        Boolean firstTime = sharedPreferences.getBoolean(Constants.FIRST_TIME, true);
        Boolean accountCreated = sharedPreferences.getBoolean(Constants.ACCOUNT_CREATED, false);
        Boolean isLoggedIn = sharedPreferences.getBoolean(LOGGED_IN, false);
        Boolean isUserVerified = sharedPreferences.getBoolean(VERIFIED, false);

        if (firstTime) {
            sharedPreferences.edit().putBoolean(FIRST_TIME, false).apply();
            SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            ViewPager mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            tabLayout.setupWithViewPager(mViewPager, true);
        } else {
            if (accountCreated) {
                if (isUserVerified) {
                    if (isLoggedIn) {
                        startActivity(new Intent(this, NammaApartmentsHome.class));
                        finish();
                    } else {
                        startActivity(new Intent(this, SignIn.class));
                        finish();
                    }
                } else {
                    String userUID = sharedPreferences.getString(USER_UID, "");
                    DatabaseReference database = PRIVATE_USERS_REFERENCE.child(userUID).child(FIREBASE_CHILD_PRIVILEGES)
                            .child(VERIFIED);
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getValue(Boolean.class)) {
                                sharedPreferences.edit().putBoolean(VERIFIED, true).apply();
                                startActivity(new Intent(SplashScreen.this, NammaApartmentsHome.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashScreen.this, ActivationRequired.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                startActivity(new Intent(this, SignIn.class));
                finish();
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new SplashNammaApartments();
                case 1:
                    return new SplashSocietyServices();
                case 2:
                    return new SplashApartmentServices();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
