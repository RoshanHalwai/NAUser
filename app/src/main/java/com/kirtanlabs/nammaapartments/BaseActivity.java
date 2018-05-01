package com.kirtanlabs.nammaapartments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/1/2018
 */

/**
 * Root activity for most of the Activities of this project.
 * Responsible for creating toolbar by getting title from the activity
 * and implementing events on back button.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected abstract int getLayoutResourceId();

    protected abstract int getActivityTitle();

    private void setActivityTitle(int resourceId) {
        TextView activityTitle = findViewById(R.id.textActivityTitle);
        activityTitle.setTypeface(Constants.setLatoRegularFont(this));
        activityTitle.setText(resourceId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setActivityTitle(getActivityTitle());
        setBackButtonListener();
    }

    protected void setBackButtonListener() {
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> onBackPressed());
    }

}
