package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class VisitorsList extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitors_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_visitors_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        VisitorsListAdapter adapter = new VisitorsListAdapter(this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(adapter);
    }
}
