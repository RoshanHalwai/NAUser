package com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate;

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
import com.kirtanlabs.nammaapartments.home.pojo.NammaApartmentService;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.emergency.RaiseAlarm;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.invitevisitors.InvitingVisitors;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.arrivals.ExpectingArrival;
import com.kirtanlabs.nammaapartments.services.societyservices.digigate.notifydigitalgate.handedthings.HandedThings;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALARM_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.ARRIVAL_TYPE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.HANDED_THINGS_TO;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/7/2018
 */
public class NotifyGateAndEmergencyAdapter extends RecyclerView.Adapter<NotifyGateAndEmergencyAdapter.NotifyGateHolder> {

    private final Context mCtx;
    private final List<NammaApartmentService> notificationServicesList;
    private final int serviceType;

    NotifyGateAndEmergencyAdapter(Context mCtx, List<NammaApartmentService> notificationServicesList, int serviceType) {
        this.mCtx = mCtx;
        this.notificationServicesList = notificationServicesList;
        this.serviceType = serviceType;
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
        NammaApartmentService nammaApartmentService = notificationServicesList.get(position);
        holder.textNotification.setTypeface(setLatoRegularFont(mCtx));
        holder.textNotification.setText(nammaApartmentService.getServiceName());
        holder.imageNotificationService.setImageResource(nammaApartmentService.getServiceImage());
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

            /*Since we are using same layout for Notify Digital Cab and Emergency we need to
             * change click event for both layout*/
            if (serviceType == R.string.notify_digital_gate) {
                switch (position) {
                    case 0: {
                        Intent intent = new Intent(mCtx, ExpectingArrival.class);
                        intent.putExtra(ARRIVAL_TYPE, R.string.expecting_cab_arrival);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(mCtx, ExpectingArrival.class);
                        intent.putExtra(ARRIVAL_TYPE, R.string.expecting_package_arrival);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(mCtx, InvitingVisitors.class);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 3: {
                        Intent intent = new Intent(mCtx, HandedThings.class);
                        intent.putExtra(HANDED_THINGS_TO, R.string.my_guests);
                        mCtx.startActivity(intent);
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(mCtx, HandedThings.class);
                        intent.putExtra(HANDED_THINGS_TO, R.string.my_daily_services);
                        mCtx.startActivity(intent);
                        break;
                    }
                }
            } else {
                Intent intent = new Intent(mCtx, RaiseAlarm.class);
                switch (position) {
                    case 0: {
                        intent.putExtra(ALARM_TYPE, R.string.medical_emergency);
                        break;
                    }
                    case 1: {
                        intent.putExtra(ALARM_TYPE, R.string.raise_fire_alarm);
                        break;
                    }
                    case 2: {
                        intent.putExtra(ALARM_TYPE, R.string.raise_theft_alarm);
                        break;
                    }
                    case 3: {
                        intent.putExtra(ALARM_TYPE, R.string.raise_water_alarm);
                        break;
                    }
                }
                mCtx.startActivity(intent);
            }
        }
    }
}