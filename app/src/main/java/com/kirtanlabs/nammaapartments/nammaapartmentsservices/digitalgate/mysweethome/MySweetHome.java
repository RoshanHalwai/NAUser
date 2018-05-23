package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mysweethome;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ListView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class MySweetHome extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private Button buttonAddFamilyMembers;
    private ListView listView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MySweetHomeAdapter adapterMySweetHome = new MySweetHomeAdapter(this);
        recyclerView.setAdapter(adapterMySweetHome);

    }
}
