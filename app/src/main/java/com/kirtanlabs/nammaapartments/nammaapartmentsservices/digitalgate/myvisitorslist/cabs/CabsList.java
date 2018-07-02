package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.cabs;

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
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate.arrivals.NammaApartmentArrival;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

public class CabsList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private List<NammaApartmentArrival> nammaApartmentArrivalList;
    private CabsListAdapter cabsListAdapter;
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
        return R.string.cab_arrivals;
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
        nammaApartmentArrivalList = new ArrayList<>();
        cabsListAdapter = new CabsListAdapter(nammaApartmentArrivalList, this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(cabsListAdapter);

        //To retrieve user cabs list from firebase
        checkAndRetrieveCabDetails();
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
            Intent cabsListIntent = new Intent(this, DigitalGateHome.class);
            cabsListIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
            cabsListIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(cabsListIntent);
        } else {
            super.onBackPressed();
        }
    }


    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Check if the flat has any cabs booked. If it does not have any cabs booked we show cabs unavailable message
     * Else, we display the cabs of the current user and their family members
     */
    private void checkAndRetrieveCabDetails() {
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myCabsReference = userDataReference.child(Constants.FIREBASE_CHILD_CABS);

        /*We first check if this flat has any cabs*/
        myCabsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.cabs_unavailable_message);
                } else {
                    //Retrieve user cabs list from firebase
                    retrieveCabsDetailsFromFirebase(NammaApartmentsGlobal.userUID);

                    //Retrieve user family member cabs list from firebase
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    Map<String, Boolean> familyMembers = currentNammaApartmentUser.getFamilyMembers();
                    if (familyMembers != null && !familyMembers.isEmpty()) {
                        for (String userUID : familyMembers.keySet()) {
                            retrieveCabsDetailsFromFirebase(userUID);
                        }
                    }
                    //TODO: Ensure user friends cabs are not added in Cabs List
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * We retrieve cabs for current user and their family members if any.
     */
    private void retrieveCabsDetailsFromFirebase(String userUid) {
        //First retrieve the current user cabs
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myCabsReference = userDataReference.child(Constants.FIREBASE_CHILD_CABS);
        myCabsReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                    for (DataSnapshot cabsSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference cabsDataReference = PUBLIC_CABS_REFERENCE
                                .child(cabsSnapshot.getKey());
                        cabsDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot nammaApartmentCabsData) {
                                NammaApartmentArrival nammaApartmentArrival = nammaApartmentCabsData.getValue(NammaApartmentArrival.class);
                                nammaApartmentArrivalList.add(index++, nammaApartmentArrival);
                                cabsListAdapter.notifyDataSetChanged();
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

