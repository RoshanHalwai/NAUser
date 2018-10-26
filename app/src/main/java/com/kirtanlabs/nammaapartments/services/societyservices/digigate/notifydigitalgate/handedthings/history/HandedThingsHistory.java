package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings.history;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.RetrievingDailyServicesList;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.guests.RetrievingGuestList;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class HandedThingsHistory extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private GuestsHistoryAdapter guestsHistoryAdapter;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private DailyServicesHistoryAdapter dailyServicesHistoryAdapter;
    private RecyclerView recyclerView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id of recycler view*/
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Based on the previous screen title we decide whose history of handed things can be displayed
         * User can give things to either Daily Services or their Guests*/
        if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_guests) {
            new RetrievingGuestList(HandedThingsHistory.this, true).getPreAndPostApprovedGuests(nammaApartmentGuestList -> {
                hideProgressIndicator();
                if (!nammaApartmentGuestList.isEmpty()) {
                    List<NammaApartmentGuest> handedThingsGuestList = new ArrayList<>();
                    for (NammaApartmentGuest nammaApartmentGuest : nammaApartmentGuestList) {
                        if (!(nammaApartmentGuest.getHandedThings() == null)) {
                            handedThingsGuestList.add(nammaApartmentGuest);
                        }
                    }
                    if (!handedThingsGuestList.isEmpty()) {
                        guestsHistoryAdapter = new GuestsHistoryAdapter(handedThingsGuestList, HandedThingsHistory.this);
                        recyclerView.setAdapter(guestsHistoryAdapter);
                    } else {
                        showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                    }
                } else {
                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                }
            });
        }

        /*History of Handed things to Daily Services*/
        else {
            checkAndRetrieveDailyServices();
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the flat has any daily service. If it does not have any daily services added we show daily service unavailable message
     * Else, we display the daily services whose status is "Entered" of the current user and their family members
     */
    private void checkAndRetrieveDailyServices() {
        new RetrievingDailyServicesList(getApplicationContext(), true).getAllDailyServices(dailyServicesList -> {
            Map<String, NammaApartmentDailyService> handedThingsHistory = new TreeMap<>();
            hideProgressIndicator();
            if (dailyServicesList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
            } else {
                for (NammaApartmentDailyService dailyService : dailyServicesList) {
                    Map<String, String> handedThingsMap = dailyService.getHandedThingsMap();
                    for (String date : handedThingsMap.keySet()) {
                        NammaApartmentDailyService ds = new NammaApartmentDailyService(dailyService);
                        ds.setDailyServiceHandedThingsDescription(handedThingsMap.get(date));
                        ds.setDateOfVisit(formatDate(date));
                        handedThingsHistory.put(date, ds);
                    }
                }
            }
            if (handedThingsHistory.isEmpty()) {
                showFeatureUnavailableLayout(R.string.no_handed_things_history);
            } else {
                nammaApartmentDailyServiceList = new ArrayList<>(handedThingsHistory.values());
                dailyServicesHistoryAdapter = new DailyServicesHistoryAdapter(nammaApartmentDailyServiceList, this);
                recyclerView.setAdapter(dailyServicesHistoryAdapter);
            }
        });
    }

    private String formatDate(final String unformattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        try {
            Date date = dateFormat.parse(unformattedDate);
            Calendar myCal = new GregorianCalendar();
            myCal.setTime(date);
            int dayOfMonth = myCal.get(Calendar.DAY_OF_MONTH);
            int month = myCal.get(Calendar.MONTH);
            int year = myCal.get(Calendar.YEAR);
            return new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}