package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.emergency;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import static com.kirtanlabs.nammaapartments.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_EMERGENCIES;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_EMERGENCY_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_EMERGENCIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class RaiseAlarm extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private int alarmType;
    private TextView textAlertRaised;
    private String emergencyType;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_raise_alarm;
    }

    @Override
    protected int getActivityTitle() {
        alarmType = getIntent().getIntExtra(Constants.ALARM_TYPE, 0);
        return alarmType;
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
        textCreateAlert.setTypeface(setLatoBoldFont(this));
        textAlertRaised.setTypeface(setLatoBoldFont(this));

        /*Setting event for image button*/
        buttonCreateAlert.setOnClickListener(v -> showEmergencyDialog());
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked when user presses on bell icon an emergency dialog pops up.
     */
    private void showEmergencyDialog() {
        AlertDialog.Builder alertEmergencyDialog = new AlertDialog.Builder(this);
        View emergencyDialog = View.inflate(this, R.layout.layout_emergency_dialog, null);

        /*Getting Id's for all the views*/
        TextView textEmergencyMessage = emergencyDialog.findViewById(R.id.textEmergencyMessage);
        TextView buttonOk = emergencyDialog.findViewById(R.id.buttonOk);
        TextView buttonCancel = emergencyDialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textEmergencyMessage.setTypeface(setLatoBoldFont(this));
        buttonOk.setTypeface(setLatoRegularFont(this));
        buttonCancel.setTypeface(setLatoRegularFont(this));

        /*Creating and setting Emergency Dialog*/
        alertEmergencyDialog.setView(emergencyDialog);
        Dialog dialog = alertEmergencyDialog.create();
        new Dialog(this);
        dialog.show();

        /*Setting OnClick Listeners to the views*/
        buttonCancel.setOnClickListener(v -> dialog.cancel());
        buttonOk.setOnClickListener(v -> {
            storeEmergencyDetails();
            textAlertRaised.setVisibility(View.VISIBLE);
            dialog.cancel();
        });
    }
    /**
     * This method gets invoked when user is trying to raise any alarm for any emergency type.
     */
    private void storeEmergencyDetails() {
        //Store emergency  uid and value under userdata->private->currentUserFlat
        DatabaseReference emergencyUIDReference = ALL_USERS_REFERENCE.child(FIREBASE_CHILD_EMERGENCIES);
        String emergencyUID = emergencyUIDReference.push().getKey();
        DatabaseReference userEmergencyDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_EMERGENCIES);
        userEmergencyDataReference.child(emergencyUID).setValue(true);

        //Creating an instance of NammaApartment User class and getting the values from it.
        NammaApartmentUser currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String flatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();
        String flatMemberName = currentNammaApartmentUser.getPersonalDetails().getFullName();
        String flatMemberMobileNo = currentNammaApartmentUser.getPersonalDetails().getPhoneNumber();
        String flatMemberApartmentName = currentNammaApartmentUser.getFlatDetails().getApartmentName();

        //Store the details of emergency in emergencies->private->uid
        DatabaseReference emergencyReference = PRIVATE_EMERGENCY_REFERENCE
                .child(FIREBASE_CHILD_ALL)
                .child(flatNumber);
        emergencyReference.child(emergencyUID).setValue(true);

        //Store the details of emergency in emergencies->public->uid
        DatabaseReference emergencyDetailsReference = PUBLIC_EMERGENCIES_REFERENCE.child(emergencyUID);
        //Here Based on the screen title we are getting proper emergency type
        switch (alarmType) {
            case R.string.medical_emergency:
                emergencyType = getString(R.string.emergency_type_medical);
                break;
            case R.string.raise_fire_alarm:
                emergencyType = getString(R.string.emergency_type_fire_alarm);
                break;
            case R.string.raise_theft_alarm:
                emergencyType = getString(R.string.emergency_type_theft_alarm);
                break;
        }
        NammaApartmentEmergency nammaApartmentEmergency = new NammaApartmentEmergency(emergencyType, flatMemberName, flatMemberMobileNo, flatMemberApartmentName, flatNumber);
        emergencyDetailsReference.setValue(nammaApartmentEmergency);
    }

}
