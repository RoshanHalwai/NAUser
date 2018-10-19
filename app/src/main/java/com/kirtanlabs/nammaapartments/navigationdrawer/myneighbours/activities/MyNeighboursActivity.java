package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.activities;

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
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.RetrievingNeighboursList;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.adapters.MyNeighboursAdapter;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.NAMMA_APARTMENTS_PREFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SENDER_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.USER_UID;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/8/2018
 */
public class MyNeighboursActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private RecyclerView recyclerViewMyNeighbour;
    private String recentMessageSenderUID = null;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_neighbours;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_neighbours;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id of recycler view*/
        recyclerViewMyNeighbour = findViewById(R.id.recyclerViewMyNeighbour);
        recyclerViewMyNeighbour.setHasFixedSize(true);
        recyclerViewMyNeighbour.setLayoutManager(new LinearLayoutManager(this));

        /*Getting Notification Message Sender UID if user is navigated to this activity on click of notification*/
        if (getIntent() != null && getIntent().getStringExtra(SENDER_UID) != null) {
            recentMessageSenderUID = getIntent().getStringExtra(SENDER_UID);
        }
        showProgressIndicator();

        /*Checking if user is already initialised or not*/
        if (NammaApartmentsGlobal.userUID == null) {
            setApplicationContextData();
        } else {
            retrievePreviousChatMessages();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve all previous chat message for that particular neighbour with if any
     */
    private void retrievePreviousChatMessages() {
        /*Retrieving Details of all Neighbour of the society*/
        new RetrievingNeighboursList(MyNeighboursActivity.this).getNeighbourDataList(recentMessageSenderUID, neighboursDataList -> {
            if (!neighboursDataList.isEmpty()) {
                hideProgressIndicator();
                /*Setting Adapter to the view*/
                recyclerViewMyNeighbour.setAdapter(new MyNeighboursAdapter(MyNeighboursActivity.this, neighboursDataList, recentMessageSenderUID));
            }
        });
    }

    /**
     * Sets the Application Context data so user can use it through out the application
     */
    private void setApplicationContextData() {
        DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(Objects.requireNonNull(getSharedPreferences(NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE)
                .getString(USER_UID, null)));
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                ((NammaApartmentsGlobal) getApplicationContext()).setNammaApartmentUser(nammaApartmentUser);
                retrievePreviousChatMessages();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
