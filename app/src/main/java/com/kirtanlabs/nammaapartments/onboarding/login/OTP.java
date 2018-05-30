package com.kirtanlabs.nammaapartments.onboarding.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.DailyServicesHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;

public class OTP extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editFirstOTPDigit;
    private EditText editSecondOTPDigit;
    private EditText editThirdOTPDigit;
    private EditText editFourthOTPDigit;
    private EditText editFifthOTPDigit;
    private EditText editSixthOTPDigit;
    private TextView textPhoneVerification;
    private Button buttonVerifyOTP;
    private int previousScreenTitle;
    private boolean allFieldsFilled;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_otp;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.phone_verification_activity_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        textPhoneVerification = findViewById(R.id.textPhoneVerification);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        editFirstOTPDigit = findViewById(R.id.editFirstOTPDigit);
        editSecondOTPDigit = findViewById(R.id.editSecondOTPDigit);
        editThirdOTPDigit = findViewById(R.id.editThirdOTPDigit);
        editFourthOTPDigit = findViewById(R.id.editFourthOTPDigit);
        editFifthOTPDigit = findViewById(R.id.editFifthOTPDigit);
        editSixthOTPDigit = findViewById(R.id.editSixthOTPDigit);

        /*Setting font for all the views*/
        textPhoneVerification.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting event for Verify OTP button*/
        buttonVerifyOTP.setOnClickListener(this);

        /* Since multiple activities make use of this class we get previous
         * screen title and update the views accordingly*/
        getPreviousScreenTitle();
        updatePhoneVerificationText();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonVerifyOTP) {
            switch (previousScreenTitle) {
                case R.string.login:
                    startActivity(new Intent(OTP.this, NammaApartmentsHome.class));
                    break;
                case R.string.add_my_service:
                    Intent intentDailyServiceHome = new Intent(OTP.this, DailyServicesHome.class);
                    intentDailyServiceHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentDailyServiceHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentDailyServiceHome);
                    break;
                case R.string.add_family_members_details_screen:
                    Intent intentMySweetHome = new Intent(OTP.this, MySweetHome.class);
                    intentMySweetHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intentMySweetHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentMySweetHome);
                    break;
            }
            finish();
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void getPreviousScreenTitle() {
        previousScreenTitle = getIntent().getIntExtra(Constants.SCREEN_TITLE, 0);
    }

    /**
     * Once user enters a digit in one edit text we move the cursor to next edit text
     */
    private void setEventsForEditText() {
        editFirstOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editSecondOTPDigit.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editSecondOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editThirdOTPDigit.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editThirdOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editFourthOTPDigit.requestFocus();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editFourthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editFifthOTPDigit.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editFifthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editSixthOTPDigit.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        editSixthOTPDigit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                allFieldsFilled = isAllFieldsFilled(new EditText[]{editFirstOTPDigit, editSecondOTPDigit, editThirdOTPDigit, editFourthOTPDigit, editFifthOTPDigit, editSixthOTPDigit});
                if (allFieldsFilled) {
                    buttonVerifyOTP.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * This method gets invoked to check if all the editTexts are filled or not.
     * @param fields consists of array of editText fields.
     * @return boolean variable which returns true or false based on the context.
     */
    private boolean isAllFieldsFilled(EditText[] fields) {
        for (EditText currentField : fields) {
            if (currentField.getText().toString().length() <= 0) {
                currentField.setError("Please Enter a Valid OTP code");
                currentField.requestFocus();
                buttonVerifyOTP.setVisibility(View.VISIBLE);
                return false;
            }
        }
        return true;
    }
    /**
     * We update the Phone Verification text based on the activity calling this class.
     */
    private void updatePhoneVerificationText() {
        switch (previousScreenTitle) {
            case R.string.login:
                textPhoneVerification.setText(R.string.enter_verification_code);
                break;
            case R.string.add_my_service:
                String service_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
                String description = getResources().getString(R.string.enter_verification_code);
                description = description.replace("account", service_type + " account");
                description = description.replace("your mobile", "their mobile");
                textPhoneVerification.setText(description);
                break;
            case R.string.add_family_members_details_screen:
                String screen_type = getIntent().getStringExtra(Constants.SERVICE_TYPE);
                String otpDescription = getResources().getString(R.string.enter_verification_code).replace("account", screen_type + " account");
                otpDescription = otpDescription.replace("your mobile", "their mobile");
                textPhoneVerification.setText(otpDescription);
        }
    }

}