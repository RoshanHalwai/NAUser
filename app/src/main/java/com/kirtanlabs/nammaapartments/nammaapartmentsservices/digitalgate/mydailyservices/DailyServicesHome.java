package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.ArrayList;
import java.util.Arrays;

public class DailyServicesHome extends BaseActivity implements View.OnClickListener, DialogInterface.OnCancelListener, AdapterView.OnItemClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private FloatingActionButton fab;
    private AlertDialog dialog;
    private Animation rotate_clockwise, rotate_anticlockwise;
    private ListView listView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

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

        /*Getting Id's for all the views*/
        fab = findViewById(R.id.fab);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DailyServicesHomeAdapter adapterDailyServices = new DailyServicesHomeAdapter(this);
        recyclerView.setAdapter(adapterDailyServices);

        rotate_clockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
        rotate_anticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);

        setupCustomDialog();

        /*Setting event views */
        fab.setOnClickListener(this);
        dialog.setOnCancelListener(this);
        listView.setOnItemClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding Event Listener Objects
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        /*Rotating Fab button clockwise*/
        fab.startAnimation(rotate_clockwise);
        dialog.show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        /*Rotating Fab button Anti-clockwise*/
        fab.startAnimation(rotate_anticlockwise);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedFromList = (String) listView.getItemAtPosition(position);
        Intent intent = new Intent(DailyServicesHome.this, AddDailyServiceAndFamilyMembers.class);
        intent.putExtra(Constants.SCREEN_TITLE, R.string.my_daily_services);
        intent.putExtra(Constants.SERVICE_TYPE, selectedFromList);
        startActivity(intent);
        dialog.cancel();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a custom dialog with a list view which contains the list of daily services. This
     * dialog is displayed when user clicks on FAB button in bottom right corner of the screen.
     */
    private void setupCustomDialog() {
        /*Custom DialogBox with list of all daily services*/
        AlertDialog.Builder dailyServicesDialog = new AlertDialog.Builder(DailyServicesHome.this);
        View listDailyServices = View.inflate(this, R.layout.list_daily_services, null);
        dailyServicesDialog.setView(listDailyServices);
        dialog = dailyServicesDialog.create();

        listView = listDailyServices.findViewById(R.id.listViewDailyServices);

        String[] dailyServices = getResources().getStringArray(R.array.daily_services);
        ArrayList<String> servicesList = new ArrayList<>(Arrays.asList(dailyServices));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(DailyServicesHome.this, android.R.layout.simple_list_item_1, servicesList);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
