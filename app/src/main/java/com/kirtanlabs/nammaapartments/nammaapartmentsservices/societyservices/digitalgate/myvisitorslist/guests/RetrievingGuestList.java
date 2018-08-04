package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.guests;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_GUESTS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_POSTAPPROVED_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PREAPPROVED_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.POSTAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/20/2018
 */
public class RetrievingGuestList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private DatabaseReference userDataReference;
    private List<String> userUIDList;
    private int count = 0;
    private boolean pastGuestListRequired;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    /**
     * @param context               of the current activity
     * @param pastGuestListRequired usually false but for Handed Things History activity, the value will be true
     */
    public RetrievingGuestList(Context context, boolean pastGuestListRequired) {
        this.pastGuestListRequired = pastGuestListRequired;
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
     * @param guestListCallback  receiving result with list of all guest data of userUID present in userUIDList
     *                           which contains list of current user preApproved Visitors and PostApproved Visitors.
     */
    public void getPreAndPostApprovedGuests(GuestListCallback guestListCallback) {
        List<NammaApartmentGuest> preAndPostApprovedGuestList = new ArrayList<>();
        getPreApprovedGuests(nammaApartmentPreApprovedGuestList -> {
            preAndPostApprovedGuestList.addAll(nammaApartmentPreApprovedGuestList);
            count = 0;
            getPostApprovedGuests(nammaApartmentPostApprovedGuestList -> {
                preAndPostApprovedGuestList.addAll(nammaApartmentPostApprovedGuestList);
                guestListCallback.onCallBack(preAndPostApprovedGuestList);
            });
        });
    }

    /**
     * @param guestListCallback receiving result with list of all guest data of userUID present in userUIDList
     *                          contains list of current user UID and their family members UID
     */
    private void getPreApprovedGuests(GuestListCallback guestListCallback) {
        List<NammaApartmentGuest> nammaApartmentAllGuestList = new ArrayList<>();
        isGuestReferenceExists(guestReferenceExits -> {
            if (guestReferenceExits) {
                for (String userUID : userUIDList) {
                    getGuests(nammaApartmentGuestList -> {
                        nammaApartmentAllGuestList.addAll(nammaApartmentGuestList);
                        count++;
                        if (count == userUIDList.size()) {
                            guestListCallback.onCallBack(nammaApartmentAllGuestList);
                        }
                    }, userUID, false);
                }
            } else {
                guestListCallback.onCallBack(nammaApartmentAllGuestList);
            }
        });
    }

    /**
     * @param guestListCallback receiving result with list of all guest data of userUID present in userUIDList
     *                          contains list of current user UID and their family members UID
     */
    private void getPostApprovedGuests(GuestListCallback guestListCallback) {
        List<NammaApartmentGuest> nammaApartmentAllGuestList = new ArrayList<>();
        isGuestReferenceExists(guestReferenceExits -> {
            if (guestReferenceExits) {
                for (String userUID : userUIDList) {
                    getGuests(nammaApartmentGuestList -> {
                        nammaApartmentAllGuestList.addAll(nammaApartmentGuestList);
                        count++;
                        if (count == userUIDList.size()) {
                            guestListCallback.onCallBack(nammaApartmentAllGuestList);
                        }
                    }, userUID, true);
                }
            } else {
                guestListCallback.onCallBack(nammaApartmentAllGuestList);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param guestListCallback receiving result with list of all guest data of userUID
     * @param userUID           whose guests needs to be retrieved from firebase
     */
    private void getGuests(GuestListCallback guestListCallback, String userUID, boolean postApprovedVisitors) {
        getGuestUIDList(guestUIDList -> getGuestsList(guestListCallback, guestUIDList, postApprovedVisitors), userUID, postApprovedVisitors);
    }

    /**
     * @param guestListCallback receiving result with list of all guest data whose UID is present in
     *                          guestUIDList
     * @param guestUIDList      contains the list of all guests UID whose data needs to be retrieved from firebase
     */
    private void getGuestsList(GuestListCallback guestListCallback, List<String> guestUIDList, boolean postApprovedVisitors) {
        List<NammaApartmentGuest> nammaApartmentGuestList = new ArrayList<>();
        if (guestUIDList.isEmpty()) {
            guestListCallback.onCallBack(nammaApartmentGuestList);
        } else {
            for (String guestUID : guestUIDList) {
                getGuestDataByUID(nammaApartmentGuest -> {
                    nammaApartmentGuestList.add(nammaApartmentGuest);
                    if (nammaApartmentGuestList.size() == guestUIDList.size()) {
                        guestListCallback.onCallBack(nammaApartmentGuestList);
                    }
                }, guestUID, postApprovedVisitors);
            }
        }
    }

    /**
     * @param guestUIDListCallback receiving result of Guest UID List
     * @param userUID              of the particular user whose guest UID List needs to be retrieved
     *                             from firebase
     */
    private void getGuestUIDList(GuestUIDListCallback guestUIDListCallback, String userUID, boolean postApprovedVisitors) {
        DatabaseReference guestListReference;
        if (postApprovedVisitors) {
            guestListReference = userDataReference.child(FIREBASE_CHILD_VISITORS)
                    .child(userUID).child(FIREBASE_CHILD_POSTAPPROVED_VISITORS).child(FIREBASE_CHILD_GUESTS);
        } else {
            guestListReference = userDataReference.child(FIREBASE_CHILD_VISITORS)
                    .child(userUID).child(FIREBASE_CHILD_PREAPPROVED_VISITORS).child(FIREBASE_CHILD_GUESTS);
        }
        guestListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> guestUIDList = new ArrayList<>();
                for (DataSnapshot visitorUIDSnapshot : dataSnapshot.getChildren()) {
                    if (visitorUIDSnapshot.getValue(Boolean.class).equals(true) || pastGuestListRequired) {
                        guestUIDList.add(visitorUIDSnapshot.getKey());
                    }
                }
                guestUIDListCallback.onCallBack(guestUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param guestDataCallback receiving result of the Guest Data
     * @param guestUID          UID of the Guest whose data is to be retrieved from firebase
     */
    private void getGuestDataByUID(GuestDataCallback guestDataCallback, String guestUID, boolean postApprovedVisitors) {
        DatabaseReference guestDataReference;
        if (postApprovedVisitors) {
            guestDataReference = POSTAPPROVED_VISITORS_REFERENCE.child(guestUID);
        } else {
            guestDataReference = PREAPPROVED_VISITORS_REFERENCE.child(guestUID);
        }
        guestDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guestDataCallback.onCallBack(dataSnapshot.getValue(NammaApartmentGuest.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param guestsReferenceCallback receives boolean value true if there is at least one visitor for the flat
     *                                else returns false
     */
    private void isGuestReferenceExists(GuestsReferenceCallback guestsReferenceCallback) {
        DatabaseReference guestDataReference = userDataReference.child(FIREBASE_CHILD_VISITORS);
        guestDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                guestsReferenceCallback.onCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface GuestListCallback {
        void onCallBack(List<NammaApartmentGuest> nammaApartmentGuestList);
    }

    private interface GuestUIDListCallback {
        void onCallBack(List<String> guestUIDList);
    }

    private interface GuestsReferenceCallback {
        void onCallback(boolean guestReferenceExits);
    }

    private interface GuestDataCallback {
        void onCallBack(NammaApartmentGuest nammaApartmentGuest);
    }

}
