package com.kirtanlabs.nammaapartments.home.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.adapters.NammaApartmentServiceAdapter;
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;

import java.util.ArrayList;
import java.util.List;

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

        /*Getting Id for the recycler view*/
        RecyclerView recyclerView = view.findViewById(R.id.listApartmentServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Initializing the apartmentServicesList*/
        List<NammaApartmentService> apartmentServicesList = new ArrayList<>();

        /*Adding apartment services to the list*/
        apartmentServicesList.add(new NammaApartmentService(R.drawable.cook_na, getString(R.string.cook)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.maid_na, getString(R.string.maid)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.car_bike_clean_na, getString(R.string.car_bike_cleaning)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.child_day_care_na, getString(R.string.child_day_care)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.newspaper_na, getString(R.string.daily_newspaper)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.milkman_na, getString(R.string.milk_man)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.laundry_na, getString(R.string.laundry)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.driver_na, getString(R.string.driver)));
        apartmentServicesList.add(new NammaApartmentService(R.drawable.grocery_na, getString(R.string.groceries)));

        /*Creating the Adapter*/
        NammaApartmentServiceAdapter nammaApartmentServiceAdapter = new NammaApartmentServiceAdapter(getActivity(), R.string.apartment_services, apartmentServicesList);

        /*Attaching adapter to the recyclerView*/
        recyclerView.setAdapter(nammaApartmentServiceAdapter);

    }

}

