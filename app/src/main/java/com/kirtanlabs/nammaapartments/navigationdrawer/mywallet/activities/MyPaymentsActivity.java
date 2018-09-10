package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.pojo.Transaction;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TRANSACTIONS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_TRANSACTION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to implement the home screen of 'My Wallet'
 */

public class MyPaymentsActivity extends BaseActivity implements PaymentResultListener {

    private static final String TAG = MyPaymentsActivity.class.getSimpleName();

    /*TODO: Get this amount from the server since each user might have different amount to be paid*/
    private int amountInPaise = 100;
    private String serviceCategory;

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

        /*Pre Load the contents of the Payment UI*/
        Checkout.preload(getApplicationContext());

        /*Getting Id's for all the views*/
        TextView textWalletTitle = findViewById(R.id.textWalletTitle);
        TextView textWalletDescription = findViewById(R.id.textWalletDescription);
        TextView textPayFor = findViewById(R.id.textPayFor);
        TextView textSocietyService = findViewById(R.id.textSocietyService);
        TextView textApartmentService = findViewById(R.id.textApartmentService);
        TextView textTransactions = findViewById(R.id.textTransactions);
        CardView layoutTransactionHistory = findViewById(R.id.layoutTransactionHistory);

        /*Setting Font's for all the views*/
        textWalletTitle.setTypeface(setLatoBoldFont(this));
        textWalletDescription.setTypeface(setLatoRegularFont(this));
        textPayFor.setTypeface(setLatoBoldFont(this));
        textSocietyService.setTypeface(setLatoRegularFont(this));
        textApartmentService.setTypeface(setLatoRegularFont(this));
        textTransactions.setTypeface(setLatoRegularFont(this));

        textSocietyService.setOnClickListener(v -> startPayment(amountInPaise, getString(R.string.society_services)));
        textApartmentService.setOnClickListener(v -> startPayment(amountInPaise, getString(R.string.apartment_services)));
        layoutTransactionHistory.setOnClickListener(v -> startActivity(new Intent(this, TransactionHistory.class)));
    }

    public void startPayment(int amount, String description) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        serviceCategory = description;
        try {
            UserPersonalDetails userPersonalDetails = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getPersonalDetails();
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description", description);
            options.put("currency", "INR");
            options.put("amount", String.valueOf(amount));

            JSONObject preFill = new JSONObject();
            preFill.put("email", userPersonalDetails.getEmail());
            preFill.put("contact", userPersonalDetails.getPhoneNumber());
            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /* ------------------------------------------------------------- *
     * Implementing PaymentResultListener Methods
     * ------------------------------------------------------------- */

    /**
     * On Payment Success, method gets called containing payment ID of the transaction
     *
     * @param paymentID - Id of Successful transaction
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String paymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + paymentID, Toast.LENGTH_SHORT).show();
            storeTransactionDetails(paymentID, "Successful");
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * On Payment Failure, method gets called containing error code and gateway response
     *
     * @param code     - Error Code
     * @param response - Payment Gateway Response
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }

    /**
     * Stores the details of transaction to firebase
     *
     * @param paymentId Unique Id to identify each transaction
     */
    private void storeTransactionDetails(final String paymentId, final String result) {
        DatabaseReference userTransactionReference = ((NammaApartmentsGlobal) getApplicationContext()).
                getUserDataReference().child(FIREBASE_CHILD_TRANSACTIONS);
        final String transactionUID = userTransactionReference.push().getKey();
        final Transaction transactionDetails = new Transaction((amountInPaise / 100), paymentId, result,
                serviceCategory, NammaApartmentsGlobal.userUID, transactionUID, System.currentTimeMillis());
        PRIVATE_TRANSACTION_REFERENCE.child(transactionUID).setValue(transactionDetails)
                .addOnCompleteListener(task -> userTransactionReference.child(transactionUID).setValue(true)
                        .addOnCompleteListener(task1 -> {
                            Checkout.clearUserData(this);
                        }));
    }

}
