package com.kirtanlabs.nammaapartments.onboarding.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import static com.kirtanlabs.nammaapartments.utilities.Constants.EDIT_TEXT_EMPTY_LENGTH;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PHONE_NUMBER_MAX_LENGTH;

public class SignIn extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    @SuppressLint("StaticFieldLeak")
    private static SignIn signIn;
    private EditText editMobileNumber;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    /**
     * This method returns the Instance of this class
     *
     * @return static variable of this class instance.
     */
    public static SignIn getInstance() {
        return signIn;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Since this is Login Screen we wouldn't want the users to go back to Splash screen,
        hence hiding the back button from the Title Bar*/
        hideBackButton();

        /*Initialising the static variable with the current context.*/
        signIn = this;

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonLogin.setTypeface(Constants.setLatoLightFont(this));

        /*Setting onClickListener for view*/
        buttonLogin.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Methods
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonLogin) {
            String mobileNumber = editMobileNumber.getText().toString().trim();
            if (mobileNumber.length() < PHONE_NUMBER_MAX_LENGTH) {
                editMobileNumber.setError(getString(R.string.number_10digit_validation));
            }
            if ((mobileNumber.length() == EDIT_TEXT_EMPTY_LENGTH)) {
                editMobileNumber.setError(getString(R.string.mobile_number_validation));
            }
            if (isValidPhone(mobileNumber)) {
                Intent intentOTP = new Intent(SignIn.this, OTP.class);
                intentOTP.putExtra(Constants.SCREEN_TITLE, R.string.login);
                intentOTP.putExtra(Constants.MOBILE_NUMBER, mobileNumber);
                startActivity(intentOTP);
            }
        }
    }

}
