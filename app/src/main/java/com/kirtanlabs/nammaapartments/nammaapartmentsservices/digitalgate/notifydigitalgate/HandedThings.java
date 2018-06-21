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
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.HANDED_THINGS_TO;

public class HandedThings extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private HandedThingsAdapter adapter;

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
        return getIntent().getIntExtra(HANDED_THINGS_TO, 0);
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
        adapter = new HandedThingsAdapter(nammaApartmentVisitorList, this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(adapter);

        /*Retrieve those visitor details who status is Entered*/
        retrieveCurrentVisitorsFromFirebase();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * We retrieve visitors for current user and their family members and display it in the card view
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
                                NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorData.getValue(NammaApartmentVisitor.class);
                                if (nammaApartmentVisitor.getStatus().equals("Entered")) {
                                    nammaApartmentVisitorList.add(0, nammaApartmentVisitor);
                                    adapter.notifyDataSetChanged();
                                }

                                //Check if current user has any familyMembers
                                if (nammaApartmentVisitorList.size() == dataSnapshot.getChildrenCount() &&
                                        currentNammaApartmentUser.getFamilyMembers() != null) {
                                    //Take each of their Visitors and add it to Visitors List
                                    addFamilyMembersVisitors();
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
     * We store the details of visitors for current user and their family members if any, in Firebase
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
                                                nammaApartmentVisitorList.add(0, nammaApartmentVisitor);
                                                adapter.notifyDataSetChanged();
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

}
