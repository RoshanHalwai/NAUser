package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.Calendar;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_SOCIETYSERVICENOTIFICATION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIFTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_GARBAGE_COLLECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SCRAP_COLLECTION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TIMESTAMP;
import static com.kirtanlabs.nammaapartments.utilities.Constants.IN_PROGRESS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SELECT_SOCIETY_SERVICE_REQUEST_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SEVENTEEN_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SOCIETY_SERVICE_PROBLEM_OTHERS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TWELVE_HOURS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class SocietyServicesHome extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.buttonImmediatelyAndLessQuantity,
            R.id.buttonMorningSlotAndMediumQuantity,
            R.id.buttonNoonSlotAndLargeQuantity,
            R.id.buttonEveningSlotAndVeryLargeQuantity};
    private int screenTitle;
    private String problemAndScrapType, societyServiceType, descriptionValue;
    private Button selectedButton, buttonDryWaste, buttonWetWaste,
            buttonMorningSlotAndMediumQuantity, buttonNoonSlotAndLargeQuantity,
            buttonEveningSlotAndVeryLargeQuantity;
    private EditText editTextSelectProblemAndScrapType, editTextDescription;
    private LinearLayout otherProblemLayout;
    private Boolean otherProblemSelected = false, isGarbageTypeSelected = false;
    private TextView textErrorGarbageType, textErrorSelectProblem;

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

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textSelectSlotAndTotalQuantity = findViewById(R.id.textSelectSlotAndTotalQuantity);
        TextView textSelectProblemAndScrapType = findViewById(R.id.textSelectProblemAndScrapType);
        TextView textDescription = findViewById(R.id.textDescription);
        TextView imageSelectProblemAndScrapType = findViewById(R.id.imageSelectProblemAndScrapType);
        TextView imageSelectSlotAndQuantity = findViewById(R.id.imageSelectSlotAndQuantity);
        textErrorGarbageType = findViewById(R.id.textErrorGarbageType);
        textErrorSelectProblem = findViewById(R.id.textErrorSelectProblem);
        Button buttonImmediatelyAndLessQuantity = findViewById(R.id.buttonImmediatelyAndLessQuantity);
        buttonMorningSlotAndMediumQuantity = findViewById(R.id.buttonMorningSlotAndMediumQuantity);
        buttonNoonSlotAndLargeQuantity = findViewById(R.id.buttonNoonSlotAndLargeQuantity);
        buttonEveningSlotAndVeryLargeQuantity = findViewById(R.id.buttonEveningSlotAndVeryLargeQuantity);
        Button buttonRequestService = findViewById(R.id.buttonRequestService);
        buttonDryWaste = findViewById(R.id.buttonDryWaste);
        buttonWetWaste = findViewById(R.id.buttonWetWaste);
        editTextSelectProblemAndScrapType = findViewById(R.id.editTextSelectProblemAndScrapType);
        editTextDescription = findViewById(R.id.editTextDescription);
        LinearLayout layoutGarbageType = findViewById(R.id.layoutGarbageType);
        LinearLayout layoutLegend = findViewById(R.id.layoutLegend);
        otherProblemLayout = findViewById(R.id.layoutProblemOthers);

        /*Setting font for all the views*/
        textSelectProblemAndScrapType.setTypeface(setLatoBoldFont(this));
        textSelectSlotAndTotalQuantity.setTypeface(setLatoBoldFont(this));
        textDescription.setTypeface(setLatoBoldFont(this));
        textErrorGarbageType.setTypeface(setLatoBoldFont(this));
        textErrorSelectProblem.setTypeface(setLatoBoldFont(this));
        editTextSelectProblemAndScrapType.setTypeface(setLatoRegularFont(this));
        editTextDescription.setTypeface(setLatoRegularFont(this));
        buttonImmediatelyAndLessQuantity.setTypeface(setLatoRegularFont(this));
        buttonMorningSlotAndMediumQuantity.setTypeface(setLatoRegularFont(this));
        buttonNoonSlotAndLargeQuantity.setTypeface(setLatoRegularFont(this));
        buttonEveningSlotAndVeryLargeQuantity.setTypeface(setLatoRegularFont(this));
        buttonRequestService.setTypeface(setLatoLightFont(this));
        buttonDryWaste.setTypeface(setLatoRegularFont(this));
        buttonWetWaste.setTypeface(setLatoRegularFont(this));

        /*We don't want the keyboard to be displayed when user clicks on editTextSelectProblemAndScrapType view*/
        editTextSelectProblemAndScrapType.setInputType(InputType.TYPE_NULL);

        /*We want Button Immediately should be selected on start of activity*/
        selectButton(R.id.buttonImmediatelyAndLessQuantity);

        /*Getting the society service type based on the screen title*/
        societyServiceType = getString(screenTitle).toLowerCase();

        /* We set button text according to screen title */
        switch (screenTitle) {
            case R.string.plumber:
                disablePastTimeSlot();
                buttonRequestService.setText(R.string.request_plumber);
                break;
            case R.string.carpenter:
                disablePastTimeSlot();
                buttonRequestService.setText(R.string.request_carpenter);
                break;
            case R.string.electrician:
                disablePastTimeSlot();
                buttonRequestService.setText(R.string.request_electrician);
                break;
            case R.string.garbage_collection:
                disablePastTimeSlot();
                buttonRequestService.setText(R.string.request);
                textSelectProblemAndScrapType.setText(R.string.select_garbage_type);
                editTextSelectProblemAndScrapType.setVisibility(View.GONE);
                layoutGarbageType.setVisibility(View.VISIBLE);
                societyServiceType = FIREBASE_CHILD_GARBAGE_COLLECTION;
                break;
            case R.string.scrap_collection:
                imageSelectProblemAndScrapType.setText(R.string.select_scrap_type);
                imageSelectSlotAndQuantity.setText(R.string.select_quantity);
                textSelectProblemAndScrapType.setText(R.string.select_scrap_type);
                textSelectSlotAndTotalQuantity.setText(R.string.total_quantity);
                buttonImmediatelyAndLessQuantity.setText(R.string.less_quantity);
                buttonMorningSlotAndMediumQuantity.setText(R.string.medium_quantity);
                buttonNoonSlotAndLargeQuantity.setText(R.string.large_quantity);
                buttonEveningSlotAndVeryLargeQuantity.setText(R.string.very_large_quantity);
                layoutLegend.setVisibility(View.GONE);
                buttonRequestService.setText(R.string.request);
                societyServiceType = FIREBASE_CHILD_SCRAP_COLLECTION;
                break;
        }

        /*Since we have History button here, we would want users to navigate to history and take a look at their
         * History of that particular Society Service*/
        ImageView historyButton = findViewById(R.id.historyButton);
        historyButton.setVisibility(View.VISIBLE);

        /*Setting event for views*/
        editTextSelectProblemAndScrapType.setOnClickListener(this);
        buttonImmediatelyAndLessQuantity.setOnClickListener(this);
        buttonMorningSlotAndMediumQuantity.setOnClickListener(this);
        buttonNoonSlotAndLargeQuantity.setOnClickListener(this);
        buttonEveningSlotAndVeryLargeQuantity.setOnClickListener(this);
        buttonRequestService.setOnClickListener(this);
        historyButton.setOnClickListener(this);
        buttonDryWaste.setOnClickListener(this);
        buttonWetWaste.setOnClickListener(this);
        editTextSelectProblemAndScrapType.setOnFocusChangeListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocus Change Listener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editTextSelectProblemAndScrapType:
                Intent intent = new Intent(SocietyServicesHome.this, SocietyServiceProblemList.class);
                intent.putExtra(Constants.SCREEN_TITLE, screenTitle);
                startActivityForResult(intent, SELECT_SOCIETY_SERVICE_REQUEST_CODE);
                break;
            case R.id.buttonImmediatelyAndLessQuantity:
                selectButton(R.id.buttonImmediatelyAndLessQuantity);
                break;
            case R.id.buttonMorningSlotAndMediumQuantity:
                selectButton(R.id.buttonMorningSlotAndMediumQuantity);
                break;
            case R.id.buttonNoonSlotAndLargeQuantity:
                selectButton(R.id.buttonNoonSlotAndLargeQuantity);
                break;
            case R.id.buttonEveningSlotAndVeryLargeQuantity:
                selectButton(R.id.buttonEveningSlotAndVeryLargeQuantity);
                break;
            case R.id.buttonRequestService:
                /*This method gets invoked to check all the editText fields and button validations.*/
                validateFields();
                break;
            case R.id.historyButton:
                Intent societyServiceHistoryIntent = new Intent(SocietyServicesHome.this, SocietyServicesHistory.class);
                societyServiceHistoryIntent.putExtra(SCREEN_TITLE, societyServiceType);
                startActivity(societyServiceHistoryIntent);
                break;
            case R.id.buttonDryWaste:
                isGarbageTypeSelected = true;
                textErrorGarbageType.setVisibility(View.GONE);
                problemAndScrapType = getString(R.string.dry_waste);
                buttonDryWaste.setBackgroundResource(R.drawable.selected_button_design);
                buttonWetWaste.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonWetWaste:
                isGarbageTypeSelected = true;
                textErrorGarbageType.setVisibility(View.GONE);
                problemAndScrapType = getString(R.string.wet_waste);
                buttonWetWaste.setBackgroundResource(R.drawable.selected_button_design);
                buttonDryWaste.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(v);
        }
    }

    /*-------------------------------------------------------------------------------
     *Overriding onActivityResult
     *-----------------------------------------------------------------------------*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECT_SOCIETY_SERVICE_REQUEST_CODE) {
            problemAndScrapType = data.getStringExtra(Constants.SOCIETY_SERVICE_PROBLEM);
            editTextSelectProblemAndScrapType.setText(problemAndScrapType);
            textErrorSelectProblem.setVisibility(View.GONE);
            if (problemAndScrapType.equals(SOCIETY_SERVICE_PROBLEM_OTHERS)) {
                otherProblemLayout.setVisibility(View.VISIBLE);
                otherProblemSelected = true;
            } else {
                otherProblemLayout.setVisibility(View.GONE);
                otherProblemSelected = false;
            }
            editTextSelectProblemAndScrapType.setError(null);
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
        String timeSlotAndScrapQuantity = selectedButton.getText().toString();

        /*Mapping Society Service UID with value in userData under Flat Number*/
        DatabaseReference societyServiceUserDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(FIREBASE_CHILD_SOCIETYSERVICENOTIFICATION);
        societyServiceUserDataReference.child(societyServiceType).child(notificationUID).setValue(true);

        if (screenTitle == R.string.scrap_collection) {
            /*Storing Scrap collection data entered by the user under new parent 'societyServiceNotifications' in firebase*/
            NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(null, null, userUID,
                    societyServiceType, notificationUID, IN_PROGRESS, null, null);
            nammaApartmentSocietyServices.setQuantity(timeSlotAndScrapQuantity);

            /*If the user selected scrap type is others then we display the description entered by the user as scrap type*/
            if (problemAndScrapType.equals(SOCIETY_SERVICE_PROBLEM_OTHERS)) {
                nammaApartmentSocietyServices.setScrapType(descriptionValue);
            } else {
                nammaApartmentSocietyServices.setScrapType(problemAndScrapType);
            }
            societyServiceNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);

            /*Storing time stamp to keep track of notifications*/
            societyServiceNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

            /*Navigating users to scrap collection history screen*/
            Intent societyServicesHistoryIntent = new Intent(SocietyServicesHome.this, SocietyServicesHistory.class);
            societyServicesHistoryIntent.putExtra(SCREEN_TITLE, societyServiceType);
            showNotificationDialog(getString(R.string.request_raised),
                    getString(R.string.scrap_collection_request_dialog_message),
                    societyServicesHistoryIntent);

            /*Clearing the editText value and deselecting the user selected button*/
            editTextSelectProblemAndScrapType.setText("");
            selectButton(R.id.buttonImmediatelyAndLessQuantity);

        } else {
            /*If the user selected problem is others then we display the description entered by the user as problem*/
            if (problemAndScrapType.equals(SOCIETY_SERVICE_PROBLEM_OTHERS)) {
                /*Storing Society Service data entered by user under new parent 'societyServiceNotifications' in Firebase*/
                NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(descriptionValue, timeSlotAndScrapQuantity,
                        userUID, societyServiceType, notificationUID, IN_PROGRESS, null, null);
                societyServiceNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);
            } else {

                /*Storing Society Service data entered by user under new parent 'societyServiceNotifications' in Firebase*/
                NammaApartmentSocietyServices nammaApartmentSocietyServices = new NammaApartmentSocietyServices(problemAndScrapType, timeSlotAndScrapQuantity,
                        userUID, societyServiceType, notificationUID, IN_PROGRESS, null, null);
                societyServiceNotificationReference.child(notificationUID).setValue(nammaApartmentSocietyServices);
            }

            /*Storing time stamp to keep track of notifications*/
            societyServiceNotificationReference.child(notificationUID).child(FIREBASE_CHILD_TIMESTAMP).setValue(System.currentTimeMillis());

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
                case R.string.garbage_collection:
                    editor.putString(Constants.GARBAGE_MANAGEMENT_SERVICE_NOTIFICATION_UID, notificationUID);
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

    /**
     * This method gets invoked to check all the validation fields.
     */
    private void validateFields() {
        boolean fieldsFilled;
        switch (screenTitle) {
            case R.string.plumber:
            case R.string.carpenter:
            case R.string.electrician:
            case R.string.scrap_collection:
                descriptionValue = editTextDescription.getText().toString();
                fieldsFilled = isAllFieldsFilled(new EditText[]{editTextSelectProblemAndScrapType});
                /*This condition checks if all fields are not filled and if user presses request button
                 *it will then display proper error messages.*/
                if (!fieldsFilled) {
                    switch (screenTitle) {
                        case R.string.scrap_collection:
                            textErrorSelectProblem.setVisibility(View.VISIBLE);
                            textErrorSelectProblem.setText(R.string.enter_other_scrap_type_desc);
                            break;
                        default:
                            textErrorSelectProblem.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                if (otherProblemSelected && TextUtils.isEmpty(descriptionValue)) {
                    if (screenTitle == R.string.scrap_collection) {
                        editTextDescription.setError(getString(R.string.enter_other_scrap_type_desc));
                    } else {
                        editTextDescription.setError(getString(R.string.enter_other_problem_desc));
                    }
                    editTextDescription.requestFocus();
                    break;
                }
                /*This condition checks for if user has filled all the fields and navigates to appropriate screen.*/
                if (fieldsFilled) {
                    /*This method stores user selected society details in Firebase.*/
                    storeSocietyServiceDetails();
                }
                break;
            case R.string.garbage_collection:
                if (!isGarbageTypeSelected) {
                    textErrorGarbageType.setVisibility(View.VISIBLE);
                } else {
                    /*This method stores user selected society details in Firebase.*/
                    storeSocietyServiceDetails();
                    break;
                }
        }
    }

    /**
     * This method is invoked to disable time slot based on current date past time.
     */
    private void disablePastTimeSlot() {
        /*Getting current hour here*/
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        /*Disabling Time slot if current time greater than that time slot*/
        if (currentHour >= TWELVE_HOURS) {
            buttonMorningSlotAndMediumQuantity.setEnabled(false);
            if (currentHour >= FIFTEEN_HOURS) {
                buttonNoonSlotAndLargeQuantity.setEnabled(false);
                if (currentHour >= SEVENTEEN_HOURS) {
                    buttonEveningSlotAndVeryLargeQuantity.setEnabled(false);
                }
            }
        }
    }

}