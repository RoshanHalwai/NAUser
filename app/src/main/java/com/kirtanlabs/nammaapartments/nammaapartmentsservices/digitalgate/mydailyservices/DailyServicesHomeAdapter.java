package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
        /*Since we are reusing the layouts we need to modify the Layout weight for Title
         * and values, since in Daily Services Home the title take less space than Visitors
         * list*/
        LinearLayout.LayoutParams layoutTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.2f
        );
        LinearLayout.LayoutParams layoutTitleValuesParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.8f
        );

        holder.layoutTitle.setLayoutParams(layoutTitleParams);
        holder.layoutTitleValues.setLayoutParams(layoutTitleValuesParams);

        holder.textServiceName.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textServiceType.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateOrServiceRating.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTime.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textEdit.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        String stringServiceName = mCtx.getResources().getString(R.string.name) + ":";
        holder.textServiceName.setText(stringServiceName);
        holder.textInvitationDateOrServiceRating.setText(R.string.rating);
        holder.textInvitedByOrNumberOfFlats.setText(R.string.flats);

        holder.textServiceNameValue.setText("Ramesh Singh");
        holder.textServiceTypeValue.setText(R.string.cook);
        holder.textInvitationDateOrServiceRatingValue.setText("4.2");
        holder.textInvitedByOrNumberOfFlatsValue.setText("3");

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

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
        final LinearLayout layoutTitle;
        final LinearLayout layoutTitleValues;

        final TextView textServiceName;
        final TextView textServiceNameValue;
        final TextView textServiceType;
        final TextView textServiceTypeValue;
        final TextView textInvitationDateOrServiceRating;
        final TextView textInvitationDateOrServiceRatingValue;
        final TextView textInvitationTime;
        final TextView textInvitationTimeValue;
        final TextView textInvitedByOrNumberOfFlats;
        final TextView textInvitedByOrNumberOfFlatsValue;

        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;

        DailyServicesHolder(View itemView) {
            super(itemView);
            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            layoutTitleValues = itemView.findViewById(R.id.layoutTitleValues);

            textServiceName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textServiceType = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textServiceNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textServiceTypeValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
        }
    }
}
