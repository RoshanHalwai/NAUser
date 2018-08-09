package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.notifydigitalgate.arrivals.NammaApartmentArrival;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ACCEPT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_ACCEPTED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DELIVERIES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_GATE_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_GUESTS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_POSTAPPROVED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_REJECTED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USER_DATA;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.MESSAGE;
import static com.kirtanlabs.nammaapartments.Constants.NOTIFICATION_ID;
import static com.kirtanlabs.nammaapartments.Constants.NOTIFICATION_UID;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_DELIVERIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.REJECT_BUTTON_CLICKED;
import static com.kirtanlabs.nammaapartments.Constants.USER_UID;
import static com.kirtanlabs.nammaapartments.Constants.VISITOR_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.Constants.VISITOR_TYPE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/2/2018
 */
public class Button_listener extends BroadcastReceiver {

    private String currentUserID, message, visitorProfilePhoto, visitorType;
    private String notificationUID;
    private String notificationResponse;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getExtras() != null) {
            String action = intent.getAction();
            int notificationId = intent.getExtras().getInt(NOTIFICATION_ID);
            /*Get current user UID from Messaging Service*/
            notificationUID = intent.getExtras().getString(NOTIFICATION_UID);
            currentUserID = intent.getExtras().getString(USER_UID);
            message = intent.getExtras().getString(MESSAGE);
            visitorType = intent.getExtras().getString(VISITOR_TYPE);

            if (action.equals(ACCEPT_BUTTON_CLICKED)) {
                notificationResponse = FIREBASE_CHILD_ACCEPTED;
                visitorProfilePhoto = intent.getExtras().getString(VISITOR_PROFILE_PHOTO);
                replyNotification();
            } else {
                notificationResponse = REJECT_BUTTON_CLICKED;
                replyNotification();
            }

            /*Clear the notification once button is pressed*/
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(manager).cancel(notificationId);
        }

    }

    private void replyNotification() {
        DatabaseReference databaseReference = PRIVATE_USERS_REFERENCE.child(currentUserID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference currentUserDataReference = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CHILD_USER_DATA)
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber());

                /*Here we are setting the notification status so that Guard gets notified and allow visitor to enter the society*/
                DatabaseReference currentUserNotificationReference = currentUserDataReference
                        .child(FIREBASE_CHILD_GATE_NOTIFICATIONS)
                        .child(currentUserID)
                        .child(visitorType)
                        .child(notificationUID);

                /*We wouldn't want to store visitors data if user has clicked on reject button*/
                if (notificationResponse.equals(REJECT_BUTTON_CLICKED)) {
                    currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).setValue(FIREBASE_CHILD_REJECTED);
                    return;
                }

                currentUserNotificationReference.child(FIREBASE_CHILD_STATUS).setValue(FIREBASE_CHILD_ACCEPTED);

                /*Here we are creating reference for storing postApproved Visitors under userdata->userFlatNumber*/
                DatabaseReference currentUserVisitorReference;
                switch (visitorType) {
                    case FIREBASE_CHILD_GUESTS:
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_VISITORS)
                                .child(currentUserID);
                        break;
                    case FIREBASE_CHILD_CABS:
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_CABS)
                                .child(currentUserID);
                        break;
                    default:
                        currentUserVisitorReference = currentUserDataReference
                                .child(FIREBASE_CHILD_DELIVERIES)
                                .child(currentUserID);
                        break;
                }

                /*We do not create new UID for post approved visitors instead we use the notification UID to
                 * identify each visitor*/
                String postApprovedVisitorUID = notificationUID;
                currentUserVisitorReference.child(postApprovedVisitorUID).setValue(true);

                /*Utility Functions to get Date and Time*/
                Calendar calendar = Calendar.getInstance();
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                String formattedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
                String concatenatedDateAndTime = formattedDate + "\t\t" + " " + formattedTime;

                if (visitorType.equals(FIREBASE_CHILD_GUESTS)) {
                    String postApprovedVisitorName = getValueFromMessage(message, 2);

                    /*Creating instance of Namma Apartment Guest*/
                    DatabaseReference postApprovedVisitorData = PRIVATE_VISITORS_REFERENCE.child(postApprovedVisitorUID);
                    NammaApartmentGuest nammaApartmentGuest = new NammaApartmentGuest(postApprovedVisitorUID,
                            postApprovedVisitorName, null, concatenatedDateAndTime, currentUserID, FIREBASE_CHILD_POSTAPPROVED);
                    nammaApartmentGuest.setStatus(ENTERED);
                    nammaApartmentGuest.setProfilePhoto(visitorProfilePhoto);
                    postApprovedVisitorData.setValue(nammaApartmentGuest);
                } else if (visitorType.equals(FIREBASE_CHILD_CABS)) {
                    String cabNumber = TextUtils.split(message, " ")[3];
                    /*Creating instance of Namma Apartment Cab*/
                    DatabaseReference postApprovedVisitorData = PRIVATE_CABS_REFERENCE.child(postApprovedVisitorUID);
                    NammaApartmentArrival nammaApartmentArrival = new NammaApartmentArrival(
                            cabNumber, concatenatedDateAndTime, "2 hrs", currentUserID, FIREBASE_CHILD_POSTAPPROVED);
                    nammaApartmentArrival.setStatus(ENTERED);
                    postApprovedVisitorData.setValue(nammaApartmentArrival);
                } else {
                    String packageVendor = getValueFromMessage(message, 3);
                    /*Creating instance of Namma Apartment Cab*/
                    DatabaseReference postApprovedVisitorData = PRIVATE_DELIVERIES_REFERENCE.child(postApprovedVisitorUID);
                    NammaApartmentArrival nammaApartmentArrival = new NammaApartmentArrival(
                            packageVendor, concatenatedDateAndTime, "2 hrs", currentUserID, FIREBASE_CHILD_POSTAPPROVED);
                    nammaApartmentArrival.setStatus(ENTERED);
                    postApprovedVisitorData.setValue(nammaApartmentArrival);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private String getValueFromMessage(String message, int index) {
        String separatedVisitorName[] = TextUtils.split(message, " ");
        StringBuilder postApprovedVisitorName = new StringBuilder();
        for (int i = index; i < separatedVisitorName.length; i++) {
            postApprovedVisitorName.append(separatedVisitorName[i]);
            if (separatedVisitorName[i + 1].equals("wants")) {
                break;
            }
            postApprovedVisitorName.append(" ");
        }
        return postApprovedVisitorName.toString();
    }

}
