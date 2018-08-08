package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.societyservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class SocietyServiceHistoryAdapter extends RecyclerView.Adapter<SocietyServiceHistoryAdapter.SocietyServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    SocietyServiceHistoryAdapter(List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList, Context mCtx) {
        this.nammaApartmentSocietyServiceList = nammaApartmentSocietyServiceList;
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public SocietyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_society_service_history, parent, false);
        return new SocietyServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SocietyServiceViewHolder holder, int position) {
        /*Creating an instance of NammaApartmentSocietyServices class and retrieving the values from Firebase*/
        NammaApartmentSocietyServices nammaApartmentSocietyServices = nammaApartmentSocietyServiceList.get(position);
        holder.textProblemValue.setText(nammaApartmentSocietyServices.getProblem());
        holder.textTimeSlotValue.setText(nammaApartmentSocietyServices.getTimeSlot());

        /*Retrieving Society Service Details*/
        DatabaseReference societyServiceReference = Constants.SOCIETY_SERVICES_REFERENCE
                .child(nammaApartmentSocietyServices.getSocietyServiceType())
                .child(Constants.FIREBASE_CHILD_PRIVATE)
                .child(Constants.FIREBASE_CHILD_DATA)
                .child(nammaApartmentSocietyServices.getTakenBy());
        societyServiceReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.textNameValue.setText(dataSnapshot.child(Constants.FIREBASE_CHILD_FULLNAME).getValue(String.class));
                holder.textMobileNumberValue.setText(dataSnapshot.child(Constants.FIREBASE_CHILD_MOBILE_NUMBER).getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return nammaApartmentSocietyServiceList.size();
    }

    class SocietyServiceViewHolder extends RecyclerView.ViewHolder {

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
            /*Getting Id's for all the views*/
            textName = itemView.findViewById(R.id.textName);
            textMobileNumber = itemView.findViewById(R.id.textMobileNumber);
            textProblem = itemView.findViewById(R.id.textProblem);
            textTimeSlot = itemView.findViewById(R.id.textTimeSlot);
            textNameValue = itemView.findViewById(R.id.textNameValue);
            textMobileNumberValue = itemView.findViewById(R.id.textMobileNumberValue);
            textProblemValue = itemView.findViewById(R.id.textProblemValue);
            textTimeSlotValue = itemView.findViewById(R.id.textTimeSlotValue);

            /*Setting Fonts for all the views on cardView*/
            textName.setTypeface(setLatoRegularFont(mCtx));
            textMobileNumber.setTypeface(setLatoRegularFont(mCtx));
            textProblem.setTypeface(setLatoRegularFont(mCtx));
            textTimeSlot.setTypeface(setLatoRegularFont(mCtx));
            textNameValue.setTypeface(setLatoBoldFont(mCtx));
            textMobileNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textProblemValue.setTypeface(setLatoBoldFont(mCtx));
            textTimeSlotValue.setTypeface(setLatoBoldFont(mCtx));
        }
    }
}
