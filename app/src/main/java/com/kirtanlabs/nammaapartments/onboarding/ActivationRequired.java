package com.kirtanlabs.nammaapartments.onboarding;

import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;

public class ActivationRequired extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_activiation_required;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.activation_required_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideBackButton();

        TextView textActivationRequiredMessage = findViewById(R.id.textActivationRequiredMessage);
        textActivationRequiredMessage.setTypeface(setLatoItalicFont(this));

    }
}
