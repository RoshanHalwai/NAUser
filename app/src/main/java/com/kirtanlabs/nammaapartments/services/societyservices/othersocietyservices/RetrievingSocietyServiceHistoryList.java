package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/3/2018
 */

public class RetrievingSocietyServiceHistoryList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingSocietyServiceHistoryList(Context mCtx) {
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void getSocietyServiceNotificationUIDList(String societyServiceType, NotificationUIDCallback notificationUIDCallback) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) mCtx.getApplicationContext());
        DatabaseReference userDataReference = nammaApartmentsGlobal.getUserDataReference().child(Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION)
                .child(societyServiceType);
        userDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> notificationUIDList = new ArrayList<>();
                    for (DataSnapshot notificationUIDDataSnapshot : dataSnapshot.getChildren()) {
                        notificationUIDList.add(notificationUIDDataSnapshot.getKey());
                    }
                    notificationUIDCallback.onCallBack(notificationUIDList);
                } else {
                    notificationUIDCallback.onCallBack(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getNotificationDataList(String societyServiceType, NotificationDataListCallback notificationDataListCallback) {
        getSocietyServiceNotificationUIDList(societyServiceType, societyServiceNotificationUIDList -> {
            if (societyServiceNotificationUIDList != null) {
                List<NammaApartmentSocietyServices> notificationDataList = new ArrayList<>();

                for (String notificationUID : societyServiceNotificationUIDList) {
                    DatabaseReference notificationData = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                            .child(notificationUID);
                    notificationData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentSocietyServices nammaApartmentSocietyServices = dataSnapshot.getValue(NammaApartmentSocietyServices.class);
                            notificationDataList.add(nammaApartmentSocietyServices);

                            notificationDataListCallback.onCallBack(notificationDataList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                notificationDataListCallback.onCallBack(null);
            }
        });
    }

    public void getSocietyServiceRequestStatus(String notificationUid, SocietyServiceRequestStatus societyServiceRequestStatus) {
        DatabaseReference statusReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                .child(notificationUid).child(Constants.FIREBASE_CHILD_STATUS);
        statusReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.getValue(String.class);
                societyServiceRequestStatus.onCallBack(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface NotificationUIDCallback {
        void onCallBack(List<String> societyServiceNotificationUIDList);
    }

    public interface NotificationDataListCallback {
        void onCallBack(List<NammaApartmentSocietyServices> societyServiceNotificationDataList);
    }

    public interface SocietyServiceRequestStatus {
        void onCallBack(String status);
    }
}
