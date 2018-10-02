package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.pojo.Transaction;
import com.kirtanlabs.nammaapartments.userpojo.UserPersonalDetails;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CONVENIENCE_CHARGES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_PENDING_DUES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TRANSACTIONS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.INDIAN_RUPEE_CURRENCY_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_CANCELLED_ERROR_CODE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_FAILURE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PAYMENT_SUCCESSFUL;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_TRANSACTION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.TRANSACTION_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Ashish Jha on 8/23/2018
 * This class has been created to implement the home screen of 'My Payments'
 */

public class MyPaymentsActivity extends BaseActivity implements PaymentResultListener, View.OnClickListener {

    private static final String TAG = MyPaymentsActivity.class.getSimpleName();
    private int pendingAmountInPaise, pendingAmount = 0;
    private float conveniencePercentage, convenienceCharge;
    private String serviceCategory, pendingAmountStr;
    private TextView textMaintenanceCostValue;
    private LinearLayout layoutPendingDues, layoutNoPendingDues;

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

        /*Getting Id's for all the views*/
        layoutPendingDues = findViewById(R.id.layoutPendingDues);
        layoutNoPendingDues = findViewById(R.id.layoutNoPendingDues);
        TextView textWalletTitle = findViewById(R.id.textWalletTitle);
        TextView textWalletDescription = findViewById(R.id.textWalletDescription);
        TextView textMakePayment = findViewById(R.id.textMakePayment);
        TextView textPayMaintenance = findViewById(R.id.textPayMaintenance);
        TextView textNoPendingDues = findViewById(R.id.textNoPendingDues);
        textMaintenanceCostValue = findViewById(R.id.textMaintenanceCostValue);
        TextView textTransactions = findViewById(R.id.textTransactions);
        CardView layoutTransactionHistory = findViewById(R.id.layoutTransactionHistory);

        /*Setting Font's for all the views*/
        textWalletTitle.setTypeface(setLatoBoldFont(this));
        textWalletDescription.setTypeface(setLatoRegularFont(this));
        textMakePayment.setTypeface(setLatoBoldFont(this));
        textPayMaintenance.setTypeface(setLatoRegularFont(this));
        textNoPendingDues.setTypeface(setLatoRegularFont(this));
        textMaintenanceCostValue.setTypeface(setLatoBoldFont(this));
        textTransactions.setTypeface(setLatoRegularFont(this));

        /*To retrieve pending dues from server*/
        getPendingDues();

        /*To Retrieve the convenienceCharge from server*/
        retrieveConvenienceCharge();

        /*Setting event for views */
        textPayMaintenance.setOnClickListener(this);
        layoutTransactionHistory.setOnClickListener(this);
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listener Objects
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textPayMaintenance:
                if (layoutNoPendingDues.getVisibility() == View.VISIBLE) {
                    showNotificationDialog(getString(R.string.no_dues_dialog_title), getString(R.string.no_dues_dialog_message), null);
                } else {
                    /*This Dialog shows the total amount to be paid by user including convenience fee*/
                    showFinalAmountDialog();
                }
                break;
            case R.id.layoutTransactionHistory:
                startActivity(new Intent(this, TransactionHistory.class));
                break;
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
            Toast.makeText(this, getString(R.string.payment_success), Toast.LENGTH_SHORT).show();
            storeTransactionDetails(paymentID, PAYMENT_SUCCESSFUL);
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.payment_success_exception), e);
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
            Toast.makeText(this, getString(R.string.payment_failed), Toast.LENGTH_SHORT).show();
            if (!(code == PAYMENT_CANCELLED_ERROR_CODE) || !(response.equals(getString(R.string.payment_cancelled)))) {
                storeTransactionDetails("", PAYMENT_FAILURE);
            }
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.payment_failure_exception), e);
        }
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * Gets the pending maintenance amount to be paid by the flat user. If there is no pending dues
     * we show "No Pending Dues" text.
     */
    private void getPendingDues() {
        showProgressDialog(this, getString(R.string.pending_dues), getString(R.string.progress_dialog_message));
        DatabaseReference userDataReference = ((NammaApartmentsGlobal) getApplicationContext())
                .getUserDataReference();
        DatabaseReference userMaintenanceCostReference = userDataReference.child(FIREBASE_CHILD_PENDING_DUES);
        userMaintenanceCostReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    layoutPendingDues.setVisibility(View.VISIBLE);
                    layoutNoPendingDues.setVisibility(View.GONE);
                    pendingAmount = 0;
                    for (DataSnapshot pendingSnapshot : dataSnapshot.getChildren()) {
                        pendingAmount = pendingAmount + Objects.requireNonNull(pendingSnapshot.getValue(Integer.class));
                    }
                    pendingAmountStr = getString(R.string.rupees_symbol) + String.valueOf(pendingAmount);
                    textMaintenanceCostValue.setText(pendingAmountStr);
                    pendingAmountInPaise = pendingAmount * 100;
                } else {
                    layoutPendingDues.setVisibility(View.GONE);
                    layoutNoPendingDues.setVisibility(View.VISIBLE);
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method retrieves the conveniencePercentage value from server.
     */
    private void retrieveConvenienceCharge() {
        TRANSACTION_REFERENCE.child(FIREBASE_CHILD_CONVENIENCE_CHARGES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                convenienceCharge = Objects.requireNonNull(dataSnapshot.getValue(Float.class));
                conveniencePercentage = (convenienceCharge / 100);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method invokes to send users to razorPay Payment Interface.
     *
     * @param amount      contains of the amount shown to user.
     * @param description contains the description of the payment service.
     */
    private void startPayment(int amount, String description) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        serviceCategory = description;
        try {
            UserPersonalDetails userPersonalDetails = ((NammaApartmentsGlobal) getApplicationContext()).getNammaApartmentUser().getPersonalDetails();
            JSONObject options = new JSONObject();
            options.put(getString(R.string.name).toLowerCase(), getString(R.string.app_name));
            options.put(getString(R.string.payment_description), description);
            options.put(getString(R.string.currency), INDIAN_RUPEE_CURRENCY_CODE);
            options.put(getString(R.string.amount), String.valueOf(amount));

            JSONObject preFill = new JSONObject();
            preFill.put(getString(R.string.email), userPersonalDetails.getEmail());
            preFill.put(getString(R.string.contact), userPersonalDetails.getPhoneNumber());
            options.put(getString(R.string.prefill), preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.payment_error) + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
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
        final Transaction transactionDetails = new Transaction((pendingAmountInPaise / 100), paymentId, result,
                serviceCategory, NammaApartmentsGlobal.userUID, transactionUID, System.currentTimeMillis());
        PRIVATE_TRANSACTION_REFERENCE.child(transactionUID).setValue(transactionDetails)
                .addOnCompleteListener(task -> userTransactionReference.child(transactionUID).setValue(true)
                        .addOnCompleteListener(task1 -> {
                            Checkout.clearUserData(this);
                            /*Remove Pending dues*/
                            ((NammaApartmentsGlobal) getApplicationContext()).getUserDataReference()
                                    .child(FIREBASE_CHILD_PENDING_DUES).removeValue();
                        }));
    }


    /**
     * This method is invoked to call a dialog box which is used to display total amount to user that
     * includes both convenience fee and maintenance amount.
     */
    private void showFinalAmountDialog() {
        /*Inflating the layout in the dialog box*/
        View maintenancePaymentDialog = View.inflate(this, R.layout.layout_event_bill_and_maintenance_payment_dialog, null);

        /*Getting Id's for all the views*/
        TextView textEventBillAndMaintenanceBill = maintenancePaymentDialog.findViewById(R.id.textEventBillAndMaintenanceBill);
        TextView textBookedSlotsNumberAndMaintenanceAmount = maintenancePaymentDialog.findViewById(R.id.textBookedSlotsNumberAndMaintenanceAmount);
        TextView textAmountPerSlot = maintenancePaymentDialog.findViewById(R.id.textAmountPerSlot);
        TextView textEstimatedAmount = maintenancePaymentDialog.findViewById(R.id.textEstimatedAmount);
        TextView textConvenienceFee = maintenancePaymentDialog.findViewById(R.id.textConvenienceFee);
        TextView textTotalAmount = maintenancePaymentDialog.findViewById(R.id.textTotalAmount);
        TextView textBookedSlotsNumberAndMaintenanceCostValue = maintenancePaymentDialog.findViewById(R.id.textBookedSlotsNumberAndMaintenanceCostValue);
        TextView textAmountPerSlotValue = maintenancePaymentDialog.findViewById(R.id.textAmountPerSlotValue);
        TextView textEstimatedAmountValue = maintenancePaymentDialog.findViewById(R.id.textEstimatedAmountValue);
        TextView textConvenienceFeeValue = maintenancePaymentDialog.findViewById(R.id.textConvenienceFeeValue);
        TextView textTotalAmountValue = maintenancePaymentDialog.findViewById(R.id.textTotalAmountValue);
        Button buttonCancel = maintenancePaymentDialog.findViewById(R.id.buttonCancel);
        Button buttonPayNow = maintenancePaymentDialog.findViewById(R.id.buttonPayNow);

        /*Setting fonts for all the views*/
        textEventBillAndMaintenanceBill.setTypeface(setLatoBoldFont(this));
        textBookedSlotsNumberAndMaintenanceAmount.setTypeface(setLatoRegularFont(this));
        textConvenienceFee.setTypeface(setLatoRegularFont(this));
        textTotalAmount.setTypeface(setLatoRegularFont(this));
        textBookedSlotsNumberAndMaintenanceCostValue.setTypeface(setLatoBoldFont(this));
        textConvenienceFeeValue.setTypeface(setLatoBoldFont(this));
        textTotalAmountValue.setTypeface(setLatoBoldFont(this));
        buttonPayNow.setTypeface(setLatoLightFont(this));

        /*Hiding UnAppropriate Views*/
        buttonCancel.setVisibility(View.GONE);
        textAmountPerSlot.setVisibility(View.GONE);
        textEstimatedAmount.setVisibility(View.GONE);
        textAmountPerSlotValue.setVisibility(View.GONE);
        textEstimatedAmountValue.setVisibility(View.GONE);

        /*Programmatically setting the buttonPayNow Margin */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        buttonPayNow.setLayoutParams(params);

        /*Setting texts of the views accordingly*/
        textEventBillAndMaintenanceBill.setText(R.string.maintenance_bill);
        textBookedSlotsNumberAndMaintenanceAmount.setText(R.string.maintenance_amount);
        String convenienceFee = getString(R.string.convenience_fee);
        convenienceFee = convenienceFee.replace(getString(R.string.num_value), String.valueOf(convenienceCharge));
        textConvenienceFee.setText(convenienceFee);

        /*Mathematical Calculations for setting the convenience fee and adding it to the total amount.*/
        textBookedSlotsNumberAndMaintenanceCostValue.setText(pendingAmountStr);

        /*Deriving Convenience Fee multiplied with pending amount of the user*/
        int convenienceAmount = (int) (conveniencePercentage * pendingAmount);
        String convenienceAmountValue = getString(R.string.rupees_symbol) + " " + String.valueOf(convenienceAmount);
        textConvenienceFeeValue.setText(convenienceAmountValue);

        /*Setting the total amount which includes convenience fee plus pending dues amount*/
        int totalAmount = pendingAmount + convenienceAmount;
        String totalAmountValue = getString(R.string.rupees_symbol) + " " + String.valueOf(totalAmount);
        textTotalAmountValue.setText(totalAmountValue);

        /*Creating the Alert Dialog and setting the view*/
        AlertDialog.Builder alertMaintenanceAmountDialog = new AlertDialog.Builder(this);
        alertMaintenanceAmountDialog.setView(maintenancePaymentDialog);
        AlertDialog dialog = alertMaintenanceAmountDialog.create();

        /*Showing the Dialog*/
        new Dialog(this);
        dialog.show();

        /*Setting Listeners to the views*/
        buttonPayNow.setOnClickListener(v -> {
            pendingAmountInPaise = totalAmount * 100;
            startPayment(pendingAmountInPaise, getString(R.string.society_services));
            dialog.cancel();
        });
    }

}