package com.kirtanlabs.nammaapartments.onboarding.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class SignIn extends BaseActivity {

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
        EditText editMobileNumber = findViewById(R.id.editDailyServiceOrFamilyMemberMobile);
        Button buttonSignIn = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCreateAnAccount.setTypeface(Constants.setLatoRegularFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonSignIn.setTypeface(Constants.setLatoLightFont(this));

        /*Setting event for Login button*/
        buttonSignIn.setOnClickListener(view -> {
            startActivity(new Intent(SignIn.this, OTP.class));
            finish();
        });

        /*Currently the App does not allow the user to Sign Up*/
        //textCreateAnAccount.setOnClickListener(view -> startActivity(new Intent(this, SignUp.class)));
    }
}
