package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.navigationdrawer.help.pojo.Support;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.DECLINED;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SCRAP_COLLECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SCRAP_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/3/2018
 */

public class RetrievingSocietyServiceHistoryList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private int count = 0;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingSocietyServiceHistoryList(Context mCtx) {
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets all the society service notification UID's of that particular society service type
     * raised by user by going into their particular user data.
     *
     * @param societyServiceType      consists of which society service notification
     * @param notificationUIDCallback callback to return list which contains all society service notification UID's
     *                                of that particular society service type.
     */
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
                    if (notificationUIDList.size() == dataSnapshot.getChildrenCount()) {
                        notificationUIDCallback.onCallBack(notificationUIDList);
                    }
                } else {
                    notificationUIDCallback.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method gets all the support UID's raised by user by going into their particular user data.
     *
     * @param notificationUIDCallback callback to return list which contains all support UID's of that
     *                                particular user.
     */
    private void getSupportUIDList(NotificationUIDCallback notificationUIDCallback) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) mCtx.getApplicationContext());
        DatabaseReference userDataReference = nammaApartmentsGlobal.getUserDataReference().child(Constants.FIREBASE_CHILD_SUPPORT);
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
                    notificationUIDCallback.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Public Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets all the society service notifications data related to that particular notification.
     *
     * @param societyServiceType           consists of which society service notification.
     * @param notificationDataListCallback callback to return list which contain data of all society service notification UID's
     *                                     of that particular society service type.
     */
    public void getNotificationDataList(String societyServiceType, NotificationDataListCallback notificationDataListCallback) {
        getSocietyServiceNotificationUIDList(societyServiceType, societyServiceNotificationUIDList -> {
            if (!societyServiceNotificationUIDList.isEmpty()) {
                List<NammaApartmentSocietyServices> notificationDataList = new ArrayList<>();

                for (String notificationUID : societyServiceNotificationUIDList) {
                    DatabaseReference notificationData = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE
                            .child(notificationUID);
                    notificationData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            count++;
                            NammaApartmentSocietyServices nammaApartmentSocietyServices = dataSnapshot.getValue(NammaApartmentSocietyServices.class);
                            String status = Objects.requireNonNull(nammaApartmentSocietyServices).getStatus();
                            if (Objects.requireNonNull(societyServiceType).equals(FIREBASE_CHILD_SCRAP_COLLECTION)) {
                                String scrapType = dataSnapshot.child(FIREBASE_CHILD_SCRAP_TYPE).getValue(String.class);
                                Objects.requireNonNull(nammaApartmentSocietyServices).setScrapType(scrapType);
                            }
                            /*Adding those items in the list whose status is not declined*/
                            if (!status.equals(DECLINED)) {
                                notificationDataList.add(nammaApartmentSocietyServices);
                            }

                            if (count == societyServiceNotificationUIDList.size()) {
                                count = 0;
                                notificationDataListCallback.onCallBack(notificationDataList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                notificationDataListCallback.onCallBack(new ArrayList<>());
            }
        });
    }

    /**
     * This method returns the society service request status of that particular society service notification.
     *
     * @param notificationUid             contains society service notification UID of that particular user.
     * @param societyServiceRequestStatus consists status of that particular notification UID.
     */
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


    /**
     * This method gets all the data related to that particular support notification.
     *
     * @param supportDataListCallBack callback to return list which contain data of all support UID's
     *                                of that particular user.
     */
    public void getSupportDataList(SupportDataListCallBack supportDataListCallBack) {
        getSupportUIDList(supportNotificationUIDList -> {
            if (!supportNotificationUIDList.isEmpty()) {
                List<Support> supportDataList = new ArrayList<>();
                for (String notificationUID : supportNotificationUIDList) {
                    DatabaseReference supportData = Constants.SUPPORT_REFERENCE
                            .child(notificationUID);
                    supportData.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Support support = dataSnapshot.getValue(Support.class);
                            supportDataList.add(support);
                            if (supportNotificationUIDList.size() == supportDataList.size()) {
                                supportDataListCallBack.onCallBack(supportDataList);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                supportDataListCallBack.onCallBack(new ArrayList<>());
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    interface NotificationUIDCallback {
        void onCallBack(List<String> societyServiceNotificationUIDList);
    }

    public interface NotificationDataListCallback {
        void onCallBack(List<NammaApartmentSocietyServices> societyServiceNotificationDataList);
    }

    public interface SocietyServiceRequestStatus {
        void onCallBack(String status);
    }

    public interface SupportDataListCallBack {
        void onCallBack(List<Support> supportList);
    }
}
