package com.kirtanlabs.nammaapartments.nammaapartmentshome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.digitalgatehome.DigitalGateHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.SocietyServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SocietyServicesHome extends Fragment implements AdapterView.OnItemClickListener {

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

        /*Getting Id for List View*/
        ListView listView = view.findViewById(R.id.listviewSocietyServices);

        /*Attaching adapter to the listview*/
        listView.setAdapter(getAdapter());

        /*Setting event for list view items*/
        listView.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnItemClickListener
     * ------------------------------------------------------------- */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), DigitalGateHome.class));
                break;
            case 1:
                Intent intentPlumber = new Intent(getActivity(), SocietyServices.class);
                intentPlumber.putExtra(Constants.SCREEN_TITLE, R.string.plumber);
                startActivity(intentPlumber);
                break;
            case 2:
                Intent intentCarpenter = new Intent(getActivity(), SocietyServices.class);
                intentCarpenter.putExtra(Constants.SCREEN_TITLE, R.string.carpenter);
                startActivity(intentCarpenter);
                break;
            case 3:
                Intent intentElectrician = new Intent(getActivity(), SocietyServices.class);
                intentElectrician.putExtra(Constants.SCREEN_TITLE, R.string.electrician);
                startActivity(intentElectrician);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private NammaApartmentServiceAdapter getAdapter() {
        List<NammaApartmentService> societyServicesList = new ArrayList<>();

        /*Adding Society Services to the list*/
        societyServicesList.add(new NammaApartmentService(R.drawable.digital_gate_services, getString(R.string.digital_gate)));
        societyServicesList.add(new NammaApartmentService(R.drawable.plumbing, getString(R.string.plumber)));
        societyServicesList.add(new NammaApartmentService(R.drawable.carpenter_service, getString(R.string.carpenter)));
        societyServicesList.add(new NammaApartmentService(R.drawable.electrician, getString(R.string.electrician)));
        societyServicesList.add(new NammaApartmentService(R.drawable.garbage_bin, getString(R.string.garbage_management)));
        societyServicesList.add(new NammaApartmentService(R.drawable.medical_emergency, getString(R.string.medical_emergency)));
        societyServicesList.add(new NammaApartmentService(R.drawable.event, getString(R.string.event_management)));
        societyServicesList.add(new NammaApartmentService(R.drawable.water_service, getString(R.string.water_services)));

        return new NammaApartmentServiceAdapter(Objects.requireNonNull(getActivity()), societyServicesList);
    }

}
