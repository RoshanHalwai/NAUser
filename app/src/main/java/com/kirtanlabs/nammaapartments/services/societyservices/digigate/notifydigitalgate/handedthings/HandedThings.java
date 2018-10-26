package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.RetrievingDailyServicesList;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.guests.RetrievingGuestList;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings.history.HandedThingsHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class HandedThings extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    public static final Map<String, Long> numberOfFlats = new HashMap<>();
    private HandedThingsToVisitorsAdapter adapterVisitors;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private HandedThingsToDailyServiceAdapter adapterDailyService;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.given_things;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nammaApartmentDailyServiceList = new ArrayList<>();
        adapterDailyService = new HandedThingsToDailyServiceAdapter(nammaApartmentDailyServiceList, this);

        /*Retrieve those visitor details who status is Entered*/
        if (getIntent().getIntExtra(HANDED_THINGS_TO, 0) == R.string.my_guests) {
            new RetrievingGuestList(HandedThings.this, false).getPreAndPostApprovedGuests(nammaApartmentGuestList -> {
                hideProgressIndicator();
                if (!nammaApartmentGuestList.isEmpty()) {
                    List<NammaApartmentGuest> enteredGuestList = new ArrayList<>();
                    for (NammaApartmentGuest nammaApartmentGuest : nammaApartmentGuestList) {
                        if (nammaApartmentGuest.getStatus().equals(ENTERED)) {
                            enteredGuestList.add(nammaApartmentGuest);
                        }
                    }
                    if (!enteredGuestList.isEmpty()) {
                        adapterVisitors = new HandedThingsToVisitorsAdapter(enteredGuestList, HandedThings.this);
                        recyclerView.setAdapter(adapterVisitors);
                    } else {
                        showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                    }
                } else {
                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                }
            });
        } else {
            //To retrieve user daily Services list from firebase only when their status is "Entered"
            recyclerView.setAdapter(adapterDailyService);
            checkAndRetrieveCurrentDailyServices();
        }

        /*Since we have History button here, we would want to users to navigate to Handed Things history
         * and display data based on the screen title*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting events for the views */
        historyButton.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.historyButton:
                Intent handedThingsHistoryIntent = new Intent(HandedThings.this, HandedThingsHistory.class);
                handedThingsHistoryIntent.putExtra(SCREEN_TITLE, getIntent().getIntExtra(HANDED_THINGS_TO, 0));
                startActivity(handedThingsHistoryIntent);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the flat has any daily service. If it does not have any daily services added we show daily service unavailable message
     * Else, we display the daily services whose status is "Entered" of the current user and their family members
     */
    private void checkAndRetrieveCurrentDailyServices() {
        new RetrievingDailyServicesList(getApplicationContext(), false).getAllDailyServices(dailyServicesList -> {
            hideProgressIndicator();
            if (dailyServicesList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
            } else {
                for (NammaApartmentDailyService dailyService : dailyServicesList) {
                    if (dailyService.getStatus().equals(ENTERED)) {
                        nammaApartmentDailyServiceList.add(index++, dailyService);
                    }
                }
                if (nammaApartmentDailyServiceList.isEmpty()) {
                    showFeatureUnavailableLayout(R.string.daily_service_not_entered);
                } else {
                    adapterDailyService.notifyDataSetChanged();
                }
            }
        });
    }
}
