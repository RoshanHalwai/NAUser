package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import static com.kirtanlabs.nammaapartments.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_ALL;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DATA;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_NOTIFICATIONS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_PRIVATE;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartments.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SELECT_SOCIETY_SERVICE_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.Constants.SOCIETYSERVICES_REFERENCE;
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
    private Button selectedButton;
    private TextView textSelectProblemValue;

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
        TextView textSelectSlot = findViewById(R.id.textSelectSlot);
        textSelectProblemValue = findViewById(R.id.textSelectProblemValue);
        Button buttonImmediately = findViewById(R.id.buttonImmediately);
        Button buttonMorningSlot = findViewById(R.id.buttonMorningSlot);
        Button buttonNoonSlot = findViewById(R.id.buttonNoonSlot);
        Button buttonEveningSlot = findViewById(R.id.buttonEveningSlot);
        Button buttonRequestService = findViewById(R.id.buttonRequestService);

        /*Setting font for all the views*/
        textSelectProblem.setTypeface(setLatoBoldFont(this));
        textSelectSlot.setTypeface(setLatoBoldFont(this));
        textSelectProblemValue.setTypeface(setLatoRegularFont(this));
        buttonImmediately.setTypeface(setLatoRegularFont(this));
        buttonMorningSlot.setTypeface(setLatoRegularFont(this));
        buttonNoonSlot.setTypeface(setLatoRegularFont(this));
        buttonEveningSlot.setTypeface(setLatoRegularFont(this));
        buttonRequestService.setTypeface(setLatoLightFont(this));

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
        }

        /*Setting event for views*/
        textSelectProblemValue.setOnClickListener(this);
        buttonImmediately.setOnClickListener(this);
        buttonMorningSlot.setOnClickListener(this);
        buttonNoonSlot.setOnClickListener(this);
        buttonEveningSlot.setOnClickListener(this);
        buttonRequestService.setOnClickListener(this);

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);
        historyButton.setOnClickListener(v -> {
            Intent societyServiceHistoryIntent = new Intent(SocietyServicesHome.this, SocietyServicesHistory.class);
            startActivity(societyServiceHistoryIntent);
        });
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textSelectProblemValue:
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
            textSelectProblemValue.setText(problem);
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
        /*Getting the societyServiceUID*/
        DatabaseReference societyServiceNotificationReference = ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
        String societyServiceUID = societyServiceNotificationReference.push().getKey();

        /*Getting the data entered by user while lodging the Society Service issue*/
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String userUID = nammaApartmentUser.getUID();
        String timeSlot = selectedButton.getText().toString();
        String societyServiceType = getString(screenTitle).toLowerCase();

        /*Creating a reference to get notification UID*/
        DatabaseReference notificationUIDReference = SOCIETYSERVICES_REFERENCE.child(FIREBASE_CHILD_ALL).child(societyServiceType)
                .child(FIREBASE_CHILD_DATA).child(FIREBASE_CHILD_PRIVATE).child(societyServiceUID);

        /*Generating a unique notification UID for every notification*/
        String notificationUID = notificationUIDReference.child(FIREBASE_CHILD_NOTIFICATIONS).push().getKey();

        /*Inserting the notification UID under 'notifications' child*/
        notificationUIDReference.child(FIREBASE_CHILD_NOTIFICATIONS).child(notificationUID).push();

        /*Storing Society Service data entered by user under new parent 'societyServiceNotifications' in Firebase*/
        NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(problem, timeSlot,
                userUID, societyServiceType, notificationUID, IN_PROGRESS);
        societyServiceNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);

        /*Storing time stamp to keep track of notifications*/
        societyServiceNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

        /*Mapping Society Service UID with value in userData under Flat Number*/
        DatabaseReference societyServiceUserDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        societyServiceUserDataReference.child(notificationUID).setValue(true);

        /*Call AwaitingResponse activity, by this time Society Service should have received the Notification
         * Since, cloud functions would have been triggered*/
        Intent awaitingResponseIntent = new Intent(SocietyServicesHome.this, AwaitingResponse.class);
        awaitingResponseIntent.putExtra("NotificationUID", notificationUID);
        awaitingResponseIntent.putExtra("societyServiceUID", societyServiceUID);
        awaitingResponseIntent.putExtra("societyServiceType", societyServiceType);
        startActivity(awaitingResponseIntent);
    }

}