package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;

import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.login.OTP;

import java.util.Calendar;
import java.util.Locale;

public class EditDailyServicesDetails extends BaseActivity {

    private EditText editMemberAndServiceName, editMobileNumber, editPickInTime;
    private TextView textDescription;
    private Button buttonUpdate;
    private String selectedTime;
    private String name;
    private String mobile;
    private String inTime;
    private String navigateTo;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_daily_services_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.edit;
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
        textDescription = findViewById(R.id.textDescription);
        editMemberAndServiceName = findViewById(R.id.editMemberAndServiceName);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        editPickInTime = findViewById(R.id.editPickInTime);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        /*Setting font for all the views*/
        textMemberAndServiceName.setTypeface(Constants.setLatoBoldFont(this));
        textMemberAndServiceMobile.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoItalicFont(this));
        textInTime.setTypeface(Constants.setLatoBoldFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editMemberAndServiceName.setTypeface(Constants.setLatoRegularFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        editPickInTime.setTypeface(Constants.setLatoRegularFont(this));
        buttonUpdate.setTypeface(Constants.setLatoLightFont(this));

        /*To fill Details Automatically in EditText when screen in loaded*/
        fillDetails();

        /*Setting events for edit text*/
        setEventsForEditText();

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickInTime.setInputType(InputType.TYPE_NULL);

        /*Setting event for  Displaying Date & Time*/
        editPickInTime.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                displayTime();
            }
        });
        editPickInTime.setOnClickListener(View -> displayTime());

        buttonUpdate.setOnClickListener(v -> {
            if (navigateTo.equals(getResources().getString(R.string.verify_otp))) {
                Intent otpIntent = new Intent(EditDailyServicesDetails.this, OTP.class);
                otpIntent.putExtra(Constants.SCREEN_TYPE, R.string.edit);
                startActivity(otpIntent);
            } else {
                Intent mdsIntent = new Intent(EditDailyServicesDetails.this, DailyServicesHome.class);
                startActivity(mdsIntent);
            }
            finish();
        });
    }

    private void fillDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        name = bundle.getString(Constants.NAME);
        mobile = bundle.getString(Constants.MOBILE_NUMBER);
        inTime = bundle.getString("inTime");
        String service_type = bundle.getString(Constants.SERVICE_TYPE, null);

        editMemberAndServiceName.setText(name);
        editMobileNumber.setText(mobile);
        editPickInTime.setText(inTime);
        String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
        textDescription.setText(description);
    }

    private void setEventsForEditText() {
        editMemberAndServiceName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editMemberAndServiceName.length() > 0) {
                    buttonUpdate.setVisibility(View.VISIBLE);
                    navigateTo = getResources().getString(R.string.my_daily_services);
                } else {
                    makeInvisibleButtonAndText();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editMemberAndServiceName.getText().toString().equals(name)) {
                    makeInvisibleButtonAndText();
                }
            }
        });

        editMobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editMobileNumber.length() > 0) {
                    textDescription.setVisibility(View.VISIBLE);
                    buttonUpdate.setVisibility(View.VISIBLE);
                    navigateTo = getResources().getString(R.string.verify_otp);
                } else {
                    makeInvisibleButtonAndText();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editMobileNumber.getText().toString().equals(mobile)) {
                    makeInvisibleButtonAndText();
                }
            }
        });

        editPickInTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonUpdate.setVisibility(View.VISIBLE);
                navigateTo = getResources().getString(R.string.my_daily_services);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editPickInTime.getText().toString().equals(inTime)) {
                    makeInvisibleButtonAndText();
                }
            }
        });
    }

    private void makeInvisibleButtonAndText() {
        textDescription.setVisibility(View.INVISIBLE);
        buttonUpdate.setVisibility(View.INVISIBLE);
    }


    /**
     * This method is invoked when user clicks on pick time icon.
     */
    private void displayTime() {
        Calendar calendar = Calendar.getInstance();
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedTime = "";
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    editPickInTime.setText(selectedTime);
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
}
