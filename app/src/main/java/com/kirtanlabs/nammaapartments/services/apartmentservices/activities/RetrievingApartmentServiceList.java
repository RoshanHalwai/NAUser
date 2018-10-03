package com.kirtanlabs.nammaapartments.services.apartmentservices.activities;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.NammaApartmentDailyService;

import java.util.ArrayList;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;

public class RetrievingApartmentServiceList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String dailyServiceUID;
    private String dailyServiceType;
    private int rating = 0;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    RetrievingApartmentServiceList(String dailyServiceUID, String dailyServiceType) {
        this.dailyServiceUID = dailyServiceUID;
        this.dailyServiceType = dailyServiceType;
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * @param dailyServiceData receiving result with data of Daily Service present under each Owner UID
     */

    public void getDailyServiceDetails(DailyServiceData dailyServiceData) {
        getOwnersUIDList(dailyServiceOwnerUIDList -> {
            /*Get Daily Service Data*/
            getOwnerData(dailyServiceOwnerUIDList.get(0), nammaApartmentDailyService -> {
                if (dailyServiceOwnerUIDList.size() != 1) {
                    for (String ownerUID : dailyServiceOwnerUIDList) {
                        getOwnerData(ownerUID, nammaApartmentDailyService1 -> {
                            rating = rating + nammaApartmentDailyService1.getRating();
                            count++;
                            if (count == dailyServiceOwnerUIDList.size()) {
                                nammaApartmentDailyService1.setRating(rating / count);
                                ApartmentServices.numberOfFlats.put(nammaApartmentDailyService1.getUID(), (long) count);
                                dailyServiceData.onCallback(nammaApartmentDailyService1);
                            }
                        });

                    }
                } else {
                    ApartmentServices.numberOfFlats.put(nammaApartmentDailyService.getUID(), (long) 1);
                    dailyServiceData.onCallback(nammaApartmentDailyService);
                }
            });
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param dailyServiceOwnerUIDListCallback receiving result with list of all Owner UID having a particular Daily Service
     */

    private void getOwnersUIDList(DailyServiceOwnerUIDListCallback dailyServiceOwnerUIDListCallback) {
        DatabaseReference dailyServiceOwnerListReference = PUBLIC_DAILYSERVICES_REFERENCE.child(dailyServiceType).child(dailyServiceUID);
        dailyServiceOwnerListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dailyServiceOwnerUIDList = new ArrayList<>();
                for (DataSnapshot dailyServiceOwnerUIDSnapshot : dataSnapshot.getChildren()) {
                    if (!dailyServiceOwnerUIDSnapshot.getKey().equals(FIREBASE_CHILD_STATUS))
                        dailyServiceOwnerUIDList.add(dailyServiceOwnerUIDSnapshot.getKey());
                }
                dailyServiceOwnerUIDListCallback.onCallBack(dailyServiceOwnerUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param ownerUID of that particular Owner whose Daily Service data needs to be retrieved
     * @param dailyServiceData to retrieve entire data of the Daily Service
     */
    private void getOwnerData(String ownerUID, DailyServiceData dailyServiceData) {
        DatabaseReference dailyServiceOwnerDataRef = PUBLIC_DAILYSERVICES_REFERENCE.child(dailyServiceType).child(dailyServiceUID).child(ownerUID);
        dailyServiceOwnerDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dailyServiceData.onCallback(dataSnapshot.getValue(NammaApartmentDailyService.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    private interface DailyServiceOwnerUIDListCallback {
        void onCallBack(List<String> dailyServiceOwnerUIDList);
    }

    public interface DailyServiceData {
        void onCallback(NammaApartmentDailyService nammaApartmentDailyService);
    }

}
