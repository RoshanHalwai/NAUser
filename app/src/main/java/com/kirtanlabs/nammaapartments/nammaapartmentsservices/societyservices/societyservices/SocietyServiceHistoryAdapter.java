package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class SocietyServiceHistoryAdapter extends RecyclerView.Adapter<SocietyServiceHistoryAdapter.SocietyServiceViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    SocietyServiceHistoryAdapter(List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList, Context mCtx) {
        this.mCtx = mCtx;
        this.nammaApartmentSocietyServiceList = nammaApartmentSocietyServiceList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public SocietyServiceHistoryAdapter.SocietyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_society_service_history, parent, false);
        return new SocietyServiceHistoryAdapter.SocietyServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocietyServiceHistoryAdapter.SocietyServiceViewHolder holder, int position) {
        //Creating an instance of NammaApartmentSocietyServices class and retrieving the values from Firebase
        NammaApartmentSocietyServices nammaApartmentSocietyServices = nammaApartmentSocietyServiceList.get(position);
        holder.textProblemValue.setText(nammaApartmentSocietyServices.getProblem());
        holder.textTimeSlotValue.setText(nammaApartmentSocietyServices.getTimeSlot());
    }

    @Override
    public int getItemCount() {
        return nammaApartmentSocietyServiceList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {

    }

    class SocietyServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textName;
        private final TextView textNameValue;
        private final TextView textMobileNumber;
        private final TextView textMobileNumberValue;
        private final TextView textProblem;
        private final TextView textProblemValue;
        private final TextView textTimeSlot;
        private final TextView textTimeSlotValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SocietyServiceViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textMobileNumber = itemView.findViewById(R.id.textMobileNumber);
            textProblem = itemView.findViewById(R.id.textProblem);
            textTimeSlot = itemView.findViewById(R.id.textTimeSlot);
            textNameValue = itemView.findViewById(R.id.textNameValue);
            textMobileNumberValue = itemView.findViewById(R.id.textMobileNumberValue);
            textProblemValue = itemView.findViewById(R.id.textProblemValue);
            textTimeSlotValue = itemView.findViewById(R.id.textTimeSlotValue);

            //Setting Fonts for all the views on cardview
            textName.setTypeface(setLatoRegularFont(mCtx));
            textMobileNumber.setTypeface(setLatoRegularFont(mCtx));
            textProblem.setTypeface(setLatoRegularFont(mCtx));
            textTimeSlot.setTypeface(setLatoRegularFont(mCtx));
            textNameValue.setTypeface(setLatoBoldFont(mCtx));
            textMobileNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textProblemValue.setTypeface(setLatoBoldFont(mCtx));
            textTimeSlotValue.setTypeface(setLatoBoldFont(mCtx));
        }

        @Override
        public void onClick(View v) {

        }
    }
}
