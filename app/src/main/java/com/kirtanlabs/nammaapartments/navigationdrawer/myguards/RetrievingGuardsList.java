package com.kirtanlabs.nammaapartments.navigationdrawer.myguards;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.pogo.NammaApartmentsGuard;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PRIVATE;

public class RetrievingGuardsList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private DatabaseReference guardReference;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingGuardsList() {
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void getGuardUIDList(GuardUIDListCallback guardUIDListCallback) {
        guardReference = Constants.GUARD_REFERENCE
                .child(FIREBASE_CHILD_PRIVATE)
                .child(FIREBASE_CHILD_DATA);
        guardReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> guardUid = new ArrayList<>();
                    for (DataSnapshot guardUidDataSnapshot : dataSnapshot.getChildren()) {
                        guardUid.add(guardUidDataSnapshot.getKey());
                    }
                    guardUIDListCallback.onCallBack(guardUid);
                } else {
                    guardUIDListCallback.onCallBack(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Public Method
     * ------------------------------------------------------------- */

    public void getGuardDataList(GuardDataListCallback guardDataListCallback) {
        getGuardUIDList(guardUIDList -> {
            if (guardUIDList != null) {
                List<NammaApartmentsGuard> guardDataList = new ArrayList<>();

                for (String guardUID : guardUIDList) {
                    guardReference.child(guardUID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentsGuard nammaApartmentsGuard = dataSnapshot.getValue(NammaApartmentsGuard.class);
                            guardDataList.add(nammaApartmentsGuard);
                            guardDataListCallback.onCallBack(guardDataList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            } else {
                guardDataListCallback.onCallBack(null);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface GuardUIDListCallback {
        void onCallBack(List<String> guardUIDList);
    }

    public interface GuardDataListCallback {
        void onCallBack(List<NammaApartmentsGuard> guardDataList);
    }
}
