package com.kirtanlabs.nammaapartments.navigationdrawer.settings;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

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

        /*Getting Id's for all the views*/
        TextView textGeneralSettings = findViewById(R.id.textGeneralSettings);
        TextView textNotificationSettings = findViewById(R.id.textNotificationSettings);
        TextView textChooseLanguage = findViewById(R.id.textChooseLanguage);
        TextView textReportABug = findViewById(R.id.textReportABug);
        TextView textAbout = findViewById(R.id.textAbout);
        Spinner spinnerChooseLanguage = findViewById(R.id.spinnerChooseLanguage);
        Switch switchSounds = findViewById(R.id.switchSounds);
        Switch switchLocationServices = findViewById(R.id.switchLocationServices);
        Switch switchNewMessageNotification = findViewById(R.id.switchNewMessageNotification);
        Switch switchEmailNotification = findViewById(R.id.switchEmailNotification);
        Switch switchVibrate = findViewById(R.id.switchVibrate);
        Switch switchInAppSoundNotification = findViewById(R.id.switchInAppSoundNotification);
        Switch switchProductUpdates = findViewById(R.id.switchProductUpdates);
        EditText editReportABug = findViewById(R.id.editReportABug);

        /*Setting font for all the views*/
        textGeneralSettings.setTypeface(Constants.setLatoBoldFont(this));
        textNotificationSettings.setTypeface(Constants.setLatoBoldFont(this));
        textChooseLanguage.setTypeface(Constants.setLatoBoldFont(this));
        textReportABug.setTypeface(Constants.setLatoBoldFont(this));
        textAbout.setTypeface(Constants.setLatoBoldFont(this));
        switchSounds.setTypeface(Constants.setLatoBoldFont(this));
        switchLocationServices.setTypeface(Constants.setLatoBoldFont(this));
        switchNewMessageNotification.setTypeface(Constants.setLatoBoldFont(this));
        switchEmailNotification.setTypeface(Constants.setLatoBoldFont(this));
        switchVibrate.setTypeface(Constants.setLatoBoldFont(this));
        switchInAppSoundNotification.setTypeface(Constants.setLatoBoldFont(this));
        switchProductUpdates.setTypeface(Constants.setLatoBoldFont(this));
        editReportABug.setTypeface(Constants.setLatoRegularFont(this));

        /*Initialising an Adapter*/
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, getResources().getStringArray(R.array.change_languages_list)) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textLanguage = view.findViewById(android.R.id.text1);
                textLanguage.setTypeface(Constants.setLatoRegularFont(NammaApartmentSettings.this));
                return view;
            }
        };

        /*Setting Adapter to a Spinner*/
        spinnerChooseLanguage.setAdapter(arrayAdapter);
    }

}
