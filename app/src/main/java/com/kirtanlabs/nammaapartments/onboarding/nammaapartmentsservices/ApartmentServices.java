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
        apartmentServicesList.add(new Service(R.drawable.cook_service, getString(R.string.cook)));
        apartmentServicesList.add(new Service(R.drawable.maid, getString(R.string.maid)));
        apartmentServicesList.add(new Service(R.drawable.car_cleaning, getString(R.string.car_bike_cleaning)));
        apartmentServicesList.add(new Service(R.drawable.child_care ,getString(R.string.child_day_care)));
        apartmentServicesList.add(new Service(R.drawable.newspaper, getString(R.string.daily_newspaper)));
        apartmentServicesList.add(new Service(R.drawable.milk, getString(R.string.milk_man)));
        apartmentServicesList.add(new Service(R.drawable.laundry_service, getString(R.string.laundry)));
        apartmentServicesList.add(new Service(R.drawable.driving_, getString(R.string.driver)));
        apartmentServicesList.add(new Service(R.drawable.groceries, getString(R.string.groceries)));

        //Creating the Adapter
        ServiceAdapter serviceAdapter = new ServiceAdapter(Objects.requireNonNull(getActivity()), apartmentServicesList);

        //Attaching adapter to the listview
        listView.setAdapter(serviceAdapter);
    }

}
