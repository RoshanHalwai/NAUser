package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.util.Locale;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.kirtanlabs.nammaapartments.Constants.DAILY_SERVICE_OBJECT;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.GRANTED_ACCESS_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.PHONE_NUMBER_MAX_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;


public class EditDailyServicesAndFamilyMemberDetails extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textDescription;
    private EditText editMemberAndServiceName, editMobileNumber, editPickInTime;
    private Button buttonUpdate;
    private Button buttonYes;
    private Button buttonNo;
    private int screenTitle;
    private String name;
    private String mobile;
    private String inTime;
    private String service_type;
    private String granted_access_type;
    private boolean nameTextChanged = false;
    private boolean mobileTextChanged = false;
    private boolean timeTextChanged = false;
    private boolean grantedAccess = false;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_daily_services_and_family_member_details;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Edit Daily Service Details and Edit Family Members details, we set the title
         * based on the user click on listview of MySweetHome and MyDailyServices*/
        if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_sweet_home) {
            screenTitle = R.string.edit_my_family_member_details;
        } else {
            screenTitle = R.string.edit_my_daily_service_details;
        }
        return screenTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textMemberAndServiceName = findViewById(R.id.textMemberAndServiceName);
        TextView textMemberAndServiceMobile = findViewById(R.id.textMemberAndServiceMobile);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textInTime = findViewById(R.id.textInTime);
        TextView textGrantAccess = findViewById(R.id.textGrantAccess);
        textDescription = findViewById(R.id.textDescription);
        editMemberAndServiceName = findViewById(R.id.editMemberAndServiceName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        editPickInTime = findViewById(R.id.editPickInTime);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickInTime.setInputType(InputType.TYPE_NULL);

        /*Setting font for all the views*/
        textMemberAndServiceName.setTypeface(setLatoBoldFont(this));
        textMemberAndServiceMobile.setTypeface(setLatoBoldFont(this));
        textCountryCode.setTypeface(setLatoItalicFont(this));
        textInTime.setTypeface(setLatoBoldFont(this));
        textGrantAccess.setTypeface(setLatoBoldFont(this));
        textDescription.setTypeface(setLatoBoldFont(this));
        editMemberAndServiceName.setTypeface(setLatoRegularFont(this));
        editMobileNumber.setTypeface(setLatoRegularFont(this));
        editPickInTime.setTypeface(setLatoRegularFont(this));
        buttonYes.setTypeface(setLatoRegularFont(this));
        buttonNo.setTypeface(setLatoRegularFont(this));
        buttonUpdate.setTypeface(setLatoLightFont(this));

        /*To fill Details Automatically in all EditTexts when screen is loaded*/
        getMyDailyServiceAndFamilyMemberDetails();

        /*Since we are using same layout for edit my daily service and add my family member details we need to show different layout
          according to the user choice.*/
        if (screenTitle == R.string.edit_my_daily_service_details) {
            LinearLayout linearLayoutTime = findViewById(R.id.layoutTime);
            linearLayoutTime.setVisibility(View.VISIBLE);
        } else {
            LinearLayout linearLayoutYesNo = findViewById(R.id.layoutYesNo);
            linearLayoutYesNo.setVisibility(View.VISIBLE);
        }

        /*Setting events for edit text*/
        setEventsForEditText();

        /*Setting event for views */
        editPickInTime.setOnFocusChangeListener(this);
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickInTime:
                pickTime(this, this);
                break;

            case R.id.buttonYes:
                buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonYes.setTextColor(Color.WHITE);
                buttonNo.setTextColor(Color.BLACK);
                if (getResources().getString(R.string.yes).equals(granted_access_type)) {
                    grantedAccess = false;
                    makeViewsVisibleOrInvisible();
                } else {
                    grantedAccess = true;
                    makeViewsVisibleOrInvisible();
                }
                break;

            case R.id.buttonNo:
                buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                buttonYes.setTextColor(Color.BLACK);
                buttonNo.setTextColor(Color.WHITE);
                if (getResources().getString(R.string.no).equals(granted_access_type)) {
                    grantedAccess = false;
                    makeViewsVisibleOrInvisible();
                } else {
                    grantedAccess = true;
                    makeViewsVisibleOrInvisible();
                }
                break;

            case R.id.buttonUpdate:
                if (isAllFieldsFilled(new EditText[]{editMemberAndServiceName, editMobileNumber}) && editMobileNumber.length() == PHONE_NUMBER_MAX_LENGTH) {
                    if (screenTitle == R.string.edit_my_daily_service_details) {
                        if (mobileTextChanged) {
                            navigatingToOTPScreen();
                        } else {
                            Intent myDailyServiceIntent = new Intent(EditDailyServicesAndFamilyMemberDetails.this, DailyServicesHome.class);
                            myDailyServiceIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            myDailyServiceIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(myDailyServiceIntent);
                        }
                    } else {
                        if (grantedAccess) {
                            openNotificationDialog();
                        } else if (mobileTextChanged) {
                            navigatingToOTPScreen();
                        } else {
                            navigatingToMySweetHomeScreen();
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickTime(this, this);
        }
    }

    /* ------------------------------------------------------------- *
     * OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editPickInTime.setText(selectedTime);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when screen is loaded and it will fill all the details
     * in editText accordingly
     */
    private void getMyDailyServiceAndFamilyMemberDetails() {
        if (getIntent().getIntExtra(SCREEN_TITLE, 0) == R.string.my_daily_services) {
            NammaApartmentDailyService nammaApartmentDailyService = (NammaApartmentDailyService) getIntent().getSerializableExtra(DAILY_SERVICE_OBJECT);
            name = nammaApartmentDailyService.getfullName();
            mobile = nammaApartmentDailyService.getPhoneNumber();
            editMemberAndServiceName.setText(name);
            editMemberAndServiceName.setSelection(name.length());
            editMobileNumber.setText(mobile);
            if (screenTitle == R.string.edit_my_daily_service_details) {
                inTime = nammaApartmentDailyService.getTimeOfVisit();
                service_type = nammaApartmentDailyService.getDailyServiceType();
                editPickInTime.setText(inTime);
                String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
                textDescription.setText(description);
            }
        } else {
          /*  NammaApartmentFamilyMembers nammaApartmentFamilyMembers = (NammaApartmentFamilyMembers) getIntent().getSerializableExtra(FAMILY_MEMBER_OBJECT);
            name = nammaApartmentFamilyMembers.getfullName();
            mobile = nammaApartmentFamilyMembers.getphoneNumber();*/
            editMemberAndServiceName.setText(name);
            editMemberAndServiceName.setSelection(name.length());
            editMobileNumber.setText(mobile);
            if (screenTitle == R.string.edit_my_family_member_details) {
                granted_access_type = getIntent().getStringExtra(GRANTED_ACCESS_TYPE);
                textDescription.setText(getResources().getString(R.string.otp_message_family_member));
            }
        }
    }

    /**
     * Once user enters edits some text in editText
     */
    private void setEventsForEditText() {
        editMemberAndServiceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editMemberAndServiceName.length() > 0) {
                    buttonUpdate.setVisibility(View.VISIBLE);
                    nameTextChanged = true;
                } else {
                    nameTextChanged = false;
                }
                makeViewsVisibleOrInvisible();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String memberOrServiceName = editMemberAndServiceName.getText().toString().trim();
                if (memberOrServiceName.length() == EDIT_TEXT_EMPTY_LENGTH || isValidPersonName(memberOrServiceName)) {
                    editMemberAndServiceName.setError(getString(R.string.accept_alphabets));
                }
                if (editMemberAndServiceName.getText().toString().equals(name)) {
                    nameTextChanged = false;
                }
                makeViewsVisibleOrInvisible();
            }
        });

        editMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String mobileNumber = editMobileNumber.getText().toString().trim();
                if (isValidPhone(mobileNumber)) {
                    textDescription.setVisibility(View.VISIBLE);
                    buttonUpdate.setVisibility(View.VISIBLE);
                    mobileTextChanged = true;
                } else if ((mobileNumber.length() == EDIT_TEXT_EMPTY_LENGTH) || mobileNumber.length() < PHONE_NUMBER_MAX_LENGTH) {
                    editMobileNumber.setError(getString(R.string.number_10digit_validation));
                    mobileTextChanged = false;
                }
                makeViewsVisibleOrInvisible();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editMobileNumber.getText().toString().equals(mobile)) {
                    mobileTextChanged = false;
                }
                makeViewsVisibleOrInvisible();
            }
        });

        editPickInTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonUpdate.setVisibility(View.VISIBLE);
                timeTextChanged = true;
                makeViewsVisibleOrInvisible();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editPickInTime.getText().toString().equals(inTime)) {
                    timeTextChanged = false;
                    makeViewsVisibleOrInvisible();
                }
            }
        });
    }

    /**
     * This method is used to make textDescription and buttonUpdate Visible or Invisible
     */
    private void makeViewsVisibleOrInvisible() {
        if (nameTextChanged || mobileTextChanged || timeTextChanged || grantedAccess) {
            buttonUpdate.setVisibility(View.VISIBLE);
            if (mobileTextChanged) {
                textDescription.setVisibility(View.VISIBLE);
            } else {
                textDescription.setVisibility(View.INVISIBLE);
            }
        } else {
            buttonUpdate.setVisibility(View.INVISIBLE);
            textDescription.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method gets invoked when user tries to add family member and also giving access.
     */
    private void openNotificationDialog() {
        AlertDialog.Builder alertNotificationDialog = new AlertDialog.Builder(this);
        View notificationDialog = View.inflate(this, R.layout.layout_dialog_grant_access_yes, null);
        alertNotificationDialog.setView(notificationDialog);

        // Setting Custom Dialog Buttons
        if (mobileTextChanged) {
            alertNotificationDialog.setPositiveButton("Accept", (dialog, which) -> navigatingToOTPScreen());
        } else {
            alertNotificationDialog.setPositiveButton("Accept", (dialog, which) -> navigatingToMySweetHomeScreen());
        }
        alertNotificationDialog.setNegativeButton("Reject", (dialog, which) -> dialog.cancel());

        new Dialog(getApplicationContext());
        alertNotificationDialog.show();
    }

    /**
     * We need to navigate to OTP Screen based on the user selection of giving access and also on mobile number change.
     */
    private void navigatingToOTPScreen() {
        Intent intentNotification = new Intent(EditDailyServicesAndFamilyMemberDetails.this, OTP.class);
        if (screenTitle == R.string.edit_my_daily_service_details) {
            intentNotification.putExtra(SCREEN_TITLE, R.string.add_my_service);
            intentNotification.putExtra(SERVICE_TYPE, service_type);
        } else {
            intentNotification.putExtra(SCREEN_TITLE, R.string.add_family_members_details_screen);
            intentNotification.putExtra(SERVICE_TYPE, "Family Member");
        }
        startActivity(intentNotification);
    }

    /**
     * We need to navigate to My Sweet Home Screen Screen based on the user selection of giving access and also on Name change.
     */
    private void navigatingToMySweetHomeScreen() {
        Intent mySweetHomeIntent = new Intent(EditDailyServicesAndFamilyMemberDetails.this, MySweetHome.class);
        mySweetHomeIntent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        mySweetHomeIntent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mySweetHomeIntent);
    }
}
