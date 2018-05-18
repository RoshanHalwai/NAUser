package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.Arrays;


public class DailyServicesHome extends BaseActivity {

    private FloatingActionButton fab;
    private AlertDialog dialog;
    private Animation rotate_clockwise, rotate_anticlockwise;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_daily_services;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_daily_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Custom DialogBox with list of all daily services*/
        AlertDialog.Builder dailyServicesDialog = new AlertDialog.Builder(DailyServicesHome.this);
        View listDailyServices = View.inflate(this, R.layout.list_daily_services, null);
        dailyServicesDialog.setView(listDailyServices);
        dialog = dailyServicesDialog.create();

        /*Getting Id's for all the views*/
        fab = findViewById(R.id.fab);
        ListView listView = listDailyServices.findViewById(R.id.listViewDailyServices);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        //Creating recycler view adapter
        DailyServicesHomeAdapter adapterDailyServices = new DailyServicesHomeAdapter(this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(adapterDailyServices);

        // We fill the services list with {@link R.array.daily_services}
        String[] dailyServices = getResources().getStringArray(R.array.daily_services);
        ArrayList<String> servicesList = new ArrayList<>(Arrays.asList(dailyServices));

        /*Creating the Adapter*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DailyServicesHome.this, android.R.layout.simple_list_item_1, servicesList);

        /*Attaching adapter to the listView*/
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        /*Setting event for list view items*/
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedFromList = (String) listView.getItemAtPosition(position);
            Intent intent = new Intent(DailyServicesHome.this, AddDailyServiceAndFamilyMembers.class);
            intent.putExtra(Constants.SERVICE_TYPE, selectedFromList);
            startActivity(intent);
            dialog.cancel();
        });

        /*Setting event for Floating action button*/
        fab.setOnClickListener(view -> {
            /*Rotating Fab button clockwise*/
            fab.startAnimation(rotate_clockwise);
            dialog.show();
        });

        /*Rotating Fab button to anti-clockwise on canceling my service list*/
        dialog.setOnCancelListener(dialog1 -> fab.startAnimation(rotate_anticlockwise));
    }

}
