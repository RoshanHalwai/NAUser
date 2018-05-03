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
        societyServicesList.add(new Service(R.drawable.digital_gate, "Digital Gate Service"));
        societyServicesList.add(new Service(R.drawable.plumber_service, "Plumber Service"));
        societyServicesList.add(new Service(R.drawable.carpenter, "Carpenter Service"));
        societyServicesList.add(new Service(R.drawable.electrician, "Electrician Service"));
        societyServicesList.add(new Service(R.drawable.garbage_truck, "Garbage Management"));
        societyServicesList.add(new Service(R.drawable.medical_services, "Medical Emergency"));
        societyServicesList.add(new Service(R.drawable.event_management, "Event Management"));
        societyServicesList.add(new Service(R.drawable.water_tanker, "Water Tanker"));

        //Creating the Adapter
        ServiceAdapter serviceAdapter = new ServiceAdapter(Objects.requireNonNull(getActivity()), societyServicesList);

        //attaching adapter to the listview
        listView.setAdapter(serviceAdapter);
    }

}
