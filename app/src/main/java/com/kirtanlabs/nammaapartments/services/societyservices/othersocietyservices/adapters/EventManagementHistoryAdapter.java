package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.pojo.NammaApartmentSocietyServices;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.kirtanlabs.nammaapartments.utilities.Constants.TIME_SLOT_FULL_DAY;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class EventManagementHistoryAdapter extends RecyclerView.Adapter<EventManagementHistoryAdapter.EventManagementViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList;
    private Set<String> availableSlotsSet = new LinkedHashSet<>();

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public EventManagementHistoryAdapter(List<NammaApartmentSocietyServices> nammaApartmentSocietyServiceList, Context mCtx) {
        this.nammaApartmentSocietyServiceList = nammaApartmentSocietyServiceList;
        this.mCtx = mCtx;
        initializeMap();
    }

    private void initializeMap() {
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_one));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_two));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_three));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_four));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_five));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_six));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_seven));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_eight));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_nine));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_ten));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_eleven));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_twelve));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_thirteen));
        availableSlotsSet.add(mCtx.getString(R.string.time_slot_fourteen));
        availableSlotsSet.add(TIME_SLOT_FULL_DAY);
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public EventManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_event_management_history, parent, false);
        return new EventManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventManagementViewHolder holder, int position) {
        NammaApartmentSocietyServices nammaApartmentSocietyServices = nammaApartmentSocietyServiceList.get(position);
        holder.textEventCategoryValue.setText(nammaApartmentSocietyServices.getCategory());
        holder.textEventDateValue.setText(nammaApartmentSocietyServices.getEventDate());
        holder.textEventTitleValue.setText(nammaApartmentSocietyServices.getEventTitle());
        holder.listViewSelectedSlots.setAdapter(new SelectedSlotsAdapter(mCtx, getSelectedSlots(nammaApartmentSocietyServices)));
    }

    @Override
    public int getItemCount() {
        return nammaApartmentSocietyServiceList.size();
    }

    /**
     * Returns a sorted list of time slots selected by the user.
     *
     * @param nammaApartmentSocietyServices to get unsorted time slots selected by user for the event
     * @return sorted list of time slots selected by the user for the event
     */
    private List<String> getSelectedSlots(final NammaApartmentSocietyServices nammaApartmentSocietyServices) {
        Set<String> bookedSlotsSet = nammaApartmentSocietyServices.getTimeSlots().keySet();
        Set<String> availableSlotsSetRef = new LinkedHashSet<>(availableSlotsSet);
        if (bookedSlotsSet.contains(TIME_SLOT_FULL_DAY)) {
            availableSlotsSetRef.clear();
            availableSlotsSetRef.add(mCtx.getString(R.string.full_day));
        } else {
            for (String slot : availableSlotsSet) {
                if (!bookedSlotsSet.contains(slot)) {
                    availableSlotsSetRef.remove(slot);
                }
            }
        }
        return new LinkedList<>(availableSlotsSetRef);
    }

    class EventManagementViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textEventTitle;
        private final TextView textEventCategory;
        private final TextView textEventDate;
        private final TextView textEventTitleValue;
        private final TextView textEventCategoryValue;
        private final TextView textEventDateValue;
        private final TextView textSelectedSlots;
        private final RecyclerView listViewSelectedSlots;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        EventManagementViewHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views*/
            textEventTitle = itemView.findViewById(R.id.textEventTitle);
            textEventCategory = itemView.findViewById(R.id.textEventCategory);
            textEventDate = itemView.findViewById(R.id.textEventDate);
            textEventTitleValue = itemView.findViewById(R.id.textEventTitleValue);
            textEventCategoryValue = itemView.findViewById(R.id.textEventCategoryValue);
            textEventDateValue = itemView.findViewById(R.id.textEventDateValue);
            textSelectedSlots = itemView.findViewById(R.id.textSelectedSlots);
            listViewSelectedSlots = itemView.findViewById(R.id.listViewSelectedSlots);
            listViewSelectedSlots.setLayoutManager(new LinearLayoutManager(mCtx, LinearLayoutManager.HORIZONTAL, false));

            /*Setting Fonts for all the views on cardView*/
            textEventTitle.setTypeface(setLatoBoldFont(mCtx));
            textEventCategory.setTypeface(setLatoBoldFont(mCtx));
            textEventDate.setTypeface(setLatoBoldFont(mCtx));
            textSelectedSlots.setTypeface(setLatoBoldFont(mCtx));
            textEventTitleValue.setTypeface(setLatoRegularFont(mCtx));
            textEventCategoryValue.setTypeface(setLatoRegularFont(mCtx));
            textEventDateValue.setTypeface(setLatoRegularFont(mCtx));
        }
    }
}
