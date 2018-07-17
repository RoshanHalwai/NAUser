package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.mysweethome;

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
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_FLAT_MEMBERS;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;

public class MySweetHome extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private List<NammaApartmentUser> nammaApartmentFamilyMembersList;
    private MySweetHomeAdapter mySweetHomeAdapter;
    private int index = 0;

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
        buttonAddFamilyMembers.setTypeface(setLatoLightFont(this));

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
                NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
                boolean isAdmin = currentNammaApartmentUser.getPrivileges().isAdmin();
                if (isAdmin) {
                    Intent intent = new Intent(MySweetHome.this, AddFamilyMember.class);
                    intent.putExtra(Constants.SCREEN_TITLE, R.string.my_sweet_home);
                    startActivity(intent);
                } else {
                    showNotificationDialog(getResources().getString(R.string.non_admin_add_family_members_title)
                            , getResources().getString(R.string.non_admin_add_family_members_message)
                            , null);
                }
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveFamilyMembersDetailsFromFirebase() {
        DatabaseReference privateFlatReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference();
        privateFlatReference.child(FIREBASE_CHILD_FLAT_MEMBERS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (dataSnapshot.getChildrenCount() == 1) {
                    showFeatureUnavailableLayout(R.string.family_member_unavailable_message);
                } else {
                    for (DataSnapshot flatSnapshot : dataSnapshot.getChildren()) {
                        if (!flatSnapshot.getKey().equals(NammaApartmentsGlobal.userUID)) {
                            DatabaseReference userReference = PRIVATE_USERS_REFERENCE.child(flatSnapshot.getKey());
                            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    NammaApartmentUser nammaApartmentFamilyMember = dataSnapshot.getValue(NammaApartmentUser.class);
                                    nammaApartmentFamilyMembersList.add(index++, nammaApartmentFamilyMember);
                                    mySweetHomeAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
