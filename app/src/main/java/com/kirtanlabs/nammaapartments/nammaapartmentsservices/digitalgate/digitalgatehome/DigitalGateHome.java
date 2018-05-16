package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.digitalgatehome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.InvitingVisitors;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.VisitorsList;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.DailyServicesHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate.NotifyGateAndEmergencyHome;

public class DigitalGateHome extends BaseActivity {

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
        DigitalGateHomeAdapter digitalGateHomeAdapter = new DigitalGateHomeAdapter(this, imageDigitalServices, stringDigitalGateServices);

        // Setting the imageAdapter
        gridViewDigitalGateServices.setAdapter(digitalGateHomeAdapter);
        digitalGateHomeAdapter.notifyDataSetChanged();

        gridViewDigitalGateServices.setOnItemClickListener((parent, view, position, id) -> {
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
                case 5:
                    Intent intentEmergency = new Intent(DigitalGateHome.this, NotifyGateAndEmergencyHome.class);
                    intentEmergency.putExtra(Constants.SERVICE_TYPE, R.string.emergency);
                    startActivity(intentEmergency);
                    break;
                default:
                    Toast.makeText(DigitalGateHome.this, "Yet to Implement", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DigitalGateHome.this, NammaApartmentsHome.class));
    }

}
