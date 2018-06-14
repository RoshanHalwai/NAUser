package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.AddDailyServiceAndFamilyMembers;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYFAMILYMEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_FAMILYMEMBERS_REFERENCE;

public class MySweetHome extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private List<NammaApartmentFamilyMembers> nammaApartmentFamilyMembersList;
    private MySweetHomeAdapter mySweetHomeAdapter;

    /*---------------------------------------------------------
        Overriding Base Activity Objects
     ----------------------------------------------------------*/
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_sweet_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_sweet_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id for the button*/
        Button buttonAddFamilyMembers = findViewById(R.id.buttonAddFamilyMembers);

        /*Setting Fonts for Add My Family Members Button*/
        buttonAddFamilyMembers.setTypeface(Constants.setLatoLightFont(this));

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Creating recycler view adapter
        nammaApartmentFamilyMembersList = new ArrayList<>();
        mySweetHomeAdapter = new MySweetHomeAdapter(nammaApartmentFamilyMembersList, this);

        //Setting adapter to recycler view
        recyclerView.setAdapter(mySweetHomeAdapter);

        /*Setting button click listener*/
        buttonAddFamilyMembers.setOnClickListener(this);

        //To retrieve user FamilyMember Details from firebase.
        retrieveFamilyMembersDetailsFromFirebase();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddFamilyMembers:
                Intent intent = new Intent(MySweetHome.this, AddDailyServiceAndFamilyMembers.class);
                intent.putExtra(Constants.SCREEN_TITLE, R.string.my_sweet_home);
                startActivity(intent);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveFamilyMembersDetailsFromFirebase() {
        PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                .child(FIREBASE_CHILD_MYFAMILYMEMBERS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            hideProgressIndicator();
                        }
                        for (DataSnapshot familyMemberSnapshot : dataSnapshot.getChildren()) {
                            DatabaseReference familyMemberReference = PUBLIC_FAMILYMEMBERS_REFERENCE
                                    .child(familyMemberSnapshot.getKey());
                            familyMemberReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot familyMemberDataSnapshot) {
                                    NammaApartmentFamilyMembers nammaApartmentFamilyMembers = familyMemberDataSnapshot.getValue(NammaApartmentFamilyMembers.class);
                                    nammaApartmentFamilyMembersList.add(0, nammaApartmentFamilyMembers);
                                    mySweetHomeAdapter.notifyDataSetChanged();
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
