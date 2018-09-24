package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class EventManagementHistoryAdapter extends RecyclerView.Adapter<EventManagementHistoryAdapter.EventManagementViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public EventManagementHistoryAdapter(List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList, Context mCtx) {
        this.nammaApartmentSocietyServiceList = nammaApartmentSocietyServiceList;
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public EventManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_event_management_history, parent, false);
        return new EventManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventManagementViewHolder holder, int position) {

        /*Creating an instance of NammaApartmentSocietyServices class and retrieving the values from Firebase*/
        NammaApartmentSocietyServices nammaApartmentSocietyServices = nammaApartmentSocietyServiceList.get(position);
        String serviceStatus = nammaApartmentSocietyServices.getStatus();
        holder.textEventStatusValue.setText(serviceStatus);
        holder.textEventDateValue.setText(nammaApartmentSocietyServices.getEventDate());
        holder.textEventTitleValue.setText(nammaApartmentSocietyServices.getEventTitle());
    }

    @Override
    public int getItemCount() {
        return nammaApartmentSocietyServiceList.size();
    }

    class EventManagementViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textEventTitle;
        private final TextView textEventStatus;
        private final TextView textEventDate;
        private final TextView textEventTitleValue;
        private final TextView textEventStatusValue;
        private final TextView textEventDateValue;
        private final TextView textMorningSlotFirstHour;
        private final TextView textMorningSlotSecondHour;
        private final TextView textMorningSlotThirdHour;
        private final TextView textMorningSlotLastHour;
        private final TextView textNoonSlotFirstHour;
        private final TextView textNoonSlotSecondHour;
        private final TextView textNoonSlotThirdHour;
        private final TextView textNoonSlotLastHour;
        private final TextView textEveningSlotFirstHour;
        private final TextView textEveningSlotSecondHour;
        private final TextView textEveningSlotThirdHour;
        private final TextView textEveningSlotLastHour;
        private final TextView textNightSlotFirstHour;
        private final TextView textNightSlotLastHour;
        private final TextView textFullDaySlot;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        EventManagementViewHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventStatus = itemView.findViewById(R.id.textEventStatus);
            textEventDate = itemView.findViewById(R.id.textEventDate);
            textEventTitleValue = itemView.findViewById(R.id.textEventTitleValue);
            textEventStatusValue = itemView.findViewById(R.id.textEventStatusValue);
            textEventDateValue = itemView.findViewById(R.id.textEventDateValue);
            textMorningSlotFirstHour = itemView.findViewById(R.id.textMorningSlotFirstHour);
            textMorningSlotSecondHour = itemView.findViewById(R.id.textMorningSlotSecondHour);
            textMorningSlotThirdHour = itemView.findViewById(R.id.textMorningSlotThirdHour);
            textMorningSlotLastHour = itemView.findViewById(R.id.textMorningSlotLastHour);
            textNoonSlotFirstHour = itemView.findViewById(R.id.textNoonSlotFirstHour);
            textNoonSlotSecondHour = itemView.findViewById(R.id.textNoonSlotSecondHour);
            textNoonSlotThirdHour = itemView.findViewById(R.id.textNoonSlotThirdHour);
            textNoonSlotLastHour = itemView.findViewById(R.id.textNoonSlotLastHour);
            textEveningSlotFirstHour = itemView.findViewById(R.id.textEveningSlotFirstHour);
            textEveningSlotSecondHour = itemView.findViewById(R.id.textEveningSlotSecondHour);
            textEveningSlotThirdHour = itemView.findViewById(R.id.textEveningSlotThirdHour);
            textEveningSlotLastHour = itemView.findViewById(R.id.textEveningSlotLastHour);
            textNightSlotFirstHour = itemView.findViewById(R.id.textNightSlotFirstHour);
            textNightSlotLastHour = itemView.findViewById(R.id.textNightSlotLastHour);
            textFullDaySlot = itemView.findViewById(R.id.textFullDaySlot);

            /*Setting Fonts for all the views on cardView*/
            textEventTitle.setTypeface(setLatoBoldFont(mCtx));
            textEventStatus.setTypeface(setLatoBoldFont(mCtx));
            textEventDate.setTypeface(setLatoBoldFont(mCtx));
            textEventTitleValue.setTypeface(setLatoRegularFont(mCtx));
            textEventStatusValue.setTypeface(setLatoRegularFont(mCtx));
            textEventDateValue.setTypeface(setLatoRegularFont(mCtx));
            textMorningSlotFirstHour.setTypeface(setLatoLightFont(mCtx));
            textMorningSlotSecondHour.setTypeface(setLatoLightFont(mCtx));
            textMorningSlotThirdHour.setTypeface(setLatoLightFont(mCtx));
            textMorningSlotLastHour.setTypeface(setLatoLightFont(mCtx));
            textNoonSlotFirstHour.setTypeface(setLatoLightFont(mCtx));
            textNoonSlotSecondHour.setTypeface(setLatoLightFont(mCtx));
            textNoonSlotThirdHour.setTypeface(setLatoLightFont(mCtx));
            textNoonSlotLastHour.setTypeface(setLatoLightFont(mCtx));
            textEveningSlotFirstHour.setTypeface(setLatoLightFont(mCtx));
            textEveningSlotSecondHour.setTypeface(setLatoLightFont(mCtx));
            textEveningSlotThirdHour.setTypeface(setLatoLightFont(mCtx));
            textEveningSlotLastHour.setTypeface(setLatoLightFont(mCtx));
            textNightSlotFirstHour.setTypeface(setLatoLightFont(mCtx));
            textNightSlotLastHour.setTypeface(setLatoLightFont(mCtx));
            textFullDaySlot.setTypeface(setLatoLightFont(mCtx));
        }
    }
}
