package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
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

import java.util.List;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/7/2018
 */
public class NotifyDigitalGateAdapter extends RecyclerView.Adapter<NotifyDigitalGateAdapter.NotifyGateHolder> {

    private final Context mCtx;

    private final List<Service> notificationServicesList;

    NotifyDigitalGateAdapter(Context mCtx, List<Service> notificationServicesList) {
        this.mCtx = mCtx;
        this.notificationServicesList = notificationServicesList;
    }


    @NonNull
    @Override
    public NotifyGateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_notify_digital_gate, parent, false);
        return new NotifyDigitalGateAdapter.NotifyGateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyDigitalGateAdapter.NotifyGateHolder holder, int position) {
        Service service = notificationServicesList.get(position);
        holder.textNotification.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textNotification.setText(service.getServiceName());
        holder.imageNotificationService.setImageResource(service.getServiceImage());
    }

    @Override
    public int getItemCount() {
        return notificationServicesList.size();
    }

    class NotifyGateHolder extends RecyclerView.ViewHolder {

        final TextView textNotification;
        final ImageView imageNotificationService;

        NotifyGateHolder(View itemView) {
            super(itemView);
            textNotification = itemView.findViewById(R.id.textNotification);
            imageNotificationService = itemView.findViewById(R.id.imageNotificationService);
        }
    }

}
