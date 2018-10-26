package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.utilities.Constants.COUNTRY_CODE_IN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.DEFAULT_CONTACT_US_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_EMAIL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class TransactionContactUsActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView textCustomerExecutiveContactNumber, textMailCustomerExecutive;
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
        textCustomerExecutiveContactNumber = findViewById(R.id.textCustomerExecutiveContactNumber);
        textMailCustomerExecutive = findViewById(R.id.textMailCustomerExecutive);

        /*Setting Fonts for all the views*/
        textContactHelp.setTypeface(setLatoBoldFont(this));
        textContactUsMessage.setTypeface(setLatoRegularFont(this));
        textCustomerExecutiveContactNumber.setTypeface(setLatoBoldFont(this));
        textMailCustomerExecutive.setTypeface(setLatoBoldFont(this));

        /*Setting Customer Executive Numbers in the views*/
        textCustomerExecutiveContactNumber.setPaintFlags(textCustomerExecutiveContactNumber.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textMailCustomerExecutive.setPaintFlags(textMailCustomerExecutive.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        showProgressDialog(this, getString(R.string.retrieving_details), getString(R.string.please_wait_a_moment));
        /*Retrieving contact details from firebase*/
        retrieveContactUsDetailsFromFirebase();

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
     * This method is invoked to retrieve Namma Apartments Customer Executive Details from firebase.
     */
    private void retrieveContactUsDetailsFromFirebase() {
        DEFAULT_CONTACT_US_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactNumber = dataSnapshot.child(FIREBASE_CHILD_NUMBER).getValue(String.class);
                mailId = dataSnapshot.child(FIREBASE_CHILD_EMAIL).getValue(String.class);

                String customerExecutiveContactNumber = COUNTRY_CODE_IN + HYPHEN + contactNumber;
                textCustomerExecutiveContactNumber.setText(customerExecutiveContactNumber);
                textMailCustomerExecutive.setText(mailId);
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

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
