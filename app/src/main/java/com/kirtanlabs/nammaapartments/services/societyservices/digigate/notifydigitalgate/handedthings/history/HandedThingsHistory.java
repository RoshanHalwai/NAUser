package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings.history;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.DailyServiceType;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.guests.RetrievingGuestList;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_HANDED_THINGS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;

public class HandedThingsHistory extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private GuestsHistoryAdapter guestsHistoryAdapter;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private DailyServicesHistoryAdapter dailyServicesHistoryAdapter;
    private int index = 0;
    private boolean isUserHandedThingsToDailyService = false;

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
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
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
            nammaApartmentDailyServiceList = new ArrayList<>();
            dailyServicesHistoryAdapter = new DailyServicesHistoryAdapter(nammaApartmentDailyServiceList, this);
            recyclerView.setAdapter(dailyServicesHistoryAdapter);
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
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myVisitorsReference = userDataReference.child(Constants.FIREBASE_CHILD_DAILYSERVICES);

        /*We first check if this flat has any visitors*/
        myVisitorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
                } else {
                    DatabaseReference privateFlatReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
                    privateFlatReference.child(FIREBASE_CHILD_FLAT_MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot flatSnapshot : dataSnapshot.getChildren()) {
                                retrieveDailyServicesDetailsFromFirebase(flatSnapshot.getKey());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Retrieves all daily services of given userUID
     *
     * @param userUID - whose daily services needs to be retrieved
     */
    private void retrieveDailyServicesDetailsFromFirebase(String userUID) {
        DatabaseReference dailyServicesListReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_DAILYSERVICES);

        /*Start with checking if a flat has any daily services*/
        dailyServicesListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot myDailyServiceSnapshot) {
                hideProgressIndicator();
                /*Flat has no daily services added*/
                if (!myDailyServiceSnapshot.exists()) {
                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message);
                }
                /*Flat has some daily services added*/
                else {
                    /*Iterate over each daily service type*/
                    for (DataSnapshot dailyServicesSnapshot : myDailyServiceSnapshot.getChildren()) {
                        String dailyServiceType = dailyServicesSnapshot.getKey();
                        DatabaseReference dailyServiceTypeReference = dailyServicesListReference.child(dailyServiceType);

                        /*For each daily service type, check how many are added. Say a user can have two
                         * cooks or maids*/
                        dailyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dailyServiceUIDSnapshot) {

                                /*Iterate over each of them and add listener to each of them*/
                                for (DataSnapshot childSnapshot : dailyServiceUIDSnapshot.getChildren()) {
                                    DatabaseReference reference = PUBLIC_DAILYSERVICES_REFERENCE
                                            .child(dailyServiceUIDSnapshot.getKey())    // Daily Service Type
                                            .child(childSnapshot.getKey());             // Daily Service UID
                                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dailyServiceCountSnapshot) {
                                            if (dailyServiceCountSnapshot.hasChild(userUID)) {
                                                DataSnapshot dailyServiceDataSnapshot = dailyServiceCountSnapshot.child(userUID);
                                                //We display only those daily service details who has handed things has one of its child
                                                if (dailyServiceDataSnapshot.hasChild(FIREBASE_CHILD_HANDED_THINGS)) {
                                                    isUserHandedThingsToDailyService = true;
                                                    DatabaseReference handedThingsReference = reference.child(userUID).child(FIREBASE_CHILD_HANDED_THINGS);
                                                    handedThingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            for (DataSnapshot dates : dataSnapshot.getChildren()) {
                                                                NammaApartmentDailyService nammaApartmentDailyService = dailyServiceDataSnapshot.getValue(NammaApartmentDailyService.class);
                                                                Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(DailyServiceType.get(dailyServiceType));
                                                                nammaApartmentDailyService.setDailyServiceHandedThingsDescription(Objects.requireNonNull(dates.getValue()).toString());
                                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                                                try {
                                                                    Date date = dateFormat.parse(dates.getKey());
                                                                    Calendar myCal = new GregorianCalendar();
                                                                    myCal.setTime(date);
                                                                    int dayOfMonth = myCal.get(Calendar.DAY_OF_MONTH);
                                                                    int month = myCal.get(Calendar.MONTH);
                                                                    int year = myCal.get(Calendar.YEAR);
                                                                    String formattedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
                                                                    nammaApartmentDailyService.setDateOfVisit(formattedDate);
                                                                    nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                                                    dailyServicesHistoryAdapter.notifyDataSetChanged();
                                                                } catch (ParseException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                                if (isUserHandedThingsToDailyService) {
                                                    hideFeatureUnavailableLayout();
                                                } else {
                                                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}