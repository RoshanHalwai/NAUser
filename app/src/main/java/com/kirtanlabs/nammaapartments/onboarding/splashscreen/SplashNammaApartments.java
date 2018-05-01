package com.kirtanlabs.nammaapartments.onboarding.splashscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.Objects;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

public class SplashNammaApartments extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.splash_namma_apartments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textNammaApartments = view.findViewById(R.id.textNammaApartments);
        TextView textNammaApartmentsDesc = view.findViewById(R.id.textNammaApartmentsDesc);

        /*Setting font for all the views*/
        textNammaApartments.setTypeface(Constants.setLatoBoldFont(Objects.requireNonNull(this.getActivity())));
        textNammaApartmentsDesc.setTypeface(Constants.setLatoRegularFont(Objects.requireNonNull(this.getActivity())));
    }

}
