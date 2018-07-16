package com.kirtanlabs.nammaapartments.nammaapartmentshome.navigationdrawer;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.List;

public class NammaApartmentsHelp extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    List<String> helpList;
    ListView listOfHelpServices;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        //We would use the same layout we created for Notify Gate.
        return R.layout.activity_namma_apartments_help;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.help;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        listOfHelpServices = findViewById(R.id.listOfHelpServices);

        /*We are initialising list with array list*/
        helpList = new ArrayList<>();

        /*Adding items in the list*/
        helpList.add((getString(R.string.frequently_asked_questions)));
        helpList.add((getString(R.string.using_na_app)));
        helpList.add((getString(R.string.contact_us)));
        helpList.add(getString(R.string.Terms_and_conditions));
        helpList.add(getString(R.string.privacy_policy));

        /*Creating the Adapter*/
        HelpAdapter helpAdapter = new HelpAdapter(this, helpList);

        //*Attaching adapter to the listview *//*
        listOfHelpServices.setAdapter(helpAdapter);
    }
}
