package com.kirtanlabs.nammaapartments.nammaapartmentshome;

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

public class ApartmentServicesHome extends Fragment {

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_namma_apartments_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id for listView*/
        ListView listView = view.findViewById(R.id.listviewApartmentServices);

        /*Attaching adapter to the listview*/
        listView.setAdapter(getAdapter());
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private NammaApartmentServiceAdapter getAdapter() {
        List<NammaApartmentService> apartmentServicesList = new ArrayList<>();

        /*Adding apartment services to the list*/
        apartmentServicesList.add(new NammaApartmentService(R.drawable.cook_service, getString(R.string.cook)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.maid, getString(R.string.maid)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.car_cleaning, getString(R.string.car_bike_cleaning)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.child_care, getString(R.string.child_day_care)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.newspaper, getString(R.string.daily_newspaper)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.milk, getString(R.string.milk_man)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.laundry_service, getString(R.string.laundry)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.driving, getString(R.string.driver)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.groceries, getString(R.string.groceries)));

        return new NammaApartmentServiceAdapter(Objects.requireNonNull(getActivity()), apartmentServicesList);
    }
}

