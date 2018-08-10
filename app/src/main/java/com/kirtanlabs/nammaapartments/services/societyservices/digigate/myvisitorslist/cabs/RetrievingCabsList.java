package com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.cabs;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.arrivals.NammaApartmentArrival;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_CABS_REFERENCE;

public class RetrievingCabsList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private DatabaseReference userDataReference;
    private List<String> userUIDList;
    private int count = 0;

    /*-------------------------------------------------------------*
     * Constructor
     *-------------------------------------------------------------*/

    /**
     * @param context of the current activity.
     */
    RetrievingCabsList(Context context) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) context.getApplicationContext());
        userDataReference = nammaApartmentsGlobal.getUserDataReference();
        userUIDList = new ArrayList<>();
        Set<String> userFamilyMemberUID = nammaApartmentsGlobal.getNammaApartmentUser().getFamilyMembers().keySet();
        userUIDList.add(NammaApartmentsGlobal.userUID);
        userUIDList.addAll(userFamilyMemberUID);
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * @param cabListCallback receiving result with list of all cab data of userUID present in userUIDList
     *                        contain in the list of current user UID and their family members UID
     */
    public void getCabs(CabListCallback cabListCallback) {
        List<NammaApartmentArrival> nammaApartmentAllCabsList = new ArrayList<>();
        isCabReferenceExists(cabReferenceExists -> {
            if (cabReferenceExists) {
                for (String userUID : userUIDList) {
                    getCabs(nammaApartmentCabList -> {
                        nammaApartmentAllCabsList.addAll(nammaApartmentCabList);
                        count++;
                        if (count == userUIDList.size()) {
                            cabListCallback.onCallBack(nammaApartmentAllCabsList);
                        }
                    }, userUID);
                }
            } else {
                cabListCallback.onCallBack(nammaApartmentAllCabsList);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param cabListCallback receiving result with list of all cab data of userUID
     * @param userUID         whose cabs needs to be retrieved from firebase
     */
    private void getCabs(CabListCallback cabListCallback, String userUID) {
        getCabUIDList(cabUIDList -> getCabsList(cabListCallback, cabUIDList), userUID);
    }

    /**
     * @param cabListCallback receiving result with list of all cab data whose UID is present in
     *                        cabUIDList
     * @param cabUIDList      contains the list of all cabs UID whose data needs to be retrieved from firebase
     */
    private void getCabsList(CabListCallback cabListCallback, List<String> cabUIDList) {
        List<NammaApartmentArrival> nammaApartmentArrivalList = new ArrayList<>();
        if (cabUIDList.isEmpty()) {
            cabListCallback.onCallBack(nammaApartmentArrivalList);
        } else {
            for (String cabUID : cabUIDList) {
                getCabDataByUID(nammaApartmentArrival -> {
                    nammaApartmentArrivalList.add(nammaApartmentArrival);
                    if (nammaApartmentArrivalList.size() == cabUIDList.size()) {
                        cabListCallback.onCallBack(nammaApartmentArrivalList);
                    }
                }, cabUID);
            }
        }
    }

    /**
     * @param cabUIDListCallback receiving result of Cab UID List
     * @param userUID            of the particular user whose cab UID List needs to be retrieved
     *                           from firebase
     */
    private void getCabUIDList(CabUIDListCallback cabUIDListCallback, String userUID) {
        DatabaseReference cabListReference = userDataReference.child(FIREBASE_CHILD_CABS).child(userUID);
        cabListReference.keepSynced(true);
        cabListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> cabUIDList = new ArrayList<>();
                for (DataSnapshot cabUIDSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(cabUIDSnapshot.getValue(Boolean.class)).equals(true)) {
                        cabUIDList.add(cabUIDSnapshot.getKey());
                    }
                }
                cabUIDListCallback.onCallBack(cabUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param cabDataCallback receiving result of the Cab Data
     * @param cabUID          UID of the Cab whose data is to be retrieved from firebase
     */
    private void getCabDataByUID(CabDataCallback cabDataCallback, String cabUID) {
        DatabaseReference cabDataReference = PRIVATE_CABS_REFERENCE.child(cabUID);
        cabDataReference.keepSynced(true);
        cabDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cabDataCallback.onCallBack(dataSnapshot.getValue(NammaApartmentArrival.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param cabsReferenceCallback receives boolean value true if there is at least one cab for the flat
     *                              else returns false.
     */
    private void isCabReferenceExists(CabsReferenceCallback cabsReferenceCallback) {
        DatabaseReference cabDataReference = userDataReference.child(FIREBASE_CHILD_CABS);
        cabDataReference.keepSynced(true);
        cabDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cabsReferenceCallback.onCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface CabListCallback {
        void onCallBack(List<NammaApartmentArrival> nammaApartmentArrivalList);
    }

    private interface CabUIDListCallback {
        void onCallBack(List<String> cabUIDList);
    }

    private interface CabsReferenceCallback {
        void onCallback(boolean cabReferenceExits);
    }

    private interface CabDataCallback {
        void onCallBack(NammaApartmentArrival nammaApartmentArrival);
    }
}
