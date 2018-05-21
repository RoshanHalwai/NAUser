package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.AddDailyServiceAndFamilyMembers;

public class MySweetHome extends BaseActivity implements View.OnClickListener {

    /*---------------------------------------------------------
        Overriding Base Activity Objects
     ----------------------------------------------------------*/
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_my_sweet_home;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.my_sweet_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id for the button*/
        Button buttonAddFamilyMembers = findViewById(R.id.buttonAddFamilyMembers);

        /*Setting Fonts for Add My Family Members Button*/
        buttonAddFamilyMembers.setTypeface(Constants.setLatoLightFont(this));

        /*Setting button click listener*/
        buttonAddFamilyMembers.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddFamilyMembers:
                Intent intent = new Intent(MySweetHome.this, AddDailyServiceAndFamilyMembers.class);
                intent.putExtra(Constants.SCREEN_TITLE, R.string.my_sweet_home);
                startActivity(intent);
        }
    }
}
