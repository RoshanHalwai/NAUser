package com.kirtanlabs.nammaapartments.services.societyservices.othersocietyservices.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kirtanlabs.nammaapartments.R;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 9/29/2018
 */
public class SelectedSlotsAdapter extends RecyclerView.Adapter<SelectedSlotsAdapter.ViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private List<String> selectedSlots;
    private Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    SelectedSlotsAdapter(Context mCtx, List<String> selectedSlots) {
        this.selectedSlots = selectedSlots;
        this.mCtx = mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter API's
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public SelectedSlotsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflating and returning our view holder*/
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_selected_slots, parent, false);
        return new SelectedSlotsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.selectedSlot.setText(selectedSlots.get(position));
    }

    @Override
    public int getItemCount() {
        return selectedSlots.size();
    }

    /* ------------------------------------------------------------- *
     * View Holder Class
     * ------------------------------------------------------------- */

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button selectedSlot;

        public ViewHolder(View itemView) {
            super(itemView);
            selectedSlot = itemView.findViewById(R.id.buttonSlot);
            selectedSlot.setTypeface(setLatoRegularFont(mCtx));
        }
    }
}
