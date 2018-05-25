package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;


/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/14/2018
 */
public class ExpectingArrival extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

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
    private int arrivalType;


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
        if (getIntent().getIntExtra(Constants.ARRIVAL_TYPE, 0) == R.string.expecting_cab_arrival) {
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
        EditText editCabOrVendorValue = findViewById(R.id.editCabOrVendorValue);
        EditText editPickDateTime = findViewById(R.id.editPickDateTime);
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
        textCabOrVendorTitle.setTypeface(Constants.setLatoBoldFont(this));
        textDateTime.setTypeface(Constants.setLatoBoldFont(this));
        textValidFor.setTypeface(Constants.setLatoBoldFont(this));
        editPickDateTime.setTypeface(Constants.setLatoRegularFont(this));
        editCabOrVendorValue.setTypeface(Constants.setLatoRegularFont(this));
        button1hr.setTypeface(Constants.setLatoRegularFont(this));
        button2hr.setTypeface(Constants.setLatoRegularFont(this));
        button4hr.setTypeface(Constants.setLatoRegularFont(this));
        button6hr.setTypeface(Constants.setLatoRegularFont(this));
        button8hr.setTypeface(Constants.setLatoRegularFont(this));
        button12hr.setTypeface(Constants.setLatoRegularFont(this));
        button16hr.setTypeface(Constants.setLatoRegularFont(this));
        button24hr.setTypeface(Constants.setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same layout for Expecting cab and package arrival we need to
         * set text for textCabOrVendorTitle to either Package Vendor Name or Cab Number*/
        textCabOrVendorTitle.setText(getCarOrPackageArrivalTitle());

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
                pickDate(R.id.editPickDateTime, true);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            pickDate(R.id.editPickDateTime, true);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private int getCarOrPackageArrivalTitle() {
        if (arrivalType == R.string.expecting_cab_arrival) {
            return R.string.cab_number;
        }
        return R.string.package_vendor;
    }


    /*Method for ValidFor 8 Button clicks*/
    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                button.setBackgroundResource(R.drawable.selected_button_design);
            } else {
                button.setBackgroundResource(R.drawable.valid_for_button_design);
            }
        }
    }
}
