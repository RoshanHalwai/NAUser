package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;
import com.kirtanlabs.nammaapartments.userpojo.UserFlatDetails;

import java.util.Objects;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 7/2/2018
 */
public class Button_listener extends BroadcastReceiver {

    private String currentUserID;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null) {
            String action = intent.getAction();
            String notificationUID = Objects.requireNonNull(intent.getExtras()).getString("Notification_UID");
            int notificationId = intent.getExtras().getInt("Notification_Id");

            /*Get current user UID from Messaging Service*/
            currentUserID = intent.getExtras().getString("User_UID");

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
        DatabaseReference databaseReference = Constants.PRIVATE_USERS_REFERENCE.child(currentUserID);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserFlatDetails userFlatDetails = Objects.requireNonNull(dataSnapshot.getValue(NammaApartmentUser.class)).getFlatDetails();
                DatabaseReference userDataReference = FirebaseDatabase.getInstance().getReference().child("userData")
                        .child(Constants.FIREBASE_CHILD_PRIVATE)
                        .child(userFlatDetails.getCity())
                        .child(userFlatDetails.getSocietyName())
                        .child(userFlatDetails.getApartmentName())
                        .child(userFlatDetails.getFlatNumber())
                        .child("notifications")
                        .child(currentUserID);
                userDataReference.child(notificationUID).child("status").setValue(status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
