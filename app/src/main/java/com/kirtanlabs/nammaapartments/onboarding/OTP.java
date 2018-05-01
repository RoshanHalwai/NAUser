package com.kirtanlabs.nammaapartments.onboarding;

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
import com.kirtanlabs.nammaapartments.onboarding.flatdetails.MyFlatDetails;

public class OTP extends BaseActivity {

    private EditText editFirstOTPDigit;
    private EditText editSecondOTPDigit;
    private EditText editThirdOTPDigit;
    private EditText editFourthOTPDigit;
    private EditText editFifthOTPDigit;
    private EditText editSixthOTPDigit;
    private Button buttonVerifyOTP;

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

        /*Getting Id's for all the views*/
        TextView textPhoneVerification = findViewById(R.id.textPhoneVerification);
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
        buttonVerifyOTP.setOnClickListener(view -> startActivity(new Intent(this, MyFlatDetails.class)));
    }

    /**
     * Once user enters a digit in one edit text we move the focus to next edit text
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
                buttonVerifyOTP.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

}
