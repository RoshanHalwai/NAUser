package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentService;

import java.util.ArrayList;
import java.util.List;

public class VisitorsList extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        //We would use the same layout we created for Notify Gate
        return R.layout.layout_notify_gate_and_emergency;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_guests_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listViewNotifyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //We need an Image and Text, hence we use the class used for Services List
        List<NammaApartmentService> notificationServicesList = new ArrayList<>();

        notificationServicesList.add(new NammaApartmentService(R.drawable.team, getString(R.string.guests)));
        notificationServicesList.add(new NammaApartmentService(R.drawable.taxi, getString(R.string.cab_arrivals)));
        notificationServicesList.add(new NammaApartmentService(R.drawable.delivery_man, getString(R.string.package_arrivals)));

        /*Creating the Adapter*/
        VisitorsListAdapter adapter = new VisitorsListAdapter(this, notificationServicesList);

        /*Attaching adapter to the listview */
        recyclerView.setAdapter(adapter);
    }

}
