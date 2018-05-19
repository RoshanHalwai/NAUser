package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.AddDailyServiceAndFamilyMembers;

public class MySweetHome extends BaseActivity {
    Button buttonAddFamilyMembers;

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
        buttonAddFamilyMembers = findViewById(R.id.buttonAddFamilyMembers);

        /*Setting Fonts for Add My Family Members Button*/
        buttonAddFamilyMembers.setTypeface(Constants.setLatoLightFont(this));

        /*Setting button click listener*/
        buttonAddFamilyMembers.setOnClickListener(v -> {
            Intent intent = new Intent(MySweetHome.this, AddDailyServiceAndFamilyMembers.class);
            intent.putExtra(Constants.SCREEN_TYPE, R.string.my_sweet_home);
            startActivity(intent);
        });
    }
}
