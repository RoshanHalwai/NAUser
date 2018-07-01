package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.guests;

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
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

public class GuestsList extends BaseActivity {

    DatabaseReference userDataReference;
    DatabaseReference myVisitorsReference;
    private List<NammaApartmentGuest> nammaApartmentGuestList;
    private GuestsListAdapter guestsListAdapter;
    private int index = 0;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_guests_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_guests;
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

        //Creating recycler view guestsListAdapter
        nammaApartmentGuestList = new ArrayList<>();
        guestsListAdapter = new GuestsListAdapter(nammaApartmentGuestList, this);

        //Setting guestsListAdapter to recycler view
        recyclerView.setAdapter(guestsListAdapter);

        //Retrieve Guest data
        checkAndRetrieveGuestDetails();
    }

    /* ------------------------------------------------------------- *
     * Overriding Back button
     * ------------------------------------------------------------- */

    /*We override these methods since after Inviting visitors we navigate users
     * to this activity and when back button is pressed we don't want users to
     * go back to Invite Visitors screen but instead navigate to Digi Gate Home*/
    @Override
    public void onBackPressed() {
        if (getIntent().getStringExtra(SCREEN_TITLE) != null) {
            Intent visitorsListIntent = new Intent(this, DigitalGateHome.class);
            visitorsListIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            visitorsListIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(visitorsListIntent);
        } else {
            super.onBackPressed();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void checkAndRetrieveGuestDetails() {
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
                    retrieveGuestDetailsFromFirebase(NammaApartmentsGlobal.userUID);

                    //Retrieve user family member guests list from firebase
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    Map<String, Boolean> familyMembers = currentNammaApartmentUser.getFamilyMembers();
                    if (familyMembers != null && !familyMembers.isEmpty()) {
                        for (String userUID : familyMembers.keySet()) {
                            retrieveGuestDetailsFromFirebase(userUID);
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
    private void retrieveGuestDetailsFromFirebase(String userUID) {
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
                            nammaApartmentGuestList.add(index++, nammaApartmentGuest);
                            guestsListAdapter.notifyDataSetChanged();
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
