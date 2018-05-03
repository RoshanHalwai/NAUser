package com.kirtanlabs.nammaapartments.onboarding.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.onboarding.OTP;
import com.kirtanlabs.nammaapartments.onboarding.login.SignIn;
import com.kirtanlabs.nammaapartments.onboarding.login.SignUp;

import java.util.Objects;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class SplashApartmentServices extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_apartment_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textApartmentServices = view.findViewById(R.id.textApartmentServices);
        TextView textApartmentServicesDesc = view.findViewById(R.id.textApartmentServicesDesc);
        Button buttonGetStarted = view.findViewById(R.id.buttonGetStarted);

        /*Setting font for all the views*/
        textApartmentServices.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(this.getActivity())));
        textApartmentServicesDesc.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(this.getActivity())));
        buttonGetStarted.setTypeface(Constants.setLatoLightFont(this.getActivity()));

        /*Setting event for Lets Get Started button*/
        buttonGetStarted.setOnClickListener(v -> startActivity(new Intent(getActivity(), SignIn.class)));
    }
}
