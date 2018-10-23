package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIRST_CUSTOMER_EXECUTIVE_CONTACT_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SECOND_CUSTOMER_EXECUTIVE_CONTACT_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class TransactionContactUsActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Methods
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_transaction_contact_us;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.contact_us;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        TextView textContactHelp = findViewById(R.id.textContactHelp);
        TextView textContactHelpMessage = findViewById(R.id.textContactHelpMessage);
        TextView textFirstCustomerExecutiveContactNumber = findViewById(R.id.textFirstCustomerExecutiveContactNumber);
        TextView textSecondCustomerExecutiveContactNumber = findViewById(R.id.textSecondCustomerExecutiveContactNumber);

        /*Setting Fonts for all the views*/
        textContactHelp.setTypeface(setLatoBoldFont(this));
        textContactHelpMessage.setTypeface(setLatoRegularFont(this));
        textFirstCustomerExecutiveContactNumber.setTypeface(setLatoBoldFont(this));
        textSecondCustomerExecutiveContactNumber.setTypeface(setLatoBoldFont(this));

        /*Setting Customer Executive Numbers in the views*/
        textFirstCustomerExecutiveContactNumber.setPaintFlags(textFirstCustomerExecutiveContactNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textSecondCustomerExecutiveContactNumber.setPaintFlags(textSecondCustomerExecutiveContactNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //TODO: To change Customer executive mobile numbers from here.
        textFirstCustomerExecutiveContactNumber.setText(FIRST_CUSTOMER_EXECUTIVE_CONTACT_NUMBER);
        textSecondCustomerExecutiveContactNumber.setText(SECOND_CUSTOMER_EXECUTIVE_CONTACT_NUMBER);

        /*Setting Click Listeners for views*/
        textFirstCustomerExecutiveContactNumber.setOnClickListener(this);
        textSecondCustomerExecutiveContactNumber.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textFirstCustomerExecutiveContactNumber:
                makePhoneCall(FIRST_CUSTOMER_EXECUTIVE_CONTACT_NUMBER);
                break;
            case R.id.textSecondCustomerExecutiveContactNumber:
                makePhoneCall(SECOND_CUSTOMER_EXECUTIVE_CONTACT_NUMBER);
                break;
        }
    }
}
