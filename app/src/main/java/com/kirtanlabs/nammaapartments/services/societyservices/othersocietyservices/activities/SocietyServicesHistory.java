package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.RetrievingSocietyServiceHistoryList;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters.SocietyServiceHistoryAdapter;
import com.kirtanlabs.nammaapartments.utilities.Constants;

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

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String societyServiceType = getIntent().getStringExtra(Constants.SCREEN_TITLE);

        /*Retrieving all society service request details which user has raised till now*/
        new RetrievingSocietyServiceHistoryList(SocietyServicesHistory.this)
                .getNotificationDataList(societyServiceType, societyServiceNotificationDataList -> {
                    if (societyServiceNotificationDataList == null) {
                        showFeatureUnavailableLayout(R.string.society_service_unavailable_message);
                    } else {
                        SocietyServiceHistoryAdapter adapter = new SocietyServiceHistoryAdapter(societyServiceNotificationDataList, SocietyServicesHistory.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }

}
