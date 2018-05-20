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

public class EditDailyServicesDetails extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textDescription;
    private EditText editMemberAndServiceName, editMobileNumber, editPickInTime;
    private Button buttonUpdate;
    private String selectedTime;
    private String name;
    private String mobile;
    private String inTime;
    private String service_type;
    private boolean nameTextChanged = false;
    private boolean mobileTextChanged = false;
    private boolean timeTextChanged = false;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_edit_daily_services_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.edit_my_daily_service_details;
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

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickInTime.setInputType(InputType.TYPE_NULL);

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

        /*To fill Details Automatically in all EditTexts when screen is loaded*/
        fillDetails();

        /*Setting events for edit text*/
        setEventsForEditText();

        /*Setting event for views */
        editPickInTime.setOnFocusChangeListener(this);
        buttonUpdate.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickInTime:
                displayTime();
                break;

            case R.id.buttonUpdate:
                if (mobileTextChanged) {
                    Intent otpIntent = new Intent(EditDailyServicesDetails.this, OTP.class);
                    otpIntent.putExtra(Constants.OTP_TYPE, service_type);
                    startActivity(otpIntent);
                } else {
                    Intent mdsIntent = new Intent(EditDailyServicesDetails.this, DailyServicesHome.class);
                    mdsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mdsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mdsIntent);
                }
                finish();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            displayTime();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when screen is loaded and it will fill all the details
     * in editText accordingly
     */
    private void fillDetails() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        name = bundle.getString(Constants.NAME);
        mobile = bundle.getString(Constants.MOBILE_NUMBER);
        inTime = bundle.getString(Constants.IN_TIME);
        service_type = bundle.getString(Constants.SERVICE_TYPE, null);

        editMemberAndServiceName.setText(name);
        editMobileNumber.setText(mobile);
        editPickInTime.setText(inTime);
        String description = getResources().getString(R.string.send_otp_message).replace("visitor", service_type);
        textDescription.setText(description);
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
                if (editMobileNumber.length() > 0) {
                    textDescription.setVisibility(View.VISIBLE);
                    buttonUpdate.setVisibility(View.VISIBLE);
                    mobileTextChanged = true;
                } else {
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

    /**
     * This method is used to make textDescription and buttonUpdate Visible or Invisible
     */
    private void makeViewsVisibleOrInvisible() {
        if (nameTextChanged || mobileTextChanged || timeTextChanged) {
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
}
