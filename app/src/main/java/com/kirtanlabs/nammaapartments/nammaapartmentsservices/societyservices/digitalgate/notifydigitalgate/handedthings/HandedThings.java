package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.handedthings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.mydailyservices.DailyServiceType;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.guests.RetrievingGuestList;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.handedthings.handedthingshistory.HandedThingsHistory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

public class HandedThings extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

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
            new RetrievingGuestList(HandedThings.this).getGuests(nammaApartmentGuestList -> {
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
            //checkAndRetrieveCurrentVisitorsFromFirebase();
        } else {
            //To retrieve user daily Services list from firebase only when their status is "Entered"
            recyclerView.setAdapter(adapterDailyService);
            checkAndRetrieveCurrentDailyServices();
        }

        /*Since we have History button here, we would want to users to navigate to Handed Things history
         * and display data based on the screen title*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(v -> {
            Intent handedThingsHistoryIntent = new Intent(HandedThings.this, HandedThingsHistory.class);
            handedThingsHistoryIntent.putExtra(SCREEN_TITLE, getIntent().getIntExtra(HANDED_THINGS_TO, 0));
            startActivity(handedThingsHistoryIntent);
        });

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the flat has any daily service. If it does not have any daily services added we show daily service unavailable message
     * Else, we display the daily services whose status is "Entered" of the current user and their family members
     */
    private void checkAndRetrieveCurrentDailyServices() {
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
                                retrieveDailyServiceFromFirebase(flatSnapshot.getKey());
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
     * Retrieve only those daily service details whose status is entered
     *
     * @param userUID - whose daily services needs to be retrieved
     */
    private void retrieveDailyServiceFromFirebase(String userUID) {
        DatabaseReference dailyServicesListReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_DAILYSERVICES);
        dailyServicesListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot myDailyServiceSnapshot) {
                for (DataSnapshot dailyServicesSnapshot : myDailyServiceSnapshot.getChildren()) {
                    String dailyServiceType = dailyServicesSnapshot.getKey();
                    DatabaseReference dailyServiceTypeReference = dailyServicesListReference.child(dailyServiceType);
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
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (Objects.requireNonNull(dataSnapshot.child("status").getValue()).toString().equals(ENTERED)) {
                                            long numberOfFlats = dataSnapshot.getChildrenCount() - 1;
                                            if (dataSnapshot.hasChild(userUID)) {
                                                DataSnapshot dailyServiceDataSnapshot = dataSnapshot.child(userUID);
                                                NammaApartmentDailyService nammaApartmentDailyService = dailyServiceDataSnapshot.getValue(NammaApartmentDailyService.class);
                                                Objects.requireNonNull(nammaApartmentDailyService).setNumberOfFlats(numberOfFlats);
                                                Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(DailyServiceType.get(dailyServiceType));
                                                nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                                adapterDailyService.notifyDataSetChanged();
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
                hideProgressIndicator();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
