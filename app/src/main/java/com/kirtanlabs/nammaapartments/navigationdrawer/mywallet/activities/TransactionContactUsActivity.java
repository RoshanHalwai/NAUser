package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.COUNTRY_CODE_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class TransactionContactUsActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private String contactNumber, mailId;

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
        TextView textContactUsMessage = findViewById(R.id.textContactUsMessage);
        TextView textCustomerExecutiveContactNumber = findViewById(R.id.textCustomerExecutiveContactNumber);
        TextView textMailCustomerExecutive = findViewById(R.id.textMailCustomerExecutive);

        /*Setting Fonts for all the views*/
        textContactHelp.setTypeface(setLatoBoldFont(this));
        textContactUsMessage.setTypeface(setLatoRegularFont(this));
        textCustomerExecutiveContactNumber.setTypeface(setLatoBoldFont(this));
        textMailCustomerExecutive.setTypeface(setLatoBoldFont(this));

        /*Setting Customer Executive Numbers in the views*/
        textCustomerExecutiveContactNumber.setPaintFlags(textCustomerExecutiveContactNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textMailCustomerExecutive.setPaintFlags(textMailCustomerExecutive.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        //TODO: To change Customer executive mobile numbers from here and retrieve this mobile number from firebase.
        contactNumber = "8041179007";
        //TODO: To change E-mail Id from here and retrieve this email from firebase.
        mailId = "skychaitanya@kirtanlabs.com";

        String customerExecutiveContactNumber = COUNTRY_CODE_IN + HYPHEN + contactNumber;
        textCustomerExecutiveContactNumber.setText(customerExecutiveContactNumber);
        textMailCustomerExecutive.setText(mailId);

        /*Setting Click Listeners for views*/
        textCustomerExecutiveContactNumber.setOnClickListener(this);
        textMailCustomerExecutive.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClickListener Method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textCustomerExecutiveContactNumber:
                makePhoneCall(contactNumber);
                break;
            case R.id.textMailCustomerExecutive:
                sendEmail(mailId);
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is used to send Email to the Namma Apartment Executive.
     *
     * @param emailId - to which e-mail needs to be sent
     */
    private void sendEmail(final String emailId) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
        emailIntent.setType("message/rfc822");
        startActivity(emailIntent);
    }
}
