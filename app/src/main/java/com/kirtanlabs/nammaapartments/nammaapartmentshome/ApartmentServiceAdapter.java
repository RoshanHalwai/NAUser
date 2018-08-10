package com.kirtanlabs.nammaapartments.nammaapartmentshome;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class ApartmentServiceAdapter extends RecyclerView.Adapter<ApartmentServiceAdapter.ApartmentServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;

    ApartmentServiceAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ApartmentServiceAdapter.ApartmentServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.activity_apartment_services, parent, false);
        return new ApartmentServiceAdapter.ApartmentServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApartmentServiceAdapter.ApartmentServiceViewHolder holder, int position) {

        //TODO: Hardcoded data will be removed once the actual data is there to retrieve
        holder.textApartmentServiceNameValue.setText("Ashish Jha");
        holder.textApartmentServiceRatingValue.setText("3");
        holder.textApartmentServiceTimeSlotValue.setText("8 PM - 10 PM");
        holder.textApartmentServiceNoOfFlatsSlotValue.setText("4");
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ApartmentServiceViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textApartmentServiceName;
        private final TextView textApartmentServiceNameValue;
        private final TextView textApartmentServiceRating;
        private final TextView textApartmentServiceRatingValue;
        private final TextView textApartmentServiceTimeSlot;
        private final TextView textApartmentServiceTimeSlotValue;
        private final TextView textApartmentServiceNoOfFlats;
        private final TextView textApartmentServiceNoOfFlatsSlotValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ApartmentServiceViewHolder(View itemView) {
            super(itemView);
            /*Getting Id's for all the views*/
            textApartmentServiceName = itemView.findViewById(R.id.textApartmentServiceName);
            textApartmentServiceRating = itemView.findViewById(R.id.textApartmentServiceRating);
            textApartmentServiceTimeSlot = itemView.findViewById(R.id.textApartmentServiceTimeSlot);
            textApartmentServiceNoOfFlats = itemView.findViewById(R.id.textApartmentServiceFlats);
            textApartmentServiceNameValue = itemView.findViewById(R.id.textApartmentServiceNameValue);
            textApartmentServiceRatingValue = itemView.findViewById(R.id.textApartmentServiceRatingValue);
            textApartmentServiceTimeSlotValue = itemView.findViewById(R.id.textApartmentServiceTimeSlotValue);
            textApartmentServiceNoOfFlatsSlotValue = itemView.findViewById(R.id.textApartmentServiceFlatsValue);

            /*Setting font for all the views*/
            textApartmentServiceName.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceRating.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceTimeSlot.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceNoOfFlats.setTypeface(setLatoBoldFont(mCtx));
            textApartmentServiceNameValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceRatingValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceTimeSlotValue.setTypeface(setLatoRegularFont(mCtx));
            textApartmentServiceNoOfFlatsSlotValue.setTypeface(setLatoRegularFont(mCtx));
        }
    }
}
