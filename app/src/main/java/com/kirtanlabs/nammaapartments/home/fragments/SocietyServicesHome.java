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
import java.util.Objects;

public class SocietyServicesHome extends Fragment {

    /* ------------------------------------------------------------- *
     * Overriding Fragment Objects
     * ------------------------------------------------------------- */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_society_services_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id for the recycler view*/
        RecyclerView recyclerView = view.findViewById(R.id.listSocietyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*Initializing the societyServicesList*/
        List<NammaApartmentService> societyServicesList = new ArrayList<>();

        /*Adding society services to the list*/
        societyServicesList.add(new NammaApartmentService(R.drawable.digi_gate_na, getString(R.string.digital_gate)));
        societyServicesList.add(new NammaApartmentService(R.drawable.plumbers_na, getString(R.string.plumber)));
        societyServicesList.add(new NammaApartmentService(R.drawable.carpenter_na, getString(R.string.carpenter)));
        societyServicesList.add(new NammaApartmentService(R.drawable.electrician_na, getString(R.string.electrician)));
        societyServicesList.add(new NammaApartmentService(R.drawable.garbage_collection_na, getString(R.string.garbage_collection)));
        societyServicesList.add(new NammaApartmentService(R.drawable.emergency_na, getString(R.string.medical_emergency)));
        societyServicesList.add(new NammaApartmentService(R.drawable.event_na, getString(R.string.event_management)));

        /*Creating the Adapter*/
        NammaApartmentServiceAdapter nammaApartmentServiceAdapter = new NammaApartmentServiceAdapter(Objects.requireNonNull(getActivity()), R.string.society_services, societyServicesList);

        /*Attaching adapter to the recyclerView*/
        recyclerView.setAdapter(nammaApartmentServiceAdapter);
    }

}
