package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.emergency;


import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import static com.kirtanlabs.nammaapartments.Constants.ALARM_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_EMERGENCIES;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_EMERGENCY_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_EMERGENCIES_REFERENCE;

public class RaiseAlarm extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textAlertRaised;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_raise_alarm;
    }

    @Override
    protected int getActivityTitle() {
        return getIntent().getIntExtra(Constants.ALARM_TYPE, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textCreateAlert = findViewById(R.id.textCreateAlert);
        textAlertRaised = findViewById(R.id.textAlertRaised);
        ImageButton buttonCreateAlert = findViewById(R.id.buttonCreateAlert);

        /*Setting font for all the views*/
        textCreateAlert.setTypeface(Constants.setLatoBoldFont(this));
        textAlertRaised.setTypeface(Constants.setLatoBoldFont(this));

        /*Setting event for image button*/
        buttonCreateAlert.setOnClickListener(v -> storeEmergencyDetails());
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user is trying to raise any alarm for any emergency type.
     */
    private void storeEmergencyDetails() {
        //Store emergency  uid and value under userdata->private->currentUserFlat
        DatabaseReference digitalGateUIDReference = ALL_USERS_REFERENCE.child(FIREBASE_CHILD_EMERGENCIES);
        String digitalGateUID = digitalGateUIDReference.push().getKey();
        DatabaseReference digitalGateReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_EMERGENCIES);
        digitalGateReference.child(digitalGateUID).setValue(true);

        //Store the details of emergency in emergencies->public->uid
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String flatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();
        DatabaseReference emergencyReference = PRIVATE_EMERGENCY_REFERENCE
                .child(FIREBASE_CHILD_ALL)
                .child(flatNumber);
        emergencyReference.child(digitalGateUID).setValue(true);
        DatabaseReference emergencyDetailsReference = PUBLIC_EMERGENCIES_REFERENCE.child(digitalGateUID);
        emergencyDetailsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String flatMemberName = currentNammaApartmentUser.getPersonalDetails().getFullName();
                String flatMemberMobileNo = currentNammaApartmentUser.getPersonalDetails().getPhoneNumber();
                String flatMemberBlock = currentNammaApartmentUser.getFlatDetails().getApartmentName();
                NammaApartmentEmergency nammaApartmentEmergency = new NammaApartmentEmergency(ALARM_TYPE, flatMemberName, flatMemberMobileNo, flatMemberBlock, flatNumber);
                emergencyDetailsReference.setValue(nammaApartmentEmergency);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
