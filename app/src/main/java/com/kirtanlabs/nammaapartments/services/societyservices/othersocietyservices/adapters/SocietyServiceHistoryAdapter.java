package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.CARPENTER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ELECTRICIAN;
import static com.kirtanlabs.nammaapartments.utilities.Constants.GARBAGE_MANAGEMENT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PLUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class SocietyServiceHistoryAdapter extends RecyclerView.Adapter<SocietyServiceHistoryAdapter.SocietyServiceViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public SocietyServiceHistoryAdapter(List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList, Context mCtx) {
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
        SimpleDateFormat sfd = new SimpleDateFormat("EEE, MMM dd, HH:mm");
        String formattedDateAndTime = sfd.format(new Date(nammaApartmentSocietyServices.getTimestamp()));
        holder.textProblem.setText(nammaApartmentSocietyServices.getProblem());
        holder.textDateAndTime.setText(formattedDateAndTime);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentSocietyServiceList.size();
    }

    class SocietyServiceViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textProblem;
        private final TextView textDateAndTime;
        private final ImageView societyServiceIcon;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SocietyServiceViewHolder(View itemView) {
            super(itemView);
            /*Getting Id's for all the views*/
            textDateAndTime = itemView.findViewById(R.id.textDateAndTime);
            textProblem = itemView.findViewById(R.id.textProblem);
            societyServiceIcon = itemView.findViewById(R.id.societyServiceIcon);

            /*Setting Fonts for all the views on cardView*/
            textDateAndTime.setTypeface(setLatoBoldFont(mCtx));
            textProblem.setTypeface(setLatoRegularFont(mCtx));

            switch (nammaApartmentSocietyServiceList.get(0).getSocietyServiceType()) {
                case PLUMBER:
                    societyServiceIcon.setImageResource(R.drawable.plumbing);
                    break;
                case CARPENTER:
                    societyServiceIcon.setImageResource(R.drawable.carpenter_service);
                    break;
                case ELECTRICIAN:
                    societyServiceIcon.setImageResource(R.drawable.electrician);
                    break;
                case GARBAGE_MANAGEMENT:
                    societyServiceIcon.setImageResource(R.drawable.garbage_bin);
                    break;
            }
        }
    }
}
