package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class TransactionSummaryActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.layout_transaction_summary;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.transaction_summary;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView transactionStatusText = findViewById(R.id.transactionStatusText);
        TextView transactionIDText = findViewById(R.id.transactionIDText);
        TextView transactionIDValue = findViewById(R.id.transactionIDValue);
        TextView textTransactionPeriod = findViewById(R.id.textTransactionPeriod);
        TextView textTransactionDate = findViewById(R.id.textTransactionDate);
        TextView textTransactionAmount = findViewById(R.id.textTransactionAmount);
        TextView transactionPeriodValue = findViewById(R.id.transactionPeriodValue);
        TextView transactionDateValue = findViewById(R.id.transactionDateValue);
        TextView transactionAmountValue = findViewById(R.id.transactionAmountValue);
        TextView textPaymentDetails = findViewById(R.id.textPaymentDetails);
        TextView contactUsText = findViewById(R.id.contactUsText);

        /*Setting font for all the views*/
        transactionStatusText.setTypeface(setLatoBoldFont(this));
        transactionIDText.setTypeface(setLatoBoldFont(this));
        transactionIDValue.setTypeface(setLatoRegularFont(this));
        textTransactionPeriod.setTypeface(setLatoRegularFont(this));
        textTransactionDate.setTypeface(setLatoRegularFont(this));
        textTransactionAmount.setTypeface(setLatoRegularFont(this));
        transactionPeriodValue.setTypeface(setLatoBoldFont(this));
        transactionDateValue.setTypeface(setLatoBoldFont(this));
        transactionAmountValue.setTypeface(setLatoBoldFont(this));
        textPaymentDetails.setTypeface(setLatoBoldFont(this));
        contactUsText.setTypeface(setLatoBoldFont(this));

    }

    //TODO: Batch Drawable for right faced arrow image
    //TODO: Retrieve values of all fields and image from Firebase
    //TODO: Implement 'Copy' functionality for Transaction ID
}
