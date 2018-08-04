package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;

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

    private DatabaseReference userDataReference;

    public void getHistoryNotificationDataList(HistoryNotificationDataListCallback historyNotificationDataListCallback) {
        getNotificationUIDList(societyServiceNotificationUIDList -> getNotificationDataList(societyServiceNotificationUIDList,
                societyServiceNotificationList -> historyNotificationDataListCallback.onCallback(societyServiceNotificationList)));
    }

    private void getNotificationUIDList(NotificationUIDCallback notificationUIDCallback) {
        DatabaseReference notificationUIDReference = userDataReference.child(Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        notificationUIDReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> notificationUIDList = new ArrayList<>();
                for (DataSnapshot notificationUIDSnapshot : dataSnapshot.getChildren()) {
                    if (notificationUIDSnapshot.getValue(Boolean.class).equals(true)) {
                        notificationUIDList.add(notificationUIDSnapshot.getKey());
                    }
                }
                notificationUIDCallback.onCallBack(notificationUIDList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * Returns a list of Notification data by taking a list of notification UID as input
     *
     * @param notificationUIDList          whose notification data needs to be returned
     * @param notificationDataListCallback callback to return list of notification data
     */
    private void getNotificationDataList(List<String> notificationUIDList, NotificationDataListCallback notificationDataListCallback) {
        List<NammaApartmentSocietyServices> notificationDataList = new ArrayList<>();
        for (String notificationUID : notificationUIDList) {
            getNotificationData(notificationUID, societyServiceNotification -> {
                notificationDataList.add(societyServiceNotification);
                if (notificationUIDList.size() == notificationDataList.size()) {
                    notificationDataListCallback.onCallback(notificationDataList);
                }
            });
        }
    }

    private void getNotificationData(String notificationUID, NotificationDataCallback notificationDataCallback) {
        DatabaseReference notificationUIDDataReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(notificationUID);
        notificationUIDDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("status").getValue(String.class);
                if (status.equals("completed")) {
                    notificationDataCallback.onCallBack(dataSnapshot.getValue(NammaApartmentSocietyServices.class));
                }
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

    public interface NotificationDataCallback {
        void onCallBack(NammaApartmentSocietyServices societyServiceNotificationDataList);
    }

    public interface NotificationDataListCallback {
        void onCallback(List<NammaApartmentSocietyServices> societyServiceNotificationList);
    }

    public interface HistoryNotificationDataListCallback {
        void onCallback(List<NammaApartmentSocietyServices> historyNotificationDataList);
    }
}
