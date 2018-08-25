package com.kirtanlabs.nammaapartments.navigationdrawer.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;

import static com.kirtanlabs.nammaapartments.utilities.Constants.LOGGED_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class NammaApartmentSettings extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_namma_apartment_settings;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.action_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textGeneralSettings = findViewById(R.id.textGeneralSettings);
        TextView textLanguage = findViewById(R.id.textLanguage);
        TextView textSoundSettings = findViewById(R.id.textSoundSettings);
        Button buttonLanguage = findViewById(R.id.buttonLanguage);
        Button buttonSignOut = findViewById(R.id.buttonSignOut);
        Switch eIntercomNotifications = findViewById(R.id.eIntercomNotifications);
        Switch switchGuestNotification = findViewById(R.id.switchGuestNotification);
        Switch switchDailyServiceNotification = findViewById(R.id.switchDailyServiceNotification);
        Switch switchCabNotification = findViewById(R.id.switchCabNotification);
        Switch switchPackageNotification = findViewById(R.id.switchPackageNotification);

        textGeneralSettings.setTypeface(setLatoBoldFont(this));
        textSoundSettings.setTypeface(setLatoBoldFont(this));
        textLanguage.setTypeface(setLatoRegularFont(this));
        buttonLanguage.setTypeface(setLatoRegularFont(this));
        buttonSignOut.setTypeface(setLatoRegularFont(this));
        eIntercomNotifications.setTypeface(setLatoRegularFont(this));
        switchGuestNotification.setTypeface(setLatoRegularFont(this));
        switchDailyServiceNotification.setTypeface(setLatoRegularFont(this));
        switchCabNotification.setTypeface(setLatoRegularFont(this));
        switchPackageNotification.setTypeface(setLatoRegularFont(this));

        buttonSignOut.setOnClickListener(v -> showLogOutDialog());
    }

    /**
     * This dialog gets invoked when user clicks on Logout button.
     */
    private void showLogOutDialog() {
        Runnable logoutUser = () ->
        {
            SharedPreferences sharedPreferences;
            SharedPreferences.Editor editor;
            sharedPreferences = getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putBoolean(LOGGED_IN, false);
            editor.putString(USER_UID, null);
            editor.apply();

            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(NammaApartmentSettings.this, SignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        };
        String confirmDialogTitle = getString(R.string.logout_dialog_title);
        String confirmDialogMessage = getString(R.string.logout_question);
        showConfirmDialog(confirmDialogTitle, confirmDialogMessage, logoutUser);
    }

}
