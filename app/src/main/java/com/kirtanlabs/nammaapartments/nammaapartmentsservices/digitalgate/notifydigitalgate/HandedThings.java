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
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.NammaApartmentDailyService;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;

public class HandedThings extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private HandedThingsToVisitorsAdapter adapterVisitors;
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private HandedThingsToDailyServiceAdapter adapterDailyService;
    private int handed_things_to;
    private int index = 0, childCount = 0;

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
            handed_things_to = R.string.handed_things_to_my_guest;
        } else {
            handed_things_to = R.string.handed_things_to_my_daily_services;
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
        nammaApartmentVisitorList = new ArrayList<>();
        adapterVisitors = new HandedThingsToVisitorsAdapter(nammaApartmentVisitorList, this);

        nammaApartmentDailyServiceList = new ArrayList<>();
        adapterDailyService = new HandedThingsToDailyServiceAdapter(nammaApartmentDailyServiceList, this);

        /*Retrieve those visitor details who status is Entered*/
        if (handed_things_to == R.string.handed_things_to_my_guest) {
            //To retrieve user visitor list from firebase based on the their status.
            recyclerView.setAdapter(adapterVisitors);
            retrieveCurrentVisitorsFromFirebase();
        } else {
            //To retrieve user daily Services list from firebase based on the their status.
            recyclerView.setAdapter(adapterDailyService);
            retrieveDailyServiceFromFirebase();
        }

    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * We show only those visitor details whose status is "Entered"
     */
    private void retrieveCurrentVisitorsFromFirebase() {
        //First retrieve the current user visitors
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        DatabaseReference myVisitorsReference = userDataReference.child(Constants.FIREBASE_CHILD_VISITORS);

        myVisitorsReference.child(NammaApartmentsGlobal.userUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (!dataSnapshot.exists() && currentNammaApartmentUser.getFamilyMembers() == null) {
                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                } else if (dataSnapshot.exists()) {
                    for (DataSnapshot visitorsSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                .child(visitorsSnapshot.getKey());
                        preApprovedVisitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot nammaApartmentVisitorData) {
                                /*We increment the children count, since after onDataChange we would want
                                to check if there any visitors of the user's family member*/
                                childCount++;
                                NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorData.getValue(NammaApartmentVisitor.class);
                                if (nammaApartmentVisitor.getStatus().equals(ENTERED)) {
                                    nammaApartmentVisitorList.add(index++, nammaApartmentVisitor);
                                    adapterVisitors.notifyDataSetChanged();
                                }

                                //Check if current user has any familyMembers
                                if (childCount == dataSnapshot.getChildrenCount() &&
                                        currentNammaApartmentUser.getFamilyMembers() != null) {
                                    //Take each of their Visitors and add it to Visitors List
                                    addFamilyMembersVisitors();
                                }
                                /*We reached a point where there are no visitors whose status is "Entered"
                                hence we show feature unavailable message*/
                                else if (childCount == dataSnapshot.getChildrenCount() &&
                                        nammaApartmentVisitorList.isEmpty()) {
                                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                } else {
                    addFamilyMembersVisitors();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * We now check if there are any users family member visitors whose status is "Entered"
     */
    private void addFamilyMembersVisitors() {
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        for (Map.Entry<String, Boolean> familyMembersUID : currentNammaApartmentUser.getFamilyMembers().entrySet()) {
            DatabaseReference visitorsReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                    .child(Constants.FIREBASE_CHILD_VISITORS);
            visitorsReference.child(familyMembersUID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        DatabaseReference visitorsReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                                .child(Constants.FIREBASE_CHILD_VISITORS);
                        visitorsReference.child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot visitorsSnapshot : dataSnapshot.getChildren()) {
                                    DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                            .child(visitorsSnapshot.getKey());
                                    preApprovedVisitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            NammaApartmentVisitor nammaApartmentVisitor = dataSnapshot.getValue(NammaApartmentVisitor.class);
                                            if (nammaApartmentVisitor.getStatus().equals(ENTERED)) {
                                                nammaApartmentVisitorList.add(index++, nammaApartmentVisitor);
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
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    /**
     * We show only those daily service details whose status is Entered
     */
    private void retrieveDailyServiceFromFirebase() {
        //First retrieve the current user daily service
        DatabaseReference dailyServicesListReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_DAILYSERVICES);
        dailyServicesListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot myDailyServiceSnapshot) {
                if (!myDailyServiceSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
                } else {
                    for (DataSnapshot dailyServicesSnapshot : myDailyServiceSnapshot.getChildren()) {
                        String dailyServiceType = dailyServicesSnapshot.getKey();
                        DatabaseReference dailyServiceTypeReference = dailyServicesListReference.child(dailyServiceType);
                        dailyServiceTypeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dailyServiceUIDSnapshot) {
                                for (DataSnapshot childSnapshot : dailyServiceUIDSnapshot.getChildren()) {
                                    DatabaseReference dailyServiceDataReference = PUBLIC_DAILYSERVICES_REFERENCE
                                            .child(dailyServiceUIDSnapshot.getKey())
                                            .child(Objects.requireNonNull(childSnapshot.getKey()));
                                    dailyServiceDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dailyServiceDataSnapshot) {
                                            NammaApartmentDailyService nammaApartmentDailyService = dailyServiceDataSnapshot.getValue(NammaApartmentDailyService.class);
                                            if (nammaApartmentDailyService.getStatus().equals(ENTERED)) {
                                                Objects.requireNonNull(nammaApartmentDailyService).setDailyServiceType(dailyServiceType.substring(0, 1).toUpperCase() + dailyServiceType.substring(1));
                                                nammaApartmentDailyServiceList.add(index++, nammaApartmentDailyService);
                                                hideFeatureUnavailableLayout();
                                                adapterDailyService.notifyDataSetChanged();
                                            }

                                            if (nammaApartmentDailyServiceList.isEmpty()) {
                                                showFeatureUnavailableLayout(R.string.daily_service_unavailable_message_handed_things);
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
                hideProgressIndicator();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
