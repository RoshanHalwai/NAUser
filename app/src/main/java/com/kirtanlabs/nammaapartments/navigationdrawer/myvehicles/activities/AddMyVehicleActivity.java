package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    private Button buttonBike;
    private Button buttonCar;
    private EditText editCabStateCode;
    private EditText editCabRtoNumber;
    private EditText editCabSerialNumberOne;
    private EditText editCabSerialNumberTwo;
    private String vehicleType;

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

        TextView textVehicleType = findViewById(R.id.textVehicleType);
        TextView textVehicleNumber = findViewById(R.id.textVehicleNumber);
        editCabStateCode = findViewById(R.id.editCabStateCode);
        editCabRtoNumber = findViewById(R.id.editCabRtoNumber);
        editCabSerialNumberOne = findViewById(R.id.editCabSerialNumberOne);
        editCabSerialNumberTwo = findViewById(R.id.editCabSerialNumberTwo);
        buttonBike = findViewById(R.id.buttonBike);
        buttonCar = findViewById(R.id.buttonCar);
        Button buttonAddVehicle = findViewById(R.id.buttonAddVehicle);

        textVehicleType.setTypeface(setLatoBoldFont(this));
        textVehicleNumber.setTypeface(setLatoBoldFont(this));
        editCabStateCode.setTypeface(setLatoRegularFont(this));
        editCabRtoNumber.setTypeface(setLatoRegularFont(this));
        editCabSerialNumberOne.setTypeface(setLatoRegularFont(this));
        editCabSerialNumberTwo.setTypeface(setLatoRegularFont(this));
        buttonBike.setTypeface(setLatoRegularFont(this));
        buttonCar.setTypeface(setLatoRegularFont(this));
        buttonAddVehicle.setTypeface(setLatoLightFont(this));

        buttonBike.setOnClickListener(this);
        buttonCar.setOnClickListener(this);
        buttonAddVehicle.setOnClickListener(this);
        setEventsForEditText();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBike:
                vehicleType = getString(R.string.vehicle_type_bike);
                buttonBike.setBackgroundResource(R.drawable.selected_button_design);
                buttonCar.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonCar:
                vehicleType = getString(R.string.vehicle_type_car);
                buttonCar.setBackgroundResource(R.drawable.selected_button_design);
                buttonBike.setBackgroundResource(R.drawable.valid_for_button_design);
                break;
            case R.id.buttonAddVehicle:
                storeUserVehicleDetailsToFirebase();
                break;
        }
    }

    /**
     * Adds Events for Vehicle Number Edit Text, since we would want cursor to move to other EditText
     * views as and when user types in.
     */
    private void setEventsForEditText() {
        editCabStateCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == CAB_NUMBER_FIELD_LENGTH) {
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
            }
        });

    }

    private void storeUserVehicleDetailsToFirebase() {
        String vehicleNumber = editCabStateCode.getText().toString().trim() + HYPHEN + editCabRtoNumber.getText().toString().trim() + HYPHEN
                + editCabSerialNumberOne.getText().toString().trim() + HYPHEN + editCabSerialNumberTwo.getText().toString().trim();

        /*Create instance of Vehicle Class*/
        NammaApartmentUser nammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();
        String userName = nammaApartmentUser.getPersonalDetails().getFullName();
        Timestamp timestamp = new Timestamp(new Date().getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, YYY");
        Vehicle vehicle = new Vehicle(userName, vehicleNumber, vehicleType, dateFormat.format(timestamp));

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
