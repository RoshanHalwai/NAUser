package com.kirtanlabs.nammaapartments.onboarding.login;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.DailyServicesHome;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome.MySweetHome;

import java.util.concurrent.TimeUnit;

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
    private Button buttonResendOTP;
    private int previousScreenTitle;

    /* ------------------------------------------------------------- *
     * Private Members for Phone Authentication
     * ------------------------------------------------------------- */

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String userMobileNumber;
    private String phoneVerificationId;

    /* ------------------------------------------------------------- *
     * Private Members for Firebase
     * ------------------------------------------------------------- */

    private DatabaseReference userPrivateInfo;
    private FirebaseAuth fbAuth;

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

        fbAuth = FirebaseAuth.getInstance();

        /*Will generate an OTP to user's mobile number*/
        userMobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
        sendOTP();

        /* Since we wouldn't want the users to go back to previous screen,
         * hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Getting Id's for all the views*/
        textPhoneVerification = findViewById(R.id.textPhoneVerification);
        buttonVerifyOTP = findViewById(R.id.buttonVerifyOTP);
        buttonResendOTP = findViewById(R.id.buttonResendOTP);
        editFirstOTPDigit = findViewById(R.id.editFirstOTPDigit);
        editSecondOTPDigit = findViewById(R.id.editSecondOTPDigit);
        editThirdOTPDigit = findViewById(R.id.editThirdOTPDigit);
        editFourthOTPDigit = findViewById(R.id.editFourthOTPDigit);
        editFifthOTPDigit = findViewById(R.id.editFifthOTPDigit);
        editSixthOTPDigit = findViewById(R.id.editSixthOTPDigit);

        /*Setting font for all the views*/
        textPhoneVerification.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));
        buttonResendOTP.setTypeface(Constants.setLatoLightFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        buttonResendOTP.setEnabled(false);


        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting event for Verify OTP button*/
        buttonVerifyOTP.setOnClickListener(this);
        buttonResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP();
            }
        });

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
        if (v.getId() == R.id.buttonResendOTP) {
            switch (previousScreenTitle) {
                case R.string.resend_otp:
                    startActivity(new Intent(OTP.this, NammaApartmentsHome.class));
            }

        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This dialog pops up when users who are not registered in Firebase, try to login from the app
     */
    private void openAlertDialog() {
        AlertDialog.Builder alertRecordNotFoundDialog = new AlertDialog.Builder(this);
        View RecordNotFoundDialog = View.inflate(this, R.layout.record_not_found_dialog, null);
        alertRecordNotFoundDialog.setView(RecordNotFoundDialog).create();

        new Dialog(getApplicationContext());
        alertRecordNotFoundDialog.show();
    }

    private void sendOTP() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks);
    }

    /**
     * Resend OTP if the user doesn't receive it for the first time
     */
    private void resendOTP() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks, resendToken);
    }

    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //TODO: Toast message has been removed because successfully logged in user's are not required to view this Toast message.
                if (phoneAuthCredential.getSmsCode() != null) {
                    char[] smsCode = phoneAuthCredential.getSmsCode().toCharArray();
                    editFirstOTPDigit.setText(String.valueOf(smsCode[0]));
                    editSecondOTPDigit.setText(String.valueOf(smsCode[1]));
                    editThirdOTPDigit.setText(String.valueOf(smsCode[2]));
                    editFourthOTPDigit.setText(String.valueOf(smsCode[3]));
                    editFifthOTPDigit.setText(String.valueOf(smsCode[4]));
                    editSixthOTPDigit.setText(String.valueOf(smsCode[5]));
                }
                signInWithPhoneAuthCredential(phoneAuthCredential);
                buttonResendOTP.setEnabled(false);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this, "Verification Failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId = s;
                resendToken = forceResendingToken;
                buttonResendOTP.setEnabled(true);
            }
        };
    }

    /**
     * Checking if user's mobile number exists in Firebase or not
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        fbAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        userPrivateInfo = database.getReference(Constants.FIREBASE_CHILD_USERS).child(Constants.FIREBASE_CHILD_ALL);
                        userPrivateInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                /* Check if User already exists */
                                if (dataSnapshot.hasChild(userMobileNumber)) {
                                    startActivity(new Intent(OTP.this, NammaApartmentsHome.class));
                                }
                                /* User is Logging in for the first time */
                                else {
                                    openAlertDialog();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
    }

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
                checkEditTextFilled();
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
                checkEditTextFilled();
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
                checkEditTextFilled();
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
                checkEditTextFilled();
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
                checkEditTextFilled();
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
                checkEditTextFilled();
            }
        });
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

    /**
     * This method gets invoked to check if all the editTexts are filled and display the proper views.
     */
    private void checkEditTextFilled() {
        boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{editFirstOTPDigit, editSecondOTPDigit, editThirdOTPDigit, editFourthOTPDigit, editFifthOTPDigit, editSixthOTPDigit});
        if (allFieldsFilled) {
            buttonVerifyOTP.setVisibility(View.VISIBLE);
            buttonResendOTP.setVisibility(View.VISIBLE);
        }
        if (!allFieldsFilled) {
            buttonVerifyOTP.setVisibility(View.INVISIBLE);
            buttonResendOTP.setVisibility(View.VISIBLE);
        }
    }
}