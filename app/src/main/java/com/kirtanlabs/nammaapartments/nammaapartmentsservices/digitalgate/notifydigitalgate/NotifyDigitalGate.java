package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.Service;

import java.util.ArrayList;
import java.util.List;

public class NotifyDigitalGate extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_notify_digital_gate;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.notify_digital_gate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listViewNotifyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the notificationServicesList
        List<Service> notificationServicesList = new ArrayList<>();

        /*Adding some values to our list*/
        notificationServicesList.add(new Service(R.drawable.cab_arrival, getString(R.string.expecting_cab_arrival)));
        notificationServicesList.add(new Service(R.drawable.delivery_man, getString(R.string.expecting_package_arrival)));
        notificationServicesList.add(new Service(R.drawable.team, getString(R.string.expecting_visitor)));
        notificationServicesList.add(new Service(R.drawable.gift, getString(R.string.handed_things_to_my_guest)));
        notificationServicesList.add(new Service(R.drawable.handed_things, getString(R.string.handed_things_to_my_daily_services)));

        /*Creating the Adapter*/
        NotifyDigitalGateAdapter adapter = new NotifyDigitalGateAdapter(this, notificationServicesList);

        /*Attaching adapter to the listview*/
        recyclerView.setAdapter(adapter);

    }
}
