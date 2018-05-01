package com.kirtanlabs.nammaapartments.onboarding.flatdetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/2/2018
 */

public class MyFlatDetails extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_flat_details;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_flat_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textCity = findViewById(R.id.textCity);
        TextView textSociety = findViewById(R.id.textSociety);
        TextView textApartment = findViewById(R.id.textApartment);
        TextView textFlat = findViewById(R.id.textFlat);
        TextView textResidentType = findViewById(R.id.textResidentType);
        TextView textVerificationMessage = findViewById(R.id.textVerificationMessage);
        EditText editCity = findViewById(R.id.editCity);
        EditText editSociety = findViewById(R.id.editSociety);
        EditText editApartment = findViewById(R.id.editApartment);
        EditText editFlat = findViewById(R.id.editFlat);
        Button buttonContinue = findViewById(R.id.buttonContinue);

        /*Setting font for all the views*/
        textCity.setTypeface(Constants.setLatoBoldFont(this));
        textSociety.setTypeface(Constants.setLatoBoldFont(this));
        textApartment.setTypeface(Constants.setLatoBoldFont(this));
        textFlat.setTypeface(Constants.setLatoBoldFont(this));
        textResidentType.setTypeface(Constants.setLatoBoldFont(this));
        textVerificationMessage.setTypeface(Constants.setLatoRegularFont(this));
        editCity.setTypeface(Constants.setLatoRegularFont(this));
        editSociety.setTypeface(Constants.setLatoRegularFont(this));
        editApartment.setTypeface(Constants.setLatoRegularFont(this));
        editFlat.setTypeface(Constants.setLatoRegularFont(this));
        buttonContinue.setTypeface(Constants.setLatoLightFont(this));
    }
}
