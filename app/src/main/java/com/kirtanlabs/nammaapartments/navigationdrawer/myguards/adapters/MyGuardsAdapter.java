package com.kirtanlabs.nammaapartments.navigationdrawer.myguards.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myguards.pogo.NammaApartmentsGuard;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/11/2018
 */
public class MyGuardsAdapter extends RecyclerView.Adapter<MyGuardsAdapter.GuardViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentsGuard> guardDataList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public MyGuardsAdapter(Context mCtx, List<NammaApartmentsGuard> guardDataList) {
        this.mCtx = mCtx;
        this.guardDataList = guardDataList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public GuardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_my_guards, parent, false);
        return new MyGuardsAdapter.GuardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyGuardsAdapter.GuardViewHolder holder, int position) {
        /*Creating an instance of NammaApartmentsGuard class and retrieving the values from Firebase*/
        NammaApartmentsGuard nammaApartmentsGuard = guardDataList.get(position);
        holder.textGuardNameValue.setText(nammaApartmentsGuard.getFullName());
        holder.textGuardStatusValue.setText(nammaApartmentsGuard.getStatus().toUpperCase());
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentsGuard.getProfilePhoto()).into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return guardDataList.size();
    }

    /* ------------------------------------------------------------- *
     * Cabs View Holder class
     * ------------------------------------------------------------- */

    class GuardViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final CircleImageView profileImage;
        private final TextView textGuardName;
        private final TextView textGateNumber;
        private final TextView textGuardStatus;
        private final TextView textGuardNameValue;
        private final TextView textGateNumberValue;
        private final TextView textGuardStatusValue;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */
        GuardViewHolder(View itemView) {
            super(itemView);

            //Getting Id's for all the views on cardview
            profileImage = itemView.findViewById(R.id.profileImage);
            textGuardName = itemView.findViewById(R.id.textGuardName);
            textGateNumber = itemView.findViewById(R.id.textGateNumber);
            textGuardStatus = itemView.findViewById(R.id.textGuardStatus);
            textGuardNameValue = itemView.findViewById(R.id.textGuardNameValue);
            textGateNumberValue = itemView.findViewById(R.id.textGateNumberValue);
            textGuardStatusValue = itemView.findViewById(R.id.textGuardStatusValue);

            //Setting Fonts for all the views on cardview
            textGuardName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textGateNumber.setTypeface(Constants.setLatoRegularFont(mCtx));
            textGuardStatus.setTypeface(Constants.setLatoRegularFont(mCtx));
            textGuardNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textGateNumberValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textGuardStatusValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        }
    }

}
