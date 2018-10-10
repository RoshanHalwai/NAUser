package com.kirtanlabs.nammaapartments.navigationdrawer.myfood.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myfood.RetrievingFoodDonationList;
import com.kirtanlabs.nammaapartments.navigationdrawer.myfood.adapters.MyFoodHistoryAdapter;

import java.util.Collections;

public class FoodDonationHistory extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_food_donation_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.food_donation_history_title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Progress Indicator in this screen*/
        showProgressIndicator();

        /*Getting Id of recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieving Details of all Food Donation requests raised by the user*/
        new RetrievingFoodDonationList(FoodDonationHistory.this).getFoodDonationDataList(donateFoodList -> {
            hideProgressIndicator();
            if (donateFoodList.isEmpty()) {
                showFeatureUnavailableLayout(R.string.no_food_donations_raised);
            } else {
                Collections.reverse(donateFoodList);
                recyclerView.setAdapter(new MyFoodHistoryAdapter(FoodDonationHistory.this, donateFoodList));
            }
        });
    }
}
