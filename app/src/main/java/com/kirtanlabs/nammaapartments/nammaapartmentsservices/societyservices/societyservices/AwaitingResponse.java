package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PRIVATE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class AwaitingResponse extends BaseActivity {

    TextView textUserResponse;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_awaiting_response;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.society_services;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textUserResponse = findViewById(R.id.textUserResponse);

        if (getIntent() != null && getIntent().getStringExtra("NotificationUID") != null) {
            String notificationUID = getIntent().getStringExtra("NotificationUID");
            String societyServiceType = getIntent().getStringExtra("societyServiceType");
            String societyServiceUID = getIntent().getStringExtra("societyServiceUID");

            /*Getting the reference till 'notificationUID' in societyServices to set 'status' in Firebase*/
            DatabaseReference societyServiceReference = Constants.ALL_SOCIETYSERVICE_REFERENCE.child(societyServiceType)
                    .child(FIREBASE_CHILD_DATA).child(FIREBASE_CHILD_PRIVATE).child(societyServiceUID).child("notifications").child(notificationUID);
            societyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    societyServiceReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                hideProgressIndicator();

                                /*Setting the value of field 'status' under notifications in Firebase*/
                                textUserResponse.setText(Objects.requireNonNull(dataSnapshot.getValue()).toString());
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
    }
}
