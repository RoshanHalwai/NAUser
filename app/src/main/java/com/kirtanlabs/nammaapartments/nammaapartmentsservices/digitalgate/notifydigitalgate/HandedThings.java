package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;

public class HandedThings extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NammaApartmentGuest> nammaApartmentGuestList;
    private HandedThingsToVisitorsAdapter adapterVisitors;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private HandedThingsToDailyServiceAdapter adapterDailyService;
    private int handed_things_to;
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
        /*We use a common class for Handed Things to my Guest and handed Things to my Daily Services, we set the title
         * based on the user click on NotifyGate Home screen*/
        if (getIntent().getIntExtra(HANDED_THINGS_TO, 0) == R.string.handed_things_to_my_guest) {
            handed_things_to = R.string.my_guests;
        } else {
            handed_things_to = R.string.my_daily_services;
        }
        return handed_things_to;
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

        //Creating recycler view adapter
        nammaApartmentGuestList = new ArrayList<>();
        adapterVisitors = new HandedThingsToVisitorsAdapter(nammaApartmentGuestList, this);

        nammaApartmentDailyServiceList = new ArrayList<>();
        adapterDailyService = new HandedThingsToDailyServiceAdapter(nammaApartmentDailyServiceList, this);

        /*Retrieve those visitor details who status is Entered*/
        if (handed_things_to == R.string.my_guests) {
            //Retrieve user visitor list from firebase only when their status is "Entered"
            recyclerView.setAdapter(adapterVisitors);
            checkAndRetrieveCurrentVisitorsFromFirebase();
        } else {
            //To retrieve user daily Services list from firebase only when their status is "Entered"
            recyclerView.setAdapter(adapterDailyService);
            checkAndRetrieveCurrentDailyServices();
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the flat has any guests. If it does not have any visitors we show guests unavailable message
     * Else, we display the guests of the current user and their family members
     */
    private void checkAndRetrieveCurrentVisitorsFromFirebase() {
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myVisitorsReference = userDataReference.child(Constants.FIREBASE_CHILD_VISITORS);

        /*We first check if this flat has any visitors*/
        myVisitorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                } else {
                    //Retrieve user guests list from firebase
                    retrieveCurrentVisitorsFromFirebase(NammaApartmentsGlobal.userUID);

                    //Retrieve user family member guests list from firebase
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    Map<String, Boolean> familyMembers = currentNammaApartmentUser.getFamilyMembers();
                    if (familyMembers != null && !familyMembers.isEmpty()) {
                        for (String userUID : familyMembers.keySet()) {
                            retrieveCurrentVisitorsFromFirebase(userUID);
                        }
                    }
                    //TODO: Ensure user friends visitors are not added in Guests List
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * We retrieve visitors for current user and their family members if any
     */
    private void retrieveCurrentVisitorsFromFirebase(String userUID) {
        //First retrieve the current user visitors
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myVisitorsReference = userDataReference.child(Constants.FIREBASE_CHILD_VISITORS);
        myVisitorsReference.child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                for (DataSnapshot visitorsSnapshot : dataSnapshot.getChildren()) {
                    DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                            .child(visitorsSnapshot.getKey());
                    preApprovedVisitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot nammaApartmentVisitorData) {
                            NammaApartmentGuest nammaApartmentGuest = nammaApartmentVisitorData.getValue(NammaApartmentGuest.class);
                            if (nammaApartmentGuest.getStatus().equals(ENTERED)) {
                                nammaApartmentGuestList.add(index++, nammaApartmentGuest);
                                adapterVisitors.notifyDataSetChanged();
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
                                        if (dataSnapshot.child("status").getValue().toString().equals(ENTERED)) {
                                            long numberOfFlats = dataSnapshot.getChildrenCount() - 1;
                                            if (dataSnapshot.hasChild(userUID)) {
                                                DataSnapshot dailyServiceDataSnapshot = dataSnapshot.child(userUID);
                                                NammaApartmentDailyService nammaApartmentDailyService = dailyServiceDataSnapshot.getValue(NammaApartmentDailyService.class);
                                                nammaApartmentDailyService.setNumberOfFlats(numberOfFlats);
                                                Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(dailyServiceType.substring(0, 1).toUpperCase() + dailyServiceType.substring(1));
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
