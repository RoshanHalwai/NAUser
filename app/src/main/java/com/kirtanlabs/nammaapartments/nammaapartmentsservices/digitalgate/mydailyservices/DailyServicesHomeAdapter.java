package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

public class DailyServicesHomeAdapter extends RecyclerView.Adapter<DailyServicesHomeAdapter.DailyServicesHolder> {

    //this context we will use to inflate the layout
    private final Context mCtx;

    //getting the context and product list with constructor
    DailyServicesHomeAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }


    @NonNull
    @Override
    public DailyServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new DailyServicesHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DailyServicesHolder holder, int position) {
        holder.textServiceName.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textServiceType.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTime.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textApartmentNo.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textApartmentNoValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textEdit.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        /*Making visibility GONE for this views because we don't need in My Daily Services List*/
        holder.textInvitationDate.setVisibility(View.GONE);
        holder.textInvitationDateValue.setVisibility(View.GONE);

        holder.textServiceName.setText("Name:");
        holder.textServiceNameValue.setText("Ramesh Singh");
        holder.textServiceTypeValue.setText(R.string.cook);
        holder.textEdit.setText(R.string.edit);
        holder.textApartmentNo.setText("Apartments:");
        holder.textApartmentNoValue.setText("3");

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit, 0, 0);

        /*Yet to Implement*/
        holder.textCall.setOnClickListener(v -> Toast.makeText(mCtx, "Yet to Implement", Toast.LENGTH_SHORT).show());

        holder.textMessage.setOnClickListener(v -> Toast.makeText(mCtx, "Yet to Implement", Toast.LENGTH_SHORT).show());

        holder.textEdit.setOnClickListener(v -> Toast.makeText(mCtx, "Yet to Implement", Toast.LENGTH_SHORT).show());

        holder.textCancel.setOnClickListener(v -> Toast.makeText(mCtx, "Yet to Implement", Toast.LENGTH_SHORT).show());

    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return 1;
    }

    class DailyServicesHolder extends RecyclerView.ViewHolder {
        final TextView textServiceName;
        final TextView textServiceNameValue;
        final TextView textServiceType;
        final TextView textServiceTypeValue;
        final TextView textInvitationDate;
        final TextView textInvitationDateValue;
        final TextView textInvitationTime;
        final TextView textInvitationTimeValue;
        final TextView textApartmentNo;
        final TextView textApartmentNoValue;

        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;

        DailyServicesHolder(View itemView) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.textVisitorAndServiceName);
            textServiceType = itemView.findViewById(R.id.textVisitorAndServiceType);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDate);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textApartmentNo = itemView.findViewById(R.id.textInvitedByAndApartmentNo);

            textServiceNameValue = itemView.findViewById(R.id.textVisitorAndServiceNameValue);
            textServiceTypeValue = itemView.findViewById(R.id.textVisitorAndServiceTypeValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textApartmentNoValue = itemView.findViewById(R.id.textInvitedByAndApartmentNoValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleAndEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
        }
    }
}
