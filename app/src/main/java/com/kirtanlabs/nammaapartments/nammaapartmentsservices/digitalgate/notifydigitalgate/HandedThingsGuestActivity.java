package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class HandedThingsGuestActivity extends BaseActivity {

    private TextView textDescription;
    private EditText editDescription;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonNotifyGate;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things_guest;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.handed_things_to_my_guest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Initialising all the views*/
        TextView textFeatureUnavailable = findViewById(R.id.textFeatureUnavailable);
        TextView textVisitorName = findViewById(R.id.textVisitorName);
        TextView textVisitorType = findViewById(R.id.textVisitorType);
        TextView textInvitationDate = findViewById(R.id.textInvitationDate);
        TextView textInvitationTime = findViewById(R.id.textInvitationTime);
        TextView textInvitedBy = findViewById(R.id.textInvitedBy);
        TextView textVisitorNameValue = findViewById(R.id.textVisitorNameValue);
        TextView textVisitorTypeValue = findViewById(R.id.textVisitorTypeValue);
        TextView textInvitationDateValue = findViewById(R.id.textInvitationDateValue);
        TextView textInvitationTimeValue = findViewById(R.id.textInvitationTimeValue);
        TextView textInvitedByValue = findViewById(R.id.textInvitedByValue);
        TextView textGivenThings = findViewById(R.id.textGivenThings);
        textDescription = findViewById(R.id.textDescription);
        editDescription = findViewById(R.id.editDescription);
        buttonNotifyGate = findViewById(R.id.buttonNotifyGate);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        /*Setting fonts to the views*/
        textFeatureUnavailable.setTypeface(Constants.setLatoRegularFont(this));
        textVisitorName.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorType.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationDate.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationTime.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedBy.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorTypeValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationDateValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedByValue.setTypeface(Constants.setLatoBoldFont(this));
        textGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editDescription.setTypeface(Constants.setLatoRegularFont(this));
        buttonYes.setTypeface(Constants.setLatoRegularFont(this));
        buttonNo.setTypeface(Constants.setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(Constants.setLatoLightFont(this));

        /*Method for radio button Yes*/
        buttonYes.setOnClickListener(v -> {
            textDescription.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.VISIBLE);
            buttonNotifyGate.setVisibility(View.VISIBLE);
            editDescription.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(editDescription, InputMethodManager.SHOW_IMPLICIT);
            }
            buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonYes.setTextColor(Color.WHITE);
            buttonNo.setTextColor(Color.BLACK);
        });

        /*Method for radio button No*/
        buttonNo.setOnClickListener(v -> {
            textDescription.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);
            buttonNotifyGate.setVisibility(View.GONE);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);

            }
            buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
            buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
            buttonYes.setTextColor(Color.BLACK);
            buttonNo.setTextColor(Color.WHITE);
        });
    }
}
