package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SERVICE_TYPE;

public class NotifyGateAndEmergencyHome extends BaseActivity {

    private int serviceType;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_notify_gate_and_emergency;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Notify Digital Gate and Emergency, we set the title
         * based on the user click on NotifyGateAndEmergencyHome Home screen*/
        if (getIntent().getIntExtra(SERVICE_TYPE, 0) == R.string.notify_digital_gate) {
            serviceType = R.string.notify_digital_gate;
        } else {
            serviceType = R.string.emergency;
        }
        return serviceType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerView = findViewById(R.id.listViewNotifyServices);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*initializing the notificationServicesList*/
        List<NammaApartmentService> notificationServicesList = new ArrayList<>();

        /*Since we are using same layout for Notify Digital Cab and emergency we need to
         * change the images for emergency and text*/
        if (getIntent().getIntExtra(SERVICE_TYPE, 0) == R.string.notify_digital_gate) {
            /*Adding some values to our list*/
            notificationServicesList.add(new NammaApartmentService(R.drawable.taxi, getString(R.string.expecting_cab_arrival)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.delivery_man, getString(R.string.expecting_package_arrival)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.team, getString(R.string.expecting_guest)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.gift, getString(R.string.handed_things_to_my_guest)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.delivery, getString(R.string.handed_things_to_my_daily_services)));

            /*Creating the Adapter*/
            NotifyGateAndEmergencyAdapter adapter = new NotifyGateAndEmergencyAdapter(this, notificationServicesList, serviceType);

            /*Attaching adapter to the listview */
            recyclerView.setAdapter(adapter);

        } else {
            notificationServicesList.add(new NammaApartmentService(R.drawable.medical_emergency_na, getString(R.string.medical_emergency)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.fire_emergency_na, getString(R.string.raise_fire_alarm)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.theft_emergency_na, getString(R.string.raise_theft_alarm)));
            notificationServicesList.add(new NammaApartmentService(R.drawable.water_emergency_na, getString(R.string.raise_water_alarm)));

            /*Creating the Adapter*/
            NotifyGateAndEmergencyAdapter adapter = new NotifyGateAndEmergencyAdapter(this, notificationServicesList, serviceType);

            /*Attaching adapter to the listview */
            recyclerView.setAdapter(adapter);
        }

    }
}