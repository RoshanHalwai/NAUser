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

public class SignUp extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setVisibility(View.INVISIBLE);

        /*Getting Id's for all the views*/
        TextView textFullName = findViewById(R.id.textFullName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textCountryCode = findViewById(R.id.textCountryCode);
        TextView textEmailId = findViewById(R.id.textEmailId);
        TextView textTermsAndConditions = findViewById(R.id.textTermsAndConditions);
        TextView textHaveAnAccount = findViewById(R.id.textHaveAnAccount);
        EditText editFullName = findViewById(R.id.editFullName);
        EditText editMobileNumber = findViewById(R.id.editMobileNumber);
        EditText editEmailId = findViewById(R.id.editEmailId);
        Button buttonSignUp = findViewById(R.id.buttonSignUp);

        /*Setting font for all the views*/
        textFullName.setTypeface(Constants.setLatoBoldFont(this));
        textMobileNumber.setTypeface(Constants.setLatoBoldFont(this));
        textCountryCode.setTypeface(Constants.setLatoBoldFont(this));
        textEmailId.setTypeface(Constants.setLatoBoldFont(this));
        textTermsAndConditions.setTypeface(Constants.setLatoRegularFont(this));
        textHaveAnAccount.setTypeface(Constants.setLatoRegularFont(this));
        editFullName.setTypeface(Constants.setLatoRegularFont(this));
        editMobileNumber.setTypeface(Constants.setLatoRegularFont(this));
        editEmailId.setTypeface(Constants.setLatoRegularFont(this));
        buttonSignUp.setTypeface(Constants.setLatoLightFont(this));

        buttonSignUp.setOnClickListener(view -> startActivity(new Intent(this, OTP.class)));
        textHaveAnAccount.setOnClickListener(view -> startActivity(new Intent(this, SignIn.class)));
    }
}
