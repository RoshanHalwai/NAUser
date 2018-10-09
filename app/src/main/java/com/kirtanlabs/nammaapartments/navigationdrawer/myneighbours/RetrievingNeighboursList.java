package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;

public class RetrievingNeighboursList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private int count = 0;
    private String currentUserApartmentName;
    private String currentUserFlatNumber;
    private String currentUserUID;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingNeighboursList(Context mCtx) {
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getNammaApartmentUser();
        currentUserApartmentName = currentNammaApartmentUser.getFlatDetails().getApartmentName();
        currentUserFlatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();
        currentUserUID = currentNammaApartmentUser.getUID();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve all user's UID from firebase under (users -> all -> usersUID) expect the current user UID
     *
     * @param neighboursUIDListCallback - callback to return list of neighbours UID
     */
    private void getNeighboursUIDList(NeighboursUIDListCallback neighboursUIDListCallback) {
        ALL_USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> neighbourUidList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot neighbourUIDSnapshot : dataSnapshot.getChildren()) {
                        String neighbourUID = neighbourUIDSnapshot.getValue(String.class);
                        /*Add all users UID to the list current except user's UID*/
                        if (!Objects.requireNonNull(neighbourUID).equals(currentUserUID)) {
                            count++;
                            neighbourUidList.add(neighbourUID);
                        }
                    }

                    if (count == neighbourUidList.size()) {
                        neighboursUIDListCallback.onCallBack(neighbourUidList);
                    }

                } else {
                    neighboursUIDListCallback.onCallBack(new ArrayList<>());
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

    /**
     * This method is invoked to retrieve the user's data from firebase under (users->private->userUID).
     *
     * @param neighboursDataListCallback - callback to return list of users data
     */
    public void getNeighbourDataList(NeighboursDataListCallback neighboursDataListCallback) {
        getNeighboursUIDList(neighboursUIDList -> {
            if (!neighboursUIDList.isEmpty()) {
                count = 0;
                List<NammaApartmentUser> neighboursDataList = new ArrayList<>();

                for (String neighbourUID : neighboursUIDList) {
                    count++;
                    /*retrieving data from (users->private->userUID) in firebase*/
                    DatabaseReference neighbourDataReference = PRIVATE_USERS_REFERENCE.child(neighbourUID);
                    neighbourDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                            /*Getting user apartment name and flat number value*/
                            String userApartmentName = Objects.requireNonNull(nammaApartmentUser).getFlatDetails().getApartmentName();
                            String userFlatNumber = nammaApartmentUser.getFlatDetails().getFlatNumber();
                            /*Adding only that users data in the list who is admin of a flat and also that user is not a flat member of current user*/
                            if (nammaApartmentUser.getPrivileges().isAdmin() &&
                                    !((userApartmentName.equals(currentUserApartmentName)) && userFlatNumber.equals(currentUserFlatNumber))) {
                                neighboursDataList.add(nammaApartmentUser);
                            }

                            if (count == neighboursUIDList.size())
                                neighboursDataListCallback.onCallBack(neighboursDataList);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                neighboursDataListCallback.onCallBack(new ArrayList<>());
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface NeighboursUIDListCallback {
        void onCallBack(List<String> neighboursUIDList);
    }

    public interface NeighboursDataListCallback {
        void onCallBack(List<NammaApartmentUser> neighboursDataList);
    }
}
