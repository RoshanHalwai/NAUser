package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.packages;

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
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.digitalgatehome.DigitalGateHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.arrivals.NammaApartmentArrival;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DELIVERIES;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DELIVERIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/28/2018
 */
public class PackagesList extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private List<NammaApartmentArrival> nammaApartmentArrivalList;
    private PackagesListAdapter packagesListAdapter;
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
        return R.string.package_arrivals;
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
        packagesListAdapter = new PackagesListAdapter(nammaApartmentArrivalList, this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(packagesListAdapter);

        //To retrieve user packages list from firebase
        checkAndRetrievePackageDetails();
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
     * Check if the flat has any deliveries booked. If it does not have any deliveries booked we show deliveries unavailable message
     * Else, we display the deliveries of the current user and their family members.
     */
    private void checkAndRetrievePackageDetails() {
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myDeliveriesReference = userDataReference.child(Constants.FIREBASE_CHILD_DELIVERIES);

        /*We first check if this flat has any deliveries*/
        myDeliveriesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    hideProgressIndicator();
                    showFeatureUnavailableLayout(R.string.deliveries_unavailable_message);
                } else {
                    //Retrieve user deliveries list from firebase
                    retrievePackageDetailsFromFirebase(NammaApartmentsGlobal.userUID);

                    //Retrieve user family member deliveries list from firebase
                    NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                    Map<String, Boolean> familyMembers = currentNammaApartmentUser.getFamilyMembers();
                    if (familyMembers != null && !familyMembers.isEmpty()) {
                        for (String userUID : familyMembers.keySet()) {
                            retrievePackageDetailsFromFirebase(userUID);
                        }
                    }
                    //TODO: Ensure user friends deliveries are not added in Packages List
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /**
     * This method gets invoked to retrieve all users package details from firebase.
     */
    private void retrievePackageDetailsFromFirebase(String userUid) {
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference myPackagesReference = userDataReference.child(FIREBASE_CHILD_DELIVERIES);
        myPackagesReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                    for (DataSnapshot deliveriesSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference deliveriesDataReference = PUBLIC_DELIVERIES_REFERENCE
                                .child(deliveriesSnapshot.getKey());
                        deliveriesDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                NammaApartmentArrival nammaApartmentArrival = dataSnapshot.getValue(NammaApartmentArrival.class);
                                nammaApartmentArrivalList.add(index++, nammaApartmentArrival);
                                packagesListAdapter.notifyDataSetChanged();
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