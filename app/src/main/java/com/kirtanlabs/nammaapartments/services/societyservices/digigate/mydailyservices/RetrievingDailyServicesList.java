package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_HANDED_THINGS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 10/13/2018
 */
public class RetrievingDailyServicesList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final DatabaseReference userDataReference;
    private final boolean isPastDailyServiceListRequired;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingDailyServicesList(final Context context, final boolean isPastDailyServiceListRequired) {
        this.isPastDailyServiceListRequired = isPastDailyServiceListRequired;
        userDataReference = ((NammaApartmentsGlobal) context).getUserDataReference();
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    public void getAllDailyServices(DailyServices dailyServices) {
        List<NammaApartmentDailyService> userDailyServicesList = new LinkedList<>();
        getAllDailyServiceUIDs(dailyServiceUIDMap -> {
            /*User has not added any daily services, hence return an empty list*/
            if (dailyServiceUIDMap.isEmpty()) {
                dailyServices.onCallback(userDailyServicesList);
            }

            for (String dsCategory : dailyServiceUIDMap.keySet()) {
                getDailyServiceCategoryData(dsCategory, dailyServiceUIDMap.get(dsCategory), dailyServiceMap -> {
                    count++;
                    userDailyServicesList.addAll(dailyServiceMap.get(dsCategory));
                    if (count == dailyServiceUIDMap.size()) {
                        count = 0;
                        dailyServices.onCallback(userDailyServicesList);
                    }
                });
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Returns a map with key as {@link DailyServiceType} and values as all daily services under that category
     * which is in turn an instance of {@link NammaApartmentDailyService}
     *
     * @param dsCategory              category of the daily service
     * @param dsUIDList               list of Map containing all daily service UIDs mapped with userUID which belong to dsCategory
     * @param dailyServiceMapCallback callback to return map which contain data of daily service which belong to dsCategory
     */
    private void getDailyServiceCategoryData(String dsCategory, List<Map<String, String>> dsUIDList, DailyServiceMapCallback dailyServiceMapCallback) {
        Map<String, List<NammaApartmentDailyService>> dailyServiceMap = new LinkedHashMap<>();
        List<NammaApartmentDailyService> dailyServiceList = new LinkedList<>();
        for (Map<String, String> dsUIDMap : dsUIDList) {
            String dsUID = dsUIDMap.entrySet().iterator().next().getKey();
            String userUID = dsUIDMap.entrySet().iterator().next().getValue();
            PUBLIC_DAILYSERVICES_REFERENCE.child(dsCategory).child(dsUID).child(userUID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dsDataSnapshot) {
                    /*To get actual data of Daily Service*/
                    DatabaseReference dailyServiceUIDRef = dsDataSnapshot.getRef().getParent();
                    dailyServiceUIDRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dsUIDSnapshot) {
                            /*To get number of flats where daily service works for*/
                            long flats = dsUIDSnapshot.getChildrenCount() - 1;
                            dailyServiceUIDRef.child(FIREBASE_CHILD_STATUS).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot statusSnapshot) {
                                    /*To get status of daily service*/
                                    NammaApartmentDailyService nammaApartmentDailyService = dsDataSnapshot.getValue(NammaApartmentDailyService.class);
                                    nammaApartmentDailyService.setType(DailyServiceType.get(Objects.requireNonNull(nammaApartmentDailyService).getType()));
                                    nammaApartmentDailyService.setNumberOfFlats(flats);
                                    nammaApartmentDailyService.setStatus(statusSnapshot.getValue(String.class));
                                    nammaApartmentDailyService.setUserUID(userUID);

                                    /*To get handed things history of daily service*/
                                    dsDataSnapshot.child(FIREBASE_CHILD_HANDED_THINGS).getRef().addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot handedThingsSnapshot) {
                                            if (handedThingsSnapshot.exists()) {
                                                Map<String, String> handedThingsMap = new LinkedHashMap<>();
                                                for (DataSnapshot handedThings : handedThingsSnapshot.getChildren()) {
                                                    handedThingsMap.put(handedThings.getKey(), handedThings.getValue(String.class));
                                                }
                                                nammaApartmentDailyService.setHandedThingsMap(handedThingsMap);
                                            }

                                            dailyServiceList.add(nammaApartmentDailyService);
                                            if (dailyServiceList.size() == dsUIDList.size()) {
                                                dailyServiceMap.put(dsCategory, dailyServiceList);
                                                dailyServiceMapCallback.onCallback(dailyServiceMap);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    /**
     * Returns all the daily service UIDs who have been added by the user
     *
     * @param dailyServiceCategoryUIDMap returns a callback which contains daily service category as key and all
     *                                   UIDs associated to each of the category
     */
    private void getAllDailyServiceUIDs(DailyServiceCategoryUIDMap dailyServiceCategoryUIDMap) {
        Map<String, List<Map<String, String>>> dailyServiceUIDMap = new LinkedHashMap<>();
        getDailyServiceCategories(dailyServiceCategoriesList -> {
            /*User has not added any daily services yet, hence return an empty map*/
            if (dailyServiceCategoriesList.isEmpty()) {
                dailyServiceCategoryUIDMap.onCallback(dailyServiceUIDMap);
            }

            for (String dailyServiceCategory : dailyServiceCategoriesList) {
                getDailyServiceUIDs(dailyServiceCategory, dailyServiceUIDList -> {

                    /*Checking if the Daily Service UIDList is not equal to zero */
                    if (dailyServiceUIDList.size() != 0) {
                        dailyServiceUIDMap.put(dailyServiceCategory, new LinkedList<>(dailyServiceUIDList));
                    }

                    if (dailyServiceUIDMap.size() == dailyServiceCategoriesList.size()) {
                        dailyServiceCategoryUIDMap.onCallback(dailyServiceUIDMap);
                    }
                });
            }
        });
    }

    /**
     * Get daily service UIDs of one category of Daily Service, say 3 UIDs of Type Cook
     *
     * @param dailyServiceCategory category of the daily service
     * @param dailyServiceUIDs     returns a callback which contains a list of Map which contains all UID which belong to daily service category
     *                             and mapped with userUID who has added that daily service.
     */
    private void getDailyServiceUIDs(String dailyServiceCategory, DailyServiceUIDs dailyServiceUIDs) {
        DatabaseReference dailyServiceUIDReference = userDataReference.child(FIREBASE_CHILD_DAILYSERVICES).child(dailyServiceCategory);
        dailyServiceUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Map<String, String>> dailyServiceUIDList = new LinkedList<>();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dailyServiceType : dataSnapshot.getChildren()) {
                        dailyServiceUIDReference.child(dailyServiceType.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot userUIDSnapshot) {
                                count++;
                                Map<String, Boolean> userUIDMap = (Map<String, Boolean>) userUIDSnapshot.getValue();
                                /*Adding only true Mapped Daily Services.*/
                                if (Objects.requireNonNull(userUIDMap).entrySet().iterator().next().getValue() || isPastDailyServiceListRequired) {
                                    Map<String, String> dailyServiceUIDMap = new HashMap<>();
                                    dailyServiceUIDMap.put(dailyServiceType.getKey(), userUIDMap.entrySet().iterator().next().getKey());
                                    dailyServiceUIDList.add(dailyServiceUIDMap);
                                }
                                if (dataSnapshot.getChildrenCount() == count) {
                                    count = 0;
                                    dailyServiceUIDs.onCallback(dailyServiceUIDList);
                                }
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

    /**
     * Returns a list of all categories of daily services added by user.
     *
     * @param dailyServiceCategories returns a callback containing list of categories
     */
    private void getDailyServiceCategories(DailyServiceCategories dailyServiceCategories) {
        userDataReference.child(FIREBASE_CHILD_DAILYSERVICES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dailyServiceCategoriesList = new LinkedList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dailyServiceType : dataSnapshot.getChildren()) {
                        dailyServiceCategoriesList.add(dailyServiceType.getKey());
                    }
                }
                dailyServiceCategories.onCallback(dailyServiceCategoriesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    private interface DailyServiceMapCallback {
        void onCallback(Map<String, List<NammaApartmentDailyService>> dailyServiceMap);
    }

    private interface DailyServiceCategoryUIDMap {
        void onCallback(Map<String, List<Map<String, String>>> dailyServiceUIDMap);
    }

    private interface DailyServiceCategories {
        void onCallback(List<String> dailyServiceCategoriesList);
    }

    private interface DailyServiceUIDs {
        void onCallback(List<Map<String, String>> dailyServiceUIDList);
    }

    public interface DailyServices {
        void onCallback(List<NammaApartmentDailyService> dailyServicesList);
    }

}
