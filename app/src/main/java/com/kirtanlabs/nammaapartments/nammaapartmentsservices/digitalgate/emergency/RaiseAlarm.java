package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.emergency;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class RaiseAlarm extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textAlertRaised;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_raise_alarm;
    }

    @Override
    protected int getActivityTitle() {
        return getIntent().getIntExtra(Constants.ALARM_TYPE, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textCreateAlert = findViewById(R.id.textCreateAlert);
        textAlertRaised = findViewById(R.id.textAlertRaised);
        ImageButton buttonCreateAlert = findViewById(R.id.buttonCreateAlert);

        /*Setting font for all the views*/
        textCreateAlert.setTypeface(Constants.setLatoBoldFont(this));
        textAlertRaised.setTypeface(Constants.setLatoBoldFont(this));

        /*Setting event for image button*/
        buttonCreateAlert.setOnClickListener(v -> textAlertRaised.setVisibility(View.VISIBLE));
    }

}
