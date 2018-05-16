package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> {

    //this context we will use to inflate the layout
    private final Context mCtx;

    //getting the context and product list with constructor
    VisitorsListAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        holder.textVisitorName.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textVisitorType.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDate.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTime.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedBy.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textVisitorTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));
    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return 5;
    }

    class VisitorViewHolder extends RecyclerView.ViewHolder {
        final TextView textVisitorName;
        final TextView textVisitorNameValue;
        final TextView textVisitorType;
        final TextView textVisitorTypeValue;
        final TextView textInvitationDate;
        final TextView textInvitationDateValue;
        final TextView textInvitationTime;
        final TextView textInvitationTimeValue;
        final TextView textInvitedBy;
        final TextView textInvitedByValue;

        final TextView textCall;
        final TextView textMessage;
        final TextView textReschedule;
        final TextView textCancel;

        VisitorViewHolder(View itemView) {
            super(itemView);
            textVisitorName = itemView.findViewById(R.id.textVisitorAndServiceName);
            textVisitorType = itemView.findViewById(R.id.textVisitorAndServiceType);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDate);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedBy = itemView.findViewById(R.id.textInvitedByAndApartmentNo);

            textVisitorNameValue = itemView.findViewById(R.id.textVisitorAndServiceNameValue);
            textVisitorTypeValue = itemView.findViewById(R.id.textVisitorAndServiceTypeValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByAndApartmentNoValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textReschedule = itemView.findViewById(R.id.textRescheduleAndEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
        }
    }

}
