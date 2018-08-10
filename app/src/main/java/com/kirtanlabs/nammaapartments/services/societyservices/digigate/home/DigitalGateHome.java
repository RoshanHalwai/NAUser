package com.kirtanlabs.nammaapartments.services.societyservices.digigate.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.InvitingVisitors;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.DailyServicesHome;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.VisitorsList;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.NotifyGateAndEmergencyHome;
import com.kirtanlabs.nammaapartments.utilities.Constants;

public class DigitalGateHome extends BaseActivity implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_digital_gate_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.digital_gate_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting for grid view*/
        GridView gridViewDigitalGateServices = findViewById(R.id.gridDigitalGate);

        // Setting the imageAdapter
        gridViewDigitalGateServices.setAdapter(getAdapter());

        //digitalGateHomeAdapter.notifyDataSetChanged();

        /*Setting event for grid view items*/
        gridViewDigitalGateServices.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(DigitalGateHome.this, InvitingVisitors.class));
                break;
            case 1:
                startActivity(new Intent(DigitalGateHome.this, VisitorsList.class));
                break;
            case 2:
                startActivity(new Intent(DigitalGateHome.this, DailyServicesHome.class));
                break;
            case 3:
                Intent intent = new Intent(DigitalGateHome.this, NotifyGateAndEmergencyHome.class);
                intent.putExtra(Constants.SERVICE_TYPE, R.string.notify_digital_gate);
                startActivity(intent);
                break;
            case 4:
                startActivity(new Intent(DigitalGateHome.this, MySweetHome.class));
                break;
            case 5:
                Intent intentEmergency = new Intent(DigitalGateHome.this, NotifyGateAndEmergencyHome.class);
                intentEmergency.putExtra(Constants.SERVICE_TYPE, R.string.emergency);
                startActivity(intentEmergency);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private DigitalGateHomeAdapter getAdapter() {
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
        return new DigitalGateHomeAdapter(this, imageDigitalServices, stringDigitalGateServices);
    }

}
