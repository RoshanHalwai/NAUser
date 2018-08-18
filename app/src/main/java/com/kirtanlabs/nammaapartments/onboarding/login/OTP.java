package com.kirtanlabs.nammaapartments.onboarding.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.home.activities.NammaApartmentsHome;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices.AddDailyService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.mysweethome.AddFamilyMember;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OTP extends BaseActivity implements View.OnClickListener, View.OnKeyListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private int RESEND_OTP_SECONDS;
    private int RESEND_OTP_MINUTE;

    private EditText editFirstOTPDigit, editSecondOTPDigit, editThirdOTPDigit, editFourthOTPDigit, editFifthOTPDigit, editSixthOTPDigit;
    private TextView textPhoneVerification, textResendOTPOrVerificationMessage, textChangeNumberOrTimer;
    private Button buttonVerifyOTP;
    private int previousScreenTitle;

    /* ------------------------------------------------------------- *
     * Private Members for Phone Authentication
     * ------------------------------------------------------------- */

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String phoneVerificationId, userMobileNumber;

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

        /* Generate an OTP to user's mobile number */
        userMobileNumber = getIntent().getStringExtra(Constants.MOBILE_NUMBER);
        sendOTP();

        /* Start the Resend OTP timer, valid for 120 seconds*/
        textResendOTPOrVerificationMessage = findViewById(R.id.textResendOTPOrVerificationMessage);
        textChangeNumberOrTimer = findViewById(R.id.textChangeNumberOrTimer);
        startResendOTPTimer();

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
        textResendOTPOrVerificationMessage.setTypeface(Constants.setLatoRegularFont(this));
        textChangeNumberOrTimer.setTypeface(Constants.setLatoRegularFont(this));
        buttonVerifyOTP.setTypeface(Constants.setLatoLightFont(this));
        editFirstOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSecondOTPDigit. setTypeface(Constants.setLatoRegularFont(this));
        editThirdOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFourthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editFifthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));
        editSixthOTPDigit.setTypeface(Constants.setLatoRegularFont(this));

        /*Setting events for OTP edit text*/
        setEventsForEditText();

        /*Setting event for Verify OTP button*/
        buttonVerifyOTP.setOnClickListener(this);
        textResendOTPOrVerificationMessage.setOnClickListener(v -> resendOTP());
        textChangeNumberOrTimer.setOnClickListener(v -> navigateToPreviousScreen());
        editFirstOTPDigit.setOnKeyListener(this);
        editSecondOTPDigit.setOnKeyListener(this);
        editThirdOTPDigit.setOnKeyListener(this);
        editFourthOTPDigit.setOnKeyListener(this);
        editFifthOTPDigit.setOnKeyListener(this);
        editSixthOTPDigit.setOnKeyListener(this);

        /* Since multiple activities make use of this class we get previous
         * screen title and update the views accordingly*/
        getPreviousScreenTitle();
        updatePhoneVerificationText();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener and OnKey Listener Methods.
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonVerifyOTP) {
            hideKeyboard();
            /*displaying progress dialog while OTP is being validated*/
            showProgressDialog(this,
                    getResources().getString(R.string.verifying_account),
                    getResources().getString(R.string.please_wait_a_moment));

            boolean allFieldsFilled = isAllFieldsFilled(new EditText[]{
                    editFirstOTPDigit,
                    editSecondOTPDigit,
                    editThirdOTPDigit,
                    editFourthOTPDigit,
                    editFifthOTPDigit,
                    editSixthOTPDigit
            });
            if (allFieldsFilled) {
                hideKeyboard();
                String code = editFirstOTPDigit.getText().toString() + editSecondOTPDigit.getText().toString() +
                        editThirdOTPDigit.getText().toString() + editFourthOTPDigit.getText().toString() + editFifthOTPDigit.getText().toString() +
                        editSixthOTPDigit.getText().toString();
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(phoneVerificationId, code));
            }
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.editFirstOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editFirstOTPDigit.requestFocus();
                }
                break;
            case R.id.editSecondOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editSecondOTPDigit.requestFocus();
                }
                break;
            case R.id.editThirdOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editThirdOTPDigit.requestFocus();
                }
                break;
            case R.id.editFourthOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editFourthOTPDigit.requestFocus();
                }
                break;
            case R.id.editFifthOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editFifthOTPDigit.requestFocus();
                }
                break;
            case R.id.editSixthOTPDigit:
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editSixthOTPDigit.requestFocus();
                }
                break;
        }
        return false;
    }
    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    private void sendOTP() {
        setUpVerificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks);
    }

    private void setUpVerificationCallbacks() {
        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                /*displaying progress dialog while OTP is being validated*/
                showProgressDialog(OTP.this,
                        getResources().getString(R.string.verifying_account),
                        getResources().getString(R.string.please_wait_a_moment));

                /*Hiding the Keyboard in case the Auto-Verification is completed*/
                hideKeyboard();
                textResendOTPOrVerificationMessage.setText(R.string.auto_verification_completed);
                textResendOTPOrVerificationMessage.setEnabled(false);
                textChangeNumberOrTimer.setVisibility(View.INVISIBLE);
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
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                phoneVerificationId = s;
                resendToken = forceResendingToken;
            }

        };
    }

    /**
     * Checking if user's mobile number exists in Firebase or not
     */
    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        fbAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, (task) -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        if (previousScreenTitle == R.string.login) {
                            userPrivateInfo = Constants.ALL_USERS_REFERENCE.child(userMobileNumber);
                            userPrivateInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    /* Check if User mobile number is found in database */
                                    if (dataSnapshot.exists()) {
                                        startActivity(new Intent(OTP.this, NammaApartmentsHome.class));
                                    }
                                    /* User record was not found in firebase hence we navigate them to Sign Up page*/
                                    else {
                                        Intent intent = new Intent(OTP.this, SignUp.class);
                                        intent.putExtra(Constants.MOBILE_NUMBER, userMobileNumber);
                                        startActivity(intent);
                                    }
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            setResult(Activity.RESULT_OK, new Intent());
                            finish();
                        }
                    } else {
                        /*Check if network is available or not*/
                        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

                        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
                        boolean isConnected = activeNetwork != null &&
                                activeNetwork.isConnectedOrConnecting();
                        if (!isConnected) {
                            /*Show this message if user is having no network connection*/
                            textResendOTPOrVerificationMessage.setText(R.string.check_network_connection);
                        } else {
                            /*Show this message if user has entered wrong OTP*/
                            textResendOTPOrVerificationMessage.setText(R.string.wrong_otp_entered);
                        }
                    }
                });
    }

    /**
     * Resend OTP if the user doesn't receive after 120 seconds
     */
    private void resendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Constants.COUNTRY_CODE_IN + userMobileNumber,
                Constants.OTP_TIMER,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks,
                resendToken);
        startResendOTPTimer();
    }

    private void startResendOTPTimer() {
        textResendOTPOrVerificationMessage.setText(R.string.waiting_for_otp);
        RESEND_OTP_MINUTE = 1;
        RESEND_OTP_SECONDS = 59;
        String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
        textChangeNumberOrTimer.setText(timer);
        textChangeNumberOrTimer.setEnabled(false);
        textResendOTPOrVerificationMessage.setEnabled(false);
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    RESEND_OTP_SECONDS -= 1;
                    String timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                    textChangeNumberOrTimer.setText(timer);
                    if (RESEND_OTP_MINUTE == 0 && RESEND_OTP_SECONDS == 0) {
                        t.cancel();

                        /*User can either change their mobile number or Resend OTP to their mobile number*/
                        textChangeNumberOrTimer.setText(R.string.change_mobile_number);
                        textResendOTPOrVerificationMessage.setText(R.string.resend_otp);
                        textResendOTPOrVerificationMessage.setEnabled(true);
                        textChangeNumberOrTimer.setEnabled(true);
                    } else if (RESEND_OTP_SECONDS == 0) {
                        timer = timeFormatter(RESEND_OTP_MINUTE) + ":" + timeFormatter(RESEND_OTP_SECONDS);
                        textChangeNumberOrTimer.setText(timer);
                        RESEND_OTP_SECONDS = 60;
                        RESEND_OTP_MINUTE = RESEND_OTP_MINUTE - 1;
                    }
                });
            }
        }, 0, 1000);
    }

    private String timeFormatter(int time) {
        return String.format(Locale.ENGLISH, "%02d", time % 60);
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
                if (charSequence.length() == 1) {
                    editSecondOTPDigit.requestFocus();
                }
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
                if (charSequence.length() == 1) {
                    editThirdOTPDigit.requestFocus();
                }
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
                if (charSequence.length() == 1) {
                    editFourthOTPDigit.requestFocus();
                }
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
                if (charSequence.length() == 1) {
                    editFifthOTPDigit.requestFocus();
                }
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
                if (charSequence.length() == 1) {
                    editSixthOTPDigit.requestFocus();
                }
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
                if (charSequence.length() == 1) {
                    buttonVerifyOTP.setVisibility(View.VISIBLE);
                    hideKeyboard();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
            case R.string.add_my_daily_service:
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
     * This method gets invoked when user wants to change mobile number based on the previous screen
     * it will navigate to that appropriate screen.
     */
    private void navigateToPreviousScreen() {
        switch (previousScreenTitle) {
            case R.string.login:
                Intent loginIntent = new Intent(OTP.this, SignIn.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
                break;
            case R.string.add_my_daily_service:
                Intent dailyServicesIntent = new Intent(OTP.this, AddDailyService.class);
                dailyServicesIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dailyServicesIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(dailyServicesIntent);
                break;
            case R.string.add_family_members_details_screen:
                Intent familyMemberIntent = new Intent(OTP.this, AddFamilyMember.class);
                familyMemberIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                familyMemberIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(familyMemberIntent);
                break;
        }
    }
}
