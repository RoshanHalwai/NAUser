package com.kirtanlabs.nammaapartments.nammaapartmentshome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.digitalgatehome.DigitalGateHome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocietyServices extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_society_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        ListView listView = view.findViewById(R.id.listviewSocietyServices);
        List<Service> societyServicesList = new ArrayList<>();

        /*Adding some values to our list*/
        societyServicesList.add(new Service(R.drawable.digital_gate_services, getString(R.string.digital_gate)));
        societyServicesList.add(new Service(R.drawable.plumbing, getString(R.string.plumber)));
        societyServicesList.add(new Service(R.drawable.carpenter_service, getString(R.string.carpenter)));
        societyServicesList.add(new Service(R.drawable.electrician, getString(R.string.electrician)));
        societyServicesList.add(new Service(R.drawable.garbage_bin, getString(R.string.garbage_management)));
        societyServicesList.add(new Service(R.drawable.medical_emergency, getString(R.string.medical_emergency)));
        societyServicesList.add(new Service(R.drawable.event, getString(R.string.event_management)));
        societyServicesList.add(new Service(R.drawable.water_services, getString(R.string.water_services)));

        /*Creating the Adapter*/
        ServiceAdapter serviceAdapter = new ServiceAdapter(Objects.requireNonNull(getActivity()), societyServicesList);

        /*Attaching adapter to the listview*/
        listView.setAdapter(serviceAdapter);

        /*Setting event for list view items*/
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            switch (position) {
                case 0: {
                    Intent intent = new Intent(getActivity(), DigitalGateHome.class);
                    startActivity(intent);
                }
            }
        });
    }

}
