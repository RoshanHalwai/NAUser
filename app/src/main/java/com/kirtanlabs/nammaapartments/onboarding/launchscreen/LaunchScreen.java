package com.kirtanlabs.nammaapartments.onboarding.launchscreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BuildConfig;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.onboarding.ActivationRequired;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.onboarding.splashscreen.SplashScreen;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ACCOUNT_CREATED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIRST_TIME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.LAUNCH_SCREEN_TIME_DURATION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.LOGGED_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PACKAGE_NAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VERIFIED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.VERSION_NAME_REFERENCE;

public class LaunchScreen extends AppCompatActivity {

    /* ------------------------------------------------------------- *
     * Overriding AppCompatActivity Object
     * ------------------------------------------------------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        /*Getting Id's for the Textview*/
        TextView textAppName = findViewById(R.id.textAppName);

        /*Setting Fonts for the Textview*/
        textAppName.setTypeface(Constants.setLatoRegularFont(this));

        /*Mentioning the time duration of the launch screen to be displayed to user*/
        new Handler().postDelayed(this::isNewVersionAvailable, LAUNCH_SCREEN_TIME_DURATION);
    }

    /* ------------------------------------------------------------- *
     * Private Method
     * ------------------------------------------------------------- */

    /**
     * Start corresponding activity based on shared preference data
     */
    private void startCorrespondingActivity() {
        SharedPreferences sharedPreferences = getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        Boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME, true);
        Boolean accountCreated = sharedPreferences.getBoolean(ACCOUNT_CREATED, false);
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

    /**
     * Checks if user has installed the latest version of Namma Apartments Application.
     * If they haven't then navigates them to Play store so they have latest app.
     */
    private void isNewVersionAvailable() {
        if (isNetworkAvailable()) {
            VERSION_NAME_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String newVersionName = dataSnapshot.getValue(String.class);
                    String currentVersionName = BuildConfig.VERSION_NAME;
                    if (newVersionName != null && newVersionName.equals(currentVersionName)) {
                        startCorrespondingActivity();
                    } else {
                        AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(LaunchScreen.this);
                        alertNotifyGateDialog.setCancelable(false);
                        alertNotifyGateDialog.setTitle(getString(R.string.new_version_title));
                        alertNotifyGateDialog.setMessage(getString(R.string.new_version_message));
                        alertNotifyGateDialog.setPositiveButton(getString(R.string.update), (dialog, which) -> redirectUsersToPlayStore());
                        alertNotifyGateDialog.setNegativeButton(getString(R.string.cancel), (dialog, which) -> finish());
                        new Dialog(LaunchScreen.this);
                        alertNotifyGateDialog.show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else {
            AlertDialog.Builder alertNotifyGateDialog = new AlertDialog.Builder(LaunchScreen.this);
            alertNotifyGateDialog.setCancelable(false);
            alertNotifyGateDialog.setTitle(getString(R.string.connection_unavailable_title));
            alertNotifyGateDialog.setMessage(getString(R.string.connection_unavailable_message));
            alertNotifyGateDialog.setPositiveButton(getString(R.string.retry), (dialog, which) -> isNewVersionAvailable());
            new Dialog(LaunchScreen.this);
            alertNotifyGateDialog.show();
        }
    }

    /**
     * Checks if user is connected to internet
     *
     * @return true if connection available else returns false
     */
    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * This method gets invoked to redirect users to playStore Link
     */
    private void redirectUsersToPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + PACKAGE_NAME)));
            finish();
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PACKAGE_NAME)));
        }
    }

}
