package com.kirtanlabs.nammaapartments.navigationdrawer.nammaapartmentssettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.HelpAndSettingsAdapter;

import java.util.ArrayList;
import java.util.List;

public class NammaApartmentSettings extends BaseActivity implements AdapterView.OnItemClickListener {

    List<String> settingsList;
    ListView listOfSettings;

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

        /*Getting Id for List View*/
        listOfSettings = findViewById(R.id.listSettings);

        /*We are initialising list with array list*/
        settingsList = new ArrayList<>();

        /*Setting event for list view items*/
        listOfSettings.setOnItemClickListener(this);

        /*Adding items in the list*/
        settingsList.add((getString(R.string.general)));
        settingsList.add((getString(R.string.notifications)));

        /*Creating the Adapter*/
        HelpAndSettingsAdapter helpAdapter = new HelpAndSettingsAdapter(this, settingsList);

        //*Attaching adapter to the listview *//*
        listOfSettings.setAdapter(helpAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(NammaApartmentSettings.this, GeneralSettings.class));
                break;
            case 1:
                startActivity(new Intent(NammaApartmentSettings.this, NotificationSettings.class));
                break;
        }
    }

}
