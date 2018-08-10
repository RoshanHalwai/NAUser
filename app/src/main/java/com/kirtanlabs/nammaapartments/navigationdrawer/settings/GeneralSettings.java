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

public class GeneralSettings extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_general_settings;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.general;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textChooseLanguage = findViewById(R.id.textChooseLanguage);
        Spinner spinnerSelectLanguage = findViewById(R.id.spinnerSelectLanguage);
        TextView textAppVersionName = findViewById(R.id.textAppVersionName);
        TextView textReportABug = findViewById(R.id.textReportABug);
        EditText editReportABug = findViewById(R.id.editReportABug);
        Switch switchLocation = findViewById(R.id.switchLocation);

        /*Setting font for all the views*/
        textChooseLanguage.setTypeface(Constants.setLatoBoldFont(this));
        textAppVersionName.setTypeface(Constants.setLatoBoldFont(this));
        textReportABug.setTypeface(Constants.setLatoBoldFont(this));
        editReportABug.setTypeface(Constants.setLatoRegularFont(this));
        switchLocation.setTypeface(Constants.setLatoBoldFont(this));

        String[] languagesList = getResources().getStringArray(R.array.change_languages_list);

        /*Setting font for all the items in the list*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, languagesList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textProblem = view.findViewById(android.R.id.text1);
                textProblem.setTypeface(Constants.setLatoRegularFont(GeneralSettings.this));
                return view;
            }
        };
        //Setting adapter to Spinner view
        spinnerSelectLanguage.setAdapter(adapter);
    }
}

