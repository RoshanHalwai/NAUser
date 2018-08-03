package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.handedthings.handedthingshistory.GuestsHistoryAdapter;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.handedthings.handedthingshistory.HandedThingsHistory;

import java.util.List;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/2/2018
 */

public class SocietyServicesHistory extends BaseActivity {

    private SocietyServiceHistoryAdapter societyServiceHistoryAdapter;

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

        RetrievingSocietyServiceHistoryList retrievingSocietyServiceHistoryList = new RetrievingSocietyServiceHistoryList();
        retrievingSocietyServiceHistoryList.getHistoryNotificationDataList(new RetrievingSocietyServiceHistoryList.HistoryNotificationDataListCallback() {
            @Override
            public void onCallback(List<NammaApartmentSocietyServices> historyNotificationDataList) {
                if (historyNotificationDataList == null) {
                    showFeatureUnavailableLayout(R.string.society_service_unavailable_message);
                } else {
                    for (NammaApartmentSocietyServices nammaApartmentSocietyServices : historyNotificationDataList) {
                        if (nammaApartmentSocietyServices.getSocietyServiceType() == "plumber") {
                            societyServiceHistoryAdapter = new SocietyServiceHistoryAdapter(historyNotificationDataList, SocietyServicesHistory.this);
                            recyclerView.setAdapter(societyServiceHistoryAdapter);
                        }
                    }
                }
            }
        });
    }

}
