package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.adapters.ContactUsAdapter;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.RetrievingSocietyServiceHistoryList;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters.SocietyServiceHistoryAdapter;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.CARPENTER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELECTRICIAN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EVENT_MANAGEMENT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SCRAP_COLLECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.GARBAGE_COLLECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PLUMBER;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/2/2018
 */

public class SocietyServicesHistory extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_service_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String societyServiceType = getIntent().getStringExtra(Constants.SCREEN_TITLE);

        if (societyServiceType.equals(getString(R.string.contact_us))) {
            new RetrievingSocietyServiceHistoryList(SocietyServicesHistory.this)
                    .getSupportDataList(supportList -> {
                        if (supportList == null) {
                            showFeatureUnavailableLayout(R.string.support_unavailable);
                        } else {
                            ContactUsAdapter contactUsAdapter = new ContactUsAdapter(SocietyServicesHistory.this, supportList);
                            recyclerView.setAdapter(contactUsAdapter);
                        }
                    });
        } else {
            /*Retrieving all society service request details which user has raised till now*/
            new RetrievingSocietyServiceHistoryList(SocietyServicesHistory.this)
                    .getNotificationDataList(societyServiceType, societyServiceNotificationDataList -> {
                        hideProgressIndicator();
                        if (societyServiceNotificationDataList == null) {
                            String messageDescription = getString(R.string.society_service_unavailable_message);
                            String textReplacement = getString(R.string.service);
                            String updatedDescription = "";
                            switch (societyServiceType) {
                                case PLUMBER:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.plumber));
                                    break;
                                case CARPENTER:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.carpenter));
                                    break;
                                case ELECTRICIAN:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.electrician));
                                    break;
                                case GARBAGE_COLLECTION:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.garbage_collection));
                                    break;
                                case EVENT_MANAGEMENT:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.event_management));
                                    break;
                                case FIREBASE_CHILD_SCRAP_COLLECTION:
                                    updatedDescription = messageDescription.replace(textReplacement, getString(R.string.scrap_collection));
                                    break;
                            }
                            showFeatureUnAvailableLayout(updatedDescription);
                        } else {
                            SocietyServiceHistoryAdapter adapter = new SocietyServiceHistoryAdapter(societyServiceNotificationDataList, SocietyServicesHistory.this);
                            recyclerView.setAdapter(adapter);
                        }
                    });
        }
    }
}