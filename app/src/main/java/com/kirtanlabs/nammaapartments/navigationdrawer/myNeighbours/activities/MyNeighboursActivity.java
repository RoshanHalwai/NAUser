package com.kirtanlabs.nammaapartments.navigationdrawer.myNeighbours.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myNeighbours.adapters.MyNeighboursAdapter;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/8/2018
 */
public class MyNeighboursActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_neighbours;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_neighbours;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id of recycler view*/
        RecyclerView recyclerViewMyNeighbour = findViewById(R.id.recyclerViewMyNeighbour);
        recyclerViewMyNeighbour.setHasFixedSize(true);
        recyclerViewMyNeighbour.setLayoutManager(new LinearLayoutManager(this));

        /*Setting Adapter to the view*/
        recyclerViewMyNeighbour.setAdapter(new MyNeighboursAdapter(this));
    }
}
