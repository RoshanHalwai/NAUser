package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class HandedThings extends BaseActivity {

    private int handed_Things_To;
    private TextView textVisitorAndServiceName;
    private TextView textInvitationDateAndRating;
    private TextView textInvitedByAndApartmentNo;
    private TextView textVisitorNameAndServiceNameValue;
    private TextView textVisitorAndServiceTypeValue;
    private TextView textInvitationDateAndRatingValue;
    private TextView textInvitedByAndApartmentNoValue;
    private TextView textDescription;
    private EditText editDescription;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonNotifyGate;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Handed Things to my Guest and handed Things to my Daily Services, we set the title
         * based on the user click on NotifyGate Home screen*/
        if (getIntent().getIntExtra(Constants.HANDED_THINGS_TO, 0) == R.string.handed_things_to_my_guest) {
            handed_Things_To = R.string.handed_things_to_my_guest;
        } else {
            handed_Things_To = R.string.handed_things_to_my_daily_services;
        }
        return handed_Things_To;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        //TODO: Write business logic to check if there are any visitors at resident house.
        /* If there are no visitors at resident house then we show
         * feature unavailable layout and pass some sensible message*/
        /*if (visitorCount == 0) {
            showFeatureUnavailableLayout(R.string.feature_unavailable_message);
        }*/

        /* We show current visitors list at resident house, so resident has
         * the ability to give things to their visitors and notify gate about it*/
        CardView cardViewVisitors = findViewById(R.id.cardViewVisitors);
        cardViewVisitors.setVisibility(View.VISIBLE);

        /*Initialising all the views*/
        textVisitorAndServiceName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorAndServiceType = findViewById(R.id.textVisitorAndServiceType);
        textInvitationDateAndRating = findViewById(R.id.textInvitationDate);
        TextView textInvitationTime = findViewById(R.id.textInvitationTime);
        textInvitedByAndApartmentNo = findViewById(R.id.textInvitedByAndApartmentNo);
        textVisitorNameAndServiceNameValue = findViewById(R.id.textVisitorAndServiceNameValue);
        textVisitorAndServiceTypeValue = findViewById(R.id.textVisitorAndServiceTypeValue);
        textInvitationDateAndRatingValue = findViewById(R.id.textInvitationDateValue);
        TextView textInvitationTimeValue = findViewById(R.id.textInvitationTimeValue);
        textInvitedByAndApartmentNoValue = findViewById(R.id.textInvitedByAndApartmentNoValue);
        TextView textGivenThings = findViewById(R.id.textGivenThings);
        textDescription = findViewById(R.id.textDescriptionDailyService);
        editDescription = findViewById(R.id.editDescription);
        buttonNotifyGate = findViewById(R.id.buttonNotifyGate);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        /*Setting fonts to the views*/
        textVisitorAndServiceName.setTypeface(Constants.setLatoRegularFont(this));
        textVisitorAndServiceType.setTypeface(Constants.setLatoRegularFont(this));
        textInvitationDateAndRating.setTypeface(Constants.setLatoRegularFont(this));
        textInvitationTime.setTypeface(Constants.setLatoRegularFont(this));
        textInvitedByAndApartmentNo.setTypeface(Constants.setLatoRegularFont(this));

        textVisitorNameAndServiceNameValue.setTypeface(Constants.setLatoBoldFont(this));
        textVisitorAndServiceTypeValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationDateAndRatingValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(this));
        textInvitedByAndApartmentNoValue.setTypeface(Constants.setLatoBoldFont(this));

        textGivenThings.setTypeface(Constants.setLatoBoldFont(this));
        textDescription.setTypeface(Constants.setLatoBoldFont(this));
        editDescription.setTypeface(Constants.setLatoRegularFont(this));
        buttonYes.setTypeface(Constants.setLatoRegularFont(this));
        buttonNo.setTypeface(Constants.setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(Constants.setLatoLightFont(this));

        /*Since we are using same layout for handed things to my guest and handed things to my daily services we need to
         * change some Titles in layout*/
        changeTitles();

        /*Method for button Yes*/
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

        /*Method for button No*/
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

    private void changeTitles() {
        if (handed_Things_To == R.string.handed_things_to_my_daily_services) {
            String stringServiceName = getResources().getString(R.string.name) + ":";
            textVisitorAndServiceName.setText(stringServiceName);
            textVisitorNameAndServiceNameValue.setText("Ramesh");
            textVisitorAndServiceTypeValue.setText(R.string.cook);
            textInvitationDateAndRating.setText(R.string.rating);
            textInvitationDateAndRatingValue.setText("4.2");
            textInvitedByAndApartmentNo.setText(R.string.flats);
            textInvitedByAndApartmentNoValue.setText("3");
        }
    }

}
