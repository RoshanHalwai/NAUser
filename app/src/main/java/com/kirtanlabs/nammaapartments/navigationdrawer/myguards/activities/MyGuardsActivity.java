package com.kirtanlabs.nammaapartments.navigationdrawer.myguards.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.RetrievingGuardsList;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.adapters.MyGuardsAdapter;

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

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showProgressIndicator();

        /*Retrieving Details of all Guards of the society*/
        new RetrievingGuardsList().getGuardDataList(guardDataList -> {
            hideProgressIndicator();
            if (guardDataList != null) {
                recyclerView.setAdapter(new MyGuardsAdapter(MyGuardsActivity.this, guardDataList));
            } else {
                showFeatureUnavailableLayout(R.string.guards_unavailable_message);
            }
        });
    }

}
