package com.kirtanlabs.nammaapartments.navigationdrawer.myguards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 8/11/2018
 */
public class MyGuardsAdapter extends RecyclerView.Adapter<MyGuardsAdapter.GuardViewHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    MyGuardsAdapter(Context mCtx) {
        this.mCtx = mCtx;
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

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /* ------------------------------------------------------------- *
     * Cabs View Holder class
     * ------------------------------------------------------------- */

    class GuardViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

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
