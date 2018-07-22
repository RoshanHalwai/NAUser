package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_AVAILABLE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.SOCIETYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class SocietyServices extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonImmediately,
            R.id.buttonMorningSlot,
            R.id.buttonNoonSlot,
            R.id.buttonEveningSlot};
    private int screenTitle;
    private String[] problemsList;
    private String selectedProblem;
    private Button selectedButton;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_society_services;
    }

    @Override
    protected int getActivityTitle() {
        screenTitle = getIntent().getIntExtra(SCREEN_TITLE, 0);
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textSelectProblem = findViewById(R.id.textSelectProblem);
        Spinner spinnerSelectProblem = findViewById(R.id.spinnerSelectProblem);
        TextView textSelectSlot = findViewById(R.id.textSelectSlot);
        Button buttonImmediately = findViewById(R.id.buttonImmediately);
        Button buttonMorningSlot = findViewById(R.id.buttonMorningSlot);
        Button buttonNoonSlot = findViewById(R.id.buttonNoonSlot);
        Button buttonEveningSlot = findViewById(R.id.buttonEveningSlot);
        Button buttonRequestService = findViewById(R.id.buttonRequestService);

        /*Setting font for all the views*/
        textSelectProblem.setTypeface(setLatoBoldFont(this));
        textSelectSlot.setTypeface(setLatoBoldFont(this));
        buttonImmediately.setTypeface(setLatoRegularFont(this));
        buttonMorningSlot.setTypeface(setLatoRegularFont(this));
        buttonNoonSlot.setTypeface(setLatoRegularFont(this));
        buttonEveningSlot.setTypeface(setLatoRegularFont(this));
        buttonRequestService.setTypeface(setLatoLightFont(this));

        // We want Button Immediately should be selected on start of activity
        selectButton(R.id.buttonImmediately);

        // We display list of issues according to screen title
        switch (screenTitle) {
            case R.string.plumber:
                buttonRequestService.setText(R.string.request_plumber);
                problemsList = getResources().getStringArray(R.array.plumbing_issues_list);
                break;
            case R.string.carpenter:
                buttonRequestService.setText(R.string.request_carpenter);
                problemsList = getResources().getStringArray(R.array.carpentry_issues_list);
                break;
            case R.string.electrician:
                buttonRequestService.setText(R.string.request_electrician);
                problemsList = getResources().getStringArray(R.array.electrical_issues_list);
        }

        /*Setting font for all the items in the list*/
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, problemsList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textProblem = view.findViewById(android.R.id.text1);
                textProblem.setTypeface(Constants.setLatoRegularFont(SocietyServices.this));
                return view;
            }
        };
        //Setting adapter to Spinner view
        spinnerSelectProblem.setAdapter(adapter);

        /*Setting event for views*/
        buttonImmediately.setOnClickListener(this);
        buttonMorningSlot.setOnClickListener(this);
        buttonNoonSlot.setOnClickListener(this);
        buttonEveningSlot.setOnClickListener(this);
        buttonRequestService.setOnClickListener(this);
        spinnerSelectProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedProblem = problemsList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonImmediately:
                selectButton(R.id.buttonImmediately);
                break;
            case R.id.buttonMorningSlot:
                selectButton(R.id.buttonMorningSlot);
                break;
            case R.id.buttonNoonSlot:
                selectButton(R.id.buttonNoonSlot);
                break;
            case R.id.buttonEveningSlot:
                selectButton(R.id.buttonEveningSlot);
                break;
            case R.id.buttonRequestService:
                storeSocietyServiceDetails();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Method to change colour of selected buttons
     *
     * @param id - of selected button
     */
    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                selectedButton = button;
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * Store the details of Society Services to Firebase
     */
    private void storeSocietyServiceDetails() {
        //Get the societyServiceUID
        DatabaseReference societyServiceNotificationReference = SOCIETYSERVICENOTIFICATION_REFERENCE;
        String societyServiceUID = societyServiceNotificationReference.push().getKey();

        //Get data of Society Service (Plumber/Carpenter/Electrician)
//        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
//        String userUID = nammaApartmentUser.getUID();
//        String timeSlot = selectedButton.getText().toString();
//        String societyServiceType = getString(screenTitle).toLowerCase();

        //Store Society Service notification details under a new parent named societyServiceNotifications
        //TODO: Notification UID has been hardcoded. It will get updated once notification is triggered.
//        NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(selectedProblem, timeSlot,
//                userUID, societyServiceType, "abc", "pending", societyServiceUID);
//        societyServiceNotificationReference.child("abc").setValue(nammaApartmentSocietyServices);

        //Store Society Service details in userData
        DatabaseReference societyServiceUserDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        societyServiceUserDataReference.child(societyServiceUID).setValue(true); //TODO: Notification UID to be inserted in place of hardcoded UID.

        //Store Society Service details under a new parent named societyServices. Reiterate through all available plumber/carpenter/electrician.
//        DatabaseReference societyServiceAvailableReference = SOCIETYSERVICES_REFERENCE.child(societyServiceType).child(FIREBASE_CHILD_AVAILABLE);
//        societyServiceAvailableReference.child(societyServiceUID).setValue("tokenId");

        /*TODO: Uncomment these when 'unavailable' Society Service feature is implemented. Right now we are just showing 'Available'*/
        /*DatabaseReference societyServiceUnavailableReference = SOCIETYSERVICES_REFERENCE.child(societyServiceType).child(FIREBASE_CHILD_UNAVAILABLE);
        societyServiceUnavailableReference.child(societyServiceUID).setValue(true);*/

        //Mapping Society Service Mobile Number with UID in societyServices->societyServiceType->data->all
//        DatabaseReference societyServiceDataReference = SOCIETYSERVICES_REFERENCE.child(societyServiceType).child("data");
//        societyServiceDataReference.child(FIREBASE_CHILD_ALL).child("9876543210").setValue(societyServiceUID);

        //Storing details of Society Service inside societyServices->societyServiceType->data->private
        //TODO: Details of Plumber/Electrician/Carpenter will be stored once the Society Service data is available.
//        societyServiceDataReference.child(FIREBASE_CHILD_PRIVATE).child(societyServiceUID).setValue("details");

        //Create a unique ID for every push notifications
//        DatabaseReference societyServiceNotificationsReference = societyServiceDataReference
//                .child(FIREBASE_CHILD_PRIVATE)
//                .child("notifications")
//                .child("abc")
//                .push();
//
//        societyServiceNotificationsReference.setValue("accepted");
    }

}