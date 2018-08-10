package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartments.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SELECT_SOCIETY_SERVICE_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class SocietyServicesHome extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonImmediately,
            R.id.buttonMorningSlot,
            R.id.buttonNoonSlot,
            R.id.buttonEveningSlot};
    private int screenTitle;
    private String problem;
    private String societyServiceType;
    private Button selectedButton, buttonDryWaste, buttonWetWaste;
    private EditText editTextSelectProblem;

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
        TextView textSelectSlot = findViewById(R.id.textSelectSlot);
        TextView textSelectProblem = findViewById(R.id.textSelectProblem);
        Button buttonImmediately = findViewById(R.id.buttonImmediately);
        Button buttonMorningSlot = findViewById(R.id.buttonMorningSlot);
        Button buttonNoonSlot = findViewById(R.id.buttonNoonSlot);
        Button buttonEveningSlot = findViewById(R.id.buttonEveningSlot);
        Button buttonRequestService = findViewById(R.id.buttonRequestService);
        buttonDryWaste = findViewById(R.id.buttonDryWaste);
        buttonWetWaste = findViewById(R.id.buttonWetWaste);
        editTextSelectProblem = findViewById(R.id.editTextSelectProblem);
        LinearLayout layoutGarbageType = findViewById(R.id.layoutGarbageType);

        /*Setting font for all the views*/
        textSelectProblem.setTypeface(setLatoBoldFont(this));
        textSelectSlot.setTypeface(setLatoBoldFont(this));
        editTextSelectProblem.setTypeface(setLatoRegularFont(this));
        buttonImmediately.setTypeface(setLatoRegularFont(this));
        buttonMorningSlot.setTypeface(setLatoRegularFont(this));
        buttonNoonSlot.setTypeface(setLatoRegularFont(this));
        buttonEveningSlot.setTypeface(setLatoRegularFont(this));
        buttonRequestService.setTypeface(setLatoLightFont(this));
        buttonDryWaste.setTypeface(setLatoRegularFont(this));
        buttonWetWaste.setTypeface(setLatoRegularFont(this));

        /*We want Button Immediately should be selected on start of activity*/
        selectButton(R.id.buttonImmediately);

        /* We set button text according to screen title */
        switch (screenTitle) {
            case R.string.plumber:
                buttonRequestService.setText(R.string.request_plumber);
                break;
            case R.string.carpenter:
                buttonRequestService.setText(R.string.request_carpenter);
                break;
            case R.string.electrician:
                buttonRequestService.setText(R.string.request_electrician);
                break;
            case R.string.garbage_management:
                buttonRequestService.setText(R.string.request);
                textSelectProblem.setText(R.string.select_garbage_type);
                editTextSelectProblem.setVisibility(View.GONE);
                layoutGarbageType.setVisibility(View.VISIBLE);
        }

        societyServiceType = getString(screenTitle).toLowerCase();

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting event for views*/
        editTextSelectProblem.setOnClickListener(this);
        buttonImmediately.setOnClickListener(this);
        buttonMorningSlot.setOnClickListener(this);
        buttonNoonSlot.setOnClickListener(this);
        buttonEveningSlot.setOnClickListener(this);
        buttonRequestService.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        buttonDryWaste.setOnClickListener(this);
        buttonWetWaste.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextSelectProblem:
                Intent intent = new Intent(SocietyServicesHome.this, SocietyServiceProblemList.class);
                intent.putExtra(Constants.SCREEN_TITLE, screenTitle);
                startActivityForResult(intent, SELECT_SOCIETY_SERVICE_REQUEST_CODE);
                break;
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
            case R.id.historyButton:
                Intent societyServiceHistoryIntent = new Intent(SocietyServicesHome.this, SocietyServicesHistory.class);
                societyServiceHistoryIntent.putExtra(SCREEN_TITLE, societyServiceType);
                startActivity(societyServiceHistoryIntent);
                break;
            case R.id.buttonDryWaste:
                buttonDryWaste.setBackgroundResource(R.drawable.selected_button_design);
                buttonWetWaste.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonWetWaste:
                buttonWetWaste.setBackgroundResource(R.drawable.selected_button_design);
                buttonDryWaste.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_SOCIETY_SERVICE_REQUEST_CODE) {
            problem = data.getStringExtra(Constants.SOCIETY_SERVICE_PROBLEM);
            editTextSelectProblem.setText(problem);
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
     * Store the details of Society Service Notifications to Firebase
     */
    private void storeSocietyServiceDetails() {
        /*Generating the societyServiceUID*/
        DatabaseReference societyServiceNotificationReference = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
        String notificationUID = societyServiceNotificationReference.push().getKey();

        /*Getting the data entered by user while logging the Society Service issue*/
        String userUID = NammaApartmentsGlobal.userUID;
        String timeSlot = selectedButton.getText().toString();

        /*Storing Society Service data entered by user under new parent 'societyServiceNotifications' in Firebase*/
        NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(problem, timeSlot,
                userUID, societyServiceType, notificationUID, IN_PROGRESS, null, null);
        societyServiceNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);

        /*Storing time stamp to keep track of notifications*/
        societyServiceNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

        /*Mapping Society Service UID with value in userData under Flat Number*/
        DatabaseReference societyServiceUserDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        societyServiceUserDataReference.child(societyServiceType).child(notificationUID).setValue(true);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.NAMMA_APARTMENTS_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (screenTitle) {
            case R.string.plumber:
                editor.putString(Constants.PLUMBER_SERVICE_NOTIFICATION_UID, notificationUID);
                break;
            case R.string.carpenter:
                editor.putString(Constants.CARPENTER_SERVICE_NOTIFICATION_UID, notificationUID);
                break;
            case R.string.electrician:
                editor.putString(Constants.ELECTRICIAN_SERVICE_NOTIFICATION_UID, notificationUID);
                break;
        }
        editor.apply();

        /*Call AwaitingResponse activity, by this time Society Service should have received the Notification
         * Since, cloud functions would have been triggered*/
        Intent awaitingResponseIntent = new Intent(SocietyServicesHome.this, AwaitingResponse.class);
        awaitingResponseIntent.putExtra(Constants.NOTIFICATION_UID, notificationUID);
        awaitingResponseIntent.putExtra(Constants.SOCIETY_SERVICE_TYPE, societyServiceType);
        startActivity(awaitingResponseIntent);
        finish();
    }

}