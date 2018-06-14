package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormatSymbols;
import java.util.Arrays;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.Constants.ARRIVAL_TYPE;
import static com.kirtanlabs.nammaapartments.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYCABS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYDELIVERIES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYDIGITALGATE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;


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
    private EditText editPickDateTime, editCabOrVendorValue;
    private int arrivalType;
    private String selectedDate;
    private String packageVendorName;
    private boolean isValidForSelected;

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
        TextView textCabOrVendorTitle = findViewById(R.id.textCabOrVendorTitle);
        TextView textDateTime = findViewById(R.id.textDateTime);
        TextView textValidFor = findViewById(R.id.textValidFor);
        editCabOrVendorValue = findViewById(R.id.editCabOrVendorValue);
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
        textValidFor.setTypeface(setLatoBoldFont(this));
        editPickDateTime.setTypeface(setLatoRegularFont(this));
        editCabOrVendorValue.setTypeface(setLatoRegularFont(this));
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
                break;
            case R.id.buttonNotifyGate:
                if (isAllFieldsFilled(new EditText[]{editCabOrVendorValue, editPickDateTime}) && isValidForSelected) {
                    if (arrivalType == R.string.expecting_cab_arrival) {
                        storeDigitalGateDetails(FIREBASE_CHILD_MYCABS);

                    } else {
                        storeDigitalGateDetails(FIREBASE_CHILD_MYDELIVERIES);
                    }
                    createNotifyGateDialog();
                } else if (editCabOrVendorValue.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editCabOrVendorValue.setError(getString(R.string.please_fill_details));
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickDate(this, this);
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
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Store the details of Arriving Cabs and Delivery details to Firebase
     */

    private void storeDigitalGateDetails(String digitalGateChild) {
        String cabDeliveryReference = editCabOrVendorValue.getText().toString();
        String timeOfVisit = editPickDateTime.getText().toString();
        String validFor = Arrays.toString(buttonIds);
        String userUID = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getUID();
        NammaApartmentDigitalGate nammaApartmentDigitalGate = new NammaApartmentDigitalGate(cabDeliveryReference, timeOfVisit, validFor);
        nammaApartmentDigitalGate.getOwnersUID().put(userUID, true);
        DatabaseReference digitaGateReference = PRIVATE_USERS_REFERENCE.child(userUID)
                .child(FIREBASE_CHILD_MYDIGITALGATE);
        digitaGateReference.child(digitalGateChild).setValue(nammaApartmentDigitalGate);

    }


    private int getCarOrPackageArrivalTitle() {
        if (arrivalType == R.string.expecting_cab_arrival) {
            return R.string.cab_number;
        }
        return R.string.package_vendor;
    }


    /*Method for ValidFor 8 Button clicks*/
    private void selectButton(int id) {
        isValidForSelected = true;
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }

    /**
     * We are handling events for editText Cab or Vendor Name.
     */
    private void setEventsForEditText() {
        editCabOrVendorValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (arrivalType == R.string.expecting_cab_arrival) {
                    if (editCabOrVendorValue.length() == EDIT_TEXT_EMPTY_LENGTH) {
                        editCabOrVendorValue.setError(getString(R.string.please_fill_details));
                    }
                } else {
                    packageVendorName = editCabOrVendorValue.getText().toString().trim();
                    if (packageVendorName.length() == EDIT_TEXT_EMPTY_LENGTH || isValidPersonName(packageVendorName)) {
                        editCabOrVendorValue.setError(getString(R.string.accept_alphabets));
                    }
                }
            }
        });
    }
}
