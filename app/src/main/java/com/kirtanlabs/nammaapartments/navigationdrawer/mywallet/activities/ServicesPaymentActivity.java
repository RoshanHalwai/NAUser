package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to display the screen which appears after the click of each grid view item
 */

public class ServicesPaymentActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_maintenance_payment;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.maintenance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textViewMaintenanceText = findViewById(R.id.textViewMaintenanceText);
        TextView textTotalAmount = findViewById(R.id.textTotalAmount);
        TextView textTotalAmountValue = findViewById(R.id.textTotalAmountValue);
        Button buttonPayNow = findViewById(R.id.buttonPayNow);

        /*Setting Fonts for all the views*/
        textViewMaintenanceText.setTypeface(setLatoRegularFont(this));
        textTotalAmount.setTypeface(setLatoBoldFont(this));
        textTotalAmountValue.setTypeface(setLatoBoldFont(this));
        buttonPayNow.setTypeface(setLatoBoldFont(this));

        /*Setting on click listeners*/
        buttonPayNow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        //TODO: Integrate Payment Gateway on click of 'Pay Now'
    }
}
