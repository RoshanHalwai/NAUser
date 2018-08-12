package com.kirtanlabs.nammaapartments.home.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.adapters.NammaApartmentServiceAdapter;
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.emergency.RaiseAlarm;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.home.DigitalGateHome;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.RetrievingSocietyServiceHistoryList;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.AwaitingResponse;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.EventManagement;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class SocietyServicesHome extends Fragment implements AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String notificationUID;

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
                /*To Check if User's previous request for that particular society service is completed or not.*/
                checkPreviousRequestStatus(R.string.plumber);
                break;
            case 2:
                /*To Check if User's previous request for that particular society service is completed or not.*/
                checkPreviousRequestStatus(R.string.carpenter);
                break;
            case 3:
                /*To Check if User's previous request for that particular society service is completed or not.*/
                checkPreviousRequestStatus(R.string.electrician);
                break;
            case 4:
                Intent garbageManagementIntent = new Intent(getActivity(), com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServicesHome.class);
                garbageManagementIntent.putExtra(Constants.SCREEN_TITLE, R.string.garbage_management);
                startActivity(garbageManagementIntent);
                break;
            case 5:
                Intent medicalIntent = new Intent(getActivity(), RaiseAlarm.class);
                medicalIntent.putExtra(Constants.ALARM_TYPE, R.string.medical_emergency);
                startActivity(medicalIntent);
                break;
            case 6:
                Intent eventManagementIntent = new Intent(getActivity(), EventManagement.class);
                eventManagementIntent.putExtra(SCREEN_TITLE, R.string.event_management);
                startActivity(eventManagementIntent);
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

        return new NammaApartmentServiceAdapter(Objects.requireNonNull(getActivity()), societyServicesList);
    }

    /**
     * This method is used to check if their is any previous society service request is active or not.
     */
    private void checkPreviousRequestStatus(int screenTitle) {
        /*Retrieving user's previous society service request UID from shared preference*/
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        switch (screenTitle) {
            case R.string.plumber:
                notificationUID = sharedPreferences.getString(Constants.PLUMBER_SERVICE_NOTIFICATION_UID, null);
                break;
            case R.string.carpenter:
                notificationUID = sharedPreferences.getString(Constants.CARPENTER_SERVICE_NOTIFICATION_UID, null);
                break;
            case R.string.electrician:
                notificationUID = sharedPreferences.getString(Constants.ELECTRICIAN_SERVICE_NOTIFICATION_UID, null);
                break;
        }

        if (notificationUID != null) {
            /*Checking status of previous Request*/
            new RetrievingSocietyServiceHistoryList(getActivity())
                    .getSocietyServiceRequestStatus(notificationUID, status -> {
                        if (status.equals(IN_PROGRESS)) {
                            Intent awaitingResponseIntent = new Intent(getActivity(), AwaitingResponse.class);
                            awaitingResponseIntent.putExtra(Constants.NOTIFICATION_UID, notificationUID);
                            awaitingResponseIntent.putExtra(Constants.SOCIETY_SERVICE_TYPE, getString(screenTitle).toLowerCase());
                            startActivity(awaitingResponseIntent);
                        } else {
                            /*Updating previous user's society service request Uid to null*/
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            switch (screenTitle) {
                                case R.string.plumber:
                                    editor.putString(Constants.PLUMBER_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.carpenter:
                                    editor.putString(Constants.CARPENTER_SERVICE_NOTIFICATION_UID, null);
                                    break;
                                case R.string.electrician:
                                    editor.putString(Constants.ELECTRICIAN_SERVICE_NOTIFICATION_UID, null);
                                    break;
                            }
                            editor.apply();
                            /*Navigating user to Society Service Home Screen where user can request for society services*/
                            makeSocietyServiceRequest(screenTitle);
                        }
                    });
        } else {
            /*Navigating user to Society Service Home Screen where user can request for society services*/
            makeSocietyServiceRequest(screenTitle);
        }
    }

    /**
     * This method is invoked to open Society Service Home screen, where user can request for society services.
     *
     * @param screenTitle - type of society service user needs.
     */
    private void makeSocietyServiceRequest(int screenTitle) {
        Intent intent = new Intent(getActivity(), com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities.SocietyServicesHome.class);
        intent.putExtra(Constants.SCREEN_TITLE, screenTitle);
        startActivity(intent);
    }
}
