package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static android.view.View.GONE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HYPHEN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.SERVICE_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class TransactionSummaryActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private TextView transactionIDValue, transactionAmountValue, transactionDateValue,
            transactionStatusText, transactionPeriodValue, textTransactionPeriod;
    private ImageView serviceStatus;
    private CardView transactionView;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

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
        TextView transactionIDText = findViewById(R.id.transactionIDText);
        textTransactionPeriod = findViewById(R.id.textTransactionPeriod);
        TextView textTransactionDate = findViewById(R.id.textTransactionDate);
        TextView textTransactionAmount = findViewById(R.id.textTransactionAmount);
        transactionPeriodValue = findViewById(R.id.transactionPeriodValue);
        TextView textPaymentDetails = findViewById(R.id.textPaymentDetails);
        TextView contactUsText = findViewById(R.id.contactUsText);
        TextView copyText = findViewById(R.id.copyText);
        transactionIDValue = findViewById(R.id.transactionIDValue);
        transactionStatusText = findViewById(R.id.transactionStatusText);
        transactionDateValue = findViewById(R.id.transactionDateValue);
        transactionAmountValue = findViewById(R.id.transactionAmountValue);
        serviceStatus = findViewById(R.id.serviceStatus);
        transactionView = findViewById(R.id.transactionView);
        LinearLayout layoutContactUs = findViewById(R.id.layoutContactUs);

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

        /*This method is invoked when User clicks on a transaction in My Transaction screen. It
        displays the summary of the same transaction*/
        retrieveTransactionData();
        /*Clicking on 'Copy' enables user to copy the Transaction ID and paste it wherever required*/
        copyText.setOnClickListener(this);
        layoutContactUs.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Objects
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.copyText:
                /*Copying transaction ID on click of 'Copy'*/
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String copiedText = transactionIDValue.getText().toString();
                ClipData clip = ClipData.newPlainText(getString(R.string.transaction_id_copy), copiedText);
                Toast.makeText(this, getString(R.string.transaction_id_copy), Toast.LENGTH_SHORT).show();
                Objects.requireNonNull(clipboard).setPrimaryClip(clip);
                break;
            case R.id.layoutContactUs:
                startActivity(new Intent(TransactionSummaryActivity.this, TransactionContactUsActivity.class));
                break;
        }
    }

    /**
     * This method retrieves all the necessary transaction data in the Transaction Summary screen.
     */
    private void retrieveTransactionData() {
        /*Getting the values from the previous Activity*/
        String transactionId = getIntent().getStringExtra(getString(R.string.paymentId));
        String transactionAmount = getIntent().getStringExtra(getString(R.string.amount));
        String dateAndTime = getIntent().getStringExtra(getString(R.string.dateAndTime));
        String transactionStatus = getIntent().getStringExtra(getString(R.string.payment_status));
        String serviceCategory = getIntent().getStringExtra(SERVICE_TYPE);
        /*Setting the image and text depending on the transaction status*/
        if (transactionStatus.equals(getResources().getString(R.string.successful))) {
            serviceStatus.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.request_accepted_na));
            transactionStatusText.setText(R.string.successful_payment);
        } else {
            serviceStatus.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.remove_new));
            transactionStatusText.setText(R.string.failed_payment);
            transactionStatusText.setTextColor(Color.RED);
            transactionView.setVisibility(GONE);
        }
        /*Setting the values in Transaction Summary screen*/
        transactionIDValue.setText(transactionId);
        transactionAmountValue.setText(transactionAmount);
        transactionDateValue.setText(dateAndTime);
        /*Getting Transaction period value*/
        /*Formatting the date to get the value of month and year from timestamp*/
        if (serviceCategory.equals(getString(R.string.event_management))) {
            textTransactionPeriod.setVisibility(GONE);
            transactionPeriodValue.setVisibility(GONE);
        } else {
            String transactionPeriod = getIntent().getStringExtra(getString(R.string.period));
            String[] separateTimePeriod = TextUtils.split(transactionPeriod, HYPHEN);
            String firstTransactionPeriod = separateTimePeriod[0];
            DateFormat originalDateFormat = new SimpleDateFormat("MMyyyy", Locale.ENGLISH);
            DateFormat targetFormat = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);
            String finalTransactionPeriod = "";
            try {
                Date date = originalDateFormat.parse(firstTransactionPeriod);
                /*Getting the transaction period for pending dues with one child*/
                finalTransactionPeriod = targetFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (separateTimePeriod.length > 1) {
                String lastTimePeriod = separateTimePeriod[1];
                Date date;
                try {
                    date = originalDateFormat.parse(lastTimePeriod);
                    String formattedSecondDate = targetFormat.format(date);
                    /*Getting the transaction period for pending dues with multiple child*/
                    finalTransactionPeriod = finalTransactionPeriod + " " + HYPHEN + " " + formattedSecondDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            /*Setting Transaction period value*/
            transactionPeriodValue.setText(finalTransactionPeriod);
        }
    }
}
