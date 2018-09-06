package com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myvehicles.pojo.Vehicle;

import java.util.List;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/13/2018
 */
public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.NotifyGateHolder> {

    private final Context mCtx;
    private final List<Vehicle> notificationServicesList;

    public MyVehiclesAdapter(Context mCtx, List<Vehicle> notificationServicesList) {
        this.mCtx = mCtx;
        this.notificationServicesList = notificationServicesList;
    }

    @NonNull
    @Override
    public MyVehiclesAdapter.NotifyGateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_my_vehicles, parent, false);
        return new MyVehiclesAdapter.NotifyGateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVehiclesAdapter.NotifyGateHolder holder, int position) {
        Vehicle vehicle = notificationServicesList.get(position);
        holder.textVehicleNumberValue.setText(vehicle.getVehicleNumber());
        holder.textAddedDateValue.setText(vehicle.getAddedDate());
        holder.textOwnerValue.setText(vehicle.getOwnerName());
        if (vehicle.getVehicleType().equals(mCtx.getString(R.string.vehicle_type_car))) {
            holder.imageVehicleType.setImageResource(R.drawable.car);
            holder.textVehicleNumber.setText(mCtx.getString(R.string.car_no));
        } else {
            holder.imageVehicleType.setImageResource(R.drawable.motorbike);
            holder.textVehicleNumber.setText(mCtx.getString(R.string.bike_no));
        }

    }

    @Override
    public int getItemCount() {
        return notificationServicesList.size();
    }

    class NotifyGateHolder extends RecyclerView.ViewHolder {

        final TextView textVehicleNumber;
        final TextView textVehicleNumberValue;
        final TextView textAddedDate;
        final TextView textAddedDateValue;
        final TextView textOwner;
        final TextView textOwnerValue;
        final ImageView imageVehicleType;

        NotifyGateHolder(View itemView) {
            super(itemView);
            textVehicleNumber = itemView.findViewById(R.id.textVehicleNumber);
            textVehicleNumberValue = itemView.findViewById(R.id.textVehicleNumberValue);
            textAddedDate = itemView.findViewById(R.id.textAddedDate);
            textAddedDateValue = itemView.findViewById(R.id.textAddedDateValue);
            textOwner = itemView.findViewById(R.id.textOwner);
            textOwnerValue = itemView.findViewById(R.id.textOwnerValue);
            imageVehicleType = itemView.findViewById(R.id.imageVehicleType);

            textVehicleNumber.setTypeface(setLatoRegularFont(mCtx));
            textAddedDate.setTypeface(setLatoRegularFont(mCtx));
            textOwner.setTypeface(setLatoRegularFont(mCtx));
            textVehicleNumberValue.setTypeface(setLatoBoldFont(mCtx));
            textAddedDateValue.setTypeface(setLatoBoldFont(mCtx));
            textOwnerValue.setTypeface(setLatoBoldFont(mCtx));
        }

    }

}
