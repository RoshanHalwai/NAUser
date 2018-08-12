package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VEHICLES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_VEHICLES_REFERENCE;

public class RetrievingVehiclesList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private DatabaseReference userDataReference;

    /*-------------------------------------------------------------*
     * Constructor
     *-------------------------------------------------------------*/

    /**
     * @param context of the current activity.
     */
    public RetrievingVehiclesList(Context context) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) context.getApplicationContext());
        userDataReference = nammaApartmentsGlobal.getUserDataReference();
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * @param vehicleListCallback receiving result with list of all vehicle data of userUID present in userUIDList
     *                            contain in the list of current user UID and their family members UID
     */
    public void getVehicles(VehicleListCallback vehicleListCallback) {
        List<Vehicle> nammaApartmentAllVehiclesList = new ArrayList<>();
        isVehicleReferenceExists(vehicleReferenceExists -> {
            if (vehicleReferenceExists) {
                getVehiclesUtil(nammaApartmentVehicleList -> {
                    nammaApartmentAllVehiclesList.addAll(nammaApartmentVehicleList);
                    vehicleListCallback.onCallBack(nammaApartmentAllVehiclesList);
                });
            } else {
                vehicleListCallback.onCallBack(nammaApartmentAllVehiclesList);
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * @param vehicleListCallback receiving result with list of all vehicle data
     */
    private void getVehiclesUtil(VehicleListCallback vehicleListCallback) {
        getVehicleUIDList(vehicleUIDList -> getVehiclesList(vehicleListCallback, vehicleUIDList));
    }

    /**
     * @param vehicleListCallback receiving result with list of all vehicle data whose UID is present in
     *                            vehicleUIDList
     * @param vehicleUIDList      contains the list of all vehicles UID whose data needs to be retrieved from firebase
     */
    private void getVehiclesList(VehicleListCallback vehicleListCallback, List<String> vehicleUIDList) {
        List<Vehicle> VehicleList = new ArrayList<>();
        if (vehicleUIDList.isEmpty()) {
            vehicleListCallback.onCallBack(VehicleList);
        } else {
            for (String vehicleUID : vehicleUIDList) {
                getVehicleDataByUID(Vehicle -> {
                    VehicleList.add(Vehicle);
                    if (VehicleList.size() == vehicleUIDList.size()) {
                        vehicleListCallback.onCallBack(VehicleList);
                    }
                }, vehicleUID);
            }
        }
    }

    /**
     * @param vehiclesUIDListCallback receiving result of Vehicle UID List
     */
    private void getVehicleUIDList(VehiclesUIDListCallback vehiclesUIDListCallback) {
        DatabaseReference vehicleListReference = userDataReference.child(FIREBASE_CHILD_VEHICLES);
        vehicleListReference.keepSynced(true);
        vehicleListReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> vehicleUIDList = new ArrayList<>();
                for (DataSnapshot vehicleUIDSnapshot : dataSnapshot.getChildren()) {
                    if (Objects.requireNonNull(vehicleUIDSnapshot.getValue(Boolean.class)).equals(true)) {
                        vehicleUIDList.add(vehicleUIDSnapshot.getKey());
                    }
                }
                vehiclesUIDListCallback.onCallBack(vehicleUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param vehicleDataCallback receiving result of the Vehicle Data
     * @param vehicleUID          UID of the Vehicle whose data is to be retrieved from firebase
     */
    private void getVehicleDataByUID(VehicleDataCallback vehicleDataCallback, String vehicleUID) {
        DatabaseReference vehicleDataReference = PRIVATE_VEHICLES_REFERENCE.child(vehicleUID);
        vehicleDataReference.keepSynced(true);
        vehicleDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vehicleDataCallback.onCallBack(dataSnapshot.getValue(Vehicle.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * @param vehiclesReferenceCallback receives boolean value true if there is at least one vehicle for the flat
     *                                  else returns false.
     */
    private void isVehicleReferenceExists(VehiclesReferenceCallback vehiclesReferenceCallback) {
        DatabaseReference vehicleDataReference = userDataReference.child(FIREBASE_CHILD_VEHICLES);
        vehicleDataReference.keepSynced(true);
        vehicleDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vehiclesReferenceCallback.onCallback(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface VehicleListCallback {
        void onCallBack(List<Vehicle> vehiclesList);
    }

    private interface VehiclesUIDListCallback {
        void onCallBack(List<String> vehicleUIDList);
    }

    private interface VehiclesReferenceCallback {
        void onCallback(boolean vehicleReferenceExists);
    }

    private interface VehicleDataCallback {
        void onCallBack(Vehicle vehicle);
    }
}
