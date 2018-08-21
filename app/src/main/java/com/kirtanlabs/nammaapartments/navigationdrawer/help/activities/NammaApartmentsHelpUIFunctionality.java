package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters.HelpAndSettingsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class NammaApartmentsHelpUIFunctionality extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private int screenTitle;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_namma_apartments_help;
    }

    @Override
    protected int getActivityTitle() {
        screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listOfHelpServices = findViewById(R.id.listOfHelpServices);

        /*We are initialising list with array list*/
        List<String> helpList = new ArrayList<>();

        switch (screenTitle) {
            case R.string.contact_us:
                /*Adding items in the list*/
                helpList.add(getString(R.string.contact_address));
                break;
            case R.string.terms_and_conditions:
                /*Adding items in the list*/
                helpList.add(getString(R.string.terms_of_use));
                break;
            case R.string.privacy_policy:
                /*Adding items in the list*/
                helpList.add(getString(R.string.policy_statement));
                break;
        }

        /*Creating the Adapter*/
        HelpAndSettingsAdapter helpAdapter = new HelpAndSettingsAdapter(this, helpList);

        /*Attaching adapter to the listview */
        listOfHelpServices.setAdapter(helpAdapter);
    }

}
