package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate.handedthings.handedthingshistory;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class HandedThingsHistory extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.given_things;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();


        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
