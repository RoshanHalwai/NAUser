package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;

public class DailyServicesHome extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private AlertDialog dailyServicesListDialog;
    private DailyServicesHomeAdapter dailyServicesHomeAdapter;
    private RecyclerView recyclerView;

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

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id's for all the views*/
        Button buttonAddDailyServices = findViewById(R.id.buttonAddDailyServices);
        recyclerView = findViewById(R.id.recyclerView);

        /*Setting font for button*/
        buttonAddDailyServices.setTypeface(setLatoLightFont(this));

        /*Setting attributes for recycler view*/
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Creating Daily Services List Dialog*/
        createDailyServicesListDialog();

        /*Setting event for views */
        buttonAddDailyServices.setOnClickListener(v -> dailyServicesListDialog.show());

        /*To retrieve DailyServicesList from firebase.*/
        checkAndRetrieveDailyServices();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Creates a DailyServicesListDialog with a list view which contains the list of daily services.
     */
    private void createDailyServicesListDialog() {
        /*Custom DialogBox with list of all daily services*/
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] dailyServices = getResources().getStringArray(R.array.apartment_services_list);
        Intent intent = new Intent(DailyServicesHome.this, AddDailyService.class);
        intent.putExtra(SCREEN_TITLE, R.string.my_daily_services);
        builder.setItems(dailyServices, (dialog, which) -> {
            dailyServicesListDialog.cancel();
            intent.putExtra(SERVICE_TYPE, dailyServices[which]);
            startActivity(intent);
        });
        dailyServicesListDialog = builder.create();
    }

    /**
     * Check if the flat has any daily service. If it does not have any daily services added we show daily service unavailable message
     * Else, we display the cardView of all daily services of the current user.
     */
    private void checkAndRetrieveDailyServices() {
        new RetrievingDailyServicesList(getApplicationContext(), false).getAllDailyServices(dailyServicesList -> {
            hideProgressIndicator();
            if (dailyServicesList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.daily_service_unavailable_message);
            } else {
                dailyServicesHomeAdapter = new DailyServicesHomeAdapter(dailyServicesList, this);
                recyclerView.setAdapter(dailyServicesHomeAdapter);
            }
        });
    }

}
