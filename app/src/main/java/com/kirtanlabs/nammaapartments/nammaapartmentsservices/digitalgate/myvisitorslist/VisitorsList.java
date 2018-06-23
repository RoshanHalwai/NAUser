package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.digitalgatehome.DigitalGateHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class VisitorsList extends BaseActivity {

    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private VisitorsListAdapter adapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_visitors_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_visitors_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Set Listener for back button here since after Inviting visitors we navigate users
         * to this activity and when back button is pressed we don't want users to
         * go back to Invite Visitors screen but instead navigate to Digi Gate Home*/
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentVisitorList = new ArrayList<>();
        adapter = new VisitorsListAdapter(nammaApartmentVisitorList, this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(adapter);

        //To retrieve user visitor list from firebase
        retrieveVisitorsDetailsFromFirebase();
    }

    /* ------------------------------------------------------------- *
     * Overriding Back button
     * ------------------------------------------------------------- */

    /*We override these methods since after Inviting visitors we navigate users
     * to this activity and when back button is pressed we don't want users to
     * go back to Invite Visitors screen but instead navigate to Digi Gate Home*/

    @Override
    public void onBackPressed() {
        Intent digitalGateHomeIntent = new Intent(this, DigitalGateHome.class);
        digitalGateHomeIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        digitalGateHomeIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(digitalGateHomeIntent);
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * We retrieve visitors for current user and their family members if any
     */
    private void retrieveVisitorsDetailsFromFirebase() {
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
                                nammaApartmentVisitorList.add(index++, nammaApartmentVisitor);
                                adapter.notifyDataSetChanged();

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


        //TODO: Ensure that Current user friends Visitors are not added to Visitors List
    }

    private void addFamilyMembersVisitors() {
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        for (Map.Entry<String, Boolean> familyMembersUID : currentNammaApartmentUser.getFamilyMembers().entrySet()) {
            DatabaseReference visitorsReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                    .child(Constants.FIREBASE_CHILD_VISITORS);
            visitorsReference.child(familyMembersUID.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot visitorsSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                    .child(visitorsSnapshot.getKey());
                            preApprovedVisitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    NammaApartmentVisitor nammaApartmentVisitor = dataSnapshot.getValue(NammaApartmentVisitor.class);
                                    nammaApartmentVisitorList.add(index++, nammaApartmentVisitor);
                                    adapter.notifyDataSetChanged();
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

}
