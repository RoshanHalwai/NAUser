package com.kirtanlabs.nammaapartments.navigationdrawer.myfood.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myfood.pojo.DonateFood;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class MyFoodHistoryAdapter extends RecyclerView.Adapter<MyFoodHistoryAdapter.MyFoodHistoryHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<DonateFood> donateFoodDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public MyFoodHistoryAdapter(Context mCtx, List<DonateFood> donateFoodDataList) {
        this.mCtx = mCtx;
        this.donateFoodDataList = donateFoodDataList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public MyFoodHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_food_donation_history, parent, false);
        return new MyFoodHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFoodHistoryHolder holder, int position) {
        /*Creating an instance of Donate Food class and retrieving the values from Firebase*/
        DonateFood donateFood = donateFoodDataList.get(position);
        holder.textFoodTypeValue.setText(donateFood.getFoodType());
        holder.textFoodQuantityValue.setText(donateFood.getFoodQuantity());
        long timestamp = donateFood.getTimestamp();

        /*Decoding Time Stamp to Date*/
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String date = DateFormat.format("MMM dd, yyyy", calendar).toString();
        holder.textFoodDonationDateValue.setText(date);
    }

    @Override
    public int getItemCount() {
        return donateFoodDataList.size();
    }

    /* ------------------------------------------------------------- *
     * MyFoodHistory Holder Class
     * ------------------------------------------------------------- */

    class MyFoodHistoryHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textFoodType;
        private final TextView textFoodTypeValue;
        private final TextView textFoodQuantity;
        private final TextView textFoodQuantityValue;
        private final TextView textFoodDonationDate;
        private final TextView textFoodDonationDateValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        MyFoodHistoryHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on card view*/
            textFoodType = itemView.findViewById(R.id.textFoodType);
            textFoodQuantity = itemView.findViewById(R.id.textFoodQuantity);
            textFoodDonationDate = itemView.findViewById(R.id.textFoodDonationDate);
            textFoodTypeValue = itemView.findViewById(R.id.textFoodTypeValue);
            textFoodQuantityValue = itemView.findViewById(R.id.textFoodQuantityValue);
            textFoodDonationDateValue = itemView.findViewById(R.id.textFoodDonationDateValue);

            /*Setting Fonts for all the views on card view*/
            textFoodType.setTypeface(setLatoRegularFont(mCtx));
            textFoodQuantity.setTypeface(setLatoRegularFont(mCtx));
            textFoodDonationDate.setTypeface(setLatoRegularFont(mCtx));
            textFoodTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textFoodQuantityValue.setTypeface(setLatoBoldFont(mCtx));
            textFoodDonationDateValue.setTypeface(setLatoBoldFont(mCtx));

            /*Setting text to the views*/
            String textFoodTypeTitle = mCtx.getString(R.string.food_type) + ":";
            String textFoodQuantityTitle = mCtx.getString(R.string.food_quantity) + ":";

            textFoodType.setText(textFoodTypeTitle);
            textFoodQuantity.setText(textFoodQuantityTitle);
        }
    }
}