package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_MYVISITORS;
import static com.kirtanlabs.nammaapartments.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class HandedThings extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private int handed_Things_To;
    private TextView textVisitorAndServiceName;
    private TextView textInvitationDateAndRating;
    private TextView textInvitedByAndApartmentNo;
    private TextView textVisitorNameAndServiceNameValue;
    private TextView textVisitorAndServiceTypeValue;
    private TextView textInvitationDateAndRatingValue;
    private CircleImageView profileImage;
    private TextView textInvitedByAndApartmentNoValue;
    private TextView textDescription;
    private EditText editDescription;
    private Button buttonYes;
    private Button buttonNo;
    private Button buttonNotifyGate;
    private TextView textInvitationTimeValue;
    private CardView cardViewVisitors;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_handed_things;
    }

    @Override
    protected int getActivityTitle() {
        /*We use a common class for Handed Things to my Guest and handed Things to my Daily Services, we set the title
         * based on the user click on NotifyGate Home screen*/
        if (getIntent().getIntExtra(HANDED_THINGS_TO, 0) == R.string.handed_things_to_my_guest) {
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

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        //TODO: Write business logic to check if there are any visitors at resident house.
        /* If there are no visitors at resident house then we show
         * feature unavailable layout and pass some sensible message*/
        /*if (visitorCount == 0) {
            showFeatureUnavailableLayout(R.string.feature_unavailable_message);
        }*/

        /* We show current visitors list at resident house, so resident has
         * the ability to give things to their visitors and notify gate about it*/

        /*Initialising the CardView*/
        cardViewVisitors = findViewById(R.id.cardViewVisitors);

        /*Initialising all the views*/
        profileImage = findViewById(R.id.profileImage);
        textVisitorAndServiceName = findViewById(R.id.textVisitorAndServiceName);
        TextView textVisitorAndServiceType = findViewById(R.id.textVisitorAndServiceType);
        textInvitationDateAndRating = findViewById(R.id.textInvitationDate);
        TextView textInvitationTime = findViewById(R.id.textInvitationTime);
        textInvitedByAndApartmentNo = findViewById(R.id.textInvitedByAndApartmentNo);
        textVisitorNameAndServiceNameValue = findViewById(R.id.textVisitorAndServiceNameValue);
        textVisitorAndServiceTypeValue = findViewById(R.id.textVisitorAndServiceTypeValue);
        textInvitationDateAndRatingValue = findViewById(R.id.textInvitationDateValue);
        textInvitationTimeValue = findViewById(R.id.textInvitationTimeValue);
        textInvitedByAndApartmentNoValue = findViewById(R.id.textInvitedByAndApartmentNoValue);
        TextView textGivenThings = findViewById(R.id.textGivenThings);
        textDescription = findViewById(R.id.textDescription);
        editDescription = findViewById(R.id.editDescription);
        buttonNotifyGate = findViewById(R.id.buttonNotifyGate);
        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);

        /*Setting fonts to the views*/
        textVisitorAndServiceName.setTypeface(setLatoRegularFont(this));
        textVisitorAndServiceType.setTypeface(setLatoRegularFont(this));
        textInvitationDateAndRating.setTypeface(setLatoRegularFont(this));
        textInvitationTime.setTypeface(setLatoRegularFont(this));
        textInvitedByAndApartmentNo.setTypeface(setLatoRegularFont(this));

        textVisitorNameAndServiceNameValue.setTypeface(setLatoBoldFont(this));
        textVisitorAndServiceTypeValue.setTypeface(setLatoBoldFont(this));
        textInvitationDateAndRatingValue.setTypeface(setLatoBoldFont(this));
        textInvitationTimeValue.setTypeface(setLatoBoldFont(this));
        textInvitedByAndApartmentNoValue.setTypeface(setLatoBoldFont(this));

        textGivenThings.setTypeface(setLatoBoldFont(this));
        textDescription.setTypeface(setLatoBoldFont(this));
        editDescription.setTypeface(setLatoRegularFont(this));
        buttonYes.setTypeface(setLatoRegularFont(this));
        buttonNo.setTypeface(setLatoRegularFont(this));
        buttonNotifyGate.setTypeface(setLatoLightFont(this));

        /*Setting events for views*/
        buttonYes.setOnClickListener(this);
        buttonNo.setOnClickListener(this);
        buttonNotifyGate.setOnClickListener(this);

        /*Since we are using same layout for handed things to my guest and handed things to my daily services we need to
         * change some Titles in layout*/
        changeTitles();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        InputMethodManager inputMethodManager;
        switch (v.getId()) {
            case R.id.buttonYes:
                textDescription.setVisibility(View.VISIBLE);
                editDescription.setVisibility(View.VISIBLE);
                buttonNotifyGate.setVisibility(View.VISIBLE);
                editDescription.requestFocus();
                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.showSoftInput(editDescription, InputMethodManager.SHOW_IMPLICIT);
                }
                buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonYes.setTextColor(Color.WHITE);
                buttonNo.setTextColor(Color.BLACK);
                break;
            case R.id.buttonNo:
                textDescription.setVisibility(View.GONE);
                editDescription.setVisibility(View.GONE);
                buttonNotifyGate.setVisibility(View.GONE);
                inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(editDescription.getWindowToken(), 0);

                }
                buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                buttonYes.setTextColor(Color.BLACK);
                buttonNo.setTextColor(Color.WHITE);
                break;

            case R.id.buttonNotifyGate:
                if (isAllFieldsFilled(new EditText[]{editDescription})) {
                    createNotifyGateDialog();
                } else {
                    editDescription.setError(getString(R.string.please_fill_details));
                }
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Since we are using same layout for handed things to my guest and handed things to my daily services we need to
     * change some Titles in layout
     */
    private void changeTitles() {
        if (handed_Things_To == R.string.handed_things_to_my_daily_services) {
            hideProgressIndicator();
            cardViewVisitors.setVisibility(View.VISIBLE);
            String stringServiceName = getResources().getString(R.string.name) + ":";
            textVisitorAndServiceName.setText(stringServiceName);
            textVisitorNameAndServiceNameValue.setText("Ramesh");
            textVisitorAndServiceTypeValue.setText(R.string.cook);
            textInvitationDateAndRating.setText(R.string.rating);
            textInvitationDateAndRatingValue.setText("4.2");
            textInvitedByAndApartmentNo.setText(R.string.flats);
            textInvitedByAndApartmentNoValue.setText("3");
        } else {
            DatabaseReference handedThingsReference = PRIVATE_USERS_REFERENCE.child(NammaApartmentsGlobal.userUID)
                    .child(FIREBASE_CHILD_MYVISITORS);
            handedThingsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        hideProgressIndicator();
                        cardViewVisitors.setVisibility(View.GONE);
                    }
                    for (DataSnapshot visitorSnapshot : dataSnapshot.getChildren()) {
                        DatabaseReference preApprovedReference = PREAPPROVED_VISITORS_REFERENCE
                                .child(visitorSnapshot.getKey());
                        preApprovedReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                NammaApartmentVisitor nammaApartmentVisitor = dataSnapshot.getValue(NammaApartmentVisitor.class);
                                if (Objects.requireNonNull(nammaApartmentVisitor).getStatus().equals(Constants.ENTERED)) {
                                    Glide.with(getApplicationContext()).load(Objects.requireNonNull(nammaApartmentVisitor).getProfilePhoto()).into(profileImage);
                                    textVisitorNameAndServiceNameValue.setText(Objects.requireNonNull(nammaApartmentVisitor).getFullName());
                                    String dateAndTime = nammaApartmentVisitor.getDateAndTimeOfVisit();
                                    String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
                                    textInvitationDateAndRatingValue.setText(separatedDateAndTime[0]);
                                    textInvitationTimeValue.setText(separatedDateAndTime[1]);
                                    String userName = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getFullName();
                                    textInvitedByAndApartmentNoValue.setText(userName);
                                    cardViewVisitors.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                    hideProgressIndicator();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
