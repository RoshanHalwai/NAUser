package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentshome.Service;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.InvitingVisitors;

import java.util.List;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/7/2018
 */
public class NotifyGateAndEmergencyAdapter extends RecyclerView.Adapter<NotifyGateAndEmergencyAdapter.NotifyGateHolder> {

    private final Context mCtx;

    private final List<Service> notificationServicesList;

    NotifyGateAndEmergencyAdapter(Context mCtx, List<Service> notificationServicesList) {
        this.mCtx = mCtx;
        this.notificationServicesList = notificationServicesList;
    }


    @NonNull
    @Override
    public NotifyGateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notify_digital_gate, parent, false);
        return new NotifyGateAndEmergencyAdapter.NotifyGateHolder(view, mCtx);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyGateAndEmergencyAdapter.NotifyGateHolder holder, int position) {
        Service service = notificationServicesList.get(position);
        holder.textNotification.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textNotification.setText(service.getServiceName());
        holder.imageNotificationService.setImageResource(service.getServiceImage());
    }

    @Override
    public int getItemCount() {
        return notificationServicesList.size();
    }

    class NotifyGateHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView textNotification;
        final ImageView imageNotificationService;

        private final Context mCtx;


        NotifyGateHolder(View itemView, Context mCtx) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.mCtx = mCtx;


            textNotification = itemView.findViewById(R.id.textNotification);
            imageNotificationService = itemView.findViewById(R.id.imageNotificationService);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            switch (position) {
                case 0: {
                    Intent intent = new Intent(mCtx, ExpectingArrival.class);
                    intent.putExtra(Constants.ARRIVAL_TYPE, R.string.expecting_cab_arrival);
                    mCtx.startActivity(intent);
                    break;
                }
                case 1: {
                    Intent intent = new Intent(mCtx, ExpectingArrival.class);
                    intent.putExtra(Constants.ARRIVAL_TYPE, R.string.expecting_package_arrival);
                    mCtx.startActivity(intent);
                    break;
                }
                case 2: {
                    Intent intent = new Intent(mCtx, InvitingVisitors.class);
                    mCtx.startActivity(intent);
                    break;
                }
                case 3: {
                    Intent intent = new Intent(mCtx, HandedThingsGuestActivity.class);
                    mCtx.startActivity(intent);
                    break;
                }
            }
        }
    }

}