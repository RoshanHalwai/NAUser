package com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.mywallet.activities.pojo.Transaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/8/2018
 */
public class TransactionHistoryAdapter extends RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<Transaction> transactionList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public TransactionHistoryAdapter(Context mCtx, List<Transaction> transactionList) {
        this.mCtx = mCtx;
        this.transactionList = transactionList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Methods
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public TransactionHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_transaction_history, parent, false);
        return new TransactionHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHistoryViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        String serviceCategory = transaction.getServiceCategory();
        String amount = "Rs. " + String.valueOf(transaction.getAmount());
        SimpleDateFormat sfd = new SimpleDateFormat("EEE, MMM dd, HH:mm");
        String formattedDateAndTime = sfd.format(new Date(transaction.getTimestamp()));
        if (transaction.getResult().equals("Successful")) {
            holder.imageTransactionResult.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.request_accepted));
        } else {
            holder.imageTransactionResult.setImageDrawable(mCtx.getResources().getDrawable(R.drawable.remove_new));
        }
        holder.textAmount.setText(amount);
        holder.textServiceCategory.setText(serviceCategory);
        holder.textDateAndTime.setText(formattedDateAndTime);
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class TransactionHistoryViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textAmount;
        private final TextView textServiceCategory;
        private final TextView textDateAndTime;
        private final ImageView imageTransactionResult;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        TransactionHistoryViewHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textAmount = itemView.findViewById(R.id.textAmount);
            textServiceCategory = itemView.findViewById(R.id.textServiceCategory);
            textDateAndTime = itemView.findViewById(R.id.textDateAndTime);
            imageTransactionResult = itemView.findViewById(R.id.imageTransactionResult);

            /*Setting Fonts for all the views on cardView*/
            textAmount.setTypeface(setLatoBoldFont(mCtx));
            textServiceCategory.setTypeface(setLatoRegularFont(mCtx));
            textDateAndTime.setTypeface(setLatoRegularFont(mCtx));
        }
    }
}
