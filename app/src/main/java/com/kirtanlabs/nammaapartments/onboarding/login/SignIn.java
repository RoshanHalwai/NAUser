package com.kirtanlabs.nammaapartments.onboarding.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.regex.Pattern;

public class SignIn extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */
    private EditText editMobileNumber;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

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

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCreateAnAccount = findViewById(R.id.textCreateAnAccount);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonLogin = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCreateAnAccount.setTypeface(Constants.setLatoRegularFont(this));
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
            if (mobileNumber.length() < 10) {
                editMobileNumber.setError("Please Enter a valid 10 digit mobile number");
            }
            if ((mobileNumber.length() <= 0)) {
                editMobileNumber.setError("Please Enter Mobile Number");
            }
            if (isValidPhone(mobileNumber)) {
                Intent intentOTP = new Intent(SignIn.this, OTP.class);
                intentOTP.putExtra(Constants.SCREEN_TITLE, R.string.login);
                startActivity(intentOTP);
                finish();
            }
        }
    }


    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is to validate whether the user is entering a valid phone number or not.
     */
    private boolean isValidPhone(String phone) {
        boolean check;
        check = !Pattern.matches("[a-zA-Z]+", phone) && phone.length() >= 10;
        return check;
    }
}
