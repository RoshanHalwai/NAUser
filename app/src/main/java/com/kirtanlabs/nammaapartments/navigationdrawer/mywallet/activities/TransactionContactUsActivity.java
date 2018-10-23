package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.CUSTOMER_EXECUTIVE_CONTACT_NUMBER;
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
        TextView textCustomerExecutiveContactNumber = findViewById(R.id.textCustomerExecutiveContactNumber);

        /*Setting Fonts for all the views*/
        textContactHelp.setTypeface(setLatoBoldFont(this));
        textContactHelpMessage.setTypeface(setLatoRegularFont(this));
        textCustomerExecutiveContactNumber.setTypeface(setLatoBoldFont(this));

        /*Setting Customer Executive Number in the view*/
        textCustomerExecutiveContactNumber.setPaintFlags(textCustomerExecutiveContactNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //TODO: To change Customer executive mobile number.
        textCustomerExecutiveContactNumber.setText(CUSTOMER_EXECUTIVE_CONTACT_NUMBER);

        /*Setting Click Listeners for views*/
        textCustomerExecutiveContactNumber.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        makePhoneCall(CUSTOMER_EXECUTIVE_CONTACT_NUMBER);
    }
}
