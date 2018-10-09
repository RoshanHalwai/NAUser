package com.kirtanlabs.nammaapartments.navigationdrawer.myprofile.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoItalicFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;

public class GatePassActivity extends BaseActivity {

    //TODO: Download Gate Pass functionality to be implemented

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textSocietyValue, textUserNameValue, textApartmentValue, textFlatValue;
    private CircleImageView currentUserProfilePic;
    private NammaApartmentUser currentNammaApartmentUser;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_gate_pass;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.gate_pass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Here we are getting the instance of current user when screen loads for the first time*/
        currentNammaApartmentUser = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser();

        /*Getting Id's for all the views*/
        currentUserProfilePic = findViewById(R.id.currentUserProfilePic);
        textSocietyValue = findViewById(R.id.textSocietyValue);
        textUserNameValue = findViewById(R.id.textUserNameValue);
        textApartmentValue = findViewById(R.id.textApartmentValue);
        textFlatValue = findViewById(R.id.textFlatValue);
        TextView textWelcomeMessage = findViewById(R.id.textWelcomeMessage);
        Button buttonDownloadGatePass = findViewById(R.id.buttonDownloadGatePass);

        /*Setting font for all the views*/
        textSocietyValue.setTypeface(setLatoBoldFont(this));
        textUserNameValue.setTypeface(setLatoBoldFont(this));
        textApartmentValue.setTypeface(setLatoBoldFont(this));
        textFlatValue.setTypeface(setLatoBoldFont(this));
        textWelcomeMessage.setTypeface(setLatoItalicFont(this));
        buttonDownloadGatePass.setTypeface(setLatoLightFont(this));

        /*Called when user presses on 'Download Gate Pass' button in 'My Profile' screen*/
        retrieveUserDetails();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve all the details of the current user and displays it in the Gate Pass screen
     */
    private void retrieveUserDetails() {
        /*Getting values of all the required fields*/
        String userName = currentNammaApartmentUser.getPersonalDetails().getFullName();
        String societyName = currentNammaApartmentUser.getFlatDetails().getSocietyName();
        String apartmentName = currentNammaApartmentUser.getFlatDetails().getApartmentName();
        String flatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();

        /*Setting values of all text views*/
        textUserNameValue.setText(userName);
        textSocietyValue.setText(societyName);
        textApartmentValue.setText(apartmentName);
        textFlatValue.setText(flatNumber);
        Glide.with(this.getApplicationContext()).load(currentNammaApartmentUser.getPersonalDetails().getProfilePhoto()).into(currentUserProfilePic);
        currentUserProfilePic.setTag(R.id.currentUserProfilePic, "Actual Image");
    }

}
