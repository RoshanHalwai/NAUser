package com.kirtanlabs.nammaapartments;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.home.fragments.ApartmentServicesHome;
import com.kirtanlabs.nammaapartments.home.fragments.SocietyServicesHome;
import com.kirtanlabs.nammaapartments.utilities.Constants;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBackButton();

        /*Getting Id's for all the views*/
        TextView textSocietyServices = findViewById(R.id.textSocietyServices);
        TextView textApartmentServices = findViewById(R.id.textApartmentServices);

        /*Setting font for all the views*/
        textSocietyServices.setTypeface(Constants.setLatoBoldFont(this));
        textApartmentServices.setTypeface(Constants.setLatoBoldFont(this));

        /*Setting event for text views*/
        textSocietyServices.setOnClickListener(v -> startActivity(new Intent(this, SocietyServicesHome.class)));
        textApartmentServices.setOnClickListener(v -> startActivity(new Intent(this, ApartmentServicesHome.class)));
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.app_name;
    }

}
