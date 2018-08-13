package com.kirtanlabs.nammaapartments.navigationdrawer.myguards.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.adapters.MyGuardsAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.RetrievingGuardsList;

public class MyGuardsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_guards;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_guards;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieving Details of all Guards of the society*/
        new RetrievingGuardsList().getGuardDataList(guardDataList -> {
            if (guardDataList != null) {
                recyclerView.setAdapter(new MyGuardsAdapter(MyGuardsActivity.this, guardDataList));
            }
        });
    }

}
