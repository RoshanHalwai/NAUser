package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_POSTAPPROVEDVISITORS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PROFILE_PHOTO;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_STATUS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_USER_DATA;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.POSTAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/2/2018
 */
public class Button_listener extends BroadcastReceiver {

    private String currentUserID, visitorName, visitorProfilePhoto;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String action = intent.getAction();
            String notificationUID = Objects.requireNonNull(intent.getExtras()).getString("Notification_UID");
            int notificationId = intent.getExtras().getInt("Notification_Id");

            /*Get current user UID from Messaging Service*/
            currentUserID = intent.getExtras().getString("User_UID");
            visitorName = intent.getExtras().getString("Message");

            if (action.equals("accept_button_clicked")) {
                replyNotification(notificationUID, "Accepted");
                Log.d("Notification Test", action);
                Log.d("notificationUID", notificationUID);
            } else {
                replyNotification(notificationUID, "Rejected");
                Log.d("Notification Test", action);
                Log.d("notificationUID", notificationUID);
            }

            /*Clear the notification once button is pressed*/
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Objects.requireNonNull(manager).cancel(notificationId);
        }

    }

    private void replyNotification(final String notificationUID, final String status) {
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
                /*Here we are setting the notification status under current userdata->userFlatNumber->notifications->notificationId->status*/
                DatabaseReference currentUserNotificationReference = currentUserDataReference
                        .child(FIREBASE_CHILD_NOTIFICATIONS)
                        .child(currentUserID);
                currentUserNotificationReference.child(notificationUID).child(FIREBASE_CHILD_STATUS).setValue(status);
                /*Here we are creating reference for storing postApproved Visitors under userdata->userFlatNumber*/
                DatabaseReference currentUserVisitorReference = currentUserDataReference
                        .child(FIREBASE_CHILD_VISITORS)
                        .child(currentUserID)
                        .child(FIREBASE_CHILD_POSTAPPROVEDVISITORS);
                String postApprovedVisitorUID = currentUserVisitorReference.push().getKey();
                currentUserVisitorReference.child(postApprovedVisitorUID).setValue(true);
                /*Here we are creating postApprovedVisitors Data Storage Reference under visitors->postApprovedVisitors*/
                DatabaseReference postApprovedVisitorData = POSTAPPROVED_VISITORS_REFERENCE.child(postApprovedVisitorUID);
                Calendar calendar = Calendar.getInstance();
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                String formattedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
                String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", currentHour, currentMinute);
                String concatenatedDateAndTime = formattedDate + "\t\t" + " " + formattedTime;
                String separatedVisitorName[] = TextUtils.split(visitorName, " ");
                String postApprovedVisitorName = separatedVisitorName[0];
                NammaApartmentGuest nammaApartmentGuest = new NammaApartmentGuest(postApprovedVisitorUID,
                        postApprovedVisitorName, null, concatenatedDateAndTime, currentUserID);
                nammaApartmentGuest.setStatus(ENTERED);
                /*Here we are creating reference for storing profile photo under postApprovedVisitors*/
                DatabaseReference profilePhotoReference = currentUserDataReference.child(FIREBASE_CHILD_NOTIFICATIONS)
                        .child(currentUserID)
                        .child(notificationUID)
                        .child(FIREBASE_CHILD_PROFILE_PHOTO);
                profilePhotoReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        visitorProfilePhoto = dataSnapshot.getValue(String.class);
                        nammaApartmentGuest.setProfilePhoto(visitorProfilePhoto);
                        postApprovedVisitorData.setValue(nammaApartmentGuest);
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
