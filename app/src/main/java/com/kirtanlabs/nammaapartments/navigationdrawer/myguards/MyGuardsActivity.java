package com.kirtanlabs.nammaapartments.navigationdrawer.myguards;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class MyGuardsActivity extends BaseActivity {

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
        recyclerView.setAdapter(new MyGuardsAdapter(this));
    }

}
