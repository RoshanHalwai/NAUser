package com.kirtanlabs.nammaapartments.onboarding.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.OTP;

public class SignIn extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.signin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.INVISIBLE);

        /*Getting Id's for all the views*/
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCreateAnAccount = findViewById(R.id.textCreateAnAccount);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        EditText editMobileNumber = findViewById(R.id.editMobileNumber);
        Button buttonSignIn = findViewById(R.id.buttonLogin);

        /*Setting font for all the views*/
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCreateAnAccount.setTypeface(Constants.setLatoRegularFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        buttonSignIn.setTypeface(Constants.setLatoLightFont(this));

        /*Setting event for LOGIN button*/
        buttonSignIn.setOnClickListener(view -> startActivity(new Intent(this, OTP.class)));
        textCreateAnAccount.setOnClickListener(view -> startActivity(new Intent(this, SignUp.class)));
    }
}
