package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to implement the home screen of 'My Wallet'
 */

public class MyPaymentsActivity extends BaseActivity {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_my_payment;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.payments;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*We need Info Button in this screen*/
        showInfoButton();

        /*Getting Id's for all the views*/
        TextView textWalletTitle = findViewById(R.id.textWalletTitle);
        TextView textWalletDescription = findViewById(R.id.textWalletDescription);
        TextView textPayFor = findViewById(R.id.textPayFor);
        TextView textSocietyService = findViewById(R.id.textSocietyService);
        TextView textApartmentService = findViewById(R.id.textApartmentService);
        TextView textTransactions = findViewById(R.id.textTransactions);

        /*Setting Font's for all the views*/
        textWalletTitle.setTypeface(setLatoBoldFont(this));
        textWalletDescription.setTypeface(setLatoRegularFont(this));
        textPayFor.setTypeface(setLatoBoldFont(this));
        textSocietyService.setTypeface(setLatoRegularFont(this));
        textApartmentService.setTypeface(setLatoRegularFont(this));
        textTransactions.setTypeface(setLatoRegularFont(this));

        textSocietyService.setOnClickListener(v -> {
            Intent maintenanceIntent = new Intent(MyPaymentsActivity.this, ServicesPaymentActivity.class);
            startActivity(maintenanceIntent);
        });

    }

}
