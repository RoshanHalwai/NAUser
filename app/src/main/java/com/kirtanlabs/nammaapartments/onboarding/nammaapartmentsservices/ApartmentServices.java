package com.kirtanlabs.nammaapartments.onboarding.nammaapartmentsservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ApartmentServices extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_apartment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listView = view.findViewById(R.id.listviewApartmentServices);
        List<Service> apartmentServicesList = new ArrayList<>();

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
        ServiceAdapter serviceAdapter = new ServiceAdapter(Objects.requireNonNull(getActivity()), apartmentServicesList);

        //Attaching adapter to the listview
        listView.setAdapter(serviceAdapter);
    }

}
