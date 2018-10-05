package com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.packages;

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

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DELIVERIES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_DELIVERIES_REFERENCE;

class RetrievingPackagesList {
    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final DatabaseReference userDataReference;
    private final List<String> userUIDList;
    private int count = 0;

    /*-------------------------------------------------------------*
     * Constructor
     *-------------------------------------------------------------*/

    /**
     * @param context of the current activity.
     */
    RetrievingPackagesList(Context context) {
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
     * @param PackageListCallBack receiving result with list of all package data of userUID present in userUIDList
     *                            contain in the list of current user UID and their family members UID
     */
    public void getPackages(RetrievingPackagesList.PackageListCallBack PackageListCallBack) {
        List<NammaApartmentArrival> nammaApartmentAllPackagesList = new ArrayList<>();
        isPackageReferenceExists(packageReferenceExists -> {
            if (packageReferenceExists) {
                for (String userUID : userUIDList) {
                    getPackages(nammaApartmentPackageList -> {
                        nammaApartmentAllPackagesList.addAll(nammaApartmentPackageList);
                        count++;
                        if (count == userUIDList.size()) {
                            PackageListCallBack.onCallBack(nammaApartmentAllPackagesList);
                        }
                    }, userUID);
                }
            } else {
                PackageListCallBack.onCallBack(nammaApartmentAllPackagesList);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param PackageListCallBack receiving result with list of all package data of userUID
     * @param userUID             whose packages needs to be retrieved from firebase
     */
    private void getPackages(RetrievingPackagesList.PackageListCallBack PackageListCallBack, String userUID) {
        getPackageUIDList(packageUIDList -> getPackagesList(PackageListCallBack, packageUIDList), userUID);
    }

    /**
     * @param packageListCallBack receiving result with list of all package data whose UID is present in
     *                            packageUIDList
     * @param packageUIDList      contains the list of all packages UID whose data needs to be retrieved from firebase
     */
    private void getPackagesList(PackageListCallBack packageListCallBack, List<String> packageUIDList) {
        List<NammaApartmentArrival> nammaApartmentArrivalList = new ArrayList<>();
        if (packageUIDList.isEmpty()) {
            packageListCallBack.onCallBack(nammaApartmentArrivalList);
        } else {
            for (String packageUID : packageUIDList) {
                getPackageDataByUID(nammaApartmentArrival -> {
                    nammaApartmentArrivalList.add(nammaApartmentArrival);
                    if (nammaApartmentArrivalList.size() == packageUIDList.size()) {
                        packageListCallBack.onCallBack(nammaApartmentArrivalList);
                    }
                }, packageUID);
            }
        }
    }

    /**
     * @param packageUIDCallBack receiving result of package UID List
     * @param userUID            of the particular user whose package UID List needs to be retrieved
     *                           from firebase
     */
    private void getPackageUIDList(PackageUIDCallBack packageUIDCallBack, String userUID) {
        DatabaseReference packageListReference = userDataReference.child(FIREBASE_CHILD_DELIVERIES).child(userUID);
        packageListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> packageUIDList = new ArrayList<>();
                for (DataSnapshot packageUIDSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(packageUIDSnapshot.getValue(Boolean.class)).equals(true)) {
                        packageUIDList.add(packageUIDSnapshot.getKey());
                    }
                }
                packageUIDCallBack.onCallBack(packageUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param packageDataCallBack receiving result of the package Data
     * @param packageUID          UID of the package whose data is to be retrieved from firebase
     */
    private void getPackageDataByUID(PackageDataCallBack packageDataCallBack, String packageUID) {
        DatabaseReference packageDataReference = PRIVATE_DELIVERIES_REFERENCE.child(packageUID);
        packageDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                packageDataCallBack.onCallBack(dataSnapshot.getValue(NammaApartmentArrival.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param packagesReferenceCallBack receives boolean value true if there is at least one package for the flat
     *                                  else returns false.
     */
    private void isPackageReferenceExists(PackagesReferenceCallBack packagesReferenceCallBack) {
        DatabaseReference packageDataReference = userDataReference.child(FIREBASE_CHILD_DELIVERIES);
        packageDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                packagesReferenceCallBack.onCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface PackageListCallBack {
        void onCallBack(List<NammaApartmentArrival> nammaApartmentArrivalList);
    }

    private interface PackageUIDCallBack {
        void onCallBack(List<String> packageUIDList);
    }

    private interface PackagesReferenceCallBack {
        void onCallback(boolean packageReferenceExists);
    }

    private interface PackageDataCallBack {
        void onCallBack(NammaApartmentArrival nammaApartmentArrival);
    }
}
