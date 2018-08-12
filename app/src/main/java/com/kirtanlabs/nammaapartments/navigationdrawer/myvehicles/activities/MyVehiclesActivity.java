package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.RetrievingVehiclesList;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.adapters.MyVehiclesAdapter;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;

public class MyVehiclesActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_vehicles;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_vehicles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showProgressIndicator();

        /*Getting Id's for Recycler View*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieve Vehicles of the Flat*/
        new RetrievingVehiclesList(MyVehiclesActivity.this).getVehicles(vehicleList -> {
            hideProgressIndicator();

            if (vehicleList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.add_your_vehicle_message);
            } else {
                MyVehiclesAdapter vehiclesAdapter = new MyVehiclesAdapter(MyVehiclesActivity.this, vehicleList);
                recyclerView.setAdapter(vehiclesAdapter);
            }
        });

        /*Getting Id for the button*/
        Button buttonAddFamilyMembers = findViewById(R.id.buttonAddMyVehicles);
        buttonAddFamilyMembers.setTypeface(setLatoLightFont(this));
        buttonAddFamilyMembers.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonAddMyVehicles) {
            startActivity(new Intent(this, AddMyVehicleActivity.class));
        }
    }
}
