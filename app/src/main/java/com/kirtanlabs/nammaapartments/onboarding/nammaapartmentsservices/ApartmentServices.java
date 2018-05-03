package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

import android.os.Bundle;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.List;

public class ApartmentServices extends BaseActivity {

    List<Service> apartmentServicesList;
    ListView listView;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_apartment_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.apartment_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        listView = findViewById(R.id.listviewApartmentServices);
        apartmentServicesList = new ArrayList<>();

        /*Adding some values to our list*/
        apartmentServicesList.add(new Service(R.drawable.cook_service, "Cook Service"));
        apartmentServicesList.add(new Service(R.drawable.maid_service, "Maid Service"));
        apartmentServicesList.add(new Service(R.drawable.car_cleaning_service, "Car/Bike Cleaning Service"));
        apartmentServicesList.add(new Service(R.drawable.child_day_care, "Child Day Care Service"));
        apartmentServicesList.add(new Service(R.drawable.newspaper_service, "Daily NewsPaper Service"));
        apartmentServicesList.add(new Service(R.drawable.milk_man_service, "Milk Man Service"));
        apartmentServicesList.add(new Service(R.drawable.laundry_service, "Laundry Service"));
        apartmentServicesList.add(new Service(R.drawable.driver, "Driver Service"));
        apartmentServicesList.add(new Service(R.drawable.grocery_service, "Grocery Service"));

        //Creating the Adapter
        ServiceAdapter serviceAdapter = new ServiceAdapter(this, R.layout.list_services, apartmentServicesList);

        //Attaching adapter to the listview
        listView.setAdapter(serviceAdapter);

    }
}
