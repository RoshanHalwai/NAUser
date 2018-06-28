package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.NammaApartmentService;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.cabs.CabsList;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.guests.GuestsList;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist.packages.PackagesList;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 6/28/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorsListHolder> {

    private final Context mCtx;
    private final List<NammaApartmentService> visitorsList;

    VisitorsListAdapter(Context mCtx, List<NammaApartmentService> visitorsList) {
        this.mCtx = mCtx;
        this.visitorsList = visitorsList;
    }

    @NonNull
    @Override
    public VisitorsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notify_digital_gate, parent, false);
        return new VisitorsListHolder(view, mCtx);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorsListHolder holder, int position) {
        NammaApartmentService nammaApartmentService = visitorsList.get(position);
        holder.textNotification.setTypeface(setLatoRegularFont(mCtx));
        holder.textNotification.setText(nammaApartmentService.getServiceName());
        holder.imageNotificationService.setImageResource(nammaApartmentService.getServiceImage());
    }

    @Override
    public int getItemCount() {
        return visitorsList.size();
    }

    public class VisitorsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textNotification;
        final ImageView imageNotificationService;
        private final Context mCtx;

        VisitorsListHolder(View itemView, Context mCtx) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mCtx = mCtx;
            textNotification = itemView.findViewById(R.id.textNotification);
            imageNotificationService = itemView.findViewById(R.id.imageNotificationService);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent intent = null;
            switch (position) {
                case 0: {
                    intent = new Intent(mCtx, GuestsList.class);
                    break;
                }
                case 1: {
                    intent = new Intent(mCtx, CabsList.class);
                    break;
                }
                case 2: {
                    intent = new Intent(mCtx, PackagesList.class);
                    break;
                }
            }
            mCtx.startActivity(intent);
        }
    }

}
