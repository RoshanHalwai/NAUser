package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PRIVATE;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 7/26/2018
 */

public class AwaitingResponse extends BaseActivity {

    /*----------------------------------------------
     *Private Members
     *-----------------------------------------------*/

    private LinearLayout layoutAwaitingResponse, layoutAcceptedResponse;
    private TextView textSocietyServiceNameValue, textMobileNumberValue;

    /*----------------------------------------------------
     *  Overriding BaseActivity Objects
     *----------------------------------------------------*/

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

        /*Getting Id's for all the views*/
        layoutAwaitingResponse = findViewById(R.id.layoutAwaitingResponse);
        layoutAcceptedResponse = findViewById(R.id.layoutAcceptedResponse);
        TextView textNotificationSent = findViewById(R.id.textNotificationSent);
        TextView textSocietyServiceAcceptedRequest = findViewById(R.id.textSocietyServiceAcceptedRequest);
        TextView textSocietyServiceName = findViewById(R.id.textSocietyServiceName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        textSocietyServiceNameValue = findViewById(R.id.textSocietyServiceNameValue);
        textMobileNumberValue = findViewById(R.id.textMobileNumberValue);
        TextView textPlumberResponse = findViewById(R.id.textPlumberResponse);

        /*Setting font for all the views*/
        textNotificationSent.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceName.setTypeface(Constants.setLatoRegularFont(this));
        textMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        textSocietyServiceNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textMobileNumberValue.setTypeface(Constants.setLatoBoldFont(this));
        textSocietyServiceAcceptedRequest.setTypeface(Constants.setLatoBoldFont(this));
        textPlumberResponse.setTypeface(Constants.setLatoBoldFont(this));

        String societyServiceNameTitle = getString(R.string.name) + ":";
        textSocietyServiceName.setText(societyServiceNameTitle);
        String societyServiceMobileTitle = getString(R.string.mobile) + ":";
        textMobileNumber.setText(societyServiceMobileTitle);

        showProgressIndicator();

        if (getIntent() != null && getIntent().getStringExtra("NotificationUID") != null) {
            String notificationUID = getIntent().getStringExtra("NotificationUID");
            String societyServiceType = getIntent().getStringExtra("societyServiceType");

            /*Getting the reference till 'notificationUID' in societyServices to set 'status' in Firebase*/
            DatabaseReference societyServiceReference = Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE.child(notificationUID).child("takenBy");
            societyServiceReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        String societyServiceUID = dataSnapshot.getValue(String.class);
                        DatabaseReference societyServiceDataReference = Constants.SOCIETYSERVICES_REFERENCE
                                .child(societyServiceType)
                                .child(FIREBASE_CHILD_PRIVATE)
                                .child(Constants.FIREBASE_CHILD_DATA)
                                .child(Objects.requireNonNull(societyServiceUID));
                        societyServiceDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                hideProgressIndicator();
                                layoutAwaitingResponse.setVisibility(View.GONE);
                                layoutAcceptedResponse.setVisibility(View.VISIBLE);

                                String societyServiceName = dataSnapshot.child(Constants.FIREBASE_CHILD_FULLNAME).getValue(String.class);
                                String societyServiceMobileNumber = dataSnapshot.child(Constants.FIREBASE_CHILD_MOBILE_NUMBER).getValue(String.class);
                                textSocietyServiceNameValue.setText(societyServiceName);
                                textMobileNumberValue.setText(societyServiceMobileNumber);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
