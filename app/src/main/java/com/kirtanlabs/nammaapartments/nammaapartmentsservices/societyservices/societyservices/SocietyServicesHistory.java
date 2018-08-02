package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/2/2018
 */

public class SocietyServicesHistory extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_society_service_history;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textName = findViewById(R.id.textName);
        TextView textMobileNumber = findViewById(R.id.textMobileNumber);
        TextView textProblem = findViewById(R.id.textProblem);
        TextView textTimeSlot = findViewById(R.id.textTimeSlot);

        /*Setting font for all the views*/
        textName.setTypeface(setLatoBoldFont(this));
        textMobileNumber.setTypeface(setLatoBoldFont(this));
        textProblem.setTypeface(setLatoBoldFont(this));
        textTimeSlot.setTypeface(setLatoBoldFont(this));

    }

    //TODO: Actual Contents of the History screen to be added. Right now the Card View contains hardcoded data
}
