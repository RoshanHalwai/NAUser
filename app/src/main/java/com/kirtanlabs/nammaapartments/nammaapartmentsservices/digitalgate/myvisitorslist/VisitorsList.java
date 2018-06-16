package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

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

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYVISITORS;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;

public class VisitorsList extends BaseActivity {

    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private VisitorsListAdapter adapter;
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
     * Private Methods
     * ------------------------------------------------------------- */

    private void retrieveVisitorsDetailsFromFirebase() {
        DatabaseReference myVisitorsReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                .child(FIREBASE_CHILD_MYVISITORS);
        myVisitorsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hideProgressIndicator();
                if (!dataSnapshot.exists()) {
                    showFeatureUnavailableLayout(R.string.visitors_unavailable_message);
                } else {
                    for (DataSnapshot visitorsSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference preApprovedVisitorReference = Constants.PREAPPROVED_VISITORS_REFERENCE
                                .child(visitorsSnapshot.getKey());
                        preApprovedVisitorReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot nammaApartmentVisitorData) {
                                NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorData.getValue(NammaApartmentVisitor.class);
                                nammaApartmentVisitorList.add(0, nammaApartmentVisitor);
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
