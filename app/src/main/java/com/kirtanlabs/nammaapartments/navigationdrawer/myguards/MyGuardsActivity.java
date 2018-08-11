package com.kirtanlabs.nammaapartments.navigationdrawer.myguards;

import android.os.Bundle;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

public class MyGuardsActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_guards;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_guards;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
