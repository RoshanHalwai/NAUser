package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.arrivals;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.cabs.CabsList;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.myvisitorslist.packages.PackagesList;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.text.DateFormatSymbols;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_DELIVERIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ARRIVAL_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.CAB_NUMBER_FIELD_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CABS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DELIVERIES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_CABS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_DELIVERIES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SCREEN_TITLE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;


/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/14/2018
 */
public class ExpectingArrival extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final int[] buttonIds = new int[]{R.id.button1Hr,
            R.id.button2Hr,
            R.id.button4Hr,
            R.id.button6Hr,
            R.id.button8Hr,
            R.id.button12Hr,
            R.id.button16Hr,
            R.id.button24Hr};
    private EditText editPickDateTime, editCabStateCode, editCabRtoNumber, editCabSerialNumberOne, editCabSerialNumberTwo, editVendorValue;
    private TextView textErrorCabNumber, textErrorDateTime, textErrorValidForButton;
    private int arrivalType;
    private String selectedDate;
    private String packageVendorName;
    private boolean isValidForSelected;
    private Button selectedButton;
    private LinearLayout cabNumberLayout;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_expecting_arrival;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Expecting Cab Arrival and Package Arrival, we set the title
         * based on the user click on NotifyGate Home screen*/
        if (getIntent().getIntExtra(ARRIVAL_TYPE, 0) == R.string.expecting_cab_arrival) {
            arrivalType = R.string.expecting_cab_arrival;
        } else {
            arrivalType = R.string.expecting_package_arrival;
        }
        return arrivalType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        cabNumberLayout = findViewById(R.id.cabNumberLayout);
        TextView textCabOrVendorTitle = findViewById(R.id.textCabOrVendorTitle);
        TextView textDateTime = findViewById(R.id.textDateTime);
        textErrorCabNumber = findViewById(R.id.textErrorCabNumber);
        textErrorDateTime = findViewById(R.id.textErrorDateTime);
        textErrorValidForButton = findViewById(R.id.textErrorValidForButton);
        TextView textValidFor = findViewById(R.id.textValidFor);
        editCabStateCode = findViewById(R.id.editCabStateCode);
        editCabRtoNumber = findViewById(R.id.editCabRtoNumber);
        editCabSerialNumberOne = findViewById(R.id.editCabSerialNumberOne);
        editCabSerialNumberTwo = findViewById(R.id.editCabSerialNumberTwo);
        editVendorValue = findViewById(R.id.editVendorValue);
        editPickDateTime = findViewById(R.id.editPickDateTime);
        Button button1hr = findViewById(R.id.button1Hr);
        Button button2hr = findViewById(R.id.button2Hr);
        Button button4hr = findViewById(R.id.button4Hr);
        Button button6hr = findViewById(R.id.button6Hr);
        Button button8hr = findViewById(R.id.button8Hr);
        Button button12hr = findViewById(R.id.button12Hr);
        Button button16hr = findViewById(R.id.button16Hr);
        Button button24hr = findViewById(R.id.button24Hr);
        Button buttonNotifyGate = findViewById(R.id.buttonNotifyGate);

        /*Setting font for all the views*/
        textCabOrVendorTitle.setTypeface(setLatoBoldFont(this));
        textDateTime.setTypeface(setLatoBoldFont(this));
        textErrorCabNumber.setTypeface(setLatoRegularFont(this));
        textErrorDateTime.setTypeface(setLatoRegularFont(this));
        textErrorValidForButton.setTypeface(setLatoRegularFont(this));
        textValidFor.setTypeface(setLatoBoldFont(this));
        editCabStateCode.setTypeface(setLatoRegularFont(this));
        editCabRtoNumber.setTypeface(setLatoRegularFont(this));
        editCabSerialNumberOne.setTypeface(setLatoRegularFont(this));
        editCabSerialNumberTwo.setTypeface(setLatoRegularFont(this));
        editVendorValue.setTypeface(setLatoRegularFont(this));
        editPickDateTime.setTypeface(setLatoRegularFont(this));
        button1hr.setTypeface(setLatoRegularFont(this));
        button2hr.setTypeface(setLatoRegularFont(this));
        button4hr.setTypeface(setLatoRegularFont(this));
        button6hr.setTypeface(setLatoRegularFont(this));
        button8hr.setTypeface(setLatoRegularFont(this));
        button12hr.setTypeface(setLatoRegularFont(this));
        button16hr.setTypeface(setLatoRegularFont(this));
        button24hr.setTypeface(setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(setLatoLightFont(this));

        /*Since we are using same layout for Expecting cab and package arrival we need to
         * set text for textCabOrVendorTitle to either Package Vendor Name or Cab Number*/
        textCabOrVendorTitle.setText(getCarOrPackageArrivalTitle());

        /*Since we are using same layout for Expecting cab and package arrival we need to
         * show and hide the appropriate editTexts according to screen.*/
        showAppropriateEditText();

        /*This method gets invoked when user is trying to modify the values on EditTexts.*/
        setEventsForEditText();

        /*Setting event for views*/
        button1hr.setOnClickListener(this);
        button2hr.setOnClickListener(this);
        button4hr.setOnClickListener(this);
        button6hr.setOnClickListener(this);
        button8hr.setOnClickListener(this);
        button12hr.setOnClickListener(this);
        button16hr.setOnClickListener(this);
        button24hr.setOnClickListener(this);
        editPickDateTime.setOnFocusChangeListener(this);
        editPickDateTime.setOnClickListener(this);
        buttonNotifyGate.setOnClickListener(this);

    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1Hr:
                selectButton(R.id.button1Hr);
                break;
            case R.id.button2Hr:
                selectButton(R.id.button2Hr);
                break;
            case R.id.button4Hr:
                selectButton(R.id.button4Hr);
                break;
            case R.id.button6Hr:
                selectButton(R.id.button6Hr);
                break;
            case R.id.button8Hr:
                selectButton(R.id.button8Hr);
                break;
            case R.id.button12Hr:
                selectButton(R.id.button12Hr);
                break;
            case R.id.button16Hr:
                selectButton(R.id.button16Hr);
                break;
            case R.id.button24Hr:
                selectButton(R.id.button24Hr);
                break;
            case R.id.editPickDateTime:
                pickDate(this, this);
                //Hide the keyboard.
                editPickDateTime.setInputType(InputType.TYPE_NULL);
                break;
            case R.id.buttonNotifyGate:
                // This method gets invoked to check all the editText fields and button validations.
                validateFields();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            onClick(editPickDateTime);
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet & OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            pickTime(this, this);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            String concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
            editPickDateTime.setText(concatenatedDateAndTime);
            textErrorDateTime.setVisibility(View.GONE);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Store the details of Arriving Cabs and Delivery details to Firebase
     */
    private void storeDigitalGateDetails(String digitalGateChild) {

        //Get the details from user
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String reference;
        if (TextUtils.isEmpty(editVendorValue.getText())) {
            reference = editCabStateCode.getText().toString().trim() + HYPHEN + editCabRtoNumber.getText().toString().trim() + HYPHEN
                    + editCabSerialNumberOne.getText().toString().trim() + HYPHEN + editCabSerialNumberTwo.getText().toString().trim();
        } else {
            reference = editVendorValue.getText().toString();
        }
        String dateTimeOfVisit = editPickDateTime.getText().toString();
        String validFor = selectedButton.getText().toString();
        String userUID = nammaApartmentUser.getUID();
        NammaApartmentArrival nammaApartmentArrival = new NammaApartmentArrival(reference, dateTimeOfVisit, validFor, userUID, "preApproved");

        //Store cabs/deliveries uid and value under userdata->private->currentUserFlat
        DatabaseReference digitalGateUIDReference = ALL_USERS_REFERENCE.child(reference);
        String digitalGateUID = digitalGateUIDReference.push().getKey();
        DatabaseReference digitalGateReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference()
                .child(digitalGateChild)
                .child(NammaApartmentsGlobal.userUID);
        digitalGateReference.child(digitalGateUID).setValue(true);

        //Store the details of cab/delivery in cabs/deliveries->public->uid
        if (arrivalType == R.string.expecting_cab_arrival) {
            DatabaseReference cabNumberReference = ALL_CABS_REFERENCE.child(reference);
            cabNumberReference.setValue(digitalGateUID);
            DatabaseReference cabDetailsReference = PRIVATE_CABS_REFERENCE.child(digitalGateUID);
            cabDetailsReference.setValue(nammaApartmentArrival);
        } else {
            DatabaseReference deliveryReference = ALL_DELIVERIES_REFERENCE.child(nammaApartmentUser.getPersonalDetails().getPhoneNumber());
            deliveryReference.setValue(digitalGateUID);
            DatabaseReference deliveryDetailsReference = PRIVATE_DELIVERIES_REFERENCE.child(digitalGateUID);
            deliveryDetailsReference.setValue(nammaApartmentArrival);
        }
    }

    private int getCarOrPackageArrivalTitle() {
        if (arrivalType == R.string.expecting_cab_arrival) {
            return R.string.cab_number;
        }
        return R.string.package_vendor;
    }

    /**
     * This method gets invoked according to the screen title in displaying appropriate editTexts.
     */
    private void showAppropriateEditText() {
        if (arrivalType == R.string.expecting_package_arrival) {
            editVendorValue.setVisibility(View.VISIBLE);
            editVendorValue.requestFocus();
        } else {
            cabNumberLayout.setVisibility(View.VISIBLE);
        }
    }

    /*Method for ValidFor 8 Button clicks*/
    private void selectButton(int id) {
        isValidForSelected = true;
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                selectedButton = button;
                button.setBackgroundResource(R.drawable.selected_button_design);
                textErrorValidForButton.setVisibility(View.GONE);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    private void setEventsForEditText() {
        editCabStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorCabNumber.setVisibility(View.GONE);
                    editCabRtoNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                    /*We check if the text user has entered is lowercase if it is in lowercase then we change it
                    to upper case*/
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editCabStateCode.setText(s);
                    editCabStateCode.setSelection(editCabStateCode.getText().length());
                }
            }
        });

        editCabRtoNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editCabStateCode.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorCabNumber.setVisibility(View.GONE);
                    editCabSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
            }
        });

        editCabSerialNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editCabRtoNumber.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorCabNumber.setVisibility(View.GONE);
                    editCabSerialNumberTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                    /*We check if the text user has entered is lowercase if it is in lowercase then we change it
                    to upper case*/
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editCabSerialNumberOne.setText(s);
                    editCabSerialNumberOne.setSelection(editCabSerialNumberOne.getText().length());
                }
            }
        });

        editCabSerialNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editCabSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                textErrorCabNumber.setVisibility(View.GONE);
            }
        });
        editVendorValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                    /*We check if the text user has entered is lowercase if it is in lowercase then we change it
                    to upper case*/
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editVendorValue.setText(s);
                    editVendorValue.setSelection(editVendorValue.getText().length());
                }
                packageVendorName = editVendorValue.getText().toString().trim();
                if (isValidPersonName(packageVendorName)) {
                    editVendorValue.setError(getString(R.string.accept_alphabets));
                }
            }
        });
    }

    private void validateFields() {
        boolean fieldsFilled;
        if (arrivalType == R.string.expecting_cab_arrival) {
            String cabStateCode = editCabStateCode.getText().toString();
            String cabRtoNumber = editCabRtoNumber.getText().toString();
            String cabSerialNumberOne = editCabSerialNumberOne.getText().toString();
            String cabSerialNumberTwo = editCabSerialNumberTwo.getText().toString();
            String cabDateTime = editPickDateTime.getText().toString();
            fieldsFilled = isAllFieldsFilled(new EditText[]{editCabStateCode, editCabRtoNumber, editCabSerialNumberOne, editCabSerialNumberTwo, editPickDateTime}) && isValidForSelected;
            //This condition checks if all fields are not filled and if user presses notify gate button it will then display proper error messages.
            if (!fieldsFilled) {
                if (TextUtils.isEmpty(cabStateCode) || TextUtils.isEmpty(cabRtoNumber) || TextUtils.isEmpty(cabSerialNumberOne) || TextUtils.isEmpty(cabSerialNumberTwo)) {
                    textErrorCabNumber.setVisibility(View.VISIBLE);
                }
                if (TextUtils.isEmpty(cabDateTime)) {
                    textErrorDateTime.setVisibility(View.VISIBLE);
                }
                if (!isValidForSelected) {
                    textErrorValidForButton.setVisibility(View.VISIBLE);
                }
            }
            //This condition checks for if user has filled all the fields and navigates to appropriate screen.
            if (fieldsFilled) {
                storeDigitalGateDetails(FIREBASE_CHILD_CABS);
                Intent cabsListIntent = new Intent(ExpectingArrival.this, CabsList.class);
                cabsListIntent.putExtra(SCREEN_TITLE, getClass().toString());
                showNotificationDialog(getResources().getString(R.string.notification_title),
                        getResources().getString(R.string.notification_message), cabsListIntent);
            }
        } else {
            packageVendorName = editVendorValue.getText().toString();
            String vendorDateTime = editPickDateTime.getText().toString();
            fieldsFilled = isAllFieldsFilled(new EditText[]{editVendorValue, editPickDateTime}) && isValidForSelected;
            //This condition checks if all fields are not filled and if user presses notify gate button it will then display proper error messages.
            if (!fieldsFilled) {
                if (TextUtils.isEmpty(packageVendorName)) {
                    editVendorValue.setError(getString(R.string.name_validation));
                }
                if (TextUtils.isEmpty(vendorDateTime)) {
                    textErrorDateTime.setVisibility(View.VISIBLE);
                }
                if (!isValidForSelected) {
                    textErrorValidForButton.setVisibility(View.VISIBLE);
                }
            }
            //This condition checks for if user has filled all the fields and navigates to appropriate screen.
            if (fieldsFilled) {
                storeDigitalGateDetails(FIREBASE_CHILD_DELIVERIES);
                Intent packagesListIntent = new Intent(ExpectingArrival.this, PackagesList.class);
                packagesListIntent.putExtra(SCREEN_TITLE, getClass().toString());
                showNotificationDialog(getResources().getString(R.string.notification_title),
                        getResources().getString(R.string.notification_message), packagesListIntent);
            }
        }
    }

}
