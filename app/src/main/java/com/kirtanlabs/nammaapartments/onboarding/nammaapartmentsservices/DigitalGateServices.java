package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

import android.os.Bundle;
import android.widget.GridView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class DigitalGateServices extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_digital_gate_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.title_activity_digital_gate_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int[] imageDigitalServices = {
                R.drawable.invite_visitors,
                R.drawable.my_visitors_list,
                R.drawable.my_daily_services,
                R.drawable.notify_digital_gate,
                R.drawable.my_sweet_home,
                R.drawable.emergency
        };

        String[] stringDigitalGateServices = {
                getString(R.string.invite_visitors),
                getString(R.string.my_visitors_list),
                getString(R.string.my_daily_services),
                getString(R.string.notify_digital_gate),
                getString(R.string.my_sweet_home),
                getString(R.string.emergency)
        };

        /*Getting Id's for all the views*/
        GridView gridViewDigitalGateServices = findViewById(R.id.gridDigitalGate);

        // Instance of ImageAdapter Class
        ImageAdapterDigitalGateServices imageAdapterDigitalGateServices = new ImageAdapterDigitalGateServices(this, imageDigitalServices, stringDigitalGateServices);

        // Setting the imageAdapter
        gridViewDigitalGateServices.setAdapter(imageAdapterDigitalGateServices);
        imageAdapterDigitalGateServices.notifyDataSetChanged();

    }
}
