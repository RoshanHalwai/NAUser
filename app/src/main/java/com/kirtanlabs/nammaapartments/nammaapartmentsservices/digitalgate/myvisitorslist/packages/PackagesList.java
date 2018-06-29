package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.packages;

import android.os.Bundle;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/28/2018
 */
public class PackagesList extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_guests_list;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.package_arrivals;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
