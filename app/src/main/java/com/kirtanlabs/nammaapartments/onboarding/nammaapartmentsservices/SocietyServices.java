package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.List;

public class SocietyServices extends BaseActivity {

    List<Service> societyServicesList;
    ListView listView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.society_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        listView = findViewById(R.id.listviewSocietyServices);
        societyServicesList = new ArrayList<>();

        /*Adding some values to our list*/
        societyServicesList.add(new Service(R.drawable.digital_gate, "Digital Gate Services"));
        societyServicesList.add(new Service(R.drawable.plumber_service, "Plumber Service"));
        societyServicesList.add(new Service(R.drawable.carpenter, "Carpenter Service"));
        societyServicesList.add(new Service(R.drawable.electrician, "Electrician Service"));
        societyServicesList.add(new Service(R.drawable.garbage_truck, "Garbage Management"));
        societyServicesList.add(new Service(R.drawable.medical_services, "Medical Emergency"));
        societyServicesList.add(new Service(R.drawable.event_management, "Event Management"));
        societyServicesList.add(new Service(R.drawable.water_tanker, "Water Tanker"));

        //Creating the Adapter
        ServiceAdapter serviceAdapter = new ServiceAdapter(this, R.layout.list_services, societyServicesList);

        //attaching adapter to the listview
        listView.setAdapter(serviceAdapter);
    }

}
