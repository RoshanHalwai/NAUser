package com.kirtanlabs.nammaapartments.onboarding.launchscreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.onboarding.ActivationRequired;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.onboarding.splashscreen.SplashScreen;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIRST_TIME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.LOGGED_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VERIFIED;

public class LaunchScreen extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Object
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        /*Mentioning the time duration of the launch screen to be displayed to user*/
        new Handler().postDelayed(this::startCorrespondingActivity, Constants.LAUNCH_SCREEN_TIME_DURATION);
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * Start corresponding activity based on shared preference data
     */
    //TODO: Logic seems to be correct but can be written in a efficient way and startActivity API can be reused
    private void startCorrespondingActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        Boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME, true);
        Boolean accountCreated = sharedPreferences.getBoolean(Constants.ACCOUNT_CREATED, false);
        Boolean isLoggedIn = sharedPreferences.getBoolean(LOGGED_IN, false);
        Boolean isUserVerified = sharedPreferences.getBoolean(VERIFIED, false);

        if (firstTime) {
            startActivity(new Intent(this, SplashScreen.class));
            finish();
        } else {
            if (accountCreated) {
                if (isUserVerified) {
                    if (isLoggedIn) {
                        startActivity(new Intent(LaunchScreen.this, NammaApartmentsHome.class));
                        finish();
                    } else {
                        startActivity(new Intent(this, SignIn.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(LaunchScreen.this, ActivationRequired.class));
                    finish();
                }
            } else {
                startActivity(new Intent(this, SignIn.class));
                finish();
            }
        }
    }
}
