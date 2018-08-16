package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo.Vehicle;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_VEHICLES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.CAB_NUMBER_FIELD_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_VEHICLES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_VEHICLES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class AddMyVehicleActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Button buttonBike, buttonCar;
    private EditText editVehicleStateCode, editVehicleRtoNumber, editVehicleSerialNumberOne, editVehicleSerialNumberTwo;
    private String vehicleType;
    private TextView textErrorVehicleNumber, textErrorVehicleType;
    private Boolean isVehicleSelected = false;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_add_my_vehicle;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.add_my_vehicles;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textVehicleType = findViewById(R.id.textVehicleType);
        TextView textVehicleNumber = findViewById(R.id.textVehicleNumber);
        textErrorVehicleType = findViewById(R.id.textErrorVehicleType);
        textErrorVehicleNumber = findViewById(R.id.textErrorVehicleNumber);
        editVehicleStateCode = findViewById(R.id.editVehicleStateCode);
        editVehicleRtoNumber = findViewById(R.id.editVehicleRtoNumber);
        editVehicleSerialNumberOne = findViewById(R.id.editVehicleSerialNumberOne);
        editVehicleSerialNumberTwo = findViewById(R.id.editVehicleSerialNumberTwo);
        buttonBike = findViewById(R.id.buttonBike);
        buttonCar = findViewById(R.id.buttonCar);
        Button buttonAddVehicle = findViewById(R.id.buttonAddVehicle);

        /*Setting Fonts for all the views*/
        textVehicleType.setTypeface(setLatoBoldFont(this));
        textVehicleNumber.setTypeface(setLatoBoldFont(this));
        textErrorVehicleType.setTypeface(setLatoRegularFont(this));
        textErrorVehicleNumber.setTypeface(setLatoRegularFont(this));
        editVehicleStateCode.setTypeface(setLatoRegularFont(this));
        editVehicleRtoNumber.setTypeface(setLatoRegularFont(this));
        editVehicleSerialNumberOne.setTypeface(setLatoRegularFont(this));
        editVehicleSerialNumberTwo.setTypeface(setLatoRegularFont(this));
        buttonBike.setTypeface(setLatoRegularFont(this));
        buttonCar.setTypeface(setLatoRegularFont(this));
        buttonAddVehicle.setTypeface(setLatoLightFont(this));

        /*Setting event for views */
        buttonBike.setOnClickListener(this);
        buttonCar.setOnClickListener(this);
        buttonAddVehicle.setOnClickListener(this);

        /*Setting events for vehicle number edit texts*/
        setEventsForEditText();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Methods
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBike:
                isVehicleSelected = true;
                vehicleType = getString(R.string.vehicle_type_bike);
                buttonBike.setBackgroundResource(R.drawable.selected_button_design);
                buttonCar.setBackgroundResource(R.drawable.valid_for_button_design);
                textErrorVehicleType.setVisibility(View.GONE);
                break;
            case R.id.buttonCar:
                isVehicleSelected = true;
                vehicleType = getString(R.string.vehicle_type_car);
                buttonCar.setBackgroundResource(R.drawable.selected_button_design);
                buttonBike.setBackgroundResource(R.drawable.valid_for_button_design);
                textErrorVehicleType.setVisibility(View.GONE);
                break;
            case R.id.buttonAddVehicle:
                /*This method gets invoked to check all the editText fields and button validations.*/
                validateFields();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method gets invoked to check all the validation fields.
     */
    private void validateFields() {
        boolean fieldsFilled;
        String vehicleStateCode = editVehicleStateCode.getText().toString();
        String vehicleRtoNumber = editVehicleRtoNumber.getText().toString();
        String vehicleSerialNumberOne = editVehicleSerialNumberOne.getText().toString();
        String vehicleSerialNumberTwo = editVehicleSerialNumberTwo.getText().toString();
        fieldsFilled = isAllFieldsFilled(new EditText[]{editVehicleStateCode, editVehicleRtoNumber, editVehicleSerialNumberOne, editVehicleSerialNumberTwo}) && isVehicleSelected;
        /*This condition checks if all fields are not filled and if user presses add my vehicle button it will then display proper error messages.*/
        if (!fieldsFilled) {
            if (!isVehicleSelected) {
                textErrorVehicleType.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(vehicleStateCode) || TextUtils.isEmpty(vehicleRtoNumber) || TextUtils.isEmpty(vehicleSerialNumberOne) || TextUtils.isEmpty(vehicleSerialNumberTwo)) {
                textErrorVehicleNumber.setVisibility(View.VISIBLE);
            }
        }
        /*This condition checks for if user has filled all the fields and navigates to appropriate screen.*/
        if (fieldsFilled) {
            /*This method stores user vehicle details in Firebase.*/
            storeUserVehicleDetailsToFirebase();
        }
    }

    /**
     * Adds Events for Vehicle Number Edit Text, since we would want cursor to move to other EditText
     * views as and when user types in.
     */
    private void setEventsForEditText() {
        editVehicleStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorVehicleNumber.setVisibility(View.GONE);
                    editVehicleRtoNumber.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                    /*We check if the text user has entered is lowercase if it is in lowercase then we change it
                    to upper case*/
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editVehicleStateCode.setText(s);
                    editVehicleStateCode.setSelection(editVehicleStateCode.getText().length());
                }
            }
        });

        editVehicleRtoNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editVehicleStateCode.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorVehicleNumber.setVisibility(View.GONE);
                    editVehicleSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
            }
        });

        editVehicleSerialNumberOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editVehicleRtoNumber.requestFocus();
                } else if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
                    textErrorVehicleNumber.setVisibility(View.GONE);
                    editVehicleSerialNumberTwo.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                String s = et.toString();
                    /*We check if the text user has entered is lowercase if it is in lowercase then we change it
                    to upper case*/
                if (!s.equals(s.toUpperCase())) {
                    s = s.toUpperCase();
                    editVehicleSerialNumberOne.setText(s);
                    editVehicleSerialNumberOne.setSelection(editVehicleSerialNumberOne.getText().length());
                }
            }
        });

        editVehicleSerialNumberTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == EDIT_TEXT_EMPTY_LENGTH) {
                    editVehicleSerialNumberOne.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable et) {
                textErrorVehicleNumber.setVisibility(View.GONE);
            }
        });

    }

    /**
     * This method stores the user vehicle details in Firebase.
     */
    private void storeUserVehicleDetailsToFirebase() {
        String vehicleNumber = editVehicleStateCode.getText().toString().trim() + HYPHEN + editVehicleRtoNumber.getText().toString().trim() + HYPHEN
                + editVehicleSerialNumberOne.getText().toString().trim() + HYPHEN + editVehicleSerialNumberTwo.getText().toString().trim();

        /*Create instance of Vehicle Class*/
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String userName = nammaApartmentUser.getPersonalDetails().getFullName();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.US);
        Vehicle vehicle = new Vehicle(userName, vehicleNumber, vehicleType, dateFormat.format(new Date()));

        /*Get User Data Vehicle Reference*/
        DatabaseReference userVehiclesReference = ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                .child(FIREBASE_CHILD_VEHICLES);
        String userVehicleUID = userVehiclesReference.push().getKey();
        userVehiclesReference.child(userVehicleUID).setValue(true).addOnSuccessListener(aVoid -> {

            DatabaseReference allVehicleReference = ALL_VEHICLES_REFERENCE.child(vehicleNumber);
            allVehicleReference.setValue(userVehicleUID);

            DatabaseReference privateVehicleReference = PRIVATE_VEHICLES_REFERENCE.child(userVehicleUID);
            privateVehicleReference.setValue(vehicle);

            /* Once we are done with storing data we need to call MyVehiclesActivity screen again
             * to show users that their vehicles have been added successfully*/
            Intent MySweetHomeIntent = new Intent(AddMyVehicleActivity.this, MyVehiclesActivity.class);
            MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MySweetHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            showNotificationDialog(getString(R.string.vehicles_added_success_title),
                    getString(R.string.vehicles_added_success_message),
                    MySweetHomeIntent);
        });
    }

}
