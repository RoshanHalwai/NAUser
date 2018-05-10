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

    /*Declaring all the views*/
    TextView textVisitorName, textVisitorType, textInvitationDate, textInvitationTime, textInvitedBy, textVisitorNameValue, textVisitorTypeValue, textInvitationDateValue, textInvitationTimeValue, textInvitedByValue, textGivenThings, textDescription;
    EditText editDescription;
    Button buttonYes, buttonNo;
    int[] buttonIds = new int[]{R.id.buttonYes,
            R.id.buttonNo};

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things_guest;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.handing_things_to_guest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Initialising all the views*/
        textVisitorName = findViewById(R.id.textVisitorName);
        textVisitorType = findViewById(R.id.textVisitorType);
        textInvitationDate = findViewById(R.id.textInvitationDate);
        textInvitationTime = findViewById(R.id.textInvitationTime);
        textInvitedBy = findViewById(R.id.textInvitedBy);
        textVisitorNameValue = findViewById(R.id.textVisitorNameValue);
        textVisitorTypeValue = findViewById(R.id.textVisitorTypeValue);
        textInvitationDateValue = findViewById(R.id.textInvitationDateValue);
        textInvitationTimeValue = findViewById(R.id.textInvitationTimeValue);
        textInvitedByValue = findViewById(R.id.textInvitedByValue);
        textGivenThings = findViewById(R.id.textGivenThings);
        textDescription = findViewById(R.id.textDescription);
        editDescription = findViewById(R.id.editDescription);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        /*Setting fonts to the views*/
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

        /*By Default Button Selection*/
        selectButton(R.id.buttonNo);

        /*Method for radio button Yes*/
        buttonYes.setOnClickListener(v -> {
            textDescription.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.VISIBLE);
            editDescription.requestFocus();
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.showSoftInput(editDescription, InputMethodManager.SHOW_IMPLICIT);
            }
            selectButton(R.id.buttonYes);
        });

        /*Method for radio button No*/
        buttonNo.setOnClickListener(v -> {
            textDescription.setVisibility(View.GONE);
            editDescription.setVisibility(View.GONE);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);
            }
            selectButton(R.id.buttonNo);
        });
    }

    private void selectButton(int id) {
        for (int buttonId : buttonIds) {
            Button button = findViewById(buttonId);
            if (buttonId == id) {
                button.setBackgroundResource(R.drawable.button_guest_selected);
                button.setTextColor(Color.WHITE);
            } else {
                button.setBackgroundResource(R.drawable.button_guest_not_selected);
                button.setTextColor(Color.BLACK);

            }
        }
    }
}
