package com.kirtanlabs.nammaapartments.navigationdrawer.help.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters.HelpAndSettingsAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class NammaApartmentsHelp extends BaseActivity implements AdapterView.OnItemClickListener {

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
        helpList.add((getString(R.string.contact_us)));
        helpList.add(getString(R.string.terms_and_conditions));
        helpList.add(getString(R.string.privacy_policy));

        /*Creating the Adapter*/
        HelpAndSettingsAdapter helpAdapter = new HelpAndSettingsAdapter(this, helpList);

        /*Attaching adapter to the listview */
        listOfHelpServices.setAdapter(helpAdapter);

        /*Setting event for list view items*/
        listOfHelpServices.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                Intent faqIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.nammaapartments.com/faq-template-master/index.html"));
                startActivity(faqIntent);
                break;
            case 1:
                Intent contactIntent = new Intent(NammaApartmentsHelp.this, NammaApartmentsHelpUIFunctionality.class);
                contactIntent.putExtra(SCREEN_TITLE, R.string.contact_us);
                startActivity(contactIntent);
                break;
            case 2:
                Intent termsIntent = new Intent(NammaApartmentsHelp.this, NammaApartmentsHelpUIFunctionality.class);
                termsIntent.putExtra(SCREEN_TITLE, R.string.terms_and_conditions);
                startActivity(termsIntent);
                break;
            case 3:
                Intent policyIntent = new Intent(NammaApartmentsHelp.this, NammaApartmentsHelpUIFunctionality.class);
                policyIntent.putExtra(SCREEN_TITLE, R.string.privacy_policy);
                startActivity(policyIntent);
                break;
        }
    }
}
